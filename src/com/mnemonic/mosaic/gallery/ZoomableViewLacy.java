/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 08.01.11
 * Time: 12:00
 *
 */
package com.mnemonic.mosaic.gallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import com.mnemonic.mosaic.imageutils.ImageList;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.preferences.PreferenceReader;

public class ZoomableViewLacy extends View {
  private double mZoomController;//breite des bildes
  private double mVerhaeltnis;

  private int mTileCount;
  private int[][] mTiles;
  private ImageList mTileList;
  private Paint mPaint;

  public ZoomableViewLacy(Context context, Bitmap bitmap, int[][] tiles) {
    super(context);
    setFocusable(true);


    mVerhaeltnis = bitmap.getHeight() / (double) bitmap.getWidth();

    mTileCount = PreferenceReader.getTileCount(context);
    mZoomController = mTileCount;
    mTiles = tiles;
    mTileList = LibraryUtil.getLibraryUtil().getImageLib(context);
    mPaint = new Paint();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int width = getWidth();
    int height = getHeight();

//    double vxd = (width / 4 * mZoomController);
//    double vyd = (width / 4 * mZoomController * mVerhaeltnis);
//    
//    int tilewith = (int) ((2*vxd) / mTileCount);
//    int tileheigth = (int) ((2*vyd) / mTileCount);
//    
//    int breite = tilewith * mTileCount;
//    int hoehe = tileheigth * mTileCount;
//    
//    int vx = breite / 2;
//    int vy = hoehe / 2;
//
    int mittex = width / 2;
    int mittey = height / 2;
//    
//    for (int i = 0; i < mTileCount; i++) {
//    	for (int j = 0; j < mTileCount; j++) {
//    		//if (mTiles[i][j] <= 0) {
//        	    Bitmap m = mTileList.getBitmap(mTiles[i][j]);
////                canvas.drawBitmap(m, new Rect(0,0,m.getWidth(), m.getHeight()), new Rect(vx + (i*tilewith),vy+(j*tileheigth),vx + (tilewith*(i+1)), vy + (tileheigth*(j+1))), mPaint);									
//                canvas.drawBitmap(m, new Rect(0,0,m.getWidth(), m.getHeight()), new Rect((mittex-vx)+(i*tilewith),(mittey-vy)+(j*tileheigth),(mittex-vx)+((i+1)*tilewith), (mittey-vy)+((j+1)*tileheigth)), mPaint);
//			//}
//		}
//	}

    int tilewith = (int) (mZoomController / mTileCount);
    int tileheigth = (int) (mZoomController * mVerhaeltnis / mTileCount);

    int halfx = (int) (mZoomController / 2);
    int halfy = (int) (mZoomController * mVerhaeltnis / 2 );

    for (int i = 0; i < mTileCount; i++) {
      for (int j = 0; j < mTileCount; j++) {
        if (mTiles[i][j] != 0) {//falls es das erste ist, nichts machen. Das erste lassen wir aus.
          Bitmap m = mTileList.getBitmap(mTiles[i][j]);
          Rect neu = new Rect((mittex - halfx) + (i * tilewith), (mittey - halfy) + (j * tileheigth), (mittex - halfx) + ((i + 1) * tilewith), (mittey - halfy) + ((j + 1) * tileheigth));
          canvas.drawBitmap(m, new Rect(0, 0, m.getWidth(), m.getHeight()), neu, mPaint);
        }
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode==KeyEvent.KEYCODE_DPAD_UP) {// zoom in
      mZoomController *=2;
    } else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN) {// zoom out
      mZoomController /=2;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
      mZoomController = mTileCount;
    }
    if(mZoomController <mTileCount)
      mZoomController =mTileCount;

    invalidate();
    return true;
  }
}