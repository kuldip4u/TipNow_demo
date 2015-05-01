package com.tipnow.category;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
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
import android.widget.Button;
import android.widget.TextView;

//import java.util.UUID;
import com.tipnow.LocationFinder;
import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.chooselocation.ChooseLocationActivity;
import com.tipnow.message.MessageActivity;
import com.tipnow.orgcustomer.CustomerList;

public class CategoryActivity extends Activity implements LocationListener {
	boolean locationFlag=false;
	Activity myContext;
	private Thread LocationThread;
	private static Handler Thandler;
	String provider;
	public final static String AUTH ="";
	TipNowApplication application;
	Double lat=0d, lng=0d;
	String location_Access=null;
	String category=null;
	String DeviceId, SerialNum, androidId;
	String mydeviceId;
	LocationManager locationManager;
	String bestProvider;
	String categoryName; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_screen);
	    application= (TipNowApplication)getApplication();
	    LoadPreferences();
	    myContext=this;
	    Thandler= new Handler();
	    TelephonyManager tm = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        
        DeviceId = tm.getDeviceId();
        SerialNum = tm.getSimSerialNumber();
        androidId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
        
        mydeviceId = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID); 
        mydeviceId = mydeviceId.trim();
       // UUID deviceUuid = new UUID(androidId.hashCode(), ((long)DeviceId.hashCode() << 32) | SerialNum.hashCode());
       // mydeviceId = deviceUuid.toString().trim();
        application.setUDID(mydeviceId);
        
        provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	   // register();
	}
	
	public void onClickCorporate(View v){
		categoryName = "corporate";
		
		if(checkInternetConnection()){
			if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
				category="corporate";
				openAlertDialog();
			}else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
				categoryName = "corporate";
				application.setcategory("CORPORATE");
				try{
					findCurrentLocation();
				}catch (Exception e) {
					e.toString();
				}
			}else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
				categoryName = "corporate";
				application.setcategory("CORPORATE");
			
				Intent intent;
			    intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
			    intent.putExtra("CATEGORYNAME", "corporate");
			    startActivity(intent);
			}
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
	
	
	public void onClickMalls(View v){
		categoryName = "malls";
		if(checkInternetConnection()){
			if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
				category="malls";
				openAlertDialog();
			}else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
				categoryName = "malls";
				application.setcategory("MALLS");
				try{
					findCurrentLocation();
				}catch (Exception e) {
					e.toString();
				}
			}else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
				categoryName = "malls";
				application.setcategory("MALLS");
				application.setLatitude("");
				application.setLongitude("");
				Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
				intent.putExtra("CATEGORYNAME", "malls");
				startActivity(intent);
			}
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
	
	
	public void onClickRetail(View v){
		categoryName = "retail";
		if(checkInternetConnection()){
			try {
				if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
					category="retail";
					openAlertDialog();
				} else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
					categoryName = "retail";
					application.setcategory("RETAIL");
					try{
						findCurrentLocation();
					}catch (Exception e) {
						e.toString();
					}
				} else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
					categoryName = "retail";
					application.setcategory("RETAIL");
					Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
					intent.putExtra("CATEGORYNAME", "retail");
					startActivity(intent);
				}	
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("Exception", e.toString());
			}
		} else {
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
	
	
	public void onClickMedia(View v){
		categoryName = "media";
		if(checkInternetConnection()){
			if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
				category="media";
				openAlertDialog();
			}else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
				categoryName = "media";
				application.setcategory("MEDIA");
				try{
					findCurrentLocation();
				}catch (Exception e) {
					e.toString();
				}
			}else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
				categoryName = "media";
				application.setcategory("MEDIA");
				Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
				intent.putExtra("CATEGORYNAME", "media");
				startActivity(intent);
			}
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
	
	
	public void onClickCities(View v){
		categoryName = "Cities";
		if(checkInternetConnection()){
			if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
				category="Cities";
				openAlertDialog();
			}else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
				categoryName = "Cities";
				application.setcategory("CITIES");
				try{
					findCurrentLocation();
				}catch (Exception e) {
					e.toString();
				}
			}else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
				categoryName = "city";
				application.setcategory("CITIES");
				Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
				intent.putExtra("CATEGORYNAME", "Cities");
				startActivity(intent);
			}
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
	
	public void onClickCampus(View v){
		categoryName = "campus";
		if(checkInternetConnection()){
			if(location_Access==null || location_Access.trim().equalsIgnoreCase("")){
				category="campus";
				openAlertDialog();
			}else if(location_Access.trim().equalsIgnoreCase("ALLOW")){
				categoryName = "campus";
				application.setcategory("CAMPUS");
				try{
					findCurrentLocation();
				}catch (Exception e) {
					e.toString();
				}
	    
			}else if(location_Access.trim().equalsIgnoreCase("DISALLOW")){
				categoryName = "campus";
				application.setcategory("CAMPUS");
				Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
				intent.putExtra("CATEGORYNAME", "campus");
				startActivity(intent);
			}
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
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
					try { 
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView( name, null, attrs );
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
	
	private void SavePreferences(String key, String value){
	    SharedPreferences sharedPreferences = getSharedPreferences("TIPNOW",MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
	private void LoadPreferences(){
	    SharedPreferences sharedPreferences = getSharedPreferences("TIPNOW",MODE_PRIVATE);
	    location_Access = sharedPreferences.getString("LOCATION_ACCESS", "");
	}
	
	private void openAlertDialog(){
		AlertDialog.Builder customDialog  = new AlertDialog.Builder(CategoryActivity.this);
		LayoutInflater layoutInflater  = (LayoutInflater)getApplicationContext().
	     					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=layoutInflater.inflate(R.layout.current_location,null);
		customDialog.setView(view);
		final AlertDialog dialog = customDialog.create();
	  
		Button buttonAllow = (Button)view.findViewById(R.id.buttonAllow);
		buttonAllow.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				SavePreferences("LOCATION_ACCESS", "ALLOW");
				LoadPreferences();
				//getCurrentLocation();
				findCurrentLocation();
				/*Intent intent = new Intent(CategoryActivity.this,CustomerList.class);
				intent.putExtra("STATENAME", "");
				intent.putExtra("COUNTRYNAME", "");
				intent.putExtra("CITYNAME", "");
				intent.putExtra("CATEGORYNAME", category);
				intent.putExtra("LATITUDE", Double.toString(lat));
				intent.putExtra("LONGITUDE", Double.toString(lng));
				startActivity(intent);*/
			}});
		
		Button buttonDisallow = (Button)view.findViewById(R.id.buttonDisallow);
		buttonDisallow.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				SavePreferences("LOCATION_ACCESS", "DISALLOW");
				LoadPreferences();
				application.setcategory(category.toUpperCase());
				Intent intent=new Intent(CategoryActivity.this, ChooseLocationActivity.class);
				intent.putExtra("CATEGORYNAME", category);
				startActivity(intent);
			}});
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
	}
	
	/*public void getCurrentLocation(){
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    LocationListener mlocListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onProviderDisabled(String provider) {
				//Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
			}
			@Override
			public void onLocationChanged(Location location) {
				lat=location.getLatitude();
				lng=location.getLongitude();
			}
		};
	    mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuItemCategories:   
        	/*Intent intentCategory = new Intent(CategoryActivity.this,CategoryActivity.class);
        	startActivity(intentCategory);*/
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(CategoryActivity.this,MessageActivity.class);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}
	
	public class GetLocationCoordinatesTask extends AsyncTask<Map<String,Object>,Integer,Map<String,Object>>
	{
		private Context context;
		private ProgressDialog progressDialog;

		public GetLocationCoordinatesTask(Context context)
		{
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(this.context);
			this.progressDialog.setTitle("Please Wait");
			this.progressDialog.setMessage("Getting Current Location....");
			this.progressDialog.setCancelable(false);
		}
		
		@Override
		protected Map<String, Object> doInBackground(Map<String, Object>... params) 
		{
			try
			{
				Map<String,Object> result = new HashMap<String,Object>();
				this.publishProgress(0);
				LocationFinder locationFinder = new LocationFinder();
				locationFinder.startFind(this.context);
				Location location = locationFinder.endFind();
				if(location != null)
				{
					result.put("location", location);
				}
				return result;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			super.onProgressUpdate(values);
			this.progressDialog.show();
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) 
		{
			super.onPostExecute(result);
			this.progressDialog.dismiss();
			Location location = (Location)result.get("location");
			if(location != null)
			{
				lat=location.getLatitude();
				lng=location.getLongitude();
				
				/*String tmpMsg = "Latitude:"; 
				tmpMsg= tmpMsg.concat(Double.toString(lat));
				tmpMsg = tmpMsg.concat("-Longitude:");
				tmpMsg= tmpMsg.concat(Double.toString(lng));
				
				final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
				 alertDialog.setMessage(tmpMsg);
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
				 
				turnGPSOff();
				locationFlag=true;
				//Log.v("GetLocationCoordinatesTask", "Before StartActivity: CustomerList, Lat: "+lat+", Long: "+lng);
				Intent intent = new Intent(CategoryActivity.this,CustomerList.class);
				intent.putExtra("STATENAME", "");
				intent.putExtra("COUNTRYNAME", "");
				intent.putExtra("CITYNAME", "");
				intent.putExtra("CATEGORYNAME",categoryName);
				intent.putExtra("LATITUDE", Double.toString(lat));
				intent.putExtra("LONGITUDE", Double.toString(lng));
				startActivity(intent);
				
			}
			else
			{
				gpsNotFoundAlertDialog();
				//Log.v("NO_GPS", "ELSE CASE");
				//String message = "Location not found.";
				//Toast.makeText(CategoryActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void gpsDisabledAlertDialog(){
		final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
		 alertDialog.setMessage("GPS service is disabled. Please enable it to get current location.");
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
	
	public void gpsNotFoundAlertDialog(){
		final AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
		 alertDialog.setMessage("Unable to get current location.");
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
	
	private boolean checkInternetConnection() {
		android.net.ConnectivityManager conMgr = (android.net.ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null && 
				conMgr.getActiveNetworkInfo().isAvailable() && 
				conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	private void turnGPSOn(){
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	}
	private void turnGPSOff(){
	    if(provider.contains("gps")){ //if gps is enabled
	    	//Log.v("GPS-ON", "Turned Off Now");
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}
	private void findCurrentLocation(){
		if(!provider.contains("gps")){
			turnGPSOn();
		}
		try {
			 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			 Criteria criteria = new Criteria();
			 bestProvider = locationManager.getBestProvider(criteria, false);
			 Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
			 String latitude = Double.toString(currentLocation.getLatitude());
			 String longitude = Double.toString(currentLocation.getLongitude());
			 if(currentLocation != null)
				{
					lat=currentLocation.getLatitude();
					lng=currentLocation.getLongitude();
					
					DecimalFormat dtime = new DecimalFormat("#.#####"); 
					lat= Double.valueOf(dtime.format(lat));
					lng= Double.valueOf(dtime.format(lng));

					Log.e("Rounded Latitude",Double.toString(lat));
		        	Log.e("Rounded Longitude",Double.toString(lng));
		        	
					turnGPSOff();
					locationFlag=true;
										
			        Intent intent = new Intent(CategoryActivity.this,CustomerList.class);
					intent.putExtra("STATENAME", "");
					intent.putExtra("COUNTRYNAME", "");
					intent.putExtra("CITYNAME", "");
					intent.putExtra("CATEGORYNAME", categoryName);
					intent.putExtra("LATITUDE", Double.toString(lat));
					intent.putExtra("LONGITUDE", Double.toString(lng));
					startActivity(intent);
				} else {
					gpsNotFoundAlertDialog();
				}
			 
			 Log.v("Latitude Vivek", latitude);
			 Log.v("Longitude Vivek", longitude);
			 locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
			 locationManager.removeUpdates(this);
			 
		} catch (NullPointerException e) {
			// TODO: handle exception
			gpsNotFoundAlertDialog();
		} finally {
			//this.finish();
		}
		 
		/*try {
			Map<String,Object> parameters = new HashMap<String,Object>();
			new GetLocationCoordinatesTask(this).execute(parameters);
		} catch (Exception e) {
			// TODO: handle exception
			gpsNotFoundAlertDialog();
		} finally {
			
			
		}*/
		// LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        LocationListener lmh = new LocationListener() {
				
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
				}
			};
	        
		 /*	try {
				String mlocProvider;
		      	Criteria hdCrit = new Criteria();
		        		 
		      	hdCrit.setAccuracy(Criteria.ACCURACY_FINE);
		      	hdCrit.setAltitudeRequired(false);
		      	hdCrit.setBearingRequired(false);
		      	hdCrit.setCostAllowed(true);
		      	hdCrit.setPowerRequirement(Criteria.POWER_LOW); 
		      		 
		      	mlocProvider = locationManager.getBestProvider(hdCrit, true);
		        		 
		        
		        Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		        locationManager.requestLocationUpdates(mlocProvider, 3000, 1000, lmh);
		       	locationManager.removeUpdates(lmh);
		        		 
		       	double currentLatitude = currentLocation.getLatitude();
		       	double currentLongitude = currentLocation.getLongitude();
		       	if(currentLocation != null)
				{
					lat=currentLocation.getLatitude();
					lng=currentLocation.getLongitude();
					
					DecimalFormat dtime = new DecimalFormat("#.#####"); 
					lat= Double.valueOf(dtime.format(lat));
					lng= Double.valueOf(dtime.format(lng));

					Log.e("Rounded Latitude",Double.toString(lat));
		        	Log.e("Rounded Longitude",Double.toString(lng));
		        	
					turnGPSOff();
					locationFlag=true;
					//Log.v("GetLocationCoordinatesTask", "Before StartActivity: CustomerList, Lat: "+lat+", Long: "+lng);
					Intent intent = new Intent(CategoryActivity.this,CustomerList.class);
					intent.putExtra("STATENAME", "");
					intent.putExtra("COUNTRYNAME", "");
					intent.putExtra("CITYNAME", "");
					intent.putExtra("CATEGORYNAME", application.getcategory().toLowerCase());
					intent.putExtra("LATITUDE", Double.toString(lat));
					intent.putExtra("LONGITUDE", Double.toString(lng));
					startActivity(intent);
			        
					Log.e("Latitude", Double.toString(currentLatitude));
			    	Log.e("Longitude", Double.toString(currentLongitude));
				} else {
					gpsNotFoundAlertDialog();
				}
			} catch (Exception e) {
				// TODO: handle exception
				gpsNotFoundAlertDialog();
			}*/
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	public void onBackPressed() {
		showAlert();
		//super.onBackPressed();
	}
	
	
	void showAlert() {

		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Exit");
		dialog.setMessage("Do you want to exit this app ?");
		dialog.setIcon(R.drawable.exit);

		dialog.setButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});

		dialog.setButton2("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

		dialog.show();
	}
	
	public void onLocationChanged(Location location) {
		//printLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// let okProvider be bestProvider
		// re-register for updates
		//output.append("\n\nProvider Disabled: " + provider);
	}

	public void onProviderEnabled(String provider) {
		// is provider better than bestProvider?
		// is yes, bestProvider = provider
		//output.append("\n\nProvider Enabled: " + provider);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		//output.append("\n\nProvider Status Changed: " + provider + ", Status="
		//		+ S[status] + ", Extras=" + extras);
	}
}
