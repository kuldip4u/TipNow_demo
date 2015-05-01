package com.tipnow.pickmedia;

import java.io.IOException;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.tipnow.R;
import com.tipnow.TipNowApplication;

public class CameraImageActivity extends Activity {

	private CameraPreview _preview;
	TipNowApplication tipNowApplication; 
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		setContentView(R.layout.camera_view);
		try {
			intent=getIntent();
			tipNowApplication = (TipNowApplication)getApplication();
			_preview = new CameraPreview(this);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
			FrameLayout frameLayoutCamera=(FrameLayout)findViewById(R.id.frameLayoutCamera);
			frameLayoutCamera.addView(_preview);	
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		
		
	}
	public void onClickBackButton(View v){
		finish();
	}
	public void onClickClickMe(View v){
		try {
			_preview._camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		//if(intent.getStringExtra("MEDIA").equalsIgnoreCase("IMAGE")){
				
		//}else if(intent.getStringExtra("MEDIA").equalsIgnoreCase("VIDEO")){
		//	_preview._camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		//}
	}
	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			//Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			//Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				tipNowApplication.setImagePath("NONE");
				tipNowApplication.set_imageData(data);	
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
			
			//finish();
		}
	};
	
	class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		SurfaceHolder _holder;
        Camera _camera;
        
        CameraPreview(Context context) {
        	super(context);
        	_holder = getHolder();
        	_holder.addCallback(this);
        	_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        
        public void surfaceCreated(SurfaceHolder holder) {
        	try {
        		_camera = Camera.open();
        		_camera.setPreviewDisplay(holder);
        	} catch (IOException exception) {
        		_camera.release();
        		_camera = null;
        		// TODO: more exception handling logic
        	}
        }
        
        public void surfaceDestroyed(SurfaceHolder holder) {
        	_camera.stopPreview();
        	_camera.release();
        	_camera = null;
        }
        
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        	Camera.Parameters parameters = _camera.getParameters();
        	parameters.setPreviewSize(w, h);
        	_camera.setParameters(parameters);
        	_camera.startPreview();
        }
	}
}
