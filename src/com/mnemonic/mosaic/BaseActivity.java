/**
 * Created by Fredimnemonic.
 *
 * User: Kollera
 * Date: 10.01.11
 * Time: 23:44
 *
 */
package com.mnemonic.mosaic;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public abstract class BaseActivity extends Activity {
  protected LinearLayout mMainPanel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);

    System.out.println("****************** " + getClass().getSimpleName() + ".onCreate");

    LinearLayout all = new LinearLayout(getBaseContext());
    all.setOrientation(LinearLayout.HORIZONTAL);

    List<Button> buttons = getMenuButtons();
    if (buttons != null) {
      LinearLayout menupanel = new LinearLayout(getBaseContext());
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT);
      menupanel.setLayoutParams(params);
      menupanel.setBackgroundColor(Color.BLACK);
      menupanel.setOrientation(LinearLayout.VERTICAL);
      menupanel.setPadding(10, 10, 10, 10);

      for (Button b : buttons) {
        b.setWidth(150);
        b.setHeight(150);
        menupanel.addView(b);
      }

      all.addView(menupanel);
    }

    mMainPanel = new LinearLayout(getBaseContext());

    all.addView(mMainPanel);

    int[] colors = new int[]{getResources().getColor(R.color.gradient_end), getResources().getColor(R.color.gradient_start)};
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
    all.setBackgroundDrawable(gradientDrawable);
    all.setPadding(0, 0, 0, 0);

    setContentView(all);
  }

  @Override
  public void onPanelClosed(int featureId, Menu menu) {
    super.onPanelClosed(featureId, menu);
    System.out.println("****************** " + getClass().getSimpleName() + ".onPanelClosed");
  }

  @Override
  public void onContentChanged() {
    super.onContentChanged();
    System.out.println("****************** " + getClass().getSimpleName() + ".onContentChanged");
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    System.out.println("****************** " + getClass().getSimpleName() + ".onLowMemory");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    System.out.println("****************** " + getClass().getSimpleName() + ".onDestroy");
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println("****************** " + getClass().getSimpleName() + ".onPause");
  }

  @Override
  protected void onResume() {
    super.onResume();
    System.out.println("****************** " + getClass().getSimpleName() + ".onResume");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    System.out.println("****************** " + getClass().getSimpleName() + ".onRestart");
  }

  @Override
  protected void onStart() {
    super.onStart();
    System.out.println("****************** " + getClass().getSimpleName() + ".onStart()");
//    Debug.startMethodTracing("mosaik");
  }

  @Override
  protected void onStop() {
    super.onStop();
    System.out.println("****************** " + getClass().getSimpleName() + ".onStop()");
//    Debug.stopMethodTracing();
  }

  protected abstract List<Button> getMenuButtons();
}
