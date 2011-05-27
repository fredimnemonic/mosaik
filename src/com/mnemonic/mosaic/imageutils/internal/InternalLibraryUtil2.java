/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 08.01.11
 * Time: 15:25
 *
 */
package com.mnemonic.mosaic.imageutils.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Toast;
import com.mnemonic.mosaic.R;
import com.mnemonic.mosaic.imageutils.ImageInfo;
import com.mnemonic.mosaic.imageutils.ImageList;
import com.mnemonic.mosaic.lib.MessageConst;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalLibraryUtil2 {
  private static final String MOSAIC_LIBNAME = "beer_mosaik.jml";

  private ImageList mImageLib;

  public ImageList getImageLib(Context context) {
    if (mImageLib == null) {
      mImageLib = new ImageList();
      try {
        FileInputStream fis = context.openFileInput(MOSAIC_LIBNAME);
        mImageLib.loadFromDisc(fis);
      } catch (IOException ioe) {
        Toast.makeText(context, "Fehler beim lesen der Library", Toast.LENGTH_LONG);
        ioe.printStackTrace();
      } catch (ClassNotFoundException e) {
        Toast.makeText(context, "Fehler beim lesen der Library", Toast.LENGTH_LONG);
        e.printStackTrace();
      }
    }

    return mImageLib;
  }

  public void createImageLib(Context context, Handler progressHandler) {
    ImageList imagelib = new ImageList();

    MeaningThread t = new MeaningThread(context, imagelib, progressHandler);
    t.start();

    try {
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    saveImageLib(context, imagelib);

    System.out.println("Done!");
  }

  private void saveImageLib(Context context, ImageList imagelib) {
    try {
      FileOutputStream fos = context.openFileOutput(MOSAIC_LIBNAME, Context.MODE_WORLD_WRITEABLE);
      imagelib.saveToDisc(fos);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private static int getMeanColor(Bitmap bMap) {
    int maxw = bMap.getWidth();
    int maxh = bMap.getHeight();

    int[] pixels = new int[maxw * maxh];

//    long start = System.currentTimeMillis();
    bMap.getPixels(pixels, 0, maxw, 0, 0, maxw, maxh);
//    System.out.println("getpixel dauert: " + (System.currentTimeMillis() - start));
//    System.out.println("Bitmapabmessung: " + bMap.getWidth() + " x " + bMap.getHeight());

    double red = 0, green = 0, blue = 0;
    for (int x = 0; x < maxw; x ++) {
      for (int y = 0; y < maxh; y ++) {
        int color = pixels[y * maxw + x];//umrechnung von zwei- in eindimensionale arrays
        red += Color.red(color);
        green += Color.green(color);
        blue += Color.blue(color);
      }
    }

    int max = maxh * maxw;
    red = red / max;
    green = green / max;
    blue = blue / max;

    return Color.argb(255, (int) red, (int) green, (int) blue);
  }

  private class MeaningThread extends Thread {
    private Context mContext;
    private ImageList mImageList;
    private Handler mProgressHandler;

    private MeaningThread(Context context, ImageList imageList, Handler progressHandler) {
      mContext = context;
      mImageList = imageList;
      mProgressHandler = progressHandler;
    }

    @Override
    public void run() {
      StringBuilder str = new StringBuilder();

      for (int beer = 901, i = R.drawable.beer0901; i < R.drawable.beer1805; i++, beer++) {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), i, o);
//        mContext.getApplicationContext().getResources().getDrawable(R.drawable.beer0901)
        //The new size we want to scale to
        final int REQUIRED_SIZE = 70;

        Bitmap b;
        if (o.outHeight > REQUIRED_SIZE || o.outWidth > REQUIRED_SIZE) {
          //Find the correct scale value. It should be the power of 2.
          int width_tmp = o.outWidth, height_tmp = o.outHeight;
          int scale = 1;
          while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
              break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
          }

          //Decode with inSampleSize
          BitmapFactory.Options o2 = new BitmapFactory.Options();
          o2.inSampleSize = scale;
          o2.inPurgeable = true;
          b = BitmapFactory.decodeResource(mContext.getResources(), i, o2);
        } else {
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inPreferQualityOverSpeed = false;
          options.inPurgeable = true;
          b = BitmapFactory.decodeResource(mContext.getResources(), i, options);
        }

        if (b != null) {
          ImageInfo imginfo = new ImageInfo(i, getMeanColor(b));
          if ((beer-901)%60==0) {
            str.append("\n");
          }
          if (beer >= 1000) {
            str.append("mImageList.add(new ImageInfo(R.drawable.beer" + (beer) + ", " + imginfo.getColor() + "));");
          } else {
            str.append("mImageList.add(new ImageInfo(R.drawable.beer0" + (beer) + ", " + imginfo.getColor() + "));");
          }
          mImageList.add(imginfo);
          b.recycle();
        } else {
          System.out.println("File ist null-> " + i);
        }
        mProgressHandler.sendEmptyMessage(0);
      }
      mProgressHandler.sendEmptyMessage(MessageConst.MessageFinish);
      System.out.println(str.toString());
    }
  }
}