/**
 * Creator:
 * 20.04.11 20:53 Fredi Koller, AbaProject,SVM
 *
 * Maintainer:
 * Fredi Koller
 *
 * Last Modification:
 * $Id: $
 *
 * Copyright (c) 2011 ABACUS Research AG, All Rights Reserved
 */
package com.mnemonic.mosaic.create;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.mnemonic.mosaic.imageutils.ImageUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LazyImageView extends ImageView {

  private ImageAdapter mAdapter;

  public LazyImageView(Context context, final ImageAdapter adapter) {
    super(context);

    mAdapter = adapter;
  }

  void drawTheBitmap(final String path) {
    if (mAdapter.mLoadedPictures.containsKey(path)) {
      Bitmap bMap = mAdapter.mLoadedPictures.get(path);
      setImageBitmap(bMap);
      return;
    }

    final Handler handler = new Handler() {
      @Override
      public void handleMessage(Message message) {
        setImageDrawable((Drawable) message.obj);
      }
    };

    Thread thread = new Thread() {
      @Override
      public void run() {
        Bitmap bMap = null;
        if (mAdapter.mLoadedPictures.containsKey(path)) {
          bMap = mAdapter.mLoadedPictures.get(path);
        } else {
          try {
            int scale = ImageUtil.getScalingFactor(path, mAdapter.mSize.height, mAdapter.mSize.width);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPurgeable = true;
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(path), null, o2);

            if (b != null) {
              bMap = Bitmap.createScaledBitmap(b, mAdapter.mSize.width, mAdapter.mSize.height, false);
              b.recycle();
              mAdapter.mLoadedPictures.put(path, bMap);
            } else {
              System.out.println("File ist null-> " + path);
            }
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }
        if (bMap != null) {
          Message message = handler.obtainMessage(1, new BitmapDrawable(bMap));
          handler.sendMessage(message);
        }
      }
    };
    thread.start();
  }
}