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
import com.mnemonic.mosaic.imageutils.ImageList;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.imageutils.TileAlgorithmus;
import com.mnemonic.mosaic.preferences.PreferenceReader;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class ImageRendererBase {
  int mTileAbstand;
  TileAlgorithmus mTileAlgorithmus;

  int mTileCountX;
  int mTileCountY;
  int mTileWidth;
  int mTileHeight;
  public int mOrigWidth;
  public int mOrigHeight;
  ImageList mTileList;
  int[][] mColors;
  int[][] mTileArray;

  Bitmap mCreatedBM;

  Bitmap mOrigBitmap;
  protected Context mBasecontext;

  final Map<String, int[]> mExportedTiles;

  ImageRendererBase(Context context, Bitmap orig) {
    mOrigBitmap = orig;
    mBasecontext = context;
    mExportedTiles = new WeakHashMap<String, int[]>();
  }

  public Bitmap setUp() {
    mTileCountX = mTileCountY = PreferenceReader.getTileCount(mBasecontext);
    mTileAbstand = PreferenceReader.getTileBetween(mBasecontext);
    mTileAlgorithmus = PreferenceReader.getTileAlgo(mBasecontext);

    mOrigWidth = mOrigBitmap.getWidth();
    mOrigHeight = mOrigBitmap.getHeight();

    mTileWidth = mOrigWidth / mTileCountX;
    mTileHeight = mOrigHeight / mTileCountY;

    int restwith = mOrigWidth - (mTileWidth * mTileCountX);
    int restheigt = mOrigHeight - (mTileHeight * mTileCountY);

    print("########## restwith = " + restwith);
    print("########## restheigt = " + restheigt);

    int result = restwith/mTileWidth;
    mTileCountX = mTileCountX + result;
    result = restheigt/mTileHeight;
    mTileCountY = mTileCountY + result;

    restwith = mOrigWidth - (mTileWidth * mTileCountX);
    restheigt = mOrigHeight - (mTileHeight * mTileCountY);

    print("########## restwith-nachher = " + restwith);
    print("########## restheigt-nachher = " + restheigt);




    mColors = new int[mTileCountX][mTileCountY];
    mTileArray = new int[mTileCountX][mTileCountY];

    mCreatedBM = Bitmap.createBitmap(mTileWidth * mTileCountX, mTileHeight * mTileCountY, mOrigBitmap.getConfig());

    return mCreatedBM;
  }

  void print(String txt, long start) {
    System.out.println("****************** " + txt + "    time: -> " + (System.currentTimeMillis() - start));
  }

  void print(String txt) {
    System.out.println("****************** " + txt);
  }

  public void renderImage(Runnable runnable) {
    print("Start reading Imagelibrary");
    long start = System.currentTimeMillis();

    readImageLibrary();

    print("End reading Imagelibrary", start);
    print("Starting read Colors");
    start = System.currentTimeMillis();

    readColors();

    print("End readColors", start);
    print("Starting findTilesAndSetColors");
    start = System.currentTimeMillis();

    findTilesAndSetColors(runnable);

    print("End findTilesAndSetColors", start);
  }

  private void readImageLibrary() {
    mTileList = LibraryUtil.getLibraryUtil().getImageLib(mBasecontext);
  }

  abstract void readColors();

  abstract void findTilesAndSetColors(Runnable runnable);
}
