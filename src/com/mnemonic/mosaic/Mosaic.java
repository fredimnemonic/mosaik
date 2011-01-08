package com.mnemonic.mosaic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.mnemonic.mosaic.create.CreateActivity;
import com.mnemonic.mosaic.gallery.GalleryActivity;
import com.mnemonic.mosaic.imagelibrary.ImageLibraryActivity;
import com.mnemonic.mosaic.preferences.PreferencesActivity;

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
        ImageLibraryActivity imglib = new ImageLibraryActivity();
        imglib.createImageLib(getBaseContext());
        break;

      case R.id.menu_preferences:
        Intent pref = new Intent(getBaseContext(), PreferencesActivity.class);
        startActivity(pref);
        break;

      default:
        return super.onOptionsItemSelected(item);
    }

    return  true;
  }
}