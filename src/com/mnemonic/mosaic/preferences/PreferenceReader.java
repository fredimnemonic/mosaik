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

  public static String getRendererClass(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("renderer", "RadiusRenderRandom");
  }
}