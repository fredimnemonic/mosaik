/************************************************
 *  plugins/radiusRenderRandom.java
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
 * radiusRenderRandom renders an image where tiles CAN be
 * repeated, but not within a certain "radius."  In
 * this implementation, the radius isn't a true radius,
 * but rather a box.  Otherwise, the tiles are selected
 * just as the medianEvaluator tiles are selected.
 * In future versions, this class may be removed, and
 * radius rendering may be implemented as an option to pass
 * into other render functions.  Also, rather than
 * rendering the tiles sequentially, it randomly
 * selects which tile to render next.  The commented out
 * "Rendering in parts" thing will be pulled out into
 * a different class later.  It allows for rendering of
 * extremely large images (10,000 x 10,000 pixels, etc.)
 ************************************************/

package com.mnemonic.mosaic.imageutils;

import android.graphics.Color;

import javax.media.jai.TiledImage;
import java.awt.image.RenderedImage;
import java.util.LinkedList;


public class RadiusRenderRandom {

  // Class variables.
  private int hTiles;                  // The number of tiles in the final image, horizontally.
  private int vTiles;                  // The number of tiles in the final image, vertically.
  private int pixWidth;                // The width of one tile.
  private int pixHeight;               // The height of one tile.
  private ImageList tileLibrary;       // The library of tiles we can pull from.
    private RenderedImage baseImage;     // The base image.
  private TiledImage newImage;         // The final image we are creating.
  private int[][] mapArray;          // The color map, showing how the base image pixillates.
  private int colorCorrectPercent;
  private int tileRepType;
  private int minDist;
  private int tileOrder;              // 1 for random, 0 for linear.

  private final String pluginName = "Radius Render (Random)";  // Overwrite this to give your plugin a name.


  /** Creates new radiusRenderRandom */
  public RadiusRenderRandom(int[][] ma, ImageList tileLib, RenderedImage baseImg, int h, int v, int pw, int ph, int ccp, int trt, int md, int to) {
    this.mapArray = ma;   // Set up all of the class variables.
    this.hTiles = h;
    this.vTiles = v;
    this.pixWidth = pw;
    this.pixHeight = ph;
    this.tileLibrary = tileLib;
    this.baseImage = baseImg;
    this.colorCorrectPercent = ccp;
    this.tileRepType = trt;
    this.minDist = md;
    this.tileOrder = to;
  }

  // Returns the completed image.
  public TiledImage getNewImage() {
    return this.newImage;
  }

  /***********************************************************
   DO NOT EDIT THIS UNLESS YOU KNOW WHAT YOU ARE DOING!
   ************************************************************/
  public void go() {  // Thread stuff.
    Render render = null;
    try {
      render =  new Render();
    } catch (Exception e) {
      System.out.println("Thread Error!");
      e.printStackTrace();
    }
  }
//************************************************************

  class Render {
    // findBestFit returns the index of a tile that best matches a certain color.
    // And it also makes sure it is not a duplicate image in the radius.
    private int findBestFit(int c, int[][] tileArray, int x, int y) {
      int radiusTiles = 5;  // The number of radius tiles... this WILL be user-configurable.
      int closestSoFar = 0;  // Index of the tile that best matches the color so far.
      int redDiff, greenDiff, blueDiff, totalDiff = 0;
      totalDiff = (256*3);  // Initialize the total difference to the largest reasonable number.

      for (int count = 0; count < tileLibrary.getSize(); count++) {  // Cycle through all of the library tiles.
        if ( !TileChecker.checkPlacement( tileRepType, minDist, tileArray, x, y, count, hTiles, vTiles) ) {  // If this tile isn't in the box, find the difference in color.
          redDiff = Math.abs(Color.red(c) - Color.red(tileLibrary.get(count).getColor()));
          blueDiff = Math.abs(Color.blue(c) - Color.blue(tileLibrary.get(count).getColor()));
          greenDiff = Math.abs(Color.green(c) - Color.green(tileLibrary.get(count).getColor()));
          if (((redDiff + blueDiff + greenDiff) < totalDiff)) { // if this is closer than the previous closest...
            totalDiff = redDiff + blueDiff + greenDiff;
            closestSoFar = count;  // Keep track of this tile.
          }
        }
      }
      return closestSoFar;  // return the tile we chose.
    }

