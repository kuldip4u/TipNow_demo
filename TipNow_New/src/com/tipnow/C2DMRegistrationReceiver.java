package com.tipnow;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/

//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.os.Vibrator;


import android.util.Log;

public class C2DMRegistrationReceiver extends BroadcastReceiver
{
	//private Vibrator vibrator; 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) 
		{
			final String registrationId = intent.getStringExtra("registration_id");
			if(registrationId!=null)
				Log.v("New Registration Key=", registrationId);
			else
				Log.v("Registration-ID", "NULL VALUE RETURNED");
			String error = intent.getStringExtra("error");
			if(error!=null)
				Log.v("ERROR", error);
			createNotification(context, registrationId);
			saveRegistrationId(context, registrationId);
		}
		
	}

	private void saveRegistrationId(Context context, String registrationId) 
	{
		SharedPreferences settings = context.getSharedPreferences(AppConfig.appName,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(AppConfig.tipNow_Message_Key,registrationId);
		editor.commit();
		Log.v("New Registration Key=", "Saved in Memory: "+registrationId);
	}

	public void createNotification(Context context, String registrationId) 
	{
		// Below code is to Create Alert box after receiving Successful Registration message for C2DM sever
		
		/*vibrator=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500); 
		Intent intent = new Intent(context, RegistrationResultActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);*/
		
	}

}
