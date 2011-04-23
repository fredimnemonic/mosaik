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
        Bitmap bMap;
        if (mAdapter.mLoadedPictures.containsKey(path)) {
          bMap = mAdapter.mLoadedPictures.get(path);
        } else {
          BitmapFactory.Options o = new BitmapFactory.Options();
          o.inPurgeable = true;
          bMap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path, o), mAdapter.mSize.width, mAdapter.mSize.height, false);
          mAdapter.mLoadedPictures.put(path, bMap);
        }

        Message message = handler.obtainMessage(1, new BitmapDrawable(bMap));
        handler.sendMessage(message);
      }
    };
    thread.start();
  }
}