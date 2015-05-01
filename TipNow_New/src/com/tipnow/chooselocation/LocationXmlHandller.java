package com.tipnow.chooselocation;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
//import android.util.Log;

    public class LocationXmlHandller extends DefaultHandler {
	Boolean currentElement = false;
    private  String currentValue = null;
	Activity context;
    String countryvalue;
    String cityvalue;
    String statevalue;
    String[] countryvalue1;
  
    static ArrayList<String> countryarray;
   public static ArrayList<String> statearray;
    static ArrayList<String> cityarray;
static ArrayList<HashMap<String,  ArrayList<String>>> LocationList= new ArrayList<HashMap<String, ArrayList<String>>>(); 

HashMap<String, ArrayList<String>> Locationmap;

	public LocationXmlHandller(Activity context) 
    {
		  countryarray = new ArrayList<String>();
		  statearray = new ArrayList<String>();
		  statearray.add("Select State");
		  cityarray = new ArrayList<String>();
		this.context = context;
	}

	@Override
	public void characters(char[] ch, int start, int length) 
				throws SAXException { 
		if(currentElement)
		{
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}
	
	@Override
	public void startDocument() throws SAXException {
		
	}
	@Override
	public void endDocument() throws SAXException {
		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	        {
		 currentElement = true;
		 if(localName.equalsIgnoreCase("location"))
		    {
			 
			  Locationmap= new HashMap<String, ArrayList<String>>();
		    }
	
	   }

	@Override
	public void endElement(String uri, String localName, String qName) 	throws SAXException {
		 currentElement = false;
	
	
		 if(localName.equalsIgnoreCase("Country"))
		{
			 
			countryvalue= currentValue;
			
			System.out.println(countryvalue);
		}
		if(localName.equalsIgnoreCase("State"))
		{ 
		   statevalue= currentValue ;
		  
		
		 
		   
		}
		if(localName.equalsIgnoreCase("Cities"))
		{ 
		   cityvalue= currentValue;
		} 
		 if(localName.equalsIgnoreCase("location"))
        {
			
			
				countryarray.add(countryvalue);
    	       statearray.add(statevalue);
    	        
    	     
           Locationmap.put("Country", countryarray);
    	   // Locationmap.put("Cities", cityvalue);
           Locationmap.put("State", statearray);
           LocationList.add(Locationmap); 
        }
	 
	
	}

	public static ArrayList<HashMap<String, ArrayList<String>>> getlocationList()
	{
		
       
		return LocationList;
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		//Log.v("Album Error", e.getLocalizedMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		//Log.v("Album Fetal Error", e.getLocalizedMessage());
	}

 

}
