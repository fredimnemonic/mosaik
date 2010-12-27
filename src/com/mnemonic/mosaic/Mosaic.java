package com.mnemonic.mosaic;

import com.mnemonic.mosaic.create.CreateActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Mosaic extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mnemonic.mosaic.R.layout.main);
    }
    
    public void actionCreate(View view) {
    	Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}