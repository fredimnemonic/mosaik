package com.mnemonic.mosaic.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mnemonic-Programmer Fredi.
 * User: Kollera
 * Date: 28.12.10
 * Time: 19:08
 */
public class LibraryCreate {
  private File[] mPictures;

  public LibraryCreate() {
    File picdir2 = Environment.getExternalStorageDirectory();
    mPictures = picdir2.listFiles(new FileFilter(){

      @Override
      public boolean accept(File pathname) {
        String name = pathname.getName();
        return name.endsWith(".jpg") || name.endsWith(".png");
      }
    });
  }

  public void initImageLib(Context context) {

    List<ImageInfo> imagelib = new ArrayList<ImageInfo>();

    long start = System.currentTimeMillis();
    for (int count = 0; count < mPictures.length; count++) {  // Cycle through all the images in the list...
      try {
        double[] mean = getMean(BitmapFactory.decodeFile(mPictures[count].getAbsolutePath()));
//        if (mean.length == 3) {  // If there were 3 bands of Data (RBG, not Monochrome..)

        int color = Color.argb(255, (int) mean[0], (int) mean[1], (int) mean[2]);  // Make a color from it.
//          if (c != null) {
            ImageInfo newImg = new ImageInfo();         // Set up an ImageInfo record with the data.
            newImg.setFilePath(mPictures[count].getAbsolutePath());
            newImg.setColor(color);
            imagelib.add(newImg);
            System.out.println(mPictures[count].getAbsolutePath());
//          }
//        } else {  // Debugging.
//          System.out.println("Wrong Number Of Bands On Image: " + mPictures[count].getName());
//        }
      } catch (Exception e) {  // Debugging.
        System.out.println("Error on image: " + count);
        System.out.println("EXCEPTION: " + e.toString());
        e.printStackTrace();
      }
    }
    System.out.println("Rendering der Lib dauert: " + (System.currentTimeMillis() - start));
    try {
      FileOutputStream fos = context.openFileOutput("mosaik.jml", Context.MODE_WORLD_WRITEABLE);
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

    System.out.println("Done!");
  }

  private static double[] getMean(Bitmap bMap) {
    double[] mean = new double[3];

    int maxw = bMap.getWidth();
    int maxh = bMap.getHeight();

    int[] pixels = new int[maxw * maxh];
    bMap.getPixels(pixels, 0, maxw, 0, 0, maxw, maxh);

    long start = System.currentTimeMillis();

    for (int x = 0; x < maxw; x ++) {
      for (int y = 0; y < maxh; y ++) {
        int color = pixels[y * maxw + x];//umrechnung von zwei- in eindimensionale arrays
        mean[0] += Color.red(color);
        mean[1] += Color.green(color);
        mean[2] += Color.blue(color);
      }
    }
    System.out.println("indirekter Zugriff dauert: " + (System.currentTimeMillis()-start));

    mean[0] = mean[0] / (maxh * maxw);
    mean[1] = mean[1] / (maxh * maxw);
    mean[2] = mean[2] / (maxh * maxw);

    return mean;
  }
}
