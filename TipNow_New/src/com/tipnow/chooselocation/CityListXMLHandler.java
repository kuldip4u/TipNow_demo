package com.tipnow.chooselocation;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;

public class CityListXMLHandler extends DefaultHandler {
	Boolean currentElement = false;
    private  String currentValue = null;
	Activity context;
    
    String cityListValue;
    
    static ArrayList<HashMap<String, String>> CityList= new ArrayList<HashMap<String,String>>(); 
    HashMap<String, String> CityListmap;

	public CityListXMLHandler(Activity context) {
		// TODO Auto-generated constructor stub
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
		 if(localName.equalsIgnoreCase("city"))
		    {
			  CityListmap = new HashMap<String, String>();
		    }
	
	   }

	@Override
	public void endElement(String uri, String localName, String qName) 	throws SAXException {
		 currentElement = false;
		
		if(localName.equalsIgnoreCase("city"))
		{
		 
		   
		   cityListValue = currentValue;
		   CityListmap.put("City", cityListValue);
		   CityList.add(CityListmap);
		}
	}

	public static ArrayList<HashMap<String, String>> getlocationList()
	{
		return CityList;
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
