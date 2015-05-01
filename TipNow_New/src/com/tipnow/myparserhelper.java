package com.tipnow;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class myparserhelper {
	Context context;
	DefaultHandler handler;
	//	http://mobileapp.tipnow.net/api/
	//	http://tipnow.rave-staging.net/newtip.php
//	public static String TpBaseURL ="http://ec2-184-72-193-81.compute-1.amazonaws.com/plus.tipnow.net/api/";
	public static String TpBaseURL ="http://www.tipnowplus.com/api/";
	//public static String TpBaseURL ="http://ec2-184-72-193-81.compute-1.amazonaws.com/mobileapp.tipnow.net/api/"; 
	
	String TpURL;
    public myparserhelper(Context context, DefaultHandler handler, String qburl) {
		this.context= context;
		this.handler= handler;
		this.TpURL = qburl;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = null;
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		XMLReader xr = null;
		try {
			xr = sp.getXMLReader();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		URL sourceUrl = null;
		try {
			sourceUrl = new URL(TpBaseURL+TpURL);
			System.out.println("Parsed-URL="+sourceUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		xr.setContentHandler(handler);
		try {
			xr.parse(new InputSource(sourceUrl.openStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
    }
}
