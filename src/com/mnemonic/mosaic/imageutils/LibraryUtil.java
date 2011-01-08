/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 08.01.11
 * Time: 15:25
 *
 */
package com.mnemonic.mosaic.imageutils;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;

public class LibraryUtil {
  private static LibraryUtil mLibraryUtil;
  private File[] mPictures;

  public static void initSingleton() {
    if (mLibraryUtil == null) {
      mLibraryUtil = new LibraryUtil();
      mLibraryUtil.openPictureDir();
    }
  }

  public static LibraryUtil getLibraryUtil() {
    if (mLibraryUtil == null) {
      throw new RuntimeException("call initSingleton first");
    }
    return mLibraryUtil;
  }

  public File[] getAvailablePictures() {
    return mPictures;
  }

  private void openPictureDir() {
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      File picdir2 = Environment.getExternalStorageDirectory();
      mPictures = picdir2.listFiles(new FileFilter(){
        @Override
        public boolean accept(File pathname) {
          String name = pathname.getName();
          return name.endsWith(".jpg") || name.endsWith(".png");
        }
      });
    }

    if (mPictures == null) {
      mPictures = new File[0];
    }
  }
}