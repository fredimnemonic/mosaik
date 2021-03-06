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
import java.util.List;

public class ImageList {
  private List<ImageInfo> mImageList;

  public ImageList() {
    mImageList = new ArrayList<ImageInfo>();
  }

  public synchronized void add(ImageInfo image) {
    mImageList.add(image);
  }

  public ImageInfo get(int index) {
    return mImageList.get(index);
  }

  public int size() {
    return mImageList.size();
  }

  public synchronized void saveToDisc(FileOutputStream fos) throws IOException {
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeInt(mImageList.size());  // First, write the length of the list.
    for (ImageInfo anImagelib : mImageList) {  // Next, cycle through each tile,
      oos.writeObject(anImagelib);           // And write its object to the file.
    }

    oos.close();
    fos.close();
  }

  public synchronized void loadFromDisc(FileInputStream fis) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(fis);
    int numObjects = ois.readInt();

    // Read all of the tiles out of the library.
    for (int count = 0; count < numObjects; count++) {
      mImageList.add((ImageInfo) ois.readObject());
    }
  }
}
