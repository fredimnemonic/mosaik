package com.mnemonic.mosaic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.mnemonic.mosaic.create.CreateActivity;
import com.mnemonic.mosaic.gallery.GalleryActivity;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.preferences.PreferencesActivity;

public class Mosaic extends Activity {
  private ProgressBar myProgressBar;
  private int myProgress = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(com.mnemonic.mosaic.R.layout.main);
  }

  //wird über action in AndroidManifest.xml angesprochen
  @SuppressWarnings({"UnusedDeclaration"})
  public void actionCreate(View view) {
    Intent intent = new Intent(this, CreateActivity.class);
    startActivity(intent);
  }

  //wird über action in AndroidManifest.xml angesprochen
  @SuppressWarnings({"UnusedDeclaration"})
  public void actionSettings(View view) {
    Intent intent = new Intent(this, PreferencesActivity.class);
    startActivity(intent);
  }

  //wird über action in AndroidManifest.xml angesprochen
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

    final Handler progressHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        dialog.incrementProgressBy(1);
      }
    };

    // display the progressbar
    dialog.show();

    // create a thread for updating the progress bar
    Thread background = new Thread (new Runnable() {
      public void run() {
        LibraryUtil.getLibraryUtil().createImageLib(getBaseContext(), progressHandler);
        dialog.dismiss();
      }
    });

    // start the background thread
    background.start();

  }
}