package com.tipnow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


public class RegistrationResultActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AlertDialog alertDialog = new AlertDialog.Builder(RegistrationResultActivity.this).create();
		alertDialog.setTitle("New Message");
		alertDialog.setMessage("Registered successfully to receive messages.");
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
