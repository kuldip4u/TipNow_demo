package com.tipnow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;

public class MessageReceivedActivity extends Activity{
	
	private Vibrator vibrator; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messege_received);

		vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500); 
		Bundle extras = getIntent().getExtras();
		String message=null;
		message = extras.getString("payload");
		final AlertDialog alertDialog = new AlertDialog.Builder(MessageReceivedActivity.this).create();
		alertDialog.setTitle("New Message");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						alertDialog.dismiss(); 
						finish();
					}
				});
			} 
		});
		alertDialog.show();
	}
}