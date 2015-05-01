package com.tipnow.signup;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import android.app.Activity;

public class SignUpXmlHandler extends DefaultHandler{
	Boolean currentElement = false;
    private  String currentValue = null;
	Activity context;
    String result;
    String value;
    static HashMap<String, String> resultMap;
    
	
	public SignUpXmlHandler(Activity context) 
    {
		this.context = context;
	}
	
	@Override
	public void startDocument() throws SAXException {

	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = true;
		if(localName.equalsIgnoreCase("response"))
		{
			resultMap= new HashMap<String, String>();
		}
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
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = false;
		if(localName.equalsIgnoreCase("result")){
			result=currentValue;
		}
		if(localName.equalsIgnoreCase("value")){
			value=currentValue;
		}
		if(localName.equalsIgnoreCase("response"))
        {
			resultMap.put("result", result);
			resultMap.put("value", value);
        }
	}
	
	public static HashMap<String, String> getResultMap()
	{
		return resultMap;
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		//Log.v("SignUp SAXParseException", e.getLocalizedMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		//Log.v("SignUp SAXParseException", e.getLocalizedMessage());
	}

}
