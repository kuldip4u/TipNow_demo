package com.tipnow.orgcustomer;

import java.util.ArrayList;

public class GetterSetter {
	/** Variables */
	private ArrayList<String> Org = new ArrayList<String>();
	private ArrayList<String> Org_id = new ArrayList<String>();
	private ArrayList<String> Address_id = new ArrayList<String>();
	private ArrayList<String> appType = new ArrayList<String>();
	/** In Setter method default it will return arraylist
	* change that to add */
	public ArrayList<String> getOrg() {
		return Org;
	}
	
	public void setOrg(String org) {
		this.Org.add(org);
	}
	
	public ArrayList<String> getOrg_id() {
		return Org_id;
	}
	
	public void setOrg_id(String org_id) {
		this.Org_id.add(org_id);
	}
	
	public ArrayList<String> getAddressId() {
		return Address_id;
	}
	
	public void setAddress_id(String address_id) {
		this.Address_id.add(address_id);
	}
	
	public ArrayList<String> getAppType() {
		return appType;
	}
	
	public void setAppType(String appType) {
		this.appType.add(appType);
	}
}
