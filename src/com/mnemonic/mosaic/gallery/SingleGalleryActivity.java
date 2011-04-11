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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;
import com.mnemonic.mosaic.imageutils.renderer.RendererFactory;
import com.mnemonic.mosaic.lib.MessageConst;
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
    final Bitmap neu = renderer.setUp();

    LinearLayout layout = new LinearLayout(getBaseContext());

    final ZoomableView zoomView = new ZoomableView(getBaseContext(), neu);

    Button b = new Button(getBaseContext());
    b.setText("Speichern");
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          MediaStore.Images.Media.insertImage(getContentResolver(), neu, "mosaik_" + System.currentTimeMillis() + ".jpg", "");

//          File pics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//          File loc = new File(pics, "mosaik_" + System.currentTimeMillis() + ".jpg");
//          if (loc.exists() || loc.createNewFile()) {
//            FileOutputStream out = new FileOutputStream(loc);
//            neu.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    layout.addView(b);
    b = new Button(getBaseContext());
    b.setText("Aktualisieren");
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          zoomView.invalidate();
          zoomView.refreshDrawableState();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    layout.addView(b);
    layout.addView(zoomView);

    setContentView(layout);

    Handler handler = new Handler(){
      int mCounter = 0;
      @Override
      public void handleMessage(Message msg) {
        zoomView.invalidate();
        if (msg.what == MessageConst.MessageFinish) {
          mCounter ++;
        }
        if (mCounter == 2) {
          Toast.makeText(getBaseContext(), "Mosaik ist fertig", Toast.LENGTH_LONG).show();
        }

      }
    };

    renderer.renderImage(handler);
  }
}
