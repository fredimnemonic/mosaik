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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.mnemonic.mosaic.imageutils.TileChecker;

class RadiusRenderRandom extends ImageRendererBase {

  RadiusRenderRandom(Context context, Bitmap orig) {
    super(context, orig);
  }

  @Override
  void readColors() {
    int[] pixels = new int[mWidth * mHeight];
    mOrigBitmap.getPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);

    int currenttilex = 0;
    double red, green, blue;
    for (int x = 0; currenttilex < mTileCount ; x += mTileWidth, currenttilex++) {
      int currenttiley = 0;
      for (int y = 0; currenttiley < mTileCount; y += mTileHeight, currenttiley++) {
        red = 0;
        green = 0;
        blue = 0;
        int counter = 0;
        for (int xs = 0; xs < mTileWidth && x + xs < mWidth; xs++) {
          for (int ys = 0; ys < mTileHeight && y + ys < mHeight; ys++) {
            counter++;
            int color = pixels[(y + ys) * mWidth + (x + xs)];//umrechnung von zwei- in eindimensionale arrays
            red += Color.red(color);
            green += Color.green(color);
            blue += Color.blue(color);
          }
        }

        red = red / counter;
        green = green / counter;
        blue = blue / counter;

        mColors[currenttilex][currenttiley] = Color.argb(255, (int) red, (int) green, (int) blue);

        System.out.println("Color " + currenttilex + " " + currenttiley + "  -  " + mColors[currenttilex][currenttiley]);
      }
    }
  }

  @Override
  void findTilesAndSetColors() {
    for (int x = 0; x < mTileCount; x++) {
      for (int y = 0; y < mTileCount; y++) {
        int tileindex = findBestFit(mColors[x][y], x, y, mTileCount);
        Bitmap origtile = BitmapFactory.decodeFile(mTileList.get(tileindex).getFilePath());
        Bitmap tile = Bitmap.createScaledBitmap(origtile, mTileWidth, mTileHeight, false);
        int[] tilepixels = new int[mTileWidth * mTileHeight];
        tile.getPixels(tilepixels, 0, mTileWidth, 0, 0, mTileWidth, mTileHeight);

        if (x * mTileWidth < mWidth && y * mTileHeight < mHeight) {
          mCreatedBM.setPixels(tilepixels, 0, mTileWidth, x * mTileWidth, y * mTileHeight, mTileWidth, mTileHeight);
        }
        System.out.println("x=" + x + "   y=" + y);
      }
    }
  }

  private int findBestFit(int c, int x, int y, int tilecount) {
//    int radiusTiles = 5;  // The number of radius tiles... this WILL be user-configurable.
    int closestSoFar = 0;  // Index of the tile that best matches the color so far.
    int redDiff, greenDiff, blueDiff, totalDiff;
    int red = Color.red(c);
    int green = Color.green(c);
    int blue = Color.blue(c);
    totalDiff = (256*3);  // Initialize the total difference to the largest reasonable number.

    int size = mTileList.size();
    for (int count = 0; count < size; count++) {  // Cycle through all of the library tiles.
      int imagecolor = mTileList.get(count).getColor();
      if ( !TileChecker.checkPlacement(1, 0, mTileArray, x, y, count, tilecount, tilecount) ) {// If this tile isn't in the box, find the difference in color.
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