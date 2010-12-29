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

    for (int count = 0; count < mPictures.length; count++) {  // Cycle through all the images in the list...
      try {
        double[] mean = getMean(BitmapFactory.decodeFile(mPictures[count].getAbsolutePath()));
//        if (mean.length == 3) {  // If there were 3 bands of Data (RBG, not Monochrome..)

        int color = Color.argb(255, (int) mean[0], (int) mean[1], (int) mean[2]);  // Make a color from it.
//          if (c != null) {
            ImageInfo newImg = new ImageInfo();         // Set up an ImageInfo record with the data.
            newImg.setFileName(mPictures[count].getName());
            newImg.setRBGVal(color);
            imagelib.add(newImg);
            System.out.println(mPictures[count].getName());
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

    try {
      FileOutputStream fos = context.openFileOutput("mosaik.jml", Context.MODE_WORLD_WRITEABLE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeInt(imagelib.size());  // First, write the length of the list.
      for (ImageInfo anImagelib : imagelib) {  // Next, cycle through each tile,
        oos.writeObject(anImagelib);           // And write its object to the file.
      }

      oos.close();
      fos.close();

      FileInputStream fis = context.openFileInput("mosaik.jml");
      ObjectInputStream ois = new ObjectInputStream(fis);
      int numObjects = ois.readInt();

      // Read all of the tiles out of the library.
      ImageList images = new ImageList();
      for (int count = 0; count < numObjects; count++) {
        images.put((ImageInfo) ois.readObject());
      }

      int[][] tileArray = new int[20][20];
      findBestFit(images, Color.RED, tileArray, 0, 0);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
//    for (int x = 0; x < maxw; x ++) {
//      for (int y = 0; y < maxh; y ++) {
//        int color = bMap.getPixel(x, y);
//        mean[0] += android.graphics.Color.red(color);
//        mean[1] += android.graphics.Color.green(color);
//        mean[2] += android.graphics.Color.blue(color);
//      }
//    }
//    System.out.println("direkter Zugriff dauert: " + (System.currentTimeMillis()-start));
//    start = System.currentTimeMillis();

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

  private int findBestFit(ImageList tileLibrary, int c, int[][] tileArray, int x, int y) {
    int radiusTiles = 5;  // The number of radius tiles... this WILL be user-configurable.
    int closestSoFar = 0;  // Index of the tile that best matches the color so far.
    int redDiff, greenDiff, blueDiff, totalDiff = 0;
    totalDiff = (256*3);  // Initialize the total difference to the largest reasonable number.

    for (int count = 0; count < tileLibrary.getSize(); count++) {  // Cycle through all of the library tiles.
      if ( !TileChecker.checkPlacement(1, 0, tileArray, x, y, count, 20, 20) ) {//todo 20/20 ersetzen  // If this tile isn't in the box, find the difference in color.
        redDiff = Math.abs(Color.red(c) - Color.red(tileLibrary.get(count).getColor()));
        blueDiff = Math.abs(Color.blue(c) - Color.blue(tileLibrary.get(count).getColor()));
        greenDiff = Math.abs(Color.green(c) - Color.green(tileLibrary.get(count).getColor()));
        if (((redDiff + blueDiff + greenDiff) < totalDiff)) { // if this is closer than the previous closest...
          totalDiff = redDiff + blueDiff + greenDiff;
          closestSoFar = count;  // Keep track of this tile.
        }
      }
    }
    return closestSoFar;  // return the tile we chose.
  }
}
