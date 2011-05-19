/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 15:00
 *
 */
package com.mnemonic.mosaic.imageutils.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.mnemonic.mosaic.imageutils.TileChecker;


@SuppressWarnings({"UnusedDeclaration"})
class ThreadedRadiusRenderRandom extends ImageRendererBase {

  ThreadedRadiusRenderRandom(Context context, Bitmap orig) {
    super(context, orig);
  }

  @Override
  void readColors() {
    int[] pixels = new int[mOrigWidth * mOrigHeight];
    mOrigBitmap.getPixels(pixels, 0, mOrigWidth, 0, 0, mOrigWidth, mOrigHeight);

    int currenttilex = 0;
    double red, green, blue;
    for (int x = 0; currenttilex < mTileCount ; x += mTileWidth, currenttilex++) {
      int currenttiley = 0;
      for (int y = 0; currenttiley < mTileCount; y += mTileHeight, currenttiley++) {
        red = 0;
        green = 0;
        blue = 0;
        int counter = 0;
        for (int xs = 0; xs < mTileWidth && x + xs < mOrigWidth; xs++) {
          for (int ys = 0; ys < mTileHeight && y + ys < mOrigHeight; ys++) {
            counter++;
            int color = pixels[(y + ys) * mOrigWidth + (x + xs)];//umrechnung von zwei- in eindimensionale arrays
            red += Color.red(color);
            green += Color.green(color);
            blue += Color.blue(color);
          }
        }

        red = red / counter;
        green = green / counter;
        blue = blue / counter;

        mColors[currenttilex][currenttiley] = Color.argb(255, (int) red, (int) green, (int) blue);
      }
    }
  }

  @Override
  void findTilesAndSetColors(final Runnable runnable) {
    int half = mTileCount / 2;
    Thread t1 = new TileThread(this, runnable, 0, half);
    Thread t2 = new TileThread(this, runnable, half, mTileCount);

//    Thread t1 = new TileThread(this, runnable, 0, mTileCount);

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  int findBestFit(int c, int x, int y, int tilecount) {
    int closestSoFar = 0;  // Index of the tile that best matches the color so far.
    int redDiff, greenDiff, blueDiff, totalDiff;
    int red = Color.red(c);
    int green = Color.green(c);
    int blue = Color.blue(c);
    totalDiff = (256*3);  // Initialize the total difference to the largest reasonable number.

    int size = mTileList.size();
    for (int count = 0; count < size; count++) {  // Cycle through all of the library tiles.
      int imagecolor = mTileList.get(count).getColor();
      // If this tile isn't in the box, find the difference in color.
      if ( !TileChecker.checkPlacement(mTileAlgorithmus, mTileAbstand, mTileArray, x, y, count, tilecount, tilecount) ) {
        redDiff = Math.abs(red - Color.red(imagecolor));
        blueDiff = Math.abs(blue - Color.blue(imagecolor));
        greenDiff = Math.abs(green - Color.green(imagecolor));
        if (((redDiff + blueDiff + greenDiff) < totalDiff)) { // if this is closer than the previous closest...
          totalDiff = redDiff + blueDiff + greenDiff;
          closestSoFar = count;  // Keep track of this tile.
        }
      }
    }
    mTileArray[x][y] = closestSoFar;
    return closestSoFar;  // return the tile we chose.
  }

  synchronized void setPixels(int[] tilepixels, int x, int y) {
    mCreatedBM.setPixels(tilepixels, 0, mTileWidth, x * mTileWidth, y * mTileHeight, mTileWidth, mTileHeight);
  }
}