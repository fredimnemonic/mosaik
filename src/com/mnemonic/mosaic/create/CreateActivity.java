package com.mnemonic.mosaic.create;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.mnemonic.mosaic.BaseActivity;
import com.mnemonic.mosaic.gallery.SingleGalleryActivity;

public class CreateActivity extends BaseActivity {

  private ImageAdapter mImageAdapter;

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
    return createPictureDialog(mImageAdapter.getImageForPosition(id).getAbsolutePath());
  }

  private Dialog createPictureDialog(final String bMappath) {
    Bitmap bMap = BitmapFactory.decodeFile(bMappath);

    final Dialog dlg = new Dialog(this);

    LinearLayout top = new LinearLayout(this);

    LinearLayout panel;
    panel = new LinearLayout(this);
    panel.setPadding(5, 5, 5, 5);

    LinearLayout l = new LinearLayout(this);
    l.setPadding(2,2,2,2);
    l.setBackgroundColor(Color.WHITE);

    ImageView imageView = new ImageView(this);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setImageBitmap(bMap);

    l.addView(imageView);
    panel.addView(l);

    top.addView(panel);

    Button accept = new Button(this);
    accept.setText("Create Mosaic");
    accept.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        dlg.dismiss();

        Intent singlegal = new Intent(getBaseContext(), SingleGalleryActivity.class);
        singlegal.putExtra("path", bMappath);
        startActivity(singlegal);
      }
    });

    Button other = new Button(this);
    other.setText("Other");
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

    return dlg;
  }
}
