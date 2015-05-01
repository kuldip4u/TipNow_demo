package com.tipnow.pickmedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.tipnow.R;
import com.tipnow.TipNowApplication;
import com.tipnow.category.CategoryActivity;
import com.tipnow.message.MessageActivity;

public class PickMediaActivity extends MediaPickerActivity {
	Bitmap bmp;
	Bitmap bitmap;
	final static int REQUEST_VIDEO_CAPTURED = 2;
	Uri uriVideo = null;
	VideoView videoviewPlay;
	MediaController mediaController;
	ImageView imageViewImageFromGallary, imageViewTakeImage,
			imageViewPlaceHolder, placeholder;
	VideoView videoViewPlaceHolder;
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_GALLERY = 2;

	private String selectedImagePath;
	static String flag = "NONE";
	Context context;
	TipNowApplication tipNowApplication;

	// Kuldip
	Uri outputFileUri;
	String dirName = "TipNow";

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_media);
		tipNowApplication = (TipNowApplication) getApplication();
		// tipNowApplication.set_imageData(null);
		// tipNowApplication.setVideoFlag(false);
		context = this;
		try {
			mediaController = new MediaController(this);
			imageViewPlaceHolder = (ImageView) findViewById(R.id.imageViewPlaceHolder);
			videoViewPlaceHolder = (VideoView) findViewById(R.id.videoViewPlaceHolder);
			imageViewTakeImage = (ImageView) findViewById(R.id.imageViewTakeMedia);
			imageViewImageFromGallary = (ImageView) findViewById(R.id.imageViewMediaFromGallary);
			intent = getIntent();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		String media = intent.getStringExtra("Media");
		if (media.equalsIgnoreCase("IMAGE")) {
			try {
				imageViewImageFromGallary
						.setImageResource(R.drawable.image_frm_gallery);
				imageViewTakeImage.setImageResource(R.drawable.take_image);
				imageViewPlaceHolder.setVisibility(View.VISIBLE);
				videoViewPlaceHolder.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}

			// placeholder.setVisibility(View.GONE);
			// ((FrameLayout) findViewById(R.id.preview)).addView(preview);
		} else if (media.equalsIgnoreCase("VIDEO")) {
			// textViewTopHeader.setText("Pick Video");
			try {
				imageViewImageFromGallary
						.setImageResource(R.drawable.video_frm_gallery);
				imageViewTakeImage.setImageResource(R.drawable.take_video);
				videoViewPlaceHolder.setVisibility(View.VISIBLE);
				imageViewPlaceHolder.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
			// placeholder.setVisibility(View.VISIBLE);
		}
	}

	public void onClickBackButton(View v) {
		finish();
	}

	public void onClickMediaFromGallary(View v) {
		// imageViewPlaceHolder.setImageDrawable(getResources().getDrawable(R.drawable.img_placeholder));

		if (intent.getStringExtra("Media").equalsIgnoreCase("IMAGE")) {
			try {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				flag = "IMAGE";
				startActivityForResult(photoPickerIntent, SELECT_GALLERY);
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}

			/*
			 * Intent intent = new Intent(); intent.setType("image/*");
			 * flag="IMAGE"; intent.setAction(Intent.ACTION_GET_CONTENT);
			 * startActivityForResult(Intent.createChooser(intent,
			 * "Select Picture"), SELECT_GALLERY);
			 */
		} else if (intent.getStringExtra("Media").equalsIgnoreCase("VIDEO")) {
			try {
				Intent intent = new Intent();
				intent.setType("video/*");
				flag = "VIDEO";
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Video"),
						SELECT_GALLERY);
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}

		}
	}

	public void onClickTakeMedia(View v) {
		// imageViewPlaceHolder.setImageDrawable(getResources().getDrawable(R.drawable.img_placeholder));
		if (intent.getStringExtra("Media").equalsIgnoreCase("IMAGE")) {
			flag = "IMAGE";
			try {
					openCamera();

				/*outputFileUri = createDirectoryAndSaveFile();// Uri.fromFile(file);

				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri.toString());
				startActivityForResult(intent, SELECT_PICTURE);*/

			} catch (Exception e) {
				return;
			}

		} else if (intent.getStringExtra("Media").equalsIgnoreCase("VIDEO")) {
			flag = "VIDEO";
			try {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}

			// Intent cameraIntent = new
			// Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			// startActivityForResult(cameraIntent, SELECT_PICTURE);
			// Intent intent = new
			// Intent(PickMediaActivity.this,VideoActivity.class);
			// startActivity(intent);
		}
	}

	private Uri createDirectoryAndSaveFile() {

		File direct = new File(Environment.getExternalStorageDirectory() + "/"
				+ dirName + "/");
		if (!direct.exists()) { // Create new Directory
			// File appDir = new File(direct.getAbsolutePath());
			direct.mkdirs();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmddhhmmss");
		String photoFile = formatter.format(new Date()) + ".jpg";

		/*
		 * String fileName = direct.getPath() + File.separator + photoFile; File
		 * pictureFile = new File(fileName);
		 */
		File file = new File(direct, photoFile);
		try {
			file.createNewFile();
		} catch (IOException e) {
		}

		return Uri.fromFile(file);

		/*
		 * File myDirectory = new File(Environment.getExternalStorageDirectory()
		 * + "/" + DIRECTORY + "/"); if (!myDirectory.exists())
		 * myDirectory.mkdirs();
		 * 
		 * 
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 * String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
		 * 
		 * File file = new File(myDirectory, fileName); imageUri =
		 * Uri.fromFile(file); currentPhotoPath = file.getAbsolutePath(); Intent
		 * intent = new
		 * Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 * intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		 * startActivityForResult(intent, CAPTURE_IMAGE);
		 */

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				if (flag.equalsIgnoreCase("IMAGE")) { // Image taken from CAMERA
														// ..

			/*		Log.i("Tag", "Receive the camera result");
					final String selection = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
					new Thread(new Runnable() {
						@Override
						public void run() {
							final Bitmap mPhotoBitmap = ImageUtil.getBitmap(selection, 400,400);
							final Bitmap bitmap = ImageUtil
									.getCircledBitmap(mPhotoBitmap);
							imageViewPlaceHolder.post(new Runnable() {
								@Override
								public void run() {
									imageViewPlaceHolder.setImageBitmap(mPhotoBitmap);
								}
							});
						}
					}).start();*/



					/*
					 * if (bitmap != null) { bitmap.recycle(); } try { bitmap =
					 * (Bitmap) data.getExtras().get("data");
					 * imageViewPlaceHolder.setImageBitmap(bitmap);
					 * 
					 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
					 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					 * byte[] bitmapdata = bos.toByteArray();
					 * tipNowApplication.set_imageData(bitmapdata);
					 * tipNowApplication.setImagePath("NONE");
					 * tipNowApplication.setVideoFlag(false); } catch (Exception
					 * e) { // TODO: handle exception return; }
					 */
				} else if (flag.equalsIgnoreCase("VIDEO")) {

					try {
						tipNowApplication.setVideoData(null);
						videoViewPlaceHolder.setBackgroundResource(0);
						uriVideo = data.getData();
						File f1 = new File(getRealPathFromURI(uriVideo));
						// Log.e("Camera VIDEO-STREAM",
						// "Size is Bytes= "+f1.length()+",KB="+(f1.length()/1024));
						if ((f1.length() / 1024) > 20480) {
							tipNowApplication.setVideoFlag(false);
							final AlertDialog alertDialog = new AlertDialog.Builder(
									PickMediaActivity.this).create();
							alertDialog.setTitle("Alert");
							alertDialog
									.setMessage("Video size should be less than 20 MB.");
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													alertDialog.dismiss();
													finish();
												}
											});
										}
									});
							alertDialog.show();
						} else {
							mediaController.setAnchorView(videoViewPlaceHolder);
							videoViewPlaceHolder
									.setMediaController(mediaController);
							videoViewPlaceHolder.setVideoURI(uriVideo);
							try {
								// InputStream iStream =
								// getContentResolver().openInputStream(uriVideo);
								tipNowApplication
										.setVideoData(getBytesFromFile(f1));

							} catch (Exception e) {
								// Log.e("SELECT_PICTURE-VIDEO",
								// "getBytesFromFile ERROR: "+e.getMessage());
								return;
								// Log.e("Video-Bytes Read Error",
								// e.toString());
							}
							tipNowApplication.setVideoName(getRealPathFromURI(
									uriVideo).substring(
									getRealPathFromURI(uriVideo).lastIndexOf(
											"/") + 1,
									getRealPathFromURI(uriVideo).length()));
							tipNowApplication.setVideoFile(f1);
							tipNowApplication.setVideoFlag(true);
						}
					} catch (Exception e) {
						Log.e("SELECT_PICTURE-VIDEO",
								"ERROR: " + e.getMessage());
						return;
					}
				} // Video
			} // Select Picture
			else if (requestCode == SELECT_GALLERY) {
				if (flag.equalsIgnoreCase("IMAGE")) {
					Uri photoUri = data.getData();
					if (photoUri != null) {

						String realPath;
						// SDK < API11
						if (Build.VERSION.SDK_INT < 11)
							realPath = RealPathUtil
									.getRealPathFromURI_BelowAPI11(this,
											photoUri);
						// SDK >= 11 && SDK < 19
						else if (Build.VERSION.SDK_INT < 19)
							realPath = RealPathUtil
									.getRealPathFromURI_API11to18(this,
											photoUri);
						// SDK > 19 (Android 4.4)
						else
							realPath = RealPathUtil.getRealPathFromURI_API19(
									this, photoUri);

						Uri uriFromPath = Uri.fromFile(new File(realPath));

						// you have two ways to display selected image

						// ( 1 ) imageView.setImageURI(uriFromPath);

						// ( 2 ) imageView.setImageBitmap(bitmap);
						Bitmap bitmap = null;
						try {
							bitmap = BitmapFactory
									.decodeStream(getContentResolver()
											.openInputStream(uriFromPath));
						} catch (Exception e) {
							e.printStackTrace();
						}

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						bitmap.compress(CompressFormat.JPEG, 70, bos);
						byte[] bitmapdata = bos.toByteArray();
						tipNowApplication.set_imageData(bitmapdata);
						imageViewPlaceHolder.setImageBitmap(bitmap);
						// imageView.setImageBitmap(bitmap);

						Log.d("HMKCODE", "Build.VERSION.SDK_INT:"
								+ Build.VERSION.SDK_INT);
						Log.d("HMKCODE", "URI Path:" + data.getData().getPath());
						Log.d("HMKCODE", "Real Path: " + realPath);
					}
				}

			} else if (flag.equalsIgnoreCase("VIDEO")) {
				// placeholder.setVisibility(View.GONE);
				tipNowApplication.setVideoData(null);
				videoViewPlaceHolder.setBackgroundResource(0);
				try {
					uriVideo = data.getData();
					// InputStream iStream =
					// getContentResolver().openInputStream(uriVideo);
					File f2 = new File(getRealPathFromURI(uriVideo));
					// Log.e("Gallery VIDEO-STREAM",
					// "Size is Bytes= "+f2.length()+",KB="+(f2.length()/1024));
					if ((f2.length() / 1024) > 20480) {
						tipNowApplication.setVideoFlag(false);
						final AlertDialog alertDialog = new AlertDialog.Builder(
								PickMediaActivity.this).create();
						alertDialog.setTitle("Alert");
						alertDialog
								.setMessage("Video size should be less than 20 MB.");
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												alertDialog.dismiss();
												finish();
											}
										});
									}
								});
						alertDialog.show();
					} else {
						mediaController.setAnchorView(videoViewPlaceHolder);

						videoViewPlaceHolder
								.setMediaController(mediaController);
						videoViewPlaceHolder.setVideoURI(uriVideo);
						try {
							// InputStream iStream =
							// getContentResolver().openInputStream(uriVideo);
							tipNowApplication
									.setVideoData(getBytesFromFile(f2));
							// tipNowApplication.setVideoData(readBytes(uriVideo));
						} catch (Exception e) {
							Log.e("SELECT_GALLERY-VIDEO",
									"getBytesFromFile ERROR: " + e.getMessage());
							return;
							// Log.e("Video-Bytes Read Error", e.toString());
						}
						tipNowApplication.setVideoName(getRealPathFromURI(
								uriVideo)
								.substring(
										getRealPathFromURI(uriVideo)
												.lastIndexOf("/") + 1,
										getRealPathFromURI(uriVideo).length()));
						tipNowApplication.setVideoFile(f2);
						tipNowApplication.setVideoFlag(true);
					}
				} catch (Exception e) {
					Log.e("GALLER_VIDEO ERROR", e.getMessage());
				}
			}
		}
	}

	public byte[] readBytes(Uri uri) throws IOException {
		// this dynamically extends to take the bytes you read
		InputStream inputStream = getContentResolver().openInputStream(uri);
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		setMenuBackground();
		return true;
	}

	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {
			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try { // Ask our inflater to create the view
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						// Kind of apply our own background
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundResource(R.drawable.rounded_corner_darker);
								((TextView) view).setTextColor(Color.WHITE);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {

					}
				}
				return null;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemCategories:
			Intent intentCategory = new Intent(PickMediaActivity.this,
					CategoryActivity.class);
			startActivity(intentCategory);
			finish();
			break;
		case R.id.menuItemMessages:
			Intent intentMessage = new Intent(PickMediaActivity.this,
					MessageActivity.class);
			startActivity(intentMessage);
			finish();
			break;
		}
		return true;
	}

	/*
	 * @Override protected void onResume() { super.onResume(); if
	 * (intent.getStringExtra("Media").equalsIgnoreCase("IMAGE")) { byte[] data
	 * = tipNowApplication.get_imageData(); //
	 * System.out.println("+++++++++++++BYTES-DATA: "+data); if (data == null) {
	 * 
	 * } else { // BitmapFactory.Options opt = new BitmapFactory.Options(); //
	 * opt.inJustDecodeBounds = true; BitmapFactory.Options options = new
	 * BitmapFactory.Options(); options.inSampleSize = 2; Bitmap bitmap =
	 * BitmapFactory.decodeByteArray(data, 0, data.length, options);
	 * 
	 * // System.out.println("+++++++++++++++++++++BITMAP="+bitmap); bitmap =
	 * getResizedBitmap(bitmap, 160, 160); //
	 * imageViewPlaceHolder.setImageBitmap(Bitmap.createScaledBitmap(bitmap, //
	 * 150, 150, false)); imageViewPlaceHolder.setImageBitmap(bitmap); } } else
	 * if (intent.getStringExtra("Media").equalsIgnoreCase("VIDEO")) {
	 * 
	 * if(tipNowApplication.isVideoFlag()==true){ SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyyMMdd_HHmmss"); String curentDateandTime =
	 * sdf.format(new Date()); videoViewPlaceHolder.setVideoPath(Environment
	 * .getExternalStorageDirectory
	 * ().getAbsolutePath()+"/sdcard/TIP"+curentDateandTime+".3gp");
	 * videoViewPlaceHolder.setMediaController(new MediaController(this));
	 * videoViewPlaceHolder.requestFocus(); }else
	 * if(tipNowApplication.isVideoFlag()==false){
	 * 
	 * }
	 * 
	 * } }
	 */

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public byte[] getBytesFromFile(File file) {
		byte[] bytesToReturn = null;
		try {
			FileInputStream is = new FileInputStream(file);
			// Get the size of the file
			long length = file.length();
			Log.e("GetBytesFromFile--", "Length=" + length);
			if (length > Integer.MAX_VALUE) {
				throw new IOException("The file is too big");
			}
			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("The file was not completely read: "
						+ file.getName());
			}
			bytesToReturn = bytes;
			bytes = null;
			// Close the input stream, all file contents are in the bytes
			// variable
			is.close();
		} catch (Exception e) {
			Log.e("getBytesFromFile", e.getMessage());
		}
		return bytesToReturn;
	}

	

	/****............IMAGES ABSTRACT METHODS...........****/
	

	
	
	@Override
	protected void onGalleryImageSelected(String fileUri, Bitmap bitmap) {
		
	}

	@Override
	protected void onCameraImageSelected(String fileUri, Bitmap bitmap) {
		imageViewPlaceHolder.setImageBitmap(bitmap);
	
		try{	
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			 byte[] bitmapdata = bos.toByteArray();
			 tipNowApplication.set_imageData(bitmapdata);
			 tipNowApplication.setImagePath("NONE");
			 tipNowApplication.setVideoFlag(false); 
			 
		}catch (Exception e) { // TODO: handle exception return; }
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onVideoCaptured(String videoPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMediaPickCanceled(int reqCode) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
