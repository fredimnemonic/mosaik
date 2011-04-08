/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 08.01.11
 * Time: 15:25
 *
 */
package com.mnemonic.mosaic.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import com.mnemonic.mosaic.lib.MessageConst;

import java.io.*;

public class LibraryUtil {
  public static final int THREADCOUNT = 4;
  private static final String MOSAIC_LIBNAME = "mosaik.jml";
  private static LibraryUtil mLibraryUtil;
  private File[] mPictures;
  private ImageList mImageLib;

  private static void initSingleton() {
    if (mLibraryUtil == null) {
      mLibraryUtil = new LibraryUtil();
      mLibraryUtil.openPictureDir();
    }
  }

  public static LibraryUtil getLibraryUtil() {
    if (mLibraryUtil == null) {
      initSingleton();
    }
    return mLibraryUtil;
  }

  public File[] getAvailablePictures() {
    return mPictures;
  }

  private void openPictureDir() {
    String state = Environment.getExternalStorageState();

    FileFilter filter = new FileFilter(){
      @Override
      public boolean accept(File pathname) {
        String name = pathname.getName();
        return name.endsWith(".jpg") || name.endsWith(".png");
      }
    };

    File[] ext = null;
    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      File picdir2 = Environment.getExternalStorageDirectory();
      ext = picdir2.listFiles(filter);
    }

    File[] pics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles(filter);

    if (pics != null && pics.length > 0) {
      mPictures = pics;
    }

    if (ext != null && ext.length > 0) {
      if (pics != null) {
        mPictures = new File[mPictures.length + ext.length];
        System.arraycopy(ext, 0, mPictures, 0, ext.length);
        System.arraycopy(pics, 0, mPictures, ext.length, pics.length);
      } else {
        mPictures = ext;
      }
    }

    if (mPictures == null) {
      mPictures = new File[0];
    }
  }

  public ImageList getImageLib(Context context) {
    if (mImageLib == null) {
      mImageLib = new ImageList();
      try {
        FileInputStream fis = context.openFileInput("mosaik.jml");
        mImageLib.loadFromDisc(fis);
      } catch (IOException ioe) {
        Toast.makeText(context, "Fehler beim lesen der Library", Toast.LENGTH_LONG);
        ioe.printStackTrace();
      } catch (ClassNotFoundException e) {
        Toast.makeText(context, "Fehler beim lesen der Library", Toast.LENGTH_LONG);
        e.printStackTrace();
      }
    }

    return mImageLib;
  }

  public void createImageLib(Context context, Handler callback) {
    ImageList imagelib = new ImageList();

    long start = System.currentTimeMillis();
    int subcount = mPictures.length / THREADCOUNT;
    int lastcount = mPictures.length % subcount + subcount * THREADCOUNT;

    MeaningThread[] th = new MeaningThread[THREADCOUNT];
    for (int i = 0; i < THREADCOUNT; i++) {
      int tstart = i*subcount;
      if (i == THREADCOUNT - 1) {
        th[i] = new MeaningThread(tstart, lastcount, imagelib, callback);
      } else {
        th[i] = new MeaningThread(tstart, tstart + subcount, imagelib, callback);
      }
    }

    for (MeaningThread t : th) {
      t.start();
    }

    try {
      for (MeaningThread t : th) {
        t.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

//    int subcount = mPictures.length / 2;
//    int lastcount = mPictures.length % subcount + subcount * 2;
//
//    MeaningThread t1 = new MeaningThread(0, subcount, imagelib, callback);
//    MeaningThread t3 = new MeaningThread(subcount, lastcount, imagelib, callback);
//
//    t1.start();
//    t3.start();
//
//    try {
//      t1.join();
//      t3.join();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    System.out.println("Rendering der Lib dauert: " + (System.currentTimeMillis() - start) + "  Anzahl bilder: " + imagelib.size());
    saveImageLib(context, imagelib);

    System.out.println("Done!");
  }

  private void saveImageLib(Context context, ImageList imagelib) {
    try {
      FileOutputStream fos = context.openFileOutput(MOSAIC_LIBNAME, Context.MODE_WORLD_WRITEABLE);
      imagelib.saveToDisc(fos);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private static int getMeanColor(Bitmap bMap) {
    int maxw = bMap.getWidth();
    int maxh = bMap.getHeight();

    int[] pixels = new int[maxw * maxh];

//    long start = System.currentTimeMillis();
    bMap.getPixels(pixels, 0, maxw, 0, 0, maxw, maxh);
//    System.out.println("getpixel dauert: " + (System.currentTimeMillis() - start));
//    System.out.println("Bitmapabmessung: " + bMap.getWidth() + " x " + bMap.getHeight());

    double red = 0, green = 0, blue = 0;
    for (int x = 0; x < maxw; x ++) {
      for (int y = 0; y < maxh; y ++) {
        int color = pixels[y * maxw + x];//umrechnung von zwei- in eindimensionale arrays
        red += Color.red(color);
        green += Color.green(color);
        blue += Color.blue(color);
      }
    }

    int max = maxh * maxw;
    red = red / max;
    green = green / max;
    blue = blue / max;

    return Color.argb(255, (int) red, (int) green, (int) blue);
  }

  private class MeaningThread extends Thread {
    private int mStart;
    private int mEnd;
    private ImageList mImageList;
    private Handler mCallback;

    private MeaningThread(int start, int end, ImageList imageList, Handler callback) {
      mStart = start;
      mEnd = end;
      mImageList = imageList;
      mCallback = callback;
    }

    @Override
    public void run() {
      for (int i = mStart; i < mEnd; i++) {
        String path = mPictures[i].getAbsolutePath();
//        long substart = System.currentTimeMillis();

        if (mPictures[i].exists()) {
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inPreferQualityOverSpeed = false;
          options.inPurgeable = true;
          Bitmap b = BitmapFactory.decodeFile(path, options);
          if (b != null) {
            mImageList.add(new ImageInfo(path, getMeanColor(b)));
            b.recycle();
          } else {
            System.out.println("File ist null-> " + path);
          }
        }

//        System.out.println(path + " -> " + (System.currentTimeMillis() - substart));

        mCallback.sendEmptyMessage(0);
      }
      mCallback.sendEmptyMessage(MessageConst.MessageFinish);
    }
  }
}