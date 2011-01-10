/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 11:53
 *
 */
package com.mnemonic.mosaic.imageutils.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import com.mnemonic.mosaic.imageutils.ImageList;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.preferences.PreferenceReader;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class ImageRendererBase {
  int mTileCount;
  int mTileWidth;
  int mTileHeight;
  int mWidth;
  int mHeight;
  ImageList mTileList;
  int[][] mColors;
  int[][] mTileArray;

  Bitmap mCreatedBM;

  Bitmap mOrigBitmap;
  private Context mBasecontext;

  Map<String, int[]> mExportedTiles;

  ImageRendererBase(Context context, Bitmap orig) {
    mOrigBitmap = orig;
    mBasecontext = context;
    mExportedTiles = new WeakHashMap<String, int[]>();
  }

  public Bitmap setUp() {
    mTileCount = PreferenceReader.getTileCount(mBasecontext);

    mWidth = mOrigBitmap.getWidth();
    mHeight = mOrigBitmap.getHeight();

    mTileWidth = mWidth / mTileCount;
    mTileHeight = mHeight / mTileCount;

    mColors = new int[mTileCount][mTileCount];
    mTileArray = new int[mTileCount][mTileCount];

    mCreatedBM = Bitmap.createBitmap(mWidth, mHeight, mOrigBitmap.getConfig());

    return mCreatedBM;
  }

  void print(String txt, long start) {
    System.out.println("****************** " + txt + "    time: -> " + (System.currentTimeMillis() - start));
  }

  void print(String txt) {
    System.out.println("****************** " + txt);
  }

  public void renderImage(Handler callback) {
    print("Start reading Imag£elibrary");
    long start = System.currentTimeMillis();

    readImageLibrary();

    print("End reading Imagelibrary", start);
    print("Starting read Colors");
    start = System.currentTimeMillis();

    readColors();

    print("End readColors", start);
    print("Starting findTilesAndSetColors");
    start = System.currentTimeMillis();

    findTilesAndSetColors(callback);

    print("End findTilesAndSetColors", start);
  }

  private void readImageLibrary() {
    mTileList = LibraryUtil.getLibraryUtil().getImageLib(mBasecontext);
  }

  abstract void readColors();

  abstract void findTilesAndSetColors(Handler callback);
}
