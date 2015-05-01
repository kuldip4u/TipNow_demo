package com.tipnow.pickmedia;

import com.tipnow.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class AndroidIntentVideoRecording extends Activity {
	
	final static int REQUEST_VIDEO_CAPTURED = 1;
	Uri uriVideo = null;
	VideoView videoviewPlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainone);
        Button buttonRecording = (Button)findViewById(R.id.recording);
        Button buttonPlayback = (Button)findViewById(R.id.playback);
        videoviewPlay = (VideoView)findViewById(R.id.videoview);
        
        buttonRecording.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
			}});
        
        buttonPlayback.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				if(uriVideo == null){
					//Toast.makeText(AndroidIntentVideoRecording.this,"No Video",Toast.LENGTH_LONG).show();
				}else{
					//Toast.makeText(AndroidIntentVideoRecording.this, "Playback: " + uriVideo.getPath(),Toast.LENGTH_LONG).show();
					videoviewPlay.setVideoURI(uriVideo);
					videoviewPlay.start();
				}
			}});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_VIDEO_CAPTURED){
				uriVideo = data.getData();
				//Toast.makeText(AndroidIntentVideoRecording.this,uriVideo.getPath(),Toast.LENGTH_LONG).show();
			}
		}else if(resultCode == RESULT_CANCELED){
			uriVideo = null;
			//Toast.makeText(AndroidIntentVideoRecording.this,"Cancelled!",Toast.LENGTH_LONG).show();
		}
	}
    
    /*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuItemCategories:     
        	Intent intentCategory = new Intent(AndroidIntentVideoRecording.this,CategoryActivity.class);
        	startActivity(intentCategory);
        	finish();
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(AndroidIntentVideoRecording.this,MessageActivity.class);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}*/
}