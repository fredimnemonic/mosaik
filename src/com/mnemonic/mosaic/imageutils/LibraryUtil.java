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

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;

public class LibraryUtil {
  private static final String MOSAIC_LIBNAME = "mosaik.jml";
  private static LibraryUtil mLibraryUtil;
  private File[] mPictures;

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

    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      File picdir2 = Environment.getExternalStorageDirectory();
      mPictures = picdir2.listFiles(new FileFilter(){
        @Override
        public boolean accept(File pathname) {
          String name = pathname.getName();
          return name.endsWith(".jpg") || name.endsWith(".png");
        }
      });
    }

    if (mPictures == null) {
      mPictures = new File[0];
    }
  }

  public void createImageLib(Context context, Handler callback) {
    ImageList imagelib = new ImageList();

    long start = System.currentTimeMillis();
    int subcount = mPictures.length / 2;
    int lastcount = mPictures.length % subcount + subcount;

    MeaningThread t1 = new MeaningThread(0, subcount, imagelib, callback);
    MeaningThread t3 = new MeaningThread(subcount, lastcount, imagelib, callback);

    t1.start();
    t3.start();

    try {
      t1.join();
      t3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Rendering der Lib dauert: " + (System.currentTimeMillis() - start));
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

    long start = System.currentTimeMillis();
    bMap.getPixels(pixels, 0, maxw, 0, 0, maxw, maxh);
    System.out.println("getpixel dauert: " + (System.currentTimeMillis() - start));
    System.out.println("Bitmapabmessung: " + bMap.getWidth() + " x " + bMap.getHeight());

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
        File pic = mPictures[i];
        long substart = System.currentTimeMillis();

        mCallback.sendEmptyMessage(0);
        int color = getMeanColor(BitmapFactory.decodeFile(pic.getAbsolutePath()));

        ImageInfo newImg = new ImageInfo();         // Set up an ImageInfo record with the data.
        newImg.setFilePath(pic.getAbsolutePath());
        newImg.setColor(color);
        mImageList.add(newImg);
        System.out.println(pic.getAbsolutePath() + " dauerte -> " + (System.currentTimeMillis() - substart));
      }
    }
  }
}