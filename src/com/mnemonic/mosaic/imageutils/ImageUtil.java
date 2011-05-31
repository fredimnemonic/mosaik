/**
 * Creator:
 * 31.05.11 22:03 Fredi Koller, AbaProject,SVM
 *
 * Maintainer:
 * Fredi Koller
 *
 * Last Modification:
 * $Id: $
 *
 * Copyright (c) 2011 ABACUS Research AG, All Rights Reserved
 */
package com.mnemonic.mosaic.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageUtil {

  public static int getScalingFactor(String imagePath, int expectedHeight, int expectedWidth) {

    BitmapFactory.Options o = new BitmapFactory.Options();
    o.inJustDecodeBounds = true;
    try {
      BitmapFactory.decodeStream(new FileInputStream(imagePath), null, o);

      Bitmap b;
      if (o.outHeight > expectedHeight || o.outWidth > expectedWidth) {
        //Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
          if (width_tmp / 2 < expectedWidth || height_tmp / 2 < expectedHeight)
            break;
          width_tmp /= 2;
          height_tmp /= 2;
          scale *= 2;
        }

        return scale;
      } else {
        return 1;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return 1;
  }
}
