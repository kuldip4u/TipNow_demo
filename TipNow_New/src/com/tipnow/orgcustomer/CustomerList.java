package com.tipnow.orgcustomer;

import java.net.URLEncoder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.os.Handler;

import android.util.AttributeSet;
import android.util.Log;
//import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.tipnow.AppConfig;
import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.myparserhelper;
import com.tipnow.category.CategoryActivity;
import com.tipnow.issues.IssueActivity;
import com.tipnow.message.MessageActivity;


public class CustomerList extends ListActivity{
   
	 Intent get_city_state_contry_intent;
	 String city="",state="",country="",categoryname="",latitude="",longitude="";
	 ListView list;
	 String orgname="",orgId="",address_id ="",appType="";
	 HashMap<String, String>customerMap;
	 Activity mycontext;
	 ArrayList<HashMap<String, String>> List;
	 private Thread LocationdownloadThread;
	 private static ProgressDialog dialog;
	 private static Handler Thandler;
	 SimpleAdapter adapter;
	 TipNowApplication tipNowApplication;
	 String pingURL=null;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.customerlistview);
        /*Bundle bundleSetting = this.getIntent().getExtras();
        state=bundleSetting.getString("STATENAME");
        country=bundleSetting.getString("COUNTRYNAME");
        city=bundleSetting.getString("CITYNAME");
        categoryname=bundleSetting.getString("CATEGORYNAME");
        latitude=bundleSetting.getString("LATITUDE");
        longitude=bundleSetting.getString("LONGITUDE");*/
        
        tipNowApplication=(TipNowApplication)getApplication();
        get_city_state_contry_intent= getIntent();
        state=get_city_state_contry_intent.getStringExtra("STATENAME");
        country=get_city_state_contry_intent.getStringExtra("COUNTRYNAME");
        city=get_city_state_contry_intent.getStringExtra("CITYNAME");
        categoryname=get_city_state_contry_intent.getStringExtra("CATEGORYNAME");
        latitude=get_city_state_contry_intent.getStringExtra("LATITUDE");
        longitude=get_city_state_contry_intent.getStringExtra("LONGITUDE");
        Log.v("Category name",categoryname );
        mycontext= this;
        Thandler= new Handler();
        if(checkInternetConnection()){
        	
        	if(latitude.equalsIgnoreCase("0.0") && longitude.equalsIgnoreCase("0.0")){
        		noCustomerFoundAlertDialog();
        	}else{
        		dialog=ProgressDialog.show(this, "Please Wait", "Loading...");
                LocationdownloadThread = new customerThread();
                LocationdownloadThread.start();
                list = getListView();
        	}
		} else {
			final AlertDialog alertDialog = new AlertDialog.Builder(CustomerList.this).create();
			 alertDialog.setMessage("No network available.");
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
	 public void onClickBackButton(View v){
		 finish();
	 }

	 static class ViewHolder {
		 TextView text;
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

	 public class customerThread extends Thread
	 {
		 @Override
		 public void run() {
			 super.run();
			 String query=null;//,altQuery=null;
			 try {
				 Log.e("Latitude 1", latitude);
		         Log.e("Longitude 1", longitude);
		        // pingURL = "orglonlat/index.php?key=9b22e8ac450bf8dabd90915b1b00a15c&lat=37.3283503&lon=-121.94126&category=" +categoryname+ "&city=&state=&country=" ; 	
			 query="orglonlat/index.php?key="+
			 		URLEncoder.encode(tipNowApplication.getApplicationKey().trim(),"UTF-8")+
			 		"&lat="+URLEncoder.encode(latitude.trim(),"UTF-8")+
			 		"&lon="+URLEncoder.encode(longitude.trim(),"UTF-8")+
			 		//"&lat="+URLEncoder.encode("37.3283503","UTF-8")+
			 		//"&lon="+URLEncoder.encode("-121.94126","UTF-8")+
			 		"&category="+ URLEncoder.encode(categoryname.trim(),"UTF-8")+
			 		"&city="+ URLEncoder.encode(city.trim(),"UTF-8")+
			 		"&state="+URLEncoder.encode(state.trim(),"UTF-8")+
			 		"&country="+URLEncoder.encode(country.trim(),"UTF-8");
			 
			 } catch (Exception e) {
				 e.printStackTrace();
			 }

			 new myparserhelper(mycontext,  new CustomerListXmlHanddler(mycontext),query);

			 //customerMap= CustomerListXmlHanddler.getOrganizationsmap();
			 
			 List= CustomerListXmlHanddler.getOrganigationcustomeList();
			 for (int i = 0; i < List.size(); i++) {
				 Log.v("Address_Id1==", List.get(i).get("address_id")+","+List.get(i).get("org_id")+","+List.get(i).get("org")+","+List.get(i).get("appType"));
	             System.out.println("Address_Id------"+List.get(i).get("org"));		 
			 }
			 //Log.v("Address_Id1", customerMap.get("address_id")); 
			 //Log.v("Org_Id1", customerMap.get("org_id")); 
			 //Log.v("Org==", customerMap.get("org")); 
			 
			 Thandler.post(new Runnable() {
				 @Override
				 public void run() {
					 dialog.dismiss();
					 
					 if(List.size()==0 || List.get(0).get("org").equals("NULL")){// || customerMap.get("org_id")==null || customerMap.get("org_id").toString().trim()=="" || customerMap.get("org").equals("NULL")){
						 
						 mycontext.runOnUiThread(new Runnable() {
							 @Override
							 public void run() {
								 noCustomerFoundAlertDialog();
							 }
						 });//end of ui thread
					 } else {
						 adapter = new SimpleAdapter(mycontext,List ,R.layout.customerlist, 
								 new String[] {"org","org_id","address_id","appType"}, new int[] {R.id.headline, });
						 mycontext.runOnUiThread(new Runnable() {
							 @Override
							 public void run() {
								 adapter.notifyDataSetChanged();
								 list.invalidate();
								 setListAdapter(adapter);
							 }
						 });//end of ui thread
						 list.setOnItemClickListener(new OnItemClickListener() {
							 @Override
							 public void onItemClick(AdapterView<?> parent, View v, int position,
									 long id) {
								 @SuppressWarnings("unchecked")
								HashMap<String, String> hmp = (HashMap<String, String>) list.getItemAtPosition(position);	
								Intent i= new Intent(CustomerList.this,IssueActivity.class);
								i.putExtra("CATEGORYNAME", categoryname);
								i.putExtra("CUSTOMER_NAME", hmp.get("org"));
								i.putExtra("ORG_ID", hmp.get("org_id"));
								i.putExtra("Address_Id", hmp.get("address_id"));
								i.putExtra("appType", hmp.get("appType"));
								i.putExtra("LATITUDE", latitude);
								i.putExtra("LONGITUDE", longitude);
								SharedPreferences prefLocation = getSharedPreferences(AppConfig.appName,0);
								Editor editor = prefLocation.edit();
								editor.putString("CATEGORYNAME", categoryname);
								editor.putString("CUSTOMER_NAME", hmp.get("org")); 
								editor.putString("ORG_ID", hmp.get("org_id")); 
								editor.putString("Address_Id", hmp.get("address_id")); 
								editor.putString("appType", hmp.get("appType")); 
								editor.putString("LATITUDE", latitude); 
								editor.putString("LONGITUDE", longitude); 
								editor.commit(); // commit changes
								startActivity(i);
							 }
						 });
					 }
				 }
			 });
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
					 try { // Ask our inflater to create the view
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
	 
	 public void noCustomerFoundAlertDialog(){
		 final AlertDialog alertDialog = new AlertDialog.Builder(CustomerList.this).create();
		 alertDialog.setMessage("There are no tipnow customers in this region.");
		 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		    	  runOnUiThread(new Runnable() {
					@Override
					public void run() {
						alertDialog.dismiss();
						finish();	
					}
				});
		    } });
		 alertDialog.show();
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
	        case R.id.menuItemCategories:     
	        	Intent intentCategory = new Intent(CustomerList.this,CategoryActivity.class);
	        	startActivity(intentCategory);
	        	finish();
	            break;
	        case R.id.menuItemMessages:   
	        	Intent intentMessage = new Intent(CustomerList.this,MessageActivity.class);
	        	startActivity(intentMessage);
	        	finish();
	            break;
			}
			return true;
		}
}
