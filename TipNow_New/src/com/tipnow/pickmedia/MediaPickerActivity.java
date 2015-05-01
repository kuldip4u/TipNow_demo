package com.tipnow.pickmedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public abstract class MediaPickerActivity extends Activity {
	protected static final int IMAGE_DIMENSION = 400;

	protected static final int REQ_CODE_TAKE_FROM_CAMERA = 500;
	protected static final int REQ_CODE_PICK_FROM_GALLERY = 501;
	protected static final int REQ_CODE_CROP_PHOTO = 502;
	protected static final int REQ_CODE_RECORD_VIDEO = 503;
	
	private final String directory_Name = "TipNowCache";
	private static final String IMAGE_UNSPECIFIED = "image/*";

	private Uri capturedImageUri, cropImageUri;
	protected Bitmap capturedImageBitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initTmpUris() {
		File proejctDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + directory_Name);
		if (!proejctDirectory.exists()) {
			proejctDirectory.mkdir();
		} else {
			// delete all old files
			for (File file : proejctDirectory.listFiles()) {
				if (file.getName().startsWith("tmp_")) {
					file.delete();
				}
			}
		}
		// Construct temporary image path and name to save the taken
		// photo
		capturedImageUri = Uri.fromFile(new File(proejctDirectory, "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		File extraOutputFile = new File(proejctDirectory, "croped_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
		extraOutputFile.setWritable(true);
		cropImageUri = Uri.fromFile(extraOutputFile);
	}

	/**
	 * This method use for take picture from camera and crop function
	 */

	protected void openCamera() {
		System.out.println("MediaPickerActivity - openCamera");

		initTmpUris();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, capturedImageUri);
		intent.putExtra("return-data", true);
		try {
			// Start a camera capturing activity REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
			// defined to identify the activity in onActivityResult() when it returns
			startActivityForResult(intent, REQ_CODE_TAKE_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}

		return;

	}

	/**
	 * This method use for open gallery and crop image using default crop of
	 * android
	 */
	protected void openGallery() {
		System.out.println("MediaPickerActivity - openGallery SDK_INT=" + Build.VERSION.SDK_INT);
		if (Build.VERSION.SDK_INT < 19) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQ_CODE_PICK_FROM_GALLERY);
		} else {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, REQ_CODE_PICK_FROM_GALLERY);
		}
	}

	protected void openVideoCamera() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
		intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
		startActivityForResult(intent, MediaPickerActivity.REQ_CODE_RECORD_VIDEO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_TAKE_FROM_CAMERA:
			if (resultCode == RESULT_OK) {
				System.out.println("REQ_CODE_TAKE_FROM_CAMERA imageUri: " + capturedImageUri);
				cropImage();
			} else {
				onMediaPickCanceled(REQ_CODE_TAKE_FROM_CAMERA);
			}
			break;
		case REQ_CODE_PICK_FROM_GALLERY:
			if (resultCode == RESULT_OK && data.getData() != null) {
				initTmpUris();
				capturedImageUri = data.getData();
				cropImage();
			} else {
				onMediaPickCanceled(REQ_CODE_PICK_FROM_GALLERY);
			}
			break;
		case REQ_CODE_CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					capturedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
					onCameraImageSelected(cropImageUri.toString(), capturedImageBitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				onMediaPickCanceled(REQ_CODE_CROP_PHOTO);
			}
			break;
		case REQ_CODE_RECORD_VIDEO:
			if (resultCode == RESULT_OK) {
				Uri vid = data.getData();
				onVideoCaptured(getVideoPathFromURI(vid));
			} else {
				onMediaPickCanceled(REQ_CODE_RECORD_VIDEO);
			}
			break;

		default:
			break;
		}
	}

	/*
	 * This method use for Crop image taken from camera
	 */
	private void cropImage() {
		// Use existing crop activity.
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(capturedImageUri, IMAGE_UNSPECIFIED);
		intent.putExtra("outputX", IMAGE_DIMENSION);
		intent.putExtra("outputY", IMAGE_DIMENSION);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cropImageUri);

		startActivityForResult(intent, REQ_CODE_CROP_PHOTO);
	}

	private String getVideoPathFromURI(Uri contentUri) {
		String videoPath = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			videoPath = cursor.getString(column_index);
		}
		cursor.close();
		return videoPath;
	}

	protected abstract void onGalleryImageSelected(String fileUri, Bitmap bitmap);

	protected abstract void onCameraImageSelected(String fileUri, Bitmap bitmap);

	protected abstract void onVideoCaptured(String videoPath);

	protected abstract void onMediaPickCanceled(int reqCode);

}
