package com.tipnow.orgcustomer;

import com.tipnow.R;
import com.tipnow.orgcustomer.CustomerList.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpecialAdapter extends BaseAdapter{
	//Defining the background color of rows. The row will alternate between green light and green dark.
	//private int[] colors = new int[] { 0xAAf6ffc8, 0xAA538d00 };
	//private int[] colors = new int[] { 0xAAF3F5F7, 0xAAE1E3E6 };
	private LayoutInflater mInflater;
	//The variable that will hold our text data to be tied to list.
	private static String[] data;

	@SuppressWarnings("static-access")
	public SpecialAdapter(Context context, String[] items) {
	    mInflater = LayoutInflater.from(context);
	    this.data = items;
	}

	@Override
	public int getCount() {
	    return data.length;
	}

	@Override
	public Object getItem(int position) {
	    return position;
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	public static String getitematpostion( int pos)
	{
		return data[pos];
	}
	
	//A view to hold each row in the list
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	// A ViewHolder keeps references to children views to avoid unneccessary calls
	// to findViewById() on each row.
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.items_list, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.headline);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// Bind the data efficiently with the holder.
		holder.text.setText(data[position]);
	    //Set the background color depending of  odd/even colorPos result
	    //int colorPos = position % colors.length;
	    //convertView.setBackgroundColor(colors[colorPos]);
	    return convertView;
	}

}