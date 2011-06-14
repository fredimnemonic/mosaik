package com.mnemonic.mosaic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.mnemonic.mosaic.create.CreateActivity;
import com.mnemonic.mosaic.gallery.GalleryActivity;
import com.mnemonic.mosaic.imageutils.LibraryUtil;
import com.mnemonic.mosaic.lib.MessageConst;
import com.mnemonic.mosaic.preferences.PreferencesActivity;

import java.util.ArrayList;
import java.util.List;

public class Mosaic extends BaseActivity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int[] colors = new int[]{getResources().getColor(R.color.gradient_end), getResources().getColor(R.color.gradient_start)};
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
    mMainPanel.setBackgroundDrawable(gradientDrawable);
    mMainPanel.setPadding(0, 5, 0, 0);
  }

  @Override
  protected List<Button> getMenuButtons() {
    List<Button> buttons = new ArrayList<Button>();

    Button b = new Button(getBaseContext());
    b.setText("Neues Mosaik");
    b.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        actionCreate();
      }
    });
    buttons.add(b);

    b = new Button(getBaseContext());
    b.setText("Gallery");
    b.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        actionGallery();
      }
    });
    buttons.add(b);

    b = new Button(getBaseContext());
    b.setText("Libraray erstellen");
    b.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        createImageLib();
      }
    });
    buttons.add(b);

    b = new Button(getBaseContext());
    b.setText("Einstellungen");
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getBaseContext(), PreferencesActivity.class));
      }
    });
    buttons.add(b);

    return buttons;
  }

  public void actionCreate() {
    Intent intent = new Intent(this, CreateActivity.class);
    startActivity(intent);
  }

  public void actionGallery() {
    Intent intent = new Intent(this, GalleryActivity.class);
    startActivity(intent);
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