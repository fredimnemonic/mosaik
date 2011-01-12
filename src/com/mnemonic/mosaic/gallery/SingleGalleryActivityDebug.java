/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 18:07
 *
 */
package com.mnemonic.mosaic.gallery;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.R;
import com.mnemonic.mosaic.imageutils.renderer.ImageRendererBase;
import com.mnemonic.mosaic.imageutils.renderer.RendererFactory;
import com.mnemonic.mosaic.preferences.PreferenceReader;

public class SingleGalleryActivityDebug extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.singlemosaic);
  }

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
    FrameLayout zoomlayout = (FrameLayout) findViewById(R.id.layout_single_zoom);
    final TextView lbmemory = (TextView) findViewById(R.id.layout_single_memory);
    final TextView lbtime = (TextView) findViewById(R.id.layout_single_time);

    zoomlayout.setBackgroundColor(getResources().getColor(R.color.background));
    zoomlayout.addView(view);

    final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    activityManager.getMemoryInfo(mi);


//    Debug.MemoryInfo mi = new Debug.MemoryInfo();
//    Debug.getMemoryInfo(mi);
    long availableMegs = mi.availMem / 1048576L;
    long trhesmem = mi.threshold / 1048576L;


    lbmemory.setText("AV-Mem: " + availableMegs);
    lbmemory.setBackgroundColor(Color.RED);
    lbmemory.setTextColor(Color.BLUE);
    lbtime.setBackgroundColor(Color.RED);
    lbtime.setTextColor(Color.BLACK);
    lbtime.setText("TH-Mem: " + trhesmem);

    zoomlayout.invalidate();

    Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
//        String message = msg.getData().getString("time");
//        if (message != null && !message.isEmpty()) {
//          //todo
//        }
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        long trhesmem = mi.threshold / 1048576L;

        lbmemory.setText("AV-Mem: " + availableMegs + " \nAV-Mem: " + availableMegs);
        lbtime.setText("TH-Mem: " + trhesmem);

        view.invalidate();
      }
    };

    renderer.renderImage(handler);
  }
}
