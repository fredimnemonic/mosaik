/**
 * Creator:
 * 24.04.11 00:22 Fredi Koller, AbaProject,SVM
 *
 * Maintainer:
 * Fredi Koller
 *
 * Last Modification:
 * $Id: $
 *
 * Copyright (c) 2011 ABACUS Research AG, All Rights Reserved
 */
package com.mnemonic.mosaic.imageutils.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TileThread extends Thread {
  private Runnable mCallback;
  private int mCurrentX;
  private int mMaxX;
  private int mCurrentY;
  private int mMaxY;
  private ThreadedRadiusRenderRandom mRenderer;

  TileThread(ThreadedRadiusRenderRandom renderer, Runnable runnable, int currentX, int maxX, int currentY, int maxY) {
    mCallback = runnable;
    mCurrentX = currentX;
    mMaxX = maxX;
    mCurrentY = currentY;
    mMaxY = maxY;
    mRenderer = renderer;
  }

  @Override
  public void run() {
    for (int x = mCurrentX; x < mMaxX; x++) {
      mCallback.run();
      for (int y = mCurrentY; y < mMaxY; y++) {
        int tileindex = mRenderer.findBestFit(mRenderer.mColors[x][y], x, y, mRenderer.mTileCountX, mRenderer.mTileCountY);

        String path = mRenderer.mTileList.get(tileindex).getFilePath();
        int[] tilepixels;

        if (mRenderer.mExportedTiles.containsKey(path)) {
          tilepixels = mRenderer.mExportedTiles.get(path);
        } else {
          tilepixels = new int[mRenderer.mTileWidth * mRenderer.mTileHeight];
          Bitmap origtile;
          if (mRenderer.mTileList.get(tileindex).isInternal()) {
            origtile = BitmapFactory.decodeResource(mRenderer.mBasecontext.getResources(), Integer.parseInt(path));
          } else {
            origtile = BitmapFactory.decodeFile(path);
          }

          if (origtile == null) {
            System.out.println("Bild nicht vorhanden: " + path);
            continue;
          }
          Bitmap tile = Bitmap.createScaledBitmap(origtile, mRenderer.mTileWidth, mRenderer.mTileHeight, false);
//          origtile.recycle();
          tile.getPixels(tilepixels, 0, mRenderer.mTileWidth, 0, 0, mRenderer.mTileWidth, mRenderer.mTileHeight);
          mRenderer.mExportedTiles.put(path, tilepixels);
        }

//          System.out.println("current: x=" +x +"   y="+y);
        mRenderer.setPixels(tilepixels, x, y);
      }
    }
    mCallback.run();
  }
}
