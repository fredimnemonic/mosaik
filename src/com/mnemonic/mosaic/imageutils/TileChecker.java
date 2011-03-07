package com.mnemonic.mosaic.imageutils;

public class TileChecker {

  public static boolean checkPlacement( TileAlgorithmus algorithmus, int minTileDist, int[][] tileArray, int currX, int currY, int currIndex, int arraySizeX, int arraySizeY) {
    boolean invalidTile = false;

    if ( algorithmus == TileAlgorithmus.SINGLE ) {
      for (int newX = 0; newX < arraySizeX; newX++) {
        for (int newY = 0; newY < arraySizeY; newY++) {
          if (tileArray[newX][newY] != 0 && tileArray[newX][newY] == currIndex ) {
            invalidTile = true;
          }
        }
      }
    } else if ( algorithmus == TileAlgorithmus.MULTIPLE ) {
      invalidTile = false; // Because you can always put tiles next to one another.
    } else if (algorithmus == TileAlgorithmus.RADIUS) {   // Radius render.
      for (int newX = (currX - minTileDist); newX < (currX+minTileDist); newX++) {  // Cycle through the width of the box...
        for (int newY = (currY - minTileDist); newY < (currY+minTileDist); newY++) {  // Cycle through the height of the box...
          // If we are still within the bounds of the image....
          //                if ((newX >= 0) && (newY >= 00) && (newX < hTiles) && (newY < vTiles)) { // && !((newX >= x) && (newY >= y))) {
          try {
            if (tileArray[newX][newY] != 0 && tileArray[newX][newY] == currIndex) {  // If that tile is already in the box...
              invalidTile = true;
            }
          } catch (Exception e) {}  // The tile we are checking is outside of the area.
          //                }
        }
      }
    }
    return invalidTile;
  }
}