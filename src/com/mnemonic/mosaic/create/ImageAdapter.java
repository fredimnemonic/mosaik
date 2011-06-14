package com.mnemonic.mosaic.create;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mnemonic.mosaic.imageutils.LibraryUtil;

import java.io.File;
import java.util.WeakHashMap;

public class ImageAdapter extends BaseAdapter {
  static final int COLCOUNT = 8;
  private CreateActivity mContext;
  GridView.LayoutParams mSize;
  private File[] mPictures;

  final WeakHashMap<String, Bitmap> mLoadedPictures;

  public ImageAdapter(CreateActivity c) {
    mContext = c;

    int dispwith = mContext.getWindowManager().getDefaultDisplay().getWidth();
    int picwith = dispwith / COLCOUNT;
    mSize = new GridView.LayoutParams(picwith, picwith);

    mPictures = LibraryUtil.getLibraryUtil().getAvailablePictures();
    mLoadedPictures = new WeakHashMap<String, Bitmap>();
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
    String path = mPictures[position].getAbsolutePath();

    LazyImageView imageView;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
      panel = new LinearLayout(mContext);
      panel.setPadding(3,3,3,3);
      panel.setBackgroundColor(Color.BLACK);

      imageView = new LazyImageView(mContext, this);
      imageView.setLayoutParams(mSize);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);

      LinearLayout l = new LinearLayout(mContext);
      l.setPadding(2,2,2,2);
      l.setBackgroundColor(Color.WHITE);
      l.addView(imageView);
      panel.setTag(imageView);
      panel.addView(l);
    } else {
      panel = (LinearLayout) convertView;
      imageView = (LazyImageView) panel.getTag();
      assert imageView != null;
    }
    
    imageView.drawTheBitmap(path);
    return panel;
  }
}
