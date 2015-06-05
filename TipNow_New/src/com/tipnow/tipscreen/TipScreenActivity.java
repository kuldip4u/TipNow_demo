package com.tipnow.tipscreen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
//import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tipnow.AppConfig;
import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.category.CategoryActivity;
import com.tipnow.message.MessageActivity;
import com.tipnow.pickmedia.PickMediaActivity;


public class TipScreenActivity extends Activity{
	boolean loaded = false;
	static HashMap<String, String> resultMap;
	private static Handler Thandler;
	private Thread SendingThread;
	private static ProgressDialog dialog;
	Intent TipTitle;
	String appType="",Title="",Customer_Name="", Category_Name="",Org_Id="",Latitude="",Longitude="",address_id="";
	EditText titlefield,editTextDescription;
	ImageView imageViewAttachment;
	String attachmentFlag="",UDID,Device_Token="";
	TipNowApplication tipNowApplication;
	Activity context;
	String afterTipSentMessage="ERROR";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip_screen);
		context=this;
		Thandler= new Handler();
		tipNowApplication=(TipNowApplication)getApplication();
		tipNowApplication.set_imageData(null);
		tipNowApplication.setVideoFlag(false);
		editTextDescription= (EditText)findViewById(R.id.editTextDescription);
		imageViewAttachment=(ImageView)findViewById(R.id.imageViewAttachment);
		imageViewAttachment.setVisibility(View.INVISIBLE);
		TipTitle= getIntent();
		
		Customer_Name = TipTitle.getStringExtra("CUSTOMER_NAME");
		Category_Name = TipTitle.getStringExtra("CATEGORYNAME");
		Org_Id 	= TipTitle.getStringExtra("ORG_ID");
		Title	= TipTitle.getStringExtra("TipTitle").toString();
		UDID 	= (tipNowApplication.getUDID()== null || tipNowApplication.getUDID().equalsIgnoreCase(""))? getUDID() : tipNowApplication.getUDID();
		Latitude= TipTitle.getStringExtra("LATITUDE");
		Longitude = TipTitle.getStringExtra("LONGITUDE");
		address_id = TipTitle.getStringExtra("Address_Id");
		appType = TipTitle.getStringExtra("appType");
		//Log.d("Get Address ID", address_id);
		//Log.d("Get ORG ID", Org_Id);
		titlefield=(EditText)findViewById(R.id.editextTipTitle);
	    titlefield.setText(Title);
	    titlefield.clearFocus();
	    editTextDescription.requestFocus();
	    
	    editTextDescription.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(editTextDescription.getText().toString().trim().equalsIgnoreCase(""))
					editTextDescription.setError("Description Required");
				else
					editTextDescription.setError(null);
			}
		});
	    
	}
	
	private String getUDID(){
		 TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 String DeviceId, SerialNum, androidId;
		 String mydeviceId;
		
		 
	        DeviceId = tm.getDeviceId();
	        SerialNum = tm.getSimSerialNumber();
	        androidId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
	        
	        mydeviceId = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID); 
	        mydeviceId = mydeviceId.trim();
	       // UUID deviceUuid = new UUID(androidId.hashCode(), ((long)DeviceId.hashCode() << 32) | SerialNum.hashCode());
	       // mydeviceId = deviceUuid.toString().trim();
	        tipNowApplication.setUDID(mydeviceId!=null? mydeviceId : "");
	        return tipNowApplication.getUDID();
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
	
	public void onClickLinearLayoutOutside(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	public void onClickAddMedia(View v){
		AlertDialog.Builder customDialog  = new AlertDialog.Builder(TipScreenActivity.this);
		LayoutInflater layoutInflater  = (LayoutInflater)getApplicationContext().
	     					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=layoutInflater.inflate(R.layout.media_item,null);
		customDialog.setView(view);
		final AlertDialog dialog = customDialog.create();
	  
		Button buttonAddImage = (Button)view.findViewById(R.id.buttonAddImage);
		buttonAddImage.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				attachmentFlag="IMAGE";
				Intent intent = new Intent(TipScreenActivity.this,PickMediaActivity.class);
				intent.putExtra("Media", "IMAGE");
				startActivity(intent);
			}});
		Button buttonAddVideo = (Button)view.findViewById(R.id.buttonAddVideo);
		buttonAddVideo.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				attachmentFlag="VIDEO";
				Intent intent = new Intent(TipScreenActivity.this,PickMediaActivity.class);
				intent.putExtra("Media", "VIDEO");
				startActivity(intent);
				dialog.dismiss();
			}});
		Button buttonCancel = (Button)view.findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}});
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	public void onClickBackButton(View v){
		finish();
	}
	
	public void onClickSendTip(View v){
		/*Log.v("SendTip_Post", "Data: UDID="+UDID+
				", CATEGORY="+Category_Name+", Sub_Category="+
				Title+", Customer_Name="+Customer_Name+
				", Device_Token="+Device_Token+", Title="+titlefield.getText().toString().trim()+
				", Decs="+editTextDescription.getText().toString().trim()+
				", Org_Id="+Org_Id+", Latitude="+Latitude+", Longitude="+Longitude);*/
		Thandler= new Handler();
		boolean flag=false;
		if(titlefield.getText().toString().trim().equalsIgnoreCase("")){
				titlefield.setError("Title Required");
				flag=true;
				titlefield.requestFocus();
		}
		if(editTextDescription.getText().toString().trim().equalsIgnoreCase("")){
			if(titlefield.getText().toString().trim().equalsIgnoreCase(""))
				titlefield.requestFocus();
			else
				editTextDescription.requestFocus();
			editTextDescription.setError("Description Required");
			flag=true;
		}
		if(flag){
			//titlefield.requestFocus();
		}else{
			if(checkInternetConnection()){
				if(editTextDescription.getText().toString().trim().contains("'") || titlefield.getText().toString().trim().contains("'")){
					
					editTextDescription.getText().toString().replace("'", "");
					titlefield.getText().toString().replace("'", "");
					
					dialog=ProgressDialog.show(context, "Please Wait", "Sending...");
					SendingThread = new SendTipThread();
					SendingThread.start();
					
					/*final AlertDialog alertDialog = new AlertDialog.Builder(TipScreenActivity.this).create();
					alertDialog.setMessage("' not allowed in Title or Description.");
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
					alertDialog.show();*/
				}else{
					dialog=ProgressDialog.show(context, "Please Wait", "Sending...");
					SendingThread = new SendTipThread();
					SendingThread.start();
				}
			}else{
				final AlertDialog alertDialog = new AlertDialog.Builder(TipScreenActivity.this).create();
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(attachmentFlag.equalsIgnoreCase("")){
			
		}else if(attachmentFlag.equalsIgnoreCase("IMAGE")){
			if(tipNowApplication.get_imageData()==null)
				imageViewAttachment.setVisibility(View.INVISIBLE);
			else{
				//Log.v("Tip-OnResume", "Image_Paths="+tipNowApplication.getImagePath());
				//Log.v("Tip-OnResume", "Image_Data(Bytes)="+tipNowApplication.get_imageData());
				imageViewAttachment.setVisibility(View.VISIBLE);
			}
		}else if(attachmentFlag.equalsIgnoreCase("VIDEO")){
			if(tipNowApplication.isVideoFlag()==true)
				imageViewAttachment.setVisibility(View.VISIBLE);
			else
				imageViewAttachment.setVisibility(View.INVISIBLE);
		}
	}
	
	// Sending the Tip using Web service.
	class SendTipThread extends Thread{
		@Override
		public void run() {
			sendTipWithImage();
			
			Thandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					if(afterTipSentMessage!=null && afterTipSentMessage!=""){
						if(afterTipSentMessage.equalsIgnoreCase("SUCCESS")){
						soundPlay();
						final AlertDialog alertDialog = new AlertDialog.Builder(TipScreenActivity.this).create();
						alertDialog.setMessage("You have successfully sent the tip.");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										alertDialog.dismiss();
										tipNowApplication.set_imageData(null);
										titlefield.setText("");
										editTextDescription.setText("");
										editTextDescription.setError(null);
										imageViewAttachment.setVisibility(View.INVISIBLE);
										tipNowApplication.set_imageData(null);
										tipNowApplication.setVideoFile(null);
									}
								});
							} 
						});
						alertDialog.show();
						}else if(afterTipSentMessage.equalsIgnoreCase("ERROR")){
							final AlertDialog alertDialog = new AlertDialog.Builder(TipScreenActivity.this).create();
                			alertDialog.setTitle("Alert");
                			alertDialog.setMessage("Tip not sent.");
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
				}
			});
		}
	}
	
	public void soundPlay(){
		MediaPlayer mPlayer2;
		mPlayer2= MediaPlayer.create(this, R.raw.jinglekey);
		mPlayer2.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    setMenuBackground();
	    return true;
	}
	
	protected void setMenuBackground(){
		getLayoutInflater().setFactory( new Factory() {
			@Override
			public View onCreateView ( String name, Context context, AttributeSet attrs ) {
				if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
					try { // Ask our inflater to create the view
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView( name, null, attrs );
						// Kind of apply our own background
						new Handler().post( new Runnable() {
							public void run () {
								view.setBackgroundResource( R.drawable.rounded_corner_darker);
								((TextView) view).setTextColor(Color.WHITE);
							}
						} );
						return view;
					}
					catch ( InflateException e ) {
					}
					catch ( ClassNotFoundException e ) {
					}
				}
				return null;
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuItemCategories:     
        	Intent intentCategory = new Intent(TipScreenActivity.this,CategoryActivity.class);
        	startActivity(intentCategory);
        	finish();
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(TipScreenActivity.this,MessageActivity.class);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}
	
	public String getDateTime(){
		String time=null;
    	Calendar c = Calendar.getInstance(); 
    	int seconds = c.get(Calendar.SECOND);
    	int min=c.get(Calendar.MINUTE);
    	int hour=c.get(Calendar.HOUR);
    	int dd=c.get(Calendar.DAY_OF_MONTH);
    	int mm=c.get(Calendar.MONTH);
    	int yy=c.get(Calendar.YEAR);
    	if(c.get(Calendar.AM_PM)==0)
    		time=yy+"-"+mm+"-"+dd+"-"+hour+"-"+min+"-"+seconds+"-AM";
    	else if(c.get(Calendar.AM_PM)==1)
        	time=yy+"-"+mm+"-"+dd+"-"+hour+"-"+min+"-"+seconds+"-PM";
    	return time;
    }
	
	public void sendTipWithImage(){
		try {
			Log.v("sendTipWithImage", "sendTipWithImage");
			TelephonyManager mTelephonyMgr;
			
			
		    mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
			
		    //TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		    String  mPhoneNumber =  "N/A";
		    if(mTelephonyMgr != null){
		    	mPhoneNumber = mTelephonyMgr.getLine1Number();
	            if(mPhoneNumber== null || mPhoneNumber.length() == 0) {
	            	mPhoneNumber ="N/A";
	            }
		    }else
		    	mPhoneNumber ="N/A";
		    
		    
          

            Log.v("mPhoneNumber", mPhoneNumber);
			String number = getIntent().getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
//			HttpPost httpPost = new HttpPost("http://ec2-184-72-193-81.compute-1.amazonaws.com/plus.tipnow.net/api/tips/newtip.php");
			HttpPost httpPost = new HttpPost("http://www.tipnowplus.com/api/tips/newtip.php");		
			//HttpPost httpPost = new HttpPost("http://mobileapp.tipnow.net/api/tips/newtip.php");
			//HttpPost httpPost = new HttpPost("http://ec2-184-72-193-81.compute-1.amazonaws.com/mobileapp.tipnow.net/api/tips/newtip.php");
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			SharedPreferences messageKey = getSharedPreferences(AppConfig.appName,0);
			
			
			entity.addPart("udid", new StringBody(UDID));
			//entity.addPart("udid", new StringBody("1234567890"));
			entity.addPart("title", new StringBody(titlefield.getText().toString().trim()));
			entity.addPart("desc", new StringBody(editTextDescription.getText().toString().trim()));
			entity.addPart("category", new StringBody(Category_Name));
			entity.addPart("subcategory", new StringBody(Title));
			entity.addPart("latitude", new StringBody(""+Latitude));
			entity.addPart("longitude", new StringBody(""+Longitude));
			entity.addPart("deviceToken", new StringBody(messageKey.getString(AppConfig.tipNow_Message_Key, "0")));
			entity.addPart("org_id", new StringBody(Org_Id));
			entity.addPart("deviceType", new StringBody("Android"));
			entity.addPart("Customer_Name", new StringBody(Customer_Name));
			entity.addPart("address_id", new StringBody(address_id));
			entity.addPart("appType", new StringBody(appType));
			entity.addPart("mobile_phone", new StringBody(mPhoneNumber));
			System.out.println("udid="+UDID+",title="+titlefield.getText().toString().trim()+
					",desc="+editTextDescription.getText().toString().trim()+
					",category="+Category_Name+",subcategory="+Title+
					",latitude="+""+Latitude+",longitude="+""+Longitude+
					",deviceToken="+messageKey.getString(AppConfig.tipNow_Message_Key, "0")+
					",org_id="+Org_Id+",deviceType=Android,Customer_Name="+Customer_Name+"App Type"+appType);
			if(tipNowApplication.isVideoFlag()==false){
				if(tipNowApplication.get_imageData()==null){
					entity.addPart("image_uploaded", new StringBody(""));
				}else{
					if(tipNowApplication.getImagePath().equalsIgnoreCase("NONE")){
						entity.addPart("image_uploaded", new ByteArrayBody(tipNowApplication.get_imageData(), getDateTime()+".jpg"));
					}else{
						entity.addPart("image_uploaded", new ByteArrayBody(tipNowApplication.get_imageData(), tipNowApplication.getImagePath()));
					}
				}
				entity.addPart("video_uploaded",new StringBody(""));
			}else if(tipNowApplication.isVideoFlag()==true){
				entity.addPart("image_uploaded", new StringBody(""));
				if(tipNowApplication.getVideoFile()==null){
					entity.addPart("video_uploaded",new StringBody(""));
				}  else {
					entity.addPart("video_uploaded", new ByteArrayBody(tipNowApplication.getVideoData(), tipNowApplication.getVideoName()));
					tipNowApplication.setVideoFlag(false);
				}
			}
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
			    total.append(line);
			}
			afterTipSentMessage = "SUCCESS";
			Log.v("SendTip-Response=", total.toString());
		} catch (Exception e) {
			e.printStackTrace();
			afterTipSentMessage="ERROR";
			
		}
	}
}