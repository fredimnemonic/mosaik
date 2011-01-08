
package com.mnemonic.mosaic.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class RadiusRenderRandom {

  public Bitmap createMap(Context context, Bitmap orig) {
    int tilecount = 40;

    int width = orig.getWidth();
    int height = orig.getHeight();
    Bitmap neu = Bitmap.createBitmap(width, height, orig.getConfig());

    long start = System.currentTimeMillis();

    List<ImageInfo> images = new ArrayList<ImageInfo>();
    try {
      FileInputStream fis = context.openFileInput("mosaik.jml");
      ObjectInputStream ois = new ObjectInputStream(fis);
      int numObjects = ois.readInt();

      // Read all of the tiles out of the library.
      for (int count = 0; count < numObjects; count++) {
        images.add((ImageInfo) ois.readObject());
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    System.out.println();

    System.out.println("lib geladen in: " + (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();

    int tilex = width / tilecount;
    int tiley = height / tilecount;

    int[][] colors = new int[tilecount][tilecount];
    int[][] tileArray = new int[tilecount][tilecount];

    int[] pixels = new int[width * height];
    orig.getPixels(pixels, 0, width, 0, 0, width, height);

    int currenttilex = 0;
    for (int x = 0; currenttilex < tilecount ; x += tilex, currenttilex++) {
      int currenttiley = 0;
      for (int y = 0; currenttiley < tilecount; y += tiley, currenttiley++) {
        double[] mean = new double[3];

        int counter = 0;
        for (int xs = 0; xs < tilex && x + xs < width; xs++) {
          for (int ys = 0; ys < tiley && y + ys < height; ys++) {
            counter++;
            int color = pixels[(y + ys) * width + (x + xs)];//umrechnung von zwei- in eindimensionale arrays
            mean[0] += Color.red(color);
            mean[1] += Color.green(color);
            mean[2] += Color.blue(color);
          }
        }

        mean[0] = mean[0] / counter;
        mean[1] = mean[1] / counter;
        mean[2] = mean[2] / counter;
/**********************************************************************/

        colors[currenttilex][currenttiley] = Color.argb(255, (int) mean[0], (int) mean[1], (int) mean[2]);
        mean[0] = 0;
        mean[1] = 0;
        mean[2] = 0;

        System.out.println("Color " + currenttilex + " " + currenttiley + "  -  " + colors[currenttilex][currenttiley]);

//          int color = Color.argb(255, (int) mean[0], (int) mean[1], (int) mean[2]);
//          int tileindex = findBestFit(images, color, tileArray, x, y, tilecount);
//          Bitmap origtile = BitmapFactory.decodeFile(images.get(tileindex).getFilePath());
//          Bitmap tile = Bitmap.createScaledBitmap(origtile, tilex, tiley, false);
//          int[] tilepixels = new int[tilex * tiley];
//          tile.getPixels(tilepixels, 0, tilex, 0, 0, tilex, tiley);
//
//          if (x + tilex < width && y + tiley < height) {
//            neu.setPixels(tilepixels, 0, tilex, x, y, tilex, tiley);
//          }

/**********************************************************************/


//        for (int xs = 0; xs < tilex && x + xs < width; xs++) {
//          for (int ys = 0; ys < tiley && y + ys < height; ys++) {
//            neu.setPixel(x + xs, y + ys, Color.argb(255, (int) mean[0], (int) mean[1], (int) mean[2]));
//          }
//        }

/**********************************************************************/
      }
    }

    System.out.println("Done reading colors! Time -> " + (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();



    for (int x = 0; x < tilecount; x++) {
      for (int y = 0; y < tilecount; y++) {
        int tileindex = findBestFit(images, colors[x][y], tileArray, x, y, tilecount);
        Bitmap origtile = BitmapFactory.decodeFile(images.get(tileindex).getFilePath());
        Bitmap tile = Bitmap.createScaledBitmap(origtile, tilex, tiley, false);
        int[] tilepixels = new int[tilex * tiley];
        tile.getPixels(tilepixels, 0, tilex, 0, 0, tilex, tiley);

        if (x * tilex < width && y * tiley < height) {
          neu.setPixels(tilepixels, 0, tilex, x*tilex, y*tiley, tilex, tiley);
        }
        System.out.println("x=" + x + "   y=" + y);
      }
    }

    System.out.println("Done setting colors! Time -> " + (System.currentTimeMillis() - start));

    return neu;
  }

  private int findBestFit(List<ImageInfo> tileLibrary, int c, int[][] tileArray, int x, int y, int tilecount) {
    int radiusTiles = 5;  // The number of radius tiles... this WILL be user-configurable.
    int closestSoFar = 0;  // Index of the tile that best matches the color so far.
    int redDiff, greenDiff, blueDiff, totalDiff = 0;
    int red = Color.red(c);
    int green = Color.green(c);
    int blue = Color.blue(c);
    totalDiff = (256*3);  // Initialize the total difference to the largest reasonable number.

    int size = tileLibrary.size();
    for (int count = 0; count < size; count++) {  // Cycle through all of the library tiles.
      int imagecolor = tileLibrary.get(count).getColor();
      if ( !TileChecker.checkPlacement(1, 0, tileArray, x, y, count, tilecount, tilecount) ) {// If this tile isn't in the box, find the difference in color.
        redDiff = Math.abs(red - Color.red(imagecolor));
        blueDiff = Math.abs(blue - Color.blue(imagecolor));
        greenDiff = Math.abs(green - Color.green(imagecolor));
        if (((redDiff + blueDiff + greenDiff) < totalDiff)) { // if this is closer than the previous closest...
          totalDiff = redDiff + blueDiff + greenDiff;
          closestSoFar = count;  // Keep track of this tile.
        }
      }
    }
    return closestSoFar;  // return the tile we chose.
  }
}