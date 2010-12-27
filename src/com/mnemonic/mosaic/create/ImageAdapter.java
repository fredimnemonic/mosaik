package com.mnemonic.mosaic.create;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.FileFilter;

public class ImageAdapter extends BaseAdapter {
  private CreateActivity mContext;
  private File[] mPictures;
  private GridView.LayoutParams mSize;

  public ImageAdapter(CreateActivity c) {
    mContext = c;

    boolean mExternalStorageAvailable;
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
      // We can read and write the media
      mExternalStorageAvailable = true;
    } else //noinspection RedundantIfStatement
      if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        // We can only read the media
        mExternalStorageAvailable = true;
      } else {
        // Something else is wrong. It may be one of many other states, but all we need
        //  to know is we can neither read nor write
        mExternalStorageAvailable = false;
      }

    if (mExternalStorageAvailable) {
      File picdir2 = Environment.getExternalStorageDirectory();
      mPictures = picdir2.listFiles(new FileFilter(){

        @Override
        public boolean accept(File pathname) {
          String name = pathname.getName();
          return name.endsWith(".jpg") || name.endsWith(".png");
        }});

      if (mPictures == null) {
        mPictures = new File[0];
      }
    }

    int dispwith = mContext.getWindowManager().getDefaultDisplay().getWidth();
//        dispwith = dispwith - 15;
    int picwith = dispwith / 3;
    mSize = new GridView.LayoutParams(picwith, picwith);
  }

  public int getCount() {
    return mPictures.length;
  }

  public Object getItem(int position) {
    return null;
  }

  public long getItemId(int position) {
    return position;
  }

  public File getImageForPosition(int position) {
    return mPictures[position];
  }

  // create a new ImageView for each item referenced by the Adapter
  public View getView(int position, View convertView, ViewGroup parent) {

    LinearLayout panel;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
      panel = new LinearLayout(mContext);
      panel.setPadding(3,3,3,3);
      panel.setBackgroundColor(Color.BLACK);

      LinearLayout l = new LinearLayout(mContext);
      l.setPadding(2,2,2,2);
      l.setBackgroundColor(Color.WHITE);

      ImageView imageView;
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(mSize);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);

      Bitmap bMap = BitmapFactory.decodeFile(mPictures[position].getAbsolutePath());
      imageView.setImageBitmap(bMap);

      l.addView(imageView);
      panel.setTag(imageView);
      panel.addView(l);
    } else {
      Bitmap bMap = BitmapFactory.decodeFile(mPictures[position].getAbsolutePath());

      panel = (LinearLayout) convertView;
      ImageView view = (ImageView) panel.getTag();
      assert view != null;
      view.setImageBitmap(bMap);
    }

    return panel;
  }
}
