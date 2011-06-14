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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;
import com.mnemonic.mosaic.imageutils.renderer.RendererFactory;
import com.mnemonic.mosaic.preferences.PreferenceReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for zoom tutorial 1
 */
public class SingleZoomActivity extends BaseActivity {

  private ImageZoomView mZoomView;
  private ZoomState mZoomState;

  private Uri mLastUri;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bitmap tomasaic = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));
    System.out.println("SingeZoomActivity-> with: " + tomasaic.getWidth() + "  height: " + tomasaic.getHeight());
    String renderername = PreferenceReader.getRendererClass(getBaseContext());
    final ImageRendererBase renderer = RendererFactory.createRenderer(renderername, getBaseContext(), tomasaic);
    Bitmap neu = renderer.setUp();
    System.out.println("SingeZoomActivity-> with: " + neu.getWidth() + "  height: " + neu.getHeight());

    mZoomView = new ImageZoomView(getBaseContext(), renderer);

    mMainPanel.addView(mZoomView);

//    layout.setBackgroundColor(Color.parseColor("#241d67"));


    mZoomState = new ZoomState();

    SimpleZoomListener zoomListener = new SimpleZoomListener();
    zoomListener.setZoomState(mZoomState);

    mZoomView.setOnTouchListener(zoomListener);
    mZoomView.setZoomState(mZoomState);
    mZoomView.setImage(neu);

    resetZoomState();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (mLastUri != null) {
      int delrows = getContentResolver().delete(mLastUri, "", new String[0]);
      if (delrows == 0) {
        System.out.println("bild konnte nicht gelöscht werden");
      }
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

//    mBitmap.recycle();
    mZoomView.setOnTouchListener(null);
    mZoomState.deleteObservers();
  }

  /**
   * Reset zoom state and notify observers
   */
  private void resetZoomState() {
    mZoomState.setPanX(0.5f);
    mZoomState.setPanY(0.5f);
    mZoomState.setZoom(1f);
    mZoomState.notifyObservers();
  }

  @Override
  protected List<Button> getMenuButtons() {
    List<Button> buttons = new ArrayList<Button>();
    Button b = new Button(getBaseContext());
    buttons.add(b);
    b.setText("Speichern");
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MediaStore.Images.Media.insertImage(getContentResolver(), mZoomView.mBitmap, "MOSAIK", "MOSAIK_DESC");
      }
    });

    b = new Button(getBaseContext());
    b.setText("Senden an");
    buttons.add(b);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String url = MediaStore.Images.Media.insertImage(getContentResolver(), mZoomView.mBitmap, "MOSAIK", "MOSAIK_DESC");
        if (url != null && !url.isEmpty()) {
          mLastUri = Uri.parse(url);
          Intent sendIntent = new Intent(Intent.ACTION_SEND);
          sendIntent.setType("image/jpg");
          sendIntent.putExtra(Intent.EXTRA_STREAM, mLastUri);
          sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Mosiak");
          sendIntent.putExtra(Intent.EXTRA_TEXT, "Send Mosiak-Created Picture!");
          startActivityForResult(Intent.createChooser(sendIntent, "Aktion auswählen:"), 0);
        }
      }
    });

    return buttons;
  }
}
