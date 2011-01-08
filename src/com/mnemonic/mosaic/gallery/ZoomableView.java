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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;

public class ZoomableView extends View {
  private Drawable mDrawable;
  private int mZoomController;
  private double mVerhaeltnis;

  public ZoomableView(Context context, Bitmap bitmap) {
    super(context);
    mDrawable = new BitmapDrawable(bitmap);
    setFocusable(true);

    mZoomController = bitmap.getWidth() / 2 - 10;
    mVerhaeltnis = bitmap.getHeight() / (double) bitmap.getWidth();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    //here u can control the width and height of the images........ this line is very important
    mDrawable.setBounds((getWidth() / 2) - mZoomController, (getHeight() / 2) - (int) (mZoomController * mVerhaeltnis),
        (getWidth() / 2) + mZoomController, (getHeight() / 2) + (int) (mZoomController * mVerhaeltnis));
    mDrawable.draw(canvas);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode==KeyEvent.KEYCODE_DPAD_UP)// zoom in
      mZoomController +=10;
    if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN) // zoom out
      mZoomController -=10;
    if(mZoomController <10)
      mZoomController =10;

    invalidate();
    return true;
  }
}