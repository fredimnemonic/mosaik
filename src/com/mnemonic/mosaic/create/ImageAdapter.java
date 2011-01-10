package com.mnemonic.mosaic.create;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mnemonic.mosaic.imageutils.LibraryUtil;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

public class ImageAdapter extends BaseAdapter {
  private CreateActivity mContext;
  private GridView.LayoutParams mSize;
  private File[] mPictures;

  private Map<String, Bitmap> mLoadedPictures;

  public ImageAdapter(CreateActivity c) {
    mContext = c;

    int dispwith = mContext.getWindowManager().getDefaultDisplay().getWidth();
    int picwith = dispwith / 3;
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
    Bitmap bMap;
    String path = mPictures[position].getAbsolutePath();
    if (mLoadedPictures.containsKey(path)) {
      bMap = mLoadedPictures.get(path);
    } else {
      bMap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), mSize.width, mSize.height, false);
      mLoadedPictures.put(path, bMap);
    }

    if (convertView == null) {  // if it's not recycled, initialize some attributes
      panel = new LinearLayout(mContext);
      panel.setPadding(3,3,3,3);
      panel.setBackgroundColor(Color.BLACK);

      ImageView imageView;
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(mSize);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      imageView.setImageBitmap(bMap);

      LinearLayout l = new LinearLayout(mContext);
      l.setPadding(2,2,2,2);
      l.setBackgroundColor(Color.WHITE);
      l.addView(imageView);
      panel.setTag(imageView);
      panel.addView(l);
    } else {
      panel = (LinearLayout) convertView;
      ImageView view = (ImageView) panel.getTag();
      assert view != null;
      view.setImageBitmap(bMap);
    }

    return panel;
  }
}
