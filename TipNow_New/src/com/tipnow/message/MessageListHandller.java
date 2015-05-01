package com.tipnow.message;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;


public class MessageListHandller  extends DefaultHandler{
	Boolean currentElement = false;
    private  String currentValue = null;
	Activity context;
	String messageValue,messageId;
    String result;
    boolean flag=false;
    String value;
    static  HashMap<String, String> responsemap;
    static ArrayList<HashMap<String, String>> messageList= new ArrayList<HashMap<String,String>>();
    public  MessageListHandller(Activity context) 
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
			if(flag){
				//responsemap.put("message", messageValue);
				//responsemap.put("id", messageId);
				flag=false;
			}
		}
	}
	
	@Override
	public void startDocument() throws SAXException {
		messageList.clear();
		
		
	}
	@Override
	public void endDocument() throws SAXException {
		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		currentElement = true;
		if(localName.equalsIgnoreCase("Message"))
		{
			responsemap= new HashMap<String, String>();
			flag = true;
			messageValue = result;
			messageId = attributes.getValue("id");
			//messageList.add(responsemap);
			//Log.v("MESSAGE-LIST", "="+messageList);
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) 	throws SAXException {
		currentElement = false;
		if(localName.equalsIgnoreCase("Message"))
		{
			result= currentValue; 
			responsemap.put("message", currentValue);
			responsemap.put("id", messageId);
			messageList.add(responsemap);
		}
	}

	public static HashMap<String, String> getMessagemap()
	{
		return responsemap;
	}
	
	public static ArrayList<HashMap<String, String>> getMessageList()
	{
		return messageList;
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