    // The actual rendering algorithm.
    Render() {
      int imgNum;
//      SampleModel sm = baseImage.getSampleModel();
///******* Comment the following TiledImage block to render in parts. ********/
//      newImage = new TiledImage(baseImage.getMinX(),
//          baseImage.getMinY(),
//          (pixWidth * hTiles),
//          (pixHeight * vTiles),
//          baseImage.getTileGridXOffset(),
//          baseImage.getTileGridYOffset(),
//          baseImage.getSampleModel(),
//          baseImage.getColorModel());
///***************************************************************************/
      RenderedImage smallImage;
      int tileArray[][] = new int[hTiles][vTiles];
      //LinkedList ll = Utils.getRandomTileList(1, hTiles, vTiles); // Get the shuffled list of tiles.
      LinkedList ll = TileRenderList.getRandomTileList(tileOrder, hTiles, vTiles);
      TileRenderList.recObj ro = new TileRenderList.recObj();
      // First, select all the tiles.
      for (int count = 0; count < ll.size(); count++) {  // Go through the tile list.
        ro = (TileRenderList.recObj) ll.get(count);  // Get the x/y data from this tile.
        int x = ro.xVal;
        int y = ro.yVal;
        try {
          Thread.sleep(5);
          tileArray[x][y] = findBestFit(mapArray[x][y], tileArray, x, y);  // Find an image for this tile.
        } catch (Exception e) {}
      }

/*********  Uncomment following to render in parts.  ************/
      /*int numBreaks = 10;
   int hBreak = hTiles / numBreaks;
   int hMin = 0;
   int hMax = hBreak;*/
/****************************************************************/
      // Now, we actually build the image (sequentially) from the chosen tiles.
      for (int x = 0; x < hTiles; x++) {  // Cycle through the image horizontally.

/*********  Uncomment following to render in parts.  ************/
        /*if ((x % hBreak) == 0) {
              hMax = x + hBreak;
              hMin = x;
              if ((hTiles - hMax) < hBreak) {
                  hMax = hTiles;
              }

              if (hMin != 0) {
              // Save Image....
                  System.out.println("Saving image to file...");
                  BMPSaveType bst = new BMPSaveType();
                  bst.saveImageToFile(newImage, "/home/jimdrewes/Images/new" + hMin + ".bmp");
                  System.out.println("Done saving!");
              }

              newImage = new TiledImage(baseImage.getMinX(),
                                 baseImage.getMinY(),
                                 (pixWidth * (hMax - hMin)),
                                 (pixHeight * vTiles),
                                 baseImage.getTileGridXOffset(),
                                 baseImage.getTileGridYOffset(),
                                 baseImage.getSampleModel(),
                                 baseImage.getColorModel());
       } */
/****************************************************************/

        for (int y = 0; y < vTiles; y++) {  // Cycle through the horizontal tiles.
          try {
//            smallImage = ImageLoader.getRenderedImage(tileLibrary.get(tileArray[x][y]).getFileName());  // load the tile image.
//            smallImage = imageOps.scale(smallImage, pixWidth, pixHeight);  // Scale it down.
//            TiledImage outTest = colorCorrect.Correct(tileLibrary.get(tileArray[x][y]).getColor(), smallImage, colorCorrectPercent);
            // Set a region of interest to paste the tile into on the final image.
//            ROIShape myROI = new ROIShape(new Rectangle((x*pixWidth), (y*pixHeight), smallImage.getWidth(), smallImage.getHeight()));
/*********  Uncomment followingh to render in parts.  ************/
//                    ROIShape myROI = new ROIShape(new Rectangle(((x-hMin)*pixWidth), (y*pixHeight), smallImage.getWidth(), smallImage.getHeight()));
/****************************************************************/
//            ParameterBlock pb = new ParameterBlock();
//            pb.addSource(outTest);
/*********  Uncomment following to render in parts.  ************/
//                        pb.add((float)((x-hMin)*pixWidth));
/****************************************************************/
//            pb.add((float)(x*pixWidth));
//            pb.add((float)(y*pixHeight));
//            pb.add(new InterpolationNearest());
//            smallImage = JAI.create("translate", pb, null);  // Move the image to the correct spot.
//            newImage.setData(smallImage.getData(), myROI);   // Paste the small tile image into the final image.
          } catch (Exception e) {e.printStackTrace();}
        }
      }
    }
  }
}