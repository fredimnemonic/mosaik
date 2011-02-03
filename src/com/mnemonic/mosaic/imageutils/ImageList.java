/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 00:35
 *
 */
package com.mnemonic.mosaic.imageutils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageList {
  private Map<String, Bitmap> mBitmaps;
  private List<ImageInfo> mImageList;

  public ImageList() {
    mImageList = new ArrayList<ImageInfo>();
    mBitmaps = new WeakHashMap<String, Bitmap>();
  }

  synchronized void add(ImageInfo image) {
    mImageList.add(image);
  }

  public ImageInfo get(int index) {
    return mImageList.get(index);
  }

  public int size() {
    return mImageList.size();
  }

  synchronized void saveToDisc(FileOutputStream fos) throws IOException {
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeInt(mImageList.size());  // First, write the length of the list.
    for (ImageInfo anImagelib : mImageList) {  // Next, cycle through each tile,
      oos.writeObject(anImagelib);           // And write its object to the file.
    }

    oos.close();
    fos.close();
  }

  synchronized void loadFromDisc(FileInputStream fis) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(fis);
    int numObjects = ois.readInt();

    // Read all of the tiles out of the library.
    for (int count = 0; count < numObjects; count++) {
      mImageList.add((ImageInfo) ois.readObject());
    }
  }
  
  public synchronized Bitmap getBitmap(int index) {
	if (!mBitmaps.containsKey(get(index).getFilePath())) {
	  BitmapFactory.Options o = new BitmapFactory.Options();
	  o.inPurgeable = true;
	  
	  Bitmap m = BitmapFactory.decodeFile(get(index).getFilePath(), o);

	  mBitmaps.put(get(index).getFilePath(), m);
	}
    return mBitmaps.get(get(index).getFilePath());
  }
}
