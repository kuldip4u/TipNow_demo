package com.tipnow.pickmedia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.util.Base64;

public class ImageUtil {

	/*public static String encodeTobase64(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		byte[] bytes = byteArrayOutputStream.toByteArray();

		String bitmapString = Base64.encodeToString(bytes, Base64.DEFAULT);

		return bitmapString;
	}

	public static Bitmap decodeBase64(String bitmapString) {
		if (null == bitmapString) {
			return null;
		}

		byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);

		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}*/

	public static Bitmap merge(Bitmap bitmap, Bitmap overlay, int left, int top, int alpha) {
		if (null == bitmap || bitmap.getWidth() < 1 || bitmap.getHeight() < 1) {
			return bitmap;
		} else if (null == overlay || overlay.getWidth() < 1 || overlay.getHeight() < 1) {
			return bitmap;
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap merged = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(merged);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		paint.setAlpha(alpha);
		// overlay = getCircledBitmap(overlay, overlay.getWidth());
		canvas.drawBitmap(overlay, left, top, paint);

		return merged;
	}

	/**
	 * Convert Square to Circle Image
	 * 
	 * @param bitmap
	 *            the bitmap to be circled
	 * @return the bitmap
	 */
	public static Bitmap getCircledBitmap(Bitmap bitmap) {
		int minDim = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
		Bitmap targetBitmap = Bitmap.createBitmap(minDim, minDim, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(minDim / 2, minDim / 2, minDim / 2, Path.Direction.CCW);

		canvas.clipPath(path);
		canvas.drawBitmap(bitmap, new Rect(0, 0, minDim, minDim), new Rect(0, 0, minDim, minDim), null);
		return targetBitmap;
	}

	public static Bitmap getBitmap(String path) {
		if (null != path) {
			ImageDecoder imageDecoder = new ImageUtil().new ImageDecoder();
			return imageDecoder.getBitmap(path);
		}
		return null;
	}

	public static Bitmap getBitmap(String path, int maxWidth, int maxHeight) {
		if (null != path) {
			ImageDecoder imageDecoder = new ImageUtil().new ImageDecoder();
			return imageDecoder.getBitmap(path, maxWidth, maxHeight);
		}
		return null;
	}

	private class ImageDecoder {
		private BitmapFactory.Options options;
		private int inSampleSize;

		public ImageDecoder() {
			options = new BitmapFactory.Options();
			inSampleSize = 1;
		}

		public Bitmap getBitmap(final String path) {
			options.inSampleSize = inSampleSize;
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(path, options);
				System.out.println("getBitmap inSampleSize=" + inSampleSize + ", width=" + bitmap.getWidth() + ", height="
						+ bitmap.getHeight());
				return bitmap;
			} catch (OutOfMemoryError e) {
				inSampleSize *= 2;
				return getBitmap(path);
			} catch (Exception e) {
				return null;
			}
		}

		public Bitmap getBitmap(final String path, int maxWidth, int maxHeight) {
			options.inSampleSize = inSampleSize;
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(path, options);
				if (bitmap.getWidth() > maxWidth || bitmap.getHeight() > maxHeight) {
					inSampleSize *= 2;
					return getBitmap(path, maxWidth, maxHeight);
				}
				System.out.println("getBitmap inSampleSize=" + inSampleSize + ", width=" + bitmap.getWidth() + ", height="
						+ bitmap.getHeight());
				return bitmap;
			} catch (OutOfMemoryError e) {
				inSampleSize *= 2;
				return getBitmap(path);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/*public static int getRotationAngle(String imagePath) {
		int rotate = 0;
		try {
			ExifInterface ei = new ExifInterface(imagePath);
			switch (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;

			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return rotate;
	}*/

}
