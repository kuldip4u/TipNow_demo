package com.tipnow;

import java.io.IOException;
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

public class MyparserhelperTemp {
	Context context;
	DefaultHandler handler;
	//•	http://tipnow.rave-staging.net/newtip.php
	//public static String TpBaseURL ="http://mobileapp.tipnow.net/api/"; 
	String TpURL;
	
    public MyparserhelperTemp(Context context, DefaultHandler handler, String qburl) {
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
			//sourceUrl = new URL(TpBaseURL+TpURL);
			sourceUrl = new URL(TpURL);
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