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
import com.mnemonic.mosaic.gallery.SingleZoomActivity;
import com.mnemonic.mosaic.imageutils.ImageUtil;

import java.util.List;

public class CreateActivity extends BaseActivity {

  private ImageAdapter mImageAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      GridView grid = new GridView(this);

      grid.setNumColumns(ImageAdapter.COLCOUNT);
      mImageAdapter = new ImageAdapter(this);
      grid.setAdapter(mImageAdapter);

      grid.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          showDialog(position);
        }
      });

      mMainPanel.addView(grid);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected List<Button> getMenuButtons() {
    return null;
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    return createPictureDialog(mImageAdapter.getImageForPosition(id).getAbsolutePath());
  }

  private Dialog createPictureDialog(final String bMappath) {
    BitmapFactory.Options o = new BitmapFactory.Options();
    o.inSampleSize = ImageUtil.getScalingFactor(bMappath, 200, 400);
    Bitmap bMap = BitmapFactory.decodeFile(bMappath, o);

    final Dialog dlg = new Dialog(this);

    LinearLayout all = new LinearLayout(getBaseContext());
    all.setOrientation(LinearLayout.HORIZONTAL);

    LinearLayout menupanel = new LinearLayout(getBaseContext());
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT);
    menupanel.setLayoutParams(params);
    menupanel.setBackgroundColor(Color.BLACK);
    menupanel.setOrientation(LinearLayout.VERTICAL);
    menupanel.setPadding(0, 0, 10, 0);

    Button accept = new Button(this);
    accept.setText("Create Mosaic");
    accept.setWidth(150);
    accept.setHeight(150);
    menupanel.addView(accept);
    accept.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dlg.dismiss();

        Intent singlegal = new Intent(getBaseContext(), SingleZoomActivity.class);
//        Intent singlegal = new Intent(getBaseContext(), SingleGalleryActivity.class);
        singlegal.putExtra("path", bMappath);
        startActivity(singlegal);
      }
    });

    Button other = new Button(this);
    other.setWidth(150);
    other.setHeight(150);
    other.setText("Other");
    menupanel.addView(other);
    other.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dlg.dismiss();
      }
    });



    all.addView(menupanel);

    LinearLayout mainpanel = new LinearLayout(getBaseContext());

    all.addView(mainpanel);
    all.setPadding(10, 10, 10, 10);

    ImageView imageView = new ImageView(this);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setImageBitmap(bMap);

    mainpanel.addView(imageView);

//    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(170, 170);
//    all.setLayoutParams(params);
    dlg.setContentView(all);
    dlg.setTitle("Auswahl akzeptieren?");

    return dlg;
  }
}
