package com.mnemonic.mosaic.imageutils;

import java.util.LinkedList;
import java.util.ListIterator;

public class ImageList {
  // Class variables.
  private LinkedList<ImageInfo> images;  // The list of images.
  private int size;           // The size of the list.

    // Constructor -- initializes variables, and creates a new LinkedList.
    public ImageList() {
        this.size = 0;
        this.images = new LinkedList<ImageInfo>();
    }

    // Clears the list and sets the size to zero.
    public void clear() {
        this.size = 0;
        this.images.clear();
        
    }

    // Adds an imageInfo record into the list at location "i".
    public void put(ImageInfo i) {
        images.add(i);
        this.size++;
    }

    // Returns an imageInfo record based on a particular file name/path.
    public ImageInfo get(String f) {
      int index = 0;
      ListIterator li = images.listIterator();     // Iterate through the LinkedList.
        while (li.hasNext()) {
            ImageInfo ii = (ImageInfo) li.next();
            if (ii.getFilePath().equals(f)) {          // If this is the record, return it.
                return ii;
            }
        }
        return null;  // No record found?  Return NULL.
    }

    // Returns an ImageInfo record basaed on its index in the list.
    public ImageInfo get(int i) {
        return (ImageInfo) images.get(i);
    }

    // Return the size of the list.
    public int getSize() {
        return size;
    }
}
