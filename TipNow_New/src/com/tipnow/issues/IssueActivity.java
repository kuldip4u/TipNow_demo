package com.tipnow.issues;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;

import android.content.Context;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.category.CategoryActivity;
import com.tipnow.message.MessageActivity;
import com.tipnow.tipscreen.TipScreenActivity;

public class IssueActivity extends ListActivity implements OnItemClickListener{
	SimpleAdapter adapter;
	Activity activityContext;
	TipNowApplication Application;
	Intent FindCategoryTypeIntent;
	String Category_Name="",Customer_Name="",Org_Id="",Latitude="",Longitude="",address_id="",appType="";
	ArrayList<HashMap<String, String>> issueListArray;
	ListView List;
	String[]CampusIssues={"Bullying","Stalking", "Hazing", "Theft", "Social Media Abuse", 
			"School Policy Violation", "Alcohol & Drugs", 
			"Noisy Party", "Depression", "Other"};
    String[]CitiesIssues={"Theft", "Child Abuse", "Elder Abuse", "Domestic Violence", 
    		"Drugs & Alcohol", "Gangs", "Car Break-in", "Other"};
	String[]MediaIssues={"School Violence", "Gun Shots, Drugs & Alcohol", "Community stories", 
			"Accident", "Other"};
	/*String[]RetailIssues={"Flash Mob", "Organized Retail Crime", "Theft", "Workplace Safety", 
			"Customer Service Feedback", "Vendor fraud", "Other"};*/
	String[]RetailIssues={"Theft of Products/Services", "Theft of Cash", "Falsification or Omission of Records", 
			"Inappropriate Behavior by Management", "Coupon Abuse", "Fraudulent Dotcom Activity", 
			"Misuse of Company Assets", "Improper Business Practices", "Safety Concerns",
			"Workplace Violence", "Suspicious Customer Activity", "Police Request", "Other"};
	String[]CorporateCategory=	{"Unethical Business Conduct", "Fraud", "Theft (Assets and IP)", 
			"Harassment", "Workplace Violence" , "Depressed/Suicidal"};
	String[]MallIssues=	{"Assualt", "Auto Theft", "Damaged Property", "Personal Illness", 
			"Robbery"," Suspicious Persons Theft","Trespassing", "Vandalism"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customerlistview);
		activityContext=this;
		Application= (TipNowApplication)getApplication();
		FindCategoryTypeIntent=getIntent();
		
		Category_Name = FindCategoryTypeIntent.getStringExtra("CATEGORYNAME");
		Customer_Name = FindCategoryTypeIntent.getStringExtra("CUSTOMER_NAME");
		Org_Id = FindCategoryTypeIntent.getStringExtra("ORG_ID");
		Latitude=FindCategoryTypeIntent.getStringExtra("LATITUDE");
		Longitude=FindCategoryTypeIntent.getStringExtra("LONGITUDE");
		address_id = FindCategoryTypeIntent.getStringExtra("Address_Id");
		appType = FindCategoryTypeIntent.getStringExtra("appType");
		
		issueListArray=new ArrayList<HashMap<String,String>>();
		List= getListView();   
		List.setOnItemClickListener(this);
		if(Category_Name.equalsIgnoreCase("CAMPUS"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CampusIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CampusIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Category_Name.equalsIgnoreCase("CITIES"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CitiesIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CitiesIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Category_Name.equalsIgnoreCase("MEDIA"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<MediaIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", MediaIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Category_Name.equalsIgnoreCase("RETAIL"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<RetailIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", RetailIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Category_Name.equalsIgnoreCase("CORPORATE"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CorporateCategory.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CorporateCategory[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Category_Name.equalsIgnoreCase("MALLS"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<MallIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", MallIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}
		/*if(Application.getcategory().equalsIgnoreCase("CAMPUS"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CampusIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CampusIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Application.getcategory().equalsIgnoreCase("CITIES"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CitiesIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CitiesIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Application.getcategory().equalsIgnoreCase("MEDIA"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<MediaIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", MediaIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Application.getcategory().equalsIgnoreCase("RETAIL"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<RetailIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", RetailIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Application.getcategory().equalsIgnoreCase("CORPORATE"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<CorporateCategory.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", CorporateCategory[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}else if(Application.getcategory().equalsIgnoreCase("MALLS"))
		{
			HashMap<String, String> hashMap=new HashMap<String, String>();
			for(int i=0;i<MallIssues.length;i++){
				hashMap=new HashMap<String, String>();
				hashMap.put("message", MallIssues[i]);
				issueListArray.add(hashMap);
			}
			
			adapter = new SimpleAdapter(activityContext,issueListArray ,R.layout.customerlist, 
					 new String[] {"message"}, new int[] {R.id.headline});
			adapter.notifyDataSetChanged();
			List.setAdapter(adapter);
		}*/
	}
	
	public void onClickBackButton(View v){
		finish();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Intent i= new Intent(IssueActivity.this,TipScreenActivity.class);
		i.putExtra("TipTitle", issueListArray.get(position).get("message").toString());
		i.putExtra("CATEGORYNAME", Category_Name);
		i.putExtra("CUSTOMER_NAME", Customer_Name);
		i.putExtra("ORG_ID", Org_Id);
		i.putExtra("LATITUDE", Latitude);
		i.putExtra("LONGITUDE", Longitude);
		i.putExtra("Address_Id", address_id);
		i.putExtra("appType", appType);
		startActivity(i);
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
        	Intent intentCategory = new Intent(IssueActivity.this,CategoryActivity.class);
        	startActivity(intentCategory);
        	finish();
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(IssueActivity.this,MessageActivity.class);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}
}