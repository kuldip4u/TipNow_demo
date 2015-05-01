package com.tipnow.chooselocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.net.ParseException;

public class XMLStringParser extends DefaultHandler {
    //
	InputStream inputStream=null;
	Boolean currentElement = false;
    private  String currentValue = null;
	String cityListValue;
	
	HashMap<String, String> CityListmap;
	List<String> list = new ArrayList<String>();
    ArrayList<HashMap<String,String>> mylist = new ArrayList<HashMap<String,String>>();

    public XMLStringParser(InputStream isResponse) {
    	this.inputStream = isResponse;
	        parseDocument();
	        
	    }
    private void parseDocument() {
        // parse
	        SAXParserFactory  factory = SAXParserFactory.newInstance();
	        try {
	            SAXParser parser = factory.newSAXParser();
	            parser.parse(inputStream, this);
	        } catch (ParserConfigurationException e) {
	            System.out.println("ParserConfig error");
	        } catch (SAXException e) {
	            System.out.println("SAXException : xml not well formed");
	        } catch (IOException e) {
	            System.out.println("IO error");
	        }
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
			   list.add(cityListValue);
			   mylist.add(CityListmap);
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
	    
	    public List<String> getCityList(){
	    	return list;
	    }
	    
	    
	    
	}