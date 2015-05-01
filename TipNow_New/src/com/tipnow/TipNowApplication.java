package com.tipnow;

import java.io.File;

import android.app.Application;

public class TipNowApplication extends Application {
	
	String category="NONE";
	boolean videoFlag=false;
	byte[] _imageData=null;
	String applicationKey="9b22e8ac450bf8dabd90915b1b00a15c";
	String UDID=null;
	String latitude="",longitude="";
	String imagePath="";
	File videoFile=null;
	String videoName=null;
	byte[] videoData=null;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public byte[] get_imageData() {
		return _imageData;
	}

	public void set_imageData(byte[] _imageData) {
		this._imageData = _imageData;
	}
	
	public void setcategory(String str)
	{
	       category=str;
    }
	
    public String getcategory()
    {
    	return category;    
    }

	public boolean isVideoFlag() {
		return videoFlag;
	}

	public void setVideoFlag(boolean videoFlag) {
		this.videoFlag = videoFlag;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public String getUDID() {
		return UDID;
	}

	public void setUDID(String uDID) {
		UDID = uDID;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public File getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
	}

	public byte[] getVideoData() {
		return videoData;
	}

	public void setVideoData(byte[] videoData) {
		this.videoData = videoData;
	}

}