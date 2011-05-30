package com.mnemonic.mosaic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.mnemonic.mosaic.create.CreateActivity;
import com.mnemonic.mosaic.gallery.GalleryActivity;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.lib.MessageConst;
import com.mnemonic.mosaic.preferences.PreferencesActivity;

public class Mosaic extends BaseActivity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainlayout);

    Button b = (Button) findViewById(R.id.btnCreate);
    b.setHeight(150);
    b.setWidth(150);

    b = (Button) findViewById(R.id.btnGallery);
    b.setHeight(150);
    b.setWidth(150);

    int[] colors = new int[]{getResources().getColor(R.color.gradient_end), getResources().getColor(R.color.gradient_start)};
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
    layout.setBackgroundDrawable(gradientDrawable);
    layout.setPadding(0, 5, 0, 0);
  }

  //wird �ber action in AndroidManifest.xml angesprochen
  @SuppressWarnings({"UnusedDeclaration"})
  public void actionCreate(View view) {
    Intent intent = new Intent(this, CreateActivity.class);
    startActivity(intent);
  }

  //wird �ber action in AndroidManifest.xml angesprochen
  @SuppressWarnings({"UnusedDeclaration"})
  public void actionGallery(View view) {
    Intent intent = new Intent(this, GalleryActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.menu_photolib:
        createImageLib();
        break;

      case R.id.menu_preferences:
        startActivity(new Intent(getBaseContext(), PreferencesActivity.class));
        break;

      default:
        return super.onOptionsItemSelected(item);
    }

    return  true;
  }

  private void createImageLib() {
    final ProgressDialog dialog = new ProgressDialog(this);
    dialog.setCancelable(true);
    dialog.setMessage("Loading...");
    // set the progress to be horizontal
    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    // reset the bar to the default value of 0
    dialog.setProgress(0);

    dialog.setMax(LibraryUtil.getLibraryUtil().getAvailablePictures().length);
    // display the progressbar
    dialog.show();

    final Handler progressHandler = new Handler() {
      private int mFinishCount;

      @Override
      public void handleMessage(Message msg) {
        long what = msg.what;
        if (what == MessageConst.MessageFinish) {
          mFinishCount ++;
          if (mFinishCount == LibraryUtil.THREADCOUNT) {
            System.out.println("FISHED");
            dialog.dismiss();
            Toast.makeText(getBaseContext(), "Imagelib has been created", Toast.LENGTH_LONG).show();
          }
        } else {
          dialog.incrementProgressBy(1);
        }
      }
    };


    // create a thread for updating the progress bar
    Thread background = new Thread (new Runnable() {
      public void run() {
        //        InternalLibraryUtil2 internal = new InternalLibraryUtil2();

        LibraryUtil.getLibraryUtil().createImageLib(getBaseContext(), progressHandler);
      }
    });

    // start the background thread
    background.start();
  }
}