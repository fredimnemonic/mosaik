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
import android.os.Debug;
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


    lbmemory.setBackgroundColor(Color.RED);
    lbmemory.setTextColor(Color.BLUE);
    lbtime.setBackgroundColor(Color.YELLOW);
    lbtime.setTextColor(Color.BLACK);

    zoomlayout.invalidate();

    Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
//        String message = msg.getData().getString("time");
//        if (message != null && !message.isEmpty()) {
//          //todo
//        }
    	ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    	Debug.MemoryInfo dmi = new Debug.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    	activityManager.getMemoryInfo(mi);

//        lbmemory.setText(
//        		"available: " + mi.availMem + "\n" +
//        		"schwelle : " + mi.threshold + "\n" + 
//        		"lowmemory: " + mi.lowMemory);

        Debug.getMemoryInfo(dmi);
        lbtime.setText(
        		"dalvikPrivateDirty	: " + dmi.dalvikPrivateDirty/1024 + "\n" +
        		"nativePrivateDirty	: " + dmi.nativePrivateDirty/1024 + "\n" + 
        		"nativePss 			: " + dmi.nativePss/1024 + "\n" + 
        		"nativeSharedDirty  : " + dmi.nativeSharedDirty/1024 + "\n" + 
        		"otherPrivateDirty	: " + dmi.otherPrivateDirty/1024 + "\n" + 
        		"otherPss 			: " + dmi.otherPss/1024 + "\n" + 
        		"getTotalPrivateDirty: " + dmi.getTotalPrivateDirty()/1024 + "\n" + 
        		"getTotalPss		: " + dmi.getTotalPss()/1024 + "\n" + 
        		"getTotalSharedDirty: " + dmi.getTotalSharedDirty()/1024 + "\n" + 
        		"dalvikSharedDirty 	: " + dmi.otherSharedDirty/1024);
        

        view.invalidate();
      }
    };

    renderer.renderImage(handler);
  }
}
