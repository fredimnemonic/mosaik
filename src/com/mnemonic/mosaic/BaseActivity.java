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
import android.os.Bundle;
import android.view.Menu;

public class BaseActivity extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    System.out.println(getClass().getSimpleName() + ".onCreate");
  }

  @Override
  public void onPanelClosed(int featureId, Menu menu) {
    super.onPanelClosed(featureId, menu);
    System.out.println(getClass().getSimpleName() + ".onPanelClosed");
  }

  @Override
  public void onContentChanged() {
    super.onContentChanged();
    System.out.println(getClass().getSimpleName() + ".onContentChanged");
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    System.out.println(getClass().getSimpleName() + ".onLowMemory");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    System.out.println(getClass().getSimpleName() + ".onDestroy");
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println(getClass().getSimpleName() + ".onPause");
  }

  @Override
  protected void onResume() {
    super.onResume();
    System.out.println(getClass().getSimpleName() + ".onResume");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    System.out.println(getClass().getSimpleName() + ".onRestart");
  }

  @Override
  protected void onStart() {
    super.onStart();
    System.out.println(getClass().getSimpleName() + ".onStart()");
//    Debug.startMethodTracing("mosaik");
  }

  @Override
  protected void onStop() {
    super.onStop();
    System.out.println(getClass().getSimpleName() + ".onStop()");
//    Debug.stopMethodTracing();
  }
}
