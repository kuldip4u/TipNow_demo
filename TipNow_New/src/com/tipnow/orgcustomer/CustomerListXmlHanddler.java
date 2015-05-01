package com.tipnow.orgcustomer;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import android.app.Activity;

public class CustomerListXmlHanddler extends DefaultHandler{
	Boolean currentElement = false;
    private  String currentValue = null;
    static int counter=1;
    boolean flag=false;
    boolean f_org=false,f_org_id=false,f_add_id=false,f_appType = false;
	Activity context;
    String org, org_id, address_id,appType;
    //public  static HashMap<String, ArrayList<HashMap<String, String>>> allListArray= new HashMap<String, ArrayList<HashMap<String,String>>>();
    static ArrayList<HashMap<String, String>> OrganigationcustomeList= new ArrayList<HashMap<String,String>>();
    static HashMap<String, String> Organizationsmap;
	
    public CustomerListXmlHanddler(Activity context) 
    {
		this.context = context;
	}

	
	
	@Override
	public void startDocument() throws SAXException {

		OrganigationcustomeList.clear();
		//allListArray.clear();
		
	}
	@Override
	public void endDocument() throws SAXException {
		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException{
		currentElement = true;
		if(localName.equalsIgnoreCase("org"))
		{
			Organizationsmap= new HashMap<String, String>();
			f_org=true;
		}
		if(localName.equalsIgnoreCase("org_id"))
		{
			f_org_id=true;
			//Organizationsmap = new HashMap<String, String>();
			//Log.v("CustomerListXmlHanddler", "OrganigationcustomeList="+OrganigationcustomeList);
		}
		if(localName.equalsIgnoreCase("address_id"))
		{
			f_add_id=true;
			//Log.v("CustomerListXmlHanddler", "OrganigationcustomeList="+OrganigationcustomeList);
		}
		if(localName.equalsIgnoreCase("appType"))
		{
			f_appType=true;
			//Log.v("CustomerListXmlHanddler", "OrganigationcustomeList="+OrganigationcustomeList);
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) 	throws SAXException {
		 currentElement = false;
		if(localName.equalsIgnoreCase("org"))
		{
			org= currentValue; 
		}
		if(localName.equalsIgnoreCase("org_id"))
		{
			org_id= currentValue ;
			
		}
		if(localName.equalsIgnoreCase("address_id"))
		{
			address_id = currentValue;
			System.out.println("In Address Tag.");
			/*System.out.println(currentValue);
			if (currentValue!=null) {
				address_id= currentValue ;
			} else {
				address_id= " " ;
			}*/
			//OrganigationcustomeList.add(Organizationsmap);	
		}
		if(localName.equalsIgnoreCase("appType"))
		{
			System.out.println("In Address Tag.");
			appType = currentValue;
			/*System.out.println(currentValue);
			if (currentValue!=null) {
				address_id= currentValue ;
			} else {
				address_id= " " ;
			}*/
			OrganigationcustomeList.add(Organizationsmap);	
		}
       /*if(localName.equalsIgnoreCase("Organizations"))
        {
    	   /*Organizationsmap.put("org", org);
    	   Organizationsmap.put("org_id", org_id);
    	   Organizationsmap.put("Address_id", address_id);*/
    	 //  OrganigationcustomeList.add(Organizationsmap);
    	   //Log.v("CustomerListXmlHanddler", "OrganigationcustomeList="+OrganigationcustomeList);
    	   //allListArray.put(org_id, OrganigationcustomeList);
        //}
	
	}
	@Override
	public void characters(char[] ch, int start, int length)
				throws SAXException { 
		if(currentElement)
		{
			currentValue = new String(ch, start, length);
			currentElement = false;
			if(f_org){
				Organizationsmap.put("org", currentValue);
				f_org=false;
			} else if(f_org_id){
				Organizationsmap.put("org_id", currentValue);
				f_org_id=false;
			} else if(f_add_id){
				Organizationsmap.put("address_id", currentValue);
				f_add_id=false;
			} else if(f_appType){
				Organizationsmap.put("appType", currentValue);
				f_appType=false;
			}
		}
	}
	public static HashMap<String, String> getOrganizationsmap()
	{
		return Organizationsmap;
	}

	
	
	public static ArrayList<HashMap<String, String>> getOrganigationcustomeList()
	{
		return OrganigationcustomeList;
	}

	/*public  static HashMap<String, ArrayList<HashMap<String, String>>> getAllListArray()
	{
		System.out.println("AllLISTSIZE.SIZE="+allListArray.size());
		return allListArray;
	}*/
	
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		//Log.v("Album Error", e.getLocalizedMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		//Log.v("Album Fetal Error", e.getLocalizedMessage());
	}
}
