package com.mnemonic.mosaic.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.mnemonic.mosaic.imageutils.LibraryCreate;

public class SettingsActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout layout = new LinearLayout(this);
    Button btnlib = new Button(this);
    btnlib.setText("Create Library");

    btnlib.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        setupLib();
      }
    });

    layout.addView(btnlib);
    setContentView(layout);
  }

  private void setupLib() {



//    try {
//      openFileInput("mosaik.jml");
//      System.out.println("verfügbar");
//    } catch (FileNotFoundException e) {
      LibraryCreate creator = new LibraryCreate();
      creator.initImageLib(this);
//    }
  }
}
