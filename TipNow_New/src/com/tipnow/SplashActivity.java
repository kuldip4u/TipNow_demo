package com.tipnow;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.tipnow.category.CategoryActivity;
import com.tipnow.issues.IssueActivity;
import com.tipnow.orgcustomer.CustomerList;

public class SplashActivity extends Activity {

	String firstName,lastName,emailID;
	SharedPreferences messageKey;
	private static int SPLASH_TIME_OUT = 3000;
	//String androidId;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        messageKey = getSharedPreferences(AppConfig.appName,0);
        if(messageKey.getString(AppConfig.tipNow_Message_Key, null)==null){
			resgisterOnC2DM();
		}
        new Handler().postDelayed(new Runnable() {
	       	  
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	String category_name = messageKey.getString("CATEGORYNAME", null);
            	System.out.println("category_name====>"+category_name);
            	 if(category_name!= null){
            		Intent i= new Intent(SplashActivity.this,IssueActivity.class);
         			i.putExtra("CATEGORYNAME", messageKey.getString("CATEGORYNAME", null));
         			i.putExtra("CUSTOMER_NAME", messageKey.getString("CUSTOMER_NAME", null));
         			i.putExtra("ORG_ID", messageKey.getString("ORG_ID", null));
         			i.putExtra("Address_Id", messageKey.getString("Address_Id", null));
         			i.putExtra("appType", messageKey.getString("appType", null));
         			i.putExtra("LATITUDE", messageKey.getString("LATITUDE", null));
         			i.putExtra("LONGITUDE", messageKey.getString("LONGITUDE", null));
         			startActivity(i);
         			finish();
         		}
            	
        		
        		else{
        			startActivity(new Intent(SplashActivity.this, CategoryActivity.class));
        			finish();
        		}           }
        }, SPLASH_TIME_OUT);
    	
    	//TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
    	//Log.v("DeviceID", "="+tm.getDeviceId());
    	//Log.v("SimSerialNo", "="+tm.getSimSerialNumber());
    	
    	//androidId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
        //Log.v("Unique-DeviceID", "="+androidId);
    	/*LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate (R.layout.splash,(ViewGroup) findViewById(R.id.linearLayoutSplashScreen));
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.FILL,0,0);
		toast.setDuration(Toast.LENGTH_LONG);
	
		toast.setView(layout);
		toast.show();
		new Handler().post(new Runnable() {
			@Override	 
			public void run() {
				//LoadPreferences();
				//if((firstName==null || firstName.trim().equalsIgnoreCase("")) && 
				//		(lastName==null || lastName.trim().equalsIgnoreCase("")) && 
				//		(emailID==null || emailID.trim().equalsIgnoreCase("")))
				//{
				//	startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
				//	finish();	 
				//}
				//else {
				    
					startActivity(new Intent(SplashActivity.this, CategoryActivity.class));
					finish();
				//}
			}
		});*/
	}

	/*private void LoadPreferences(){
	    SharedPreferences sharedPreferences = getSharedPreferences("TIPNOW",MODE_PRIVATE);
	    firstName = sharedPreferences.getString("FIRST_NAME", "");
	    lastName = sharedPreferences.getString("LAST_NAME", "");
	    emailID = sharedPreferences.getString("EMAIL_ID", "");
	}*/
	public void resgisterOnC2DM(){
		try{
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", "cyril.rayan@gmail.com");//ravetest2012@gmail.com
		startService(registrationIntent);
		}catch (Exception e) {
			
		}
	}
}