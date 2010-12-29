/************************************************
 *  imageUtils/imageList.java
 *  Author: Jim Drewes, 2002
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *
 * imageList is a class that utilizes Java's
 * LinkedList functionality, and uses it for
 * storing image records (imageInfo.class) in
 * a list.
 ************************************************/

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
            if (ii.getFileName() == f) {          // If this is the record, return it.
                return ii;
            }
        }
        return null;  // No record found?  Return NULL.
    }

    // Returns an ImageInfo record basaed on its index in the list.
    public ImageInfo get(int i) {
        return (ImageInfo) images.get(i);
    }

    // Returns the file name/path of an image based on its index in the list.
    public String getName(int i) {
        ImageInfo ii = (ImageInfo) images.get(i);
        return ii.getFileName();
    }

    // Returns the file name, without path, of an image based on its index.
    public String getNameNoPath(int i) {
      ImageInfo ii = (ImageInfo) images.get(i);
      return ii.getFileName().substring(ii.getFileName().lastIndexOf('/') +1, ii.getFileName().length());  // String parsing.
    }

    // Removes an ImageInfo record at index "i".
    public void remove(int i) {
        images.remove(i);
        size--;
    }

    // Changes the ImageInfo record that is in a specific location.
    public void replace(int i, ImageInfo img) {
        images.set(i, img);
    }

    // This is used for debugging purposes.  It echos the list out to std_out.
    public void printList() {
        ListIterator li = images.listIterator();
        while (li.hasNext()) {
            ImageInfo ii = (ImageInfo) li.next();
            System.out.println("Name: " + ii.getFileName() + "   Color: " + ii.getColor());
        }
    }

    // Return the size of the list.
    public int getSize() {
        return size;
    }
}
