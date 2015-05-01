package com.tipnow.message;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.myparserhelper;
import com.tipnow.category.CategoryActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class MessageActivity extends ListActivity{
	Thread messageThread;
	ProgressDialog dialog;
	Context context;
	private static Handler Thandler;
	TipNowApplication application;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> messageListArray;
	ListView listView;
	Activity activityContext;
	SimpleAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.massgescreen);
		application = (TipNowApplication)getApplication();
		context = this;
		activityContext=this;
		listView = getListView();
		if(checkInternetConnection()){
			Thandler = new Handler();
			dialog=ProgressDialog.show(this, "Please Wait", "Loading Messages...");
			messageThread = new MessageListThread();
			messageThread.start();
		}else{
			final AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).create();
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuItemCategories:     
        	Intent intentCategory = new Intent(MessageActivity.this,CategoryActivity.class);
        	startActivity(intentCategory);
        	finish();
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(MessageActivity.this,MessageActivity.class);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}
	
	
	public class MessageListThread extends Thread{
		@Override
		public void run() {
			String udid=null;
			try{
				udid=URLEncoder.encode(application.getUDID().trim(),"UTF-8");
			}catch (Exception e) {
			}
			//new Myparserhelper(context, new MessageListHandller(activityContext), "http://tipnow.rave-staging.net/getmsg_xml.php?udid="+udid);
			new myparserhelper(context, new MessageListHandller(activityContext), "tips/getmsg_xml.php?udid="+udid);//f74fe17508c78504ad614dec6fb04a0522d0dd8e");
			Thandler.post(new Runnable() {
				@Override
				public void run() {
					hashMap = MessageListHandller.getMessagemap();
					messageListArray = MessageListHandller.getMessageList();
					dialog.dismiss();
					if(messageListArray.size()==0){
						
					}else{
						adapter = new SimpleAdapter(activityContext,messageListArray ,R.layout.customerlist, 
								 new String[] {"message","id"}, new int[] {R.id.headline, });
						activityContext.runOnUiThread(new Runnable() {
							 @Override
							 public void run() {
								 adapter.notifyDataSetChanged();
								 listView.invalidate();
								 setListAdapter(adapter);
							 }
						 });
						listView.setOnItemClickListener(new OnItemClickListener() {
							 @Override
							 public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
								 @SuppressWarnings("unchecked")
								HashMap<String, String> hmp = (HashMap<String, String>) listView.getItemAtPosition(position);	
								 Intent i= new Intent(MessageActivity.this,MessageDetailActivity.class);
								 i.putExtra("message", hmp.get("message"));
								 i.putExtra("id", hmp.get("id"));
								 startActivity(i);
								 finish();
							 }
						});
					}
				}
			});
		}
	}
}