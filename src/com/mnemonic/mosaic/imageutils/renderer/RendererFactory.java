/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 09.01.11
 * Time: 15:09
 *
 */
package com.mnemonic.mosaic.imageutils.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.lang.reflect.Constructor;

public class RendererFactory {

  public static ImageRendererBase createRenderer(String renderername, Context context, Bitmap bitmap) {
    try {
      Class rendererclass = Class.forName(RendererFactory.class.getPackage().getName() + "." + renderername);
      Constructor c = rendererclass.getDeclaredConstructor(Context.class, Bitmap.class);
      return (ImageRendererBase) c.newInstance(context, bitmap);
    } catch (Exception e) {
      Log.e(RendererFactory.class.getName(), "Renderer konnte nicht erstellt werden.", e);
      return new RadiusRenderRandom(context, bitmap);
    }
  }
}
