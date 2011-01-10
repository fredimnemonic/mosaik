/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 18:07
 *
 */
package com.mnemonic.mosaic.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;
import com.mnemonic.mosaic.imageutils.renderer.RendererFactory;
import com.mnemonic.mosaic.preferences.PreferenceReader;

public class SingleGalleryActivity extends BaseActivity {


  @Override
  protected void onStart() {
    super.onStart();

    render();
  }

  private void render() {
    Bitmap tomasaic = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));

    String renderername = PreferenceReader.getRendererClass(getBaseContext());
    final ImageRendererBase renderer = RendererFactory.createRenderer(renderername, getBaseContext(), tomasaic);
    Bitmap neu = renderer.setUp();

    final ZoomableView view = new ZoomableView(getBaseContext(), neu);
    setContentView(view);

    Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
        view.invalidate();
      }
    };

    renderer.renderImage(handler);
  }
}
