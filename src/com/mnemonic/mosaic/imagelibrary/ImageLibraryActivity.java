/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 08.01.11
 * Time: 21:46
 *
 */
package com.mnemonic.mosaic.imagelibrary;

import android.content.Context;
import com.mnemonic.mosaic.imageutils.LibraryCreate;

public class ImageLibraryActivity {

  public void createImageLib(Context context) {
      LibraryCreate creator = new LibraryCreate();
      creator.initImageLib(context);

  }
}
