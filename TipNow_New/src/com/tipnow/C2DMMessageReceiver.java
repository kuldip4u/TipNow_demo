package com.tipnow;

import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;

public class C2DMMessageReceiver extends BroadcastReceiver
{
	private Vibrator vibrator; 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) 
		{
			//---- Below key 'message' must be same on the Third Party Server also.
			String payload = intent.getStringExtra("message");
			if(payload==null)
				payload="";
			Intent i=new Intent(context, MessageReceivedActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.putExtra("payload", payload);
			context.startActivity(i);
		}
	}

	public void createNotification(Context context, String payload) 
	{
		vibrator.vibrate(500); 
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("New Message");
		alertDialog.setMessage(payload);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
						vibrator.cancel();
						alertDialog.dismiss();
			} 
		});
		alertDialog.show();
		/*NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.mail, "Message received", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context,  MessageReceivedActivity.class);
		intent.putExtra("payload", payload);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, "Message", "TipNow Message Received", pendingIntent);
		notificationManager.notify(0, notification);*/
	}

}
