/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mnemonic.mosaic.gallery;

//import com.sonyericsson.zoom.ImageZoomView;
//import com.sonyericsson.zoom.SimpleZoomListener;
//import com.sonyericsson.zoom.ZoomState;
//import com.sonyericsson.zoom.SimpleZoomListener.ControlType;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;
import com.mnemonic.mosaic.imageutils.renderer.RendererFactory;
import com.mnemonic.mosaic.preferences.PreferenceReader;

/**
 * Activity for zoom tutorial 1
 */
public class SingleZoomActivity extends BaseActivity {

  /** Constant used as menu item id for setting zoom control type */
  private static final int MENU_ID_ZOOM = 0;

  /** Constant used as menu item id for setting pan control type */
  private static final int MENU_ID_PAN = 1;

  /** Constant used as menu item id for resetting zoom state */
  private static final int MENU_ID_RESET = 2;

  /** Image zoom view */
  private ImageZoomView mZoomView;

  /** Zoom state */
  private ZoomState mZoomState;

  /** Decoded bitmap image */
  private Bitmap mBitmap;

  /** On touch listener for zoom view */
  private SimpleZoomListener mZoomListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bitmap tomasaic = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));

    String renderername = PreferenceReader.getRendererClass(getBaseContext());
    final ImageRendererBase renderer = RendererFactory.createRenderer(renderername, getBaseContext(), tomasaic);
    final Bitmap neu = renderer.setUp();

    LinearLayout layout = new LinearLayout(getBaseContext());

    mZoomView = new ImageZoomView(getBaseContext(), renderer);
    mZoomView.setBackgroundColor(Color.GREEN);



//    Button b = new Button(getBaseContext());
//    b.setText("Speichern");
//    b.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        try {
//          MediaStore.Images.Media.insertImage(getContentResolver(), neu, "mosaik_" + System.currentTimeMillis() + ".jpg", "");
//
////          File pics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
////          File loc = new File(pics, "mosaik_" + System.currentTimeMillis() + ".jpg");
////          if (loc.exists() || loc.createNewFile()) {
////            FileOutputStream out = new FileOutputStream(loc);
////            neu.compress(Bitmap.CompressFormat.JPEG, 100, out);
////            out.flush();
////            out.close();
////          }
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    });
//    layout.addView(b);
//    b = new Button(getBaseContext());
//    b.setText("Aktualisieren");
//    b.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        try {
//          mZoomView.invalidate();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    });
//    layout.addView(b);
    layout.addView(mZoomView);

    setContentView(layout);

//    Handler handler = new Handler(){
//      int mCounter = 0;
//      @Override
//      public void handleMessage(Message msg) {
//        mZoomView.invalidate();
//        if (msg.what == MessageConst.MessageFinish) {
//          mCounter ++;
//        }
//        if (mCounter == 2) {
//          Toast.makeText(getBaseContext(), "Mosaik ist fertig", Toast.LENGTH_LONG).show();
//        }
//
//      }
//    };




    mZoomState = new ZoomState();

//    mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image800x600);

    mZoomListener = new SimpleZoomListener();
    mZoomListener.setZoomState(mZoomState);
    mZoomView.setZoomState(mZoomState);
    mZoomView.setImage(neu);

    this.mZoomView.setOnTouchListener(mZoomListener);

    resetZoomState();


//    renderer.renderImage(handler);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

//    mBitmap.recycle();
    mZoomView.setOnTouchListener(null);
    mZoomState.deleteObservers();
  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    menu.add(Menu.NONE, MENU_ID_ZOOM, 0, R.string.menu_zoom);
//    menu.add(Menu.NONE, MENU_ID_PAN, 1, R.string.menu_pan);
//    menu.add(Menu.NONE, MENU_ID_RESET, 2, R.string.menu_reset);
//    return super.onCreateOptionsMenu(menu);
//  }

//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    switch (item.getItemId()) {
//      case MENU_ID_ZOOM:
//        mZoomListener.setControlType(SimpleZoomListener.ControlType.ZOOM);
//        break;
//
//      case MENU_ID_PAN:
//        mZoomListener.setControlType(SimpleZoomListener.ControlType.PAN);
//        break;
//
//      case MENU_ID_RESET:
//        resetZoomState();
//        break;
//    }
//
//    return super.onOptionsItemSelected(item);
//  }

  /**
   * Reset zoom state and notify observers
   */
  private void resetZoomState() {
    mZoomState.setPanX(0.5f);
    mZoomState.setPanY(0.5f);
    mZoomState.setZoom(1f);
    mZoomState.notifyObservers();
  }

}
