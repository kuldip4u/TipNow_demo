package com.tipnow.pickmedia;

import com.tipnow.R;

//import com.tipnow.category.CategoryActivity;
//import com.tipnow.message.MessageActivity;

import android.app.Activity;
//import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;

public class CameraVideoActivity extends Activity{
	private CamcorderView camcorderView;
	private boolean recording = false; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		setContentView(R.layout.camcoder_preview); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		camcorderView = (CamcorderView) findViewById(R.id.camcorder_preview);
	}
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) 
        { 
      	  if (recording) { 
      		  	camcorderView.stopRecording();
               finish(); 
           } else { 
               recording = true; 
               camcorderView.startRecording(); 
           } 
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    }
	
}
