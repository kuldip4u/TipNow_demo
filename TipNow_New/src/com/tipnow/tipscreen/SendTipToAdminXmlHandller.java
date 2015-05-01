package com.tipnow.tipscreen;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
//import android.util.Log;

public class SendTipToAdminXmlHandller extends DefaultHandler{
	Boolean currentElement = false;
    private  String currentValue = null;
	Activity context;
    String result;
    String value;
    static  HashMap<String, String> responsemap;
    public SendTipToAdminXmlHandller(Activity context) 
    {
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
		 if(localName.equalsIgnoreCase("response"))
		    {
			 responsemap= new HashMap<String, String>();
		    }
		   }
	@Override
	public void endElement(String uri, String localName, String qName) 	throws SAXException {
		 currentElement = false;
		if(localName.equalsIgnoreCase("result"))
		{
			result= currentValue; 
		}
		if(localName.equalsIgnoreCase("org_id"))
		{
			value= currentValue ;
		}
		
       if(localName.equalsIgnoreCase("response"))
        {
    	   responsemap.put("result", result);
    	   responsemap.put("value", value);
        }
	
	}

	public static HashMap<String, String> getOrganizationsmap()
	{
		return responsemap;
	}

	@Override
	public void error(SAXParseException e) throws SAXException 
	{
		//Log.v("Album Error", e.getLocalizedMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		//Log.v("Album Fetal Error", e.getLocalizedMessage());
	}

}
