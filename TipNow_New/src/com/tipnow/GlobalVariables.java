package com.tipnow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class GlobalVariables {

	public static void showNetworkDialog(Activity activity) {

		final AlertDialog alertDialog = new AlertDialog.Builder(activity)
				.create();
		alertDialog.setMessage("No network available.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				alertDialog.dismiss();

			}
		});
		alertDialog.show();
	}
}
