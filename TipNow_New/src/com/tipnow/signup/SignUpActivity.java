package com.tipnow.signup;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.Validations;
import com.tipnow.myparserhelper;
import com.tipnow.category.CategoryActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
//import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
//import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SignUpActivity extends Activity{
	EditText editTextFirstName,editTextLastName,editTextEmailID;
	//Button buttonSignUp;
	String DeviceId, SerialNum, androidId;
	String mydeviceId;
	TipNowApplication tipNowApplication;
	Activity context;
	String fname,lname,emailID;
	HashMap<String, String> resultMap;
	private static Handler Thandler;
	private static ProgressDialog dialog;
	private Thread SigningThread;
	UUID deviceUuid=null;
	Validations validations;
	//private String android_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		context = this;
		Thandler= new Handler();
		validations = new Validations();
		tipNowApplication=(TipNowApplication)getApplication();
		editTextFirstName=(EditText)findViewById(R.id.editTextFirstName);
		editTextLastName=(EditText)findViewById(R.id.editTextLastName);
		editTextEmailID=(EditText)findViewById(R.id.editTextEmailID);
		//buttonSignUp=(Button)findViewById(R.id.buttonSignUp);
		
		TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        try{
         DeviceId = tm.getDeviceId();
         //SerialNum = tm.getSimSerialNumber();
         //androidId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
         //Log.v("Unique-DeviceID", "="+androidId);
         //android_id= Secure.getString(this.getContext().getContentResolver(), Secure.ANDROID_ID);
         //deviceUuid= new UUID(androidId.hashCode(), ((long)DeviceId.hashCode() << 32) | SerialNum.hashCode());
         //mydeviceId = deviceUuid.toString().trim();
         mydeviceId = DeviceId.toString().trim();
         //Log.v("DEVICE-ID", "="+mydeviceId);
        }catch (Exception e) {
        	//Log.e("SignUp-OnCreate Error", "="+e.toString());
		}
		
	}
	public void onClickSubmit(View v){
		if(checkInternetConnection()){
			Thandler= new Handler();
			dialog=ProgressDialog.show(context, "Please Wait", "Authenticating..");
			SigningThread = new SignUpThread();
			SigningThread.start();
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
			 alertDialog.setMessage("No network available.");
			 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int which) {
					 runOnUiThread(new Runnable() {
						 @Override
						 public void run() {
							 alertDialog.dismiss();
						 }
					 });
				 } 
			 });
			 alertDialog.show();
		}
	}
	
	private void SavePreferences(String key, String value){
	    SharedPreferences sharedPreferences = getSharedPreferences("TIPNOW",MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
	
	class SignUpThread extends Thread{
		@Override
		public void run() {
			String query=null;
			try{
				if(editTextFirstName.getText().toString().trim()==null ||
						editTextFirstName.getText().toString().trim().equalsIgnoreCase(""))
					fname="NA";
				else
					fname=editTextFirstName.getText().toString().trim();
				if(editTextLastName.getText().toString().trim()==null ||
						editTextLastName.getText().toString().trim().equalsIgnoreCase(""))
					lname="NA";
				else
					lname=editTextLastName.getText().toString().trim();
				if(editTextEmailID.getText().toString().trim()==null ||
						editTextEmailID.getText().toString().trim().equalsIgnoreCase(""))
					emailID="NA";
				else
					emailID=editTextEmailID.getText().toString().trim();
				tipNowApplication.setUDID(mydeviceId);
				SavePreferences("FIRST_NAME", fname);
				SavePreferences("LAST_NAME", lname);
				SavePreferences("EMAIL_ID", emailID);
				
				query="optin/index.php?" +
					"key="+URLEncoder.encode(tipNowApplication.getApplicationKey().toString().trim(),"UTF-8")+
					"&fn="+URLEncoder.encode(editTextFirstName.getText().toString().trim(),"UTF-8")+
					"&ln="+URLEncoder.encode(editTextLastName.getText().toString().trim(),"UTF-8")+
					"&ea="+URLEncoder.encode(editTextEmailID.getText().toString().trim(),"UTF-8")+
					"&id="+URLEncoder.encode(mydeviceId,"UTF-8");
			}catch (Exception e) {
				//System.out.println("SignUpURLError="+e.toString());
			}
			new myparserhelper(context, new SignUpXmlHandler(context), query);
			Thandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					resultMap = SignUpXmlHandler.getResultMap();
					if(resultMap==null){
						//System.out.println("SignUp-Parsing Error, No Value returned.");
					}else{
						if(resultMap.get("result").trim().equalsIgnoreCase("Success")){
							//System.out.println("SignUpSuccess="+resultMap.get("value"));
						}else if(resultMap.get("result").trim().equalsIgnoreCase("Failure")){
							//System.out.println("SignUpError="+resultMap.get("value"));
						}
					}
					Intent intent = new Intent(SignUpActivity.this,CategoryActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	} // End of SignUpThread class
	
	public void onClickLinearLayoutOutside(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	private boolean checkInternetConnection() {
		android.net.ConnectivityManager conMgr = (android.net.ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if (conMgr.getActiveNetworkInfo() != null && 
				conMgr.getActiveNetworkInfo().isAvailable() && 
				conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
}
