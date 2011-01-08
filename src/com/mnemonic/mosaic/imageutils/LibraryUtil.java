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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    List<ImageInfo> imagelib = new ArrayList<ImageInfo>();

    long start = System.currentTimeMillis();
    for (File pic : mPictures) {
      long substart = System.currentTimeMillis();

      callback.sendEmptyMessage(0);
      int color = getMeanColor(BitmapFactory.decodeFile(pic.getAbsolutePath()));

      ImageInfo newImg = new ImageInfo();         // Set up an ImageInfo record with the data.
      newImg.setFilePath(pic.getAbsolutePath());
      newImg.setColor(color);
      imagelib.add(newImg);
      System.out.println(pic.getAbsolutePath() + " dauerte -> " + (System.currentTimeMillis() - substart));
    }
    System.out.println("Rendering der Lib dauert: " + (System.currentTimeMillis() - start));
    saveImageLib(context, imagelib);

    System.out.println("Done!");
  }

  private void saveImageLib(Context context, List<ImageInfo> imagelib) {
    try {
      FileOutputStream fos = context.openFileOutput(MOSAIC_LIBNAME, Context.MODE_WORLD_WRITEABLE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeInt(imagelib.size());  // First, write the length of the list.
      for (ImageInfo anImagelib : imagelib) {  // Next, cycle through each tile,
        oos.writeObject(anImagelib);           // And write its object to the file.
      }

      oos.close();
      fos.close();
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
}