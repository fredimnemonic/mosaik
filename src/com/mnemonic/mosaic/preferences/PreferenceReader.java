/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 11:56
 *
 */
package com.mnemonic.mosaic.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mnemonic.mosaic.imageutils.TileAlgorithmus;

public class PreferenceReader {

  public static int getTileCount(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String tilecount = prefs.getString("tilecount", "20");

    try {
      return Integer.parseInt(tilecount);
    } catch (NumberFormatException e) {
      return 20;
    }
  }

  public static int getTileBetween(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String tilecount = prefs.getString("tileabstand", "3");

    try {
      return Integer.parseInt(tilecount);
    } catch (NumberFormatException e) {
      return 3;
    }
  }

  public static TileAlgorithmus getTileAlgo(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String tilecount = prefs.getString("algo", "0");

    try {
      return TileAlgorithmus.values()[Integer.parseInt(tilecount)];
    } catch (NumberFormatException e) {
      return TileAlgorithmus.SINGLE;
    }
  }

  public static String getRendererClass(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("renderer", "RadiusRenderRandom");
  }
}