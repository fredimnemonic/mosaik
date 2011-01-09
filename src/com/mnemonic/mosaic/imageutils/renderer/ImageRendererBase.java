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
import com.mnemonic.mosaic.preferences.PreferenceReader;

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

  protected ImageRendererBase(Context context, Bitmap orig) {
    mOrigBitmap = orig;
    mBasecontext = context;
  }

  public Bitmap setUp() {
    mTileCount = PreferenceReader.getTileCount(mBasecontext);

    mWidth = mOrigBitmap.getWidth();
    mHeight = mOrigBitmap.getHeight();

    mTileWidth = mWidth / mTileCount;
    mTileHeight = mHeight / mTileCount;

    mCreatedBM = Bitmap.createBitmap(mWidth, mHeight, mOrigBitmap.getConfig());

    mTileList = LibraryUtil.getLibraryUtil().getImageLib(mBasecontext);

    mColors = new int[mTileCount][mTileCount];
    mTileArray = new int[mTileCount][mTileCount];

    return mCreatedBM;
  }

  public void renderImage() {

  }
}
