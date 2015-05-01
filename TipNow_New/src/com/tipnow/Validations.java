package com.tipnow;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Validations {
	/*http://www.earthtools.org/timezone-1.1/latt/longg";*/
	static String urlISO="http://www.earthtools.org/timezone-1.1/";
	
	// Default Constructor
	public Validations() {
	}
	
	// Email Validation
	public static boolean emailValidation(String string){
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(string);
        return emailMatcher.matches();
	}
	public static boolean integerGreaterThanZeroValidation(int num){
		if(num>0)
			return true;
		else if(num==0)
			return false;
		else if(num<0)
			return false;
		return false;
	}
	public static boolean floatGreaterThanZeroValidation(float num){
		if(num>0.0f)
			return true;
		else if(num==0.0f)
			return false;
		else if(num<0.0f)
			return false;
		return false;
	}
	// Integer Validation
	public boolean integerValidation(String numberstring)
	{
		Pattern numberPattern = Pattern.compile("[0-9]*");
		Matcher numberMatcher = numberPattern.matcher(numberstring);
		return numberMatcher.matches();
	}
	
	public static boolean floatValidation(String str){
		try{
			Float.parseFloat(str);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	/* Phone Number Validation */
	public static String phoneNumberLengthValidation(String phoneString){
		if(phoneString.length()==10){
			if(phoneString.toCharArray()[0]!='1'){
				return "INDIAN_NUMBER";
			}
			else 
				return "INVALID";
		}else if(phoneString.length()==11){
			if(phoneString.toCharArray()[0]=='1'){
				return "US_NUMBER";
			}
			else 
				return "INVALID";
		}
		return "INVALID";
	}
	
	// SMS Addtress Validation, Not Tested
	public boolean smsAddressValidation(String string){
		return 	android.telephony.PhoneNumberUtils.isWellFormedSmsAddress (string);
	}
	
	/* Set Number to 2 digits of decimal Fraction */
	public static Float decimalFractionValidation(Float value){
		DecimalFormat twoDForm = new DecimalFormat();
   	    twoDForm.setMaximumFractionDigits(2);
   	    twoDForm.setMinimumFractionDigits(2);
        twoDForm.format(value);
        return value;
	}
	
	// Zip Code Validation
	/*Some Example of US ZipCode are
	 * Zip 50266-234A is Invalid
	 * Zip 50266-2342 is Valid
	 * Zip 5026A-2344 is Invalid
	 * Zip 5026A-234A is Invalid
	 * Zip 50266 is Valid
	 * Zip 230 is Invalid
	 */
	public static boolean usZipCodeVAlidation(String string){
		String regex = "^\\d{5}(-\\d{4})?$";
		return Pattern.matches(regex, string);
	}
	
	// DIFFERENCE CHECK BETWEEN TWO GIVEN DATES
	public String dateFrom_DateTo_Compare(String strFrom, String strTo){
		Date dateFrom=null,dateTo=null;
		SimpleDateFormat simpleDateFormat=null;
		try{
			simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy");
			dateFrom=simpleDateFormat.parse(strFrom);
			dateTo=simpleDateFormat.parse(strTo);
			if(dateFrom.compareTo(dateTo)>0)
				return "d1>d2";
			else if(dateFrom.compareTo(dateTo)<0)
				return "d1<d2";
			else
				return "d1==d2";
		}catch (Exception e) {
			//System.out.println("DateParsing Error="+e.toString());
		}
		return "ERROR";
	}
	
	//Check if a date lies in between Date From and Date To
	public boolean dateLiesInBetween(String from, String to, String current){
    	SimpleDateFormat simpleDateFormat=null;
    	Date dateFrom=null, dateTo=null, dateCurrent=null;
    	try{
			simpleDateFormat=new SimpleDateFormat("MM/dd/yyyy");
			dateFrom=simpleDateFormat.parse(from);
			dateTo=simpleDateFormat.parse(to);
			dateCurrent=simpleDateFormat.parse(current);
			if(dateCurrent.compareTo(dateFrom)>=0 && dateCurrent.compareTo(dateTo)<=0)
				return true;
			else
				return false;
    	}catch (Exception e) {
    		//System.out.println("Date parsing Error: "+e.toString());
		}
    	return false;
    }
	
	//Check is Given Date is Before Current Date
	public static boolean isDateBeforeCurrentDate(String strDate){
		//String getIsoDate=getISOTime();
		//String currentDate=getIsoDate.substring(5,7)+"-"+getIsoDate.substring(8,10)+"-"+getIsoDate.substring(0,4);
		Date dateCurrent=null,dateToBeChecked=null;
		SimpleDateFormat simpleDateFormat=null;
		try{
			simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy");
			//dateCurrent=simpleDateFormat.parse(currentDate);
			dateCurrent=new Date(); // USE This Line to get System Date
			dateToBeChecked=simpleDateFormat.parse(strDate);
			if(dateCurrent.compareTo(dateToBeChecked)>0)
				return true;
			else if(dateCurrent.compareTo(dateToBeChecked)<0)
				return false;
			else
				return false;
		}catch (Exception e) {
			//System.out.println("DateParsing Error="+e.toString());
		}
		return false;
	}
	
	//Check is Given Date is After Current Date
	public static boolean isDateAfterCurrentDate(String strDate){
		String getIsoDate=getISOTime(); // GET CURRENT ISO DATE
		String currentDate=getIsoDate.substring(5,7)+"-"+getIsoDate.substring(8,10)+"-"+getIsoDate.substring(0,4);
		Date dateCurrent=null,dateToBeChecked=null;
		SimpleDateFormat simpleDateFormat=null;
		try{
			simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy");
			dateCurrent=simpleDateFormat.parse(currentDate);
			//dateCurrent=new Date(); // USE This Line to get System Date
			dateToBeChecked=simpleDateFormat.parse(strDate);
			if(dateCurrent.compareTo(dateToBeChecked)>0)
				return false;
			else if(dateCurrent.compareTo(dateToBeChecked)<0)
				return true;
			else
				return false;
		}catch (Exception e) {
			//System.out.println("DateParsing Error="+e.toString());
		}
		return false;
	}
	
	// Get Current ISO Date/Time by Latitude & Longitude
	public static String getISOTime(){
		String isoTIME=null;
		String lattitude="22.7166667";//Get & use Current values Here
		String longitude="75.8333333";//
		try{
			/*
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//timezone/isotime/text()";
			InputSource inputSource = new InputSource("http://www.earthtools.org/timezone-1.1/"+longitude+"/"+lattitude);
			isoTIME = (String) xpath.evaluate(expression, inputSource, XPathConstants.STRING);
			*/
			URL url = new URL(urlISO+lattitude+"/"+longitude);
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = db.parse(new InputSource(url.openStream()));
    		doc.getDocumentElement().normalize();
    		Element root = doc.getDocumentElement();
    		NodeList resultList = root.getElementsByTagName("timezone");
    		for(int i=0;i<resultList.getLength();i++){
    			Node resultNode = resultList.item(i);
    			NodeList resultChildList = resultNode.getChildNodes();
    			for(int j=0;j<resultChildList.getLength();j++){
    				Node childNodeOfResult = resultChildList.item(j);
    				String name = childNodeOfResult.getNodeName();
    				if(name.equalsIgnoreCase("isotime")){
    					isoTIME=childNodeOfResult.getFirstChild().getNodeValue();
    				}
    			}
    		}
		}catch (Exception e) {
    		e.printStackTrace();
		}
    	return isoTIME;
	}
	///////////////////////////////////////////////////////
	public static boolean isEmpty(String txt){
		if(txt!=null && txt.trim().length()!=0){
			return false;
		}
		return true;
	}
	
	// Check if given string contain a-z or A-Z only
	public boolean isAlphabeticString(String value){
		if(value.matches("^[a-zA-Z]+$")){
			return true;
		}else
			return false;
	}
}
