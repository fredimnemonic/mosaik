package com.mnemonic.mosaic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.mnemonic.mosaic.create.CreateActivity;
import com.mnemonic.mosaic.gallery.GalleryActivity;
import com.mnemonic.mosaic.settings.SettingsActivity;

public class Mosaic extends Activity {
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
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  //wird über action in AndroidManifest.xml angesprochen
  @SuppressWarnings({"UnusedDeclaration"})
  public void actionGallery(View view) {
    Intent intent = new Intent(this, GalleryActivity.class);
    startActivity(intent);
  }
}