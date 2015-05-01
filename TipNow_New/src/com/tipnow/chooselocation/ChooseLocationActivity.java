package com.tipnow.chooselocation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tipnow.AppConfig;
import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.myparserhelper;
import com.tipnow.category.CategoryActivity;
import com.tipnow.message.MessageActivity;
import com.tipnow.orgcustomer.CustomerList;

public class ChooseLocationActivity extends Activity implements
		OnItemSelectedListener {
	TipNowApplication application;
	String[] country;
	String[] cityArray = { "Select City" };
	String[] cityList;

	String[] state;
	String[] state1;
	String[] city;
	String[] TempStringArray;
	ImageView submitbutton;
	Intent Getcategoryname;
	ArrayAdapter<String> adapter;
	SimpleAdapter simpleAdapter;
//	TextView TextDropDown;
	String categoryname, cname, sname = "", cityname;
	String sname1 = "";
	ArrayList<HashMap<String, ArrayList<String>>> LocationList;
	ArrayList<HashMap<String, String>> CityList;

	HashMap<String, ArrayList<String>> LocationMap;
	HashMap<String, String> CityMap;
	Activity mycontext;
	private Thread LocationdownloadThread;
	private static ProgressDialog dialog;
	private static Handler Thandler;
	int pos;
	Spinner CountryTextfield, StateTextFild, CityTexfield;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.choose_location);

		//TextDropDown = (TextView) findViewById(R.id.TextDropDown);

	/*	TextDropDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean flag = false;
				// TODO Auto-generated method stub
				if (CountryTextfield.getSelectedItem().toString().trim()
						.equalsIgnoreCase("Select Country")
						|| StateTextFild.getSelectedItem().toString().trim()
								.equalsIgnoreCase("Select State")) {
					// Toast.makeText(
					// ChooseLocationActivity.this,(CharSequence)
					// CountryTextfield ,Toast.LENGTH_LONG).show();
					flag = true;
					if (flag) {
						final AlertDialog alertDialog = new AlertDialog.Builder(
								ChooseLocationActivity.this).create();
						alertDialog
								.setMessage("Please select country and state first.");
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
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
				} else {
					CityTexfield.performClick();
					CityTexfield.setVisibility(View.VISIBLE);
					TextDropDown.setVisibility(View.GONE);
				}
			}
		});*/
		application = (TipNowApplication) getApplication();
		Getcategoryname = getIntent();
		categoryname = Getcategoryname.getStringExtra("CATEGORYNAME");

		CountryTextfield = (Spinner) findViewById(R.id.CountrySpinner);
		CountryTextfield
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg, View arg1,
							int position, long arg3) {

						cname = arg.getItemAtPosition(position).toString();
						System.out.println("cname----------->"+cname);

				// Add this code for list of state coresponding Country		
						pos = (int) arg.getItemIdAtPosition(position);
						String sss;
						sss = LocationXmlHandller.statearray.get(pos);
						ArrayList<String> ssss = new ArrayList<String>();

						String selectstate = "Select State";
						String[] items = sss.split(",");

						for (String item : items) {

							ssss.add(item);
						}

						ArrayAdapter dd = new ArrayAdapter(mycontext,
								android.R.layout.simple_spinner_item, ssss);

						dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

						if (sname.equalsIgnoreCase(selectstate)) {

						} else {

							sname = arg.getItemAtPosition(position).toString();
						}
						StateTextFild.setAdapter(dd);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		StateTextFild = (Spinner) findViewById(R.id.StateSpinner);
		StateTextFild.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg, View arg1,
					int position, long arg3) {

				// Use sname1 for save the state string selected
			    sname = arg.getItemAtPosition(position).toString();
				sname1 = sname;
				System.out.println("sname1----------->"+sname1);
			//	TextDropDown.setClickable(false);
				getCities();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		CityTexfield = (Spinner) findViewById(R.id.CitySpinner);

		CityTexfield.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg, View arg1, int position, long arg3) {

				Log.v("onItemSelected", "position"+position+ "\n arg"+arg.toString());
				cityname = arg.getItemAtPosition(position).toString();
				System.out.println("cityname----------->"+cityname);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		
		mycontext = this;
		Thandler = new Handler();
		if (checkInternetConnection()) {
			dialog = ProgressDialog.show(this, "Please Wait", "Loading...");
			LocationdownloadThread = new LocationThread();
			LocationdownloadThread.start();
		} else {
			final AlertDialog alertDialog = new AlertDialog.Builder(
					ChooseLocationActivity.this).create();
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
		android.net.ConnectivityManager conMgr = (android.net.ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public void Submit(View v) {
		//boolean flag = false;
		if (CountryTextfield.getSelectedItem().toString().trim().equalsIgnoreCase("Select Country")|| StateTextFild.getSelectedItem().toString().trim().equalsIgnoreCase("Select State")
				||  CityTexfield.getSelectedItem().toString().trim().equalsIgnoreCase("Select City")){
				
			final AlertDialog alertDialog = new AlertDialog.Builder(
					ChooseLocationActivity.this).create();
			alertDialog.setMessage("All fields are required.");
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
		} else {
			Intent i = new Intent(ChooseLocationActivity.this,
					CustomerList.class);
			i.putExtra("CATEGORYNAME",
					Getcategoryname.getStringExtra("CATEGORYNAME"));
			i.putExtra("COUNTRYNAME", cname.toString());
			i.putExtra("STATENAME", sname.toString());
			i.putExtra("CITYNAME", cityname.toString());
			i.putExtra("LATITUDE", "");
			i.putExtra("LONGITUDE", "");
			startActivity(i);
		}
	}

	public synchronized String[] splitStringByComa(String str) {
		String[] a;
		ArrayList<String> List = new ArrayList<String>();
		int i = 0;
		StringTokenizer st2 = new StringTokenizer(str, ",");
		while (st2.hasMoreElements()) {
			String l = st2.nextElement().toString().trim();
			List.add(i, l);
			i++;
		}
		a = new String[List.size()];
		for (int m = 0; m < List.size(); m++) {
			a[m] = List.get(m).toString().trim();
		}
		return a;
	}
// Add this code for split String comas 
	public synchronized String splitStringByComa1(String str) {

		String strarray[] = str.split(",");
		int intarray[] = new int[strarray.length];

		for (int count = 0; count < intarray.length; count++) {
			intarray[count] = Integer.parseInt(strarray[count]);
		}

		for (int s : intarray) {
			System.out.println(s);
		}
		return str;
		

	}

	public class LocationThread extends Thread {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				new myparserhelper(mycontext,
						new LocationXmlHandller(mycontext),
						"custlist/index2.php?key="
								+ application.getApplicationKey()
								+ "&category=Campus");
				LocationList = LocationXmlHandller.getlocationList();
				@SuppressWarnings("rawtypes")
				Iterator it = LocationList.iterator();
				if (it.hasNext()) {
					LocationMap = (HashMap<String, ArrayList<String>>) it
							.next();
                    // We will pass the country array   
					country = splitStringByComa(LocationMap.get("Country")
							.toString());// Cities//State
					// Split country array indication
					for (int i = 0; i < country.length; i++) {

						country[i] = country[i].replace("[", "");
						country[i] = country[i].replace("]", "");
						System.out.println(country[i]);
					}
					
					state = splitStringByComa("");

				}
				// -------- Add Country-List First Element as 'Select Country'
				TempStringArray = new String[country.length + 1];
				TempStringArray[0] = "Select Country";
				for (int i = 1; i < TempStringArray.length; i++)
					TempStringArray[i] = country[i - 1];
				country = new String[TempStringArray.length];
				country = TempStringArray;
				// -------- Add State-List First Element as 'Select State'
				TempStringArray = new String[state.length + 1];
				TempStringArray[0] = "Select State";
				for (int i = 1; i < TempStringArray.length; i++)
					TempStringArray[i] = state[i - 1];
				state = new String[TempStringArray.length];
				state = TempStringArray;
				// -------- Add City-List First Element as 'Select City'
				/*
				 * TempStringArray=new String[cityArray.length+1];
				 * TempStringArray[0]="Select City"; /*for(int
				 * i=1;i<TempStringArray.length;i++)
				 * TempStringArray[i]=city[i-1]; city = new
				 * String[TempStringArray.length];
				 */
				// cityArray=TempStringArray;
				// TempStringArray=null;
				// --------------------------------------------------------------
				Thandler.post(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									@SuppressWarnings("rawtypes")
									ArrayAdapter aa = new ArrayAdapter(ChooseLocationActivity.this,android.R.layout.simple_spinner_item,country);
									aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									CountryTextfield.setAdapter(aa);
									
									
									if(cityArray != null && cityArray[0].equals("null")){
										TempStringArray=new String[0];
										TempStringArray[0]="Select City"; 
										cityArray=TempStringArray;
									}
									
									ArrayAdapter bb = new ArrayAdapter(mycontext,android.R.layout.simple_spinner_item,	cityArray);
									bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									CityTexfield.setAdapter(bb);
									@SuppressWarnings("rawtypes")
									ArrayAdapter dd = new ArrayAdapter(mycontext,android.R.layout.simple_spinner_item,state);
									dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									StateTextFild.setAdapter(dd);
								} catch (Exception e) {

								}
							}
						});
					}
				});
			} catch (NullPointerException e) {
				// TODO: handle exception
				final AlertDialog alertDialog = new AlertDialog.Builder(
						ChooseLocationActivity.this).create();
				alertDialog.setMessage("Unable to get values from server.");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
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

	public void getCities() {
		

		//CityTexfield.setVisibility(View.GONE);
//		TextDropDown.setVisibility(View.VISIBLE);
		HttpClient httpclient = new DefaultHttpClient();
		// HttpPost httppost = new
		// HttpPost("http://ec2-184-72-193-81.compute-1.amazonaws.com/plus.tipnow.net/api/custlist/getcity.php?key=9b22e8ac450bf8dabd90915b1b00a15c");
		HttpPost httppost = new HttpPost(
				"http://tipnowplus.com/api/custlist/getcity.php?key=9b22e8ac450bf8dabd90915b1b00a15c&category=Campus");
		// HttpPost httppost = new
		// HttpPost("http://tipnowplus.rave-staging.net/api/custlist/getcity.php?key=9b22e8ac450bf8dabd90915b1b00a15c");
		Log.e("cname", "" + cname);
		Log.e("state", "" + sname1);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("country", cname));
			nameValuePairs.add(new BasicNameValuePair("state", sname1));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			BasicResponseHandler responseHandler = new BasicResponseHandler();
			String strResponse = null;
			if (response != null) {
				try {
					strResponse = responseHandler.handleResponse(response);
				} catch (HttpResponseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Log.e("City List", "City List ********** Response" + strResponse);
			
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(strResponse.getBytes());
				XMLStringParser xmlStringParser = new XMLStringParser(byteArrayInputStream);
				List<String> mylist = new ArrayList<String>();
				mylist = xmlStringParser.getCityList();
				String cityStrings = mylist.toString();
				cityStrings = cityStrings.replace("[", " ");
				cityStrings = cityStrings.replace("]", " ");
				cityStrings = cityStrings.trim();
				Log.e("StringArray", "" + cityStrings);
				cityArray = splitStringByComa(cityStrings);
				for (String str : cityArray) {
					Log.e("Value=", str);
				}
				
			try {
				if(cityArray != null && cityArray[0].equals("null")){
					TempStringArray=new String[0];
					TempStringArray[0]="Select City"; 
					cityArray=TempStringArray;
				}
				
				@SuppressWarnings("rawtypes")
				ArrayAdapter bb = new ArrayAdapter(mycontext, android.R.layout.simple_spinner_item, cityArray);
				bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				CityTexfield.setAdapter(bb);
				/*CityTexfield.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(cityArray!= null && cityArray.length>0){
							CityTexfield.setSelection(0);
							
							cityname = cityArray[0];
						}
					}
				}, 50);*/
				
			} catch (Exception e) {
				Log.e("Error=", e.toString());// TODO: handle exception
			}

//			TextDropDown.setClickable(true);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		/*
		 * new myparserhelper(mycontext, new LocationXmlHandller(mycontext),
		 * "custlist/getcity.php?key="+application.getApplicationKey());
		 * CityList = CityListXMLHandler.getlocationList();
		 * 
		 * @SuppressWarnings("rawtypes") Iterator it= CityList.iterator();
		 * if(it.hasNext()) { CityMap = (HashMap<String, String>) it.next();
		 * cityList= splitStringByComa(CityMap.get("City").toString()); }
		 */

	}

	public void onClickBackButton(View v) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		setMenuBackground();
		return true;
	}

	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {
			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundResource(R.drawable.rounded_corner_darker);
								((TextView) view).setTextColor(Color.WHITE);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
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

			Intent intentCategory = new Intent(ChooseLocationActivity.this,
					CategoryActivity.class);
			startActivity(intentCategory);
			finish();
			break;
		case R.id.menuItemMessages:
			Intent intentMessage = new Intent(ChooseLocationActivity.this,
					MessageActivity.class);
			startActivity(intentMessage);
			finish();
			break;
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// Toast.makeText(arg0.getContext(),
		// "You have selected : " + arg0.getItemAtPosition(position).toString()
		// +"position "+ position,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Auto-generated method stub
	}
	public void saveSharedPrefrence()
	{
		SharedPreferences prefLocation = getSharedPreferences("TIPNOW",0);
		Editor editor = prefLocation.edit();
		editor.putString("COUNTRY", cname); 
		editor.putString("STATE", sname1);
		editor.putString("CITY", cityname);
		
		editor.commit(); // commit changes
		
	}
}