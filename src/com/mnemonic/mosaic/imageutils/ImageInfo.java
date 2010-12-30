/************************************************
 *  imageUtils/imageInfo.java
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
 * imageInfo is simply the record structure for
 * an image/tile stored in an image library.
 * Class is serializable for storage purposes.
 ************************************************/

package com.mnemonic.mosaic.imageutils;

import java.io.Serializable;

public class ImageInfo implements Serializable {

  // Class variables.
  private String mFilePath;
  private int mColor;     // The RBG value for the image.

    // Constructor.
    public ImageInfo() {
    }

    // Accessor to set the RBG value for the image.
    public void setColor(int color) {
        this.mColor = color;
    }

    // Accessor to retrieve the RBG value for the image.
    public int getColor() {
        return this.mColor;
    }

  public String getFilePath() {
    return mFilePath;
  }

  public void setFilePath(String filePath) {
    mFilePath = filePath;
  }
}
