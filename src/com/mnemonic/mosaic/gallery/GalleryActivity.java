package com.mnemonic.mosaic.gallery;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.mnemonic.mosaic.R;

public class GalleryActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout layout = new LinearLayout(getBaseContext());
    int[] colors = new int[]{getResources().getColor(R.color.gradient_end), getResources().getColor(R.color.gradient_start)};
    GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
    layout.setBackgroundDrawable(d);

    setContentView(layout);
  }
}
