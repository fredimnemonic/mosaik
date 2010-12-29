package com.mnemonic.mosaic.imageutils;

import java.util.LinkedList;
import java.util.Random;

public class TileRenderList extends LinkedList {

    // These are the objects that are stored in the tile lists.
    public static class recObj {
        public int xVal;
        public int yVal;
    }

    public TileRenderList() {}

    // Returns a randomized list of tiles, if type==1, or a linear list if type==0
    public static LinkedList getRandomTileList(int type, int hTiles, int vTiles) {
      TileRenderList tileList = new TileRenderList();  // The new, random list of tiles.
        // These loops are just to populate the list with the tiles in order.
        for (int x = 0; x < hTiles; x++) {         // Cycle through all the tiles (Horiz. and Vertic.)
            for (int y = 0; y < vTiles; y++) {
                recObj ro = new recObj();       // Create a new record with the coordinates of the tile.
                ro.xVal = x;
                ro.yVal = y;
                tileList.add(ro);
            }
        }

        if (type ==1) {
            // Now we shuffle up that list.
            Random rand = new Random();
            int randInt = 0;
            for (int count = 0; count < tileList.size(); count++) {  // Go through all of the objects.
                recObj ro = new recObj();
                ro = (recObj) tileList.get(count);
                randInt = rand.nextInt(tileList.size());
                tileList.set(count, tileList.get(randInt));    // Swap the next tile with a random other tile.
                tileList.set(randInt, ro);
                
            }
        }
        return tileList;
    }


}