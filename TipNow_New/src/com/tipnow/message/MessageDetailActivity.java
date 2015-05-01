package com.tipnow.message;

import com.tipnow.R;
import com.tipnow.category.CategoryActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MessageDetailActivity extends Activity{

	Intent  intent; 
	TextView textViewMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);
		
		textViewMessage = (TextView) findViewById(R.id.textViewMessage);
		intent = getIntent();
		textViewMessage.setText(intent.getStringExtra("message"));
		
	}
	public void onClickBackButton(View v){
		Intent intentMessage = new Intent(MessageDetailActivity.this, MessageActivity.class);
		//intentMessage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intentMessage);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuItemCategories:     
        	Intent intentCategory = new Intent(MessageDetailActivity.this,CategoryActivity.class);
        	//intentCategory.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        	startActivity(intentCategory);
        	finish();
            break;
        case R.id.menuItemMessages:   
        	Intent intentMessage = new Intent(MessageDetailActivity.this,MessageActivity.class);
        	//intentMessage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        	startActivity(intentMessage);
        	finish();
            break;
		}
		return true;
	}
}