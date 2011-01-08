package com.mnemonic.mosaic.create;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.mnemonic.mosaic.imageutils.RadiusRenderRandom;

public class CreateActivity extends Activity {

  private ImageAdapter mImageAdapter;
  private ProgressDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    GridView grid = new GridView(this);

    grid.setNumColumns(3);
    mImageAdapter = new ImageAdapter(this);
    grid.setAdapter(mImageAdapter);

    grid.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        showDialog(position);
      }
    });

    setContentView(grid);
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    Bitmap bMap = BitmapFactory.decodeFile(mImageAdapter.getImageForPosition(id).getAbsolutePath());

    return createPictureDialog(bMap);    //To change body of overridden methods use File | Settings | File Templates.
  }

  private Dialog createPictureDialog(final Bitmap bMap) {
    final Dialog dlg = new Dialog(this);

    LinearLayout top = new LinearLayout(this);

    LinearLayout panel;
    panel = new LinearLayout(this);
    panel.setPadding(5, 5, 5, 5);

    LinearLayout l = new LinearLayout(this);
    l.setPadding(2,2,2,2);
    l.setBackgroundColor(Color.WHITE);

    ImageView imageView;
    imageView = new ImageView(this);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(170,170));
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    imageView.setImageBitmap(bMap);

    l.addView(imageView);
    panel.addView(l);

    top.addView(panel);

    Button accept = new Button(this);
    accept.setText("accept");
    accept.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        dlg.dismiss();
        mapPictureNewRandom(bMap);
      }
    });
    Button other = new Button(this);
    other.setText("other");
    other.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        dlg.dismiss();
      }
    });

    LinearLayout but = new LinearLayout(this);
    but.setOrientation(LinearLayout.HORIZONTAL);
    but.addView(accept);
    but.addView(other);

    LinearLayout all = new LinearLayout(this);
    all.setOrientation(LinearLayout.VERTICAL);
    all.addView(but);
    all.addView(top);

    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(170, 170);
    all.setLayoutParams(params);
    dlg.setContentView(all);
    dlg.setTitle("Auswahl akzeptieren?");

    return dlg;    //To change body of overridden methods use File | Settings | File Templates.
  }

  private void showLoader(DialogInterface.OnShowListener listener) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setTitle("Create Mosaic");
      mProgressDialog.setMessage("Loading, please wait...");
      mProgressDialog.setOnShowListener(listener);
    }
    mProgressDialog.show();
  }

  private void mapPictureNewRandom(Bitmap bmap) {
    RadiusRenderRandom r = new RadiusRenderRandom();
    Dialog dlg = createPictureDialog(r.createMap(this, bmap));
    dlg.show();


//    RadiusRenderRandom renderTask = new RadiusRenderRandom(mapArray,
//        images,
//        baseImage,
//        hTiles,
//        vTiles,
//        tileWidth,
//        tileHeight,
//        optionBox.getColorCorrectPercent(),
//        optionBox.getTileRep(),
//        optionBox.getMinDistNum(),
//        optionBox.getTileOrder());
//    formProgressBar.setValue(0); // Set up the progress bar.
//    formProgressBar.setMaximum(renderTask.getTaskLen());
//    formProgressBar.setStringPainted(true);
//
//    renderTask.go();  // Start the task.

    /*SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());
                CheckboxPreference = prefs.getBoolean("checkboxPref", true);
                ListPreference = prefs.getString("listPref", "nr1");
                editTextPreference = prefs.getString("editTextPref",
                                "Nothing has been entered");
                ringtonePreference = prefs.getString("ringtonePref",
                                "DEFAULT_RINGTONE_URI");
                secondEditTextPreference = prefs.getString("SecondEditTextPref",
                                "Nothing has been entered");
                // Get the custom preference
                SharedPreferences mySharedPreferences = getSharedPreferences(
                                "myCustomSharedPrefs", Activity.MODE_PRIVATE);
                customPref = mySharedPreferences.getString("myCusomPref", "");*/
  }



  private void mapPicture(final Bitmap bMap) {
    try {
//      File fcreate = new File(getFilesDir(), "mapped_neuesbild.jpg");
//      boolean create = fcreate.createNewFile();

      final Bitmap outRast = Bitmap.createBitmap(bMap.getWidth(), bMap.getHeight(), bMap.getConfig());

      Dialog dlg = createPictureDialog(outRast);
      Thread t = new Thread(new Runnable(){
        @Override
        public void run() {
          long start = System.currentTimeMillis();
          int faktor = 4;
          for (int x=0; x<  bMap.getWidth(); x+=faktor) {
            for (int y=0; y<bMap.getHeight(); y+=faktor) {
              int r = 0;
              int g = 0;
              int b = 0;
              int counter = 0;
              for (int xs = 0; xs < faktor && x+xs < bMap.getWidth(); xs++) {
                for (int ys = 0; ys < faktor && y+ys < bMap.getHeight(); ys++) {
                  counter++;
                  int color = bMap.getPixel(x + xs, y + ys);
                  r += Color.red(color);
                  g += Color.green(color);
                  b += Color.blue(color);
                }
              }

              r = r / counter;
              g = g / counter;
              b = b / counter;

              for (int xs = 0; xs < faktor && x+xs < bMap.getWidth(); xs++) {
                for (int ys = 0; ys < faktor && y+ys < bMap.getHeight(); ys++) {
                  outRast.setPixel(x + xs, y + ys, Color.rgb(r, g, b));
                }
              }
            }
          }
          System.out.println("ganzes rendering dauert: " + (System.currentTimeMillis() - start));
        }
      });

      dlg.show();

      t.start();

    }catch (Exception e) {
      e.printStackTrace();
      // TODO: handle exception
    }
  }
}
