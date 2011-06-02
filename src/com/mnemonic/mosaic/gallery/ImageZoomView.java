/**
 * Creator:
 * 11.04.11 22:25 Fredi Koller, AbaProject,SVM
 *
 * Maintainer:
 * Fredi Koller
 *
 * Last Modification:
 * $Id: $
 *
 * Copyright (c) 2011 ABACUS Research AG, All Rights Reserved
 */
package com.mnemonic.mosaic.gallery;

import android.content.Context;
import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;

import java.util.Observable;
import java.util.Observer;

public class ImageZoomView extends SurfaceView implements Observer, SurfaceHolder.Callback {

  /** Paint object used when drawing bitmap. */
  private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

  /** Rectangle used (and re-used) for cropping source image. */
  private final Rect mRectSrc = new Rect();

  /** Rectangle used (and re-used) for specifying drawing area on canvas. */
  private final Rect mRectDst = new Rect();

  /** The bitmap that we're zooming in, and drawing on the screen. */
  private Bitmap mBitmap;

  /** Pre-calculated aspect quotient. */
  private float mAspectQuotient;

  /** State of the zoom. */
  private ZoomState mState;

//  private TutorialThread _thread;
  private ImageRendererBase mRenderer;
  // Public methods

  public ImageZoomView(Context context, ImageRendererBase renderer) {
    super(context);
    getHolder().addCallback(this);
//    _thread = new TutorialThread(getHolder(), this);
    mRenderer = renderer;
    mPaint.setColor(Color.parseColor("#241d67"));
  }

  /**
   * Set image bitmap
   *
   * @param bitmap The bitmap to view and zoom into
   */
  public void setImage(Bitmap bitmap) {
    mBitmap = bitmap;

    calculateAspectQuotient();

    invalidate();
  }

  /**
   * Set object holding the zoom state that should be used
   *
   * @param state The zoom state
   */
  public void setZoomState(ZoomState state) {
    if (mState != null) {
      mState.deleteObserver(this);
    }

    mState = state;
    mState.addObserver(this);

    invalidate();
  }

  // Private methods

  private void calculateAspectQuotient() {
    if (mBitmap != null) {
      mAspectQuotient = (((float)mBitmap.getWidth()) / mBitmap.getHeight())
          / (((float)getWidth()) / getHeight());
    }
  }

  // Superclass overrides

  @Override
  protected void onDraw(Canvas canvas) {
    if (mBitmap != null && mState != null && canvas!=null) {
      final int viewWidth = getWidth();
      final int viewHeight = getHeight();
      final int bitmapWidth = mBitmap.getWidth();
      final int bitmapHeight = mBitmap.getHeight();

      final float panX = mState.getPanX();
      final float panY = mState.getPanY();
      final float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
      final float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;

      // Setup source and destination rectangles
      mRectSrc.left = (int)(panX * bitmapWidth - viewWidth / (zoomX * 2));
      mRectSrc.top = (int)(panY * bitmapHeight - viewHeight / (zoomY * 2));
      mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
      mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
      mRectDst.left = getLeft();
      mRectDst.top = 0;
      mRectDst.right = getRight();
      mRectDst.bottom = getBottom();

      canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mPaint);

      //Oiginaalgrösse zeichnen
//      Rect r = new Rect(getWidth()/2 - mRenderer.mOrigWidth / 2, getHeight()/2 - mRenderer.mOrigHeight / 2, getWidth()/2 + mRenderer.mOrigWidth / 2, getHeight()/2 + mRenderer.mOrigHeight / 2);
//      new Paint();
//      p.setColor(Color.YELLOW);
//      canvas.drawRect(r, p);

      // Adjust source rectangle so that it fits within the source image.
      if (mRectSrc.left < 0) {
        mRectDst.left += -mRectSrc.left * zoomX;
        mRectSrc.left = 0;
      }
      if (mRectSrc.right > bitmapWidth) {
        mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
        mRectSrc.right = bitmapWidth;
      }
      if (mRectSrc.top < 0) {
        mRectDst.top += -mRectSrc.top * zoomY;
        mRectSrc.top = 0;
      }
      if (mRectSrc.bottom > bitmapHeight) {
        mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
        mRectSrc.bottom = bitmapHeight;
      }

      canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    calculateAspectQuotient();
  }

  // implements Observer
  public void update(Observable observable, Object data) {
    repaint();
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    new Thread(){
      @Override
      public void run() {
        mRenderer.renderImage(new Runnable() {
          @Override
          public void run() {
            repaint();
          }
        });
      }
    }.start();
  }

  private void repaint() {
    Canvas c = null;
    try {

      c = getHolder().lockCanvas(null);
      synchronized (getHolder()) {
        onDraw(c);
      }
    } finally {
      if (c != null) {
        getHolder().unlockCanvasAndPost(c);
      }
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
// simply copied from sample application LunarLander:
    // we have to tell thread to shut down & wait for it to finish, or else
    // it might touch the Surface after we return and explode
//    boolean retry = true;
//    _thread.setRunning(false);
//    while (retry) {
//      try {
//        _thread.join();
//        retry = false;
//      } catch (InterruptedException e) {
//        // we will try it again and again...
//      }
//    }
  }

//  class TutorialThread extends Thread {
//    private final SurfaceHolder _surfaceHolder;
//    private ImageZoomView _panel;
//
//    public TutorialThread(SurfaceHolder surfaceHolder, ImageZoomView panel) {
//      _surfaceHolder = surfaceHolder;
//      _panel = panel;
//    }
//
//    public void setRunning(boolean run) {
//    }
//
//    @Override
//    public void run() {
//      mRenderer.renderImage(new Runnable() {
//        @Override
//        public void run() {
//          Canvas c = null;
//          try {
//            c = _surfaceHolder.lockCanvas(null);
//            synchronized (_surfaceHolder) {
//              _panel.onDraw(c);
//            }
//          } finally {
//            if (c != null) {
//              _surfaceHolder.unlockCanvasAndPost(c);
//            }
//          }
//        }
//      });
//    }
//  }
}
