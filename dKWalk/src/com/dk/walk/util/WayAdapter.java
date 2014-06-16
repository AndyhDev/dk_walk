package com.dk.walk.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;

public class WayAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private static final String TAG = "WayAdapter";
	
	private List<SQLWay> wayList;
	private LayoutInflater songInf;
	private static Context context;
	private String wayText;
	private Integer expandedIndex;
	
	final int INVALID_ID = -1;
	
	public WayAdapter(Context c, List<SQLWay> ways){
		context = c;
		wayList = ways;
		songInf = LayoutInflater.from(c);
		
		wayText = context.getString(R.string.way);
	}
	
	public void setSongList(List<SQLWay> ways){
		wayList = ways;
	}
	@Override
	public int getCount() {
		if(wayList == null){
			return 0;
		}
		return wayList.size();
	}

	@Override
	public SQLWay getItem(int position) {
		return wayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (position < 0 || position >= wayList.size()) {
            return INVALID_ID;
        }
        SQLWay item = getItem(position);
        return item.getId();
	}
	public int getIndex(SQLWay way){
		return getIndex(way.getId());
	}
	public int getIndex(long id){
		if(wayList != null){
			for(int i = 0; i < wayList.size(); i++){
				if(wayList.get(i).getId() == id){
					return i;
				}
			}
		}
		return 0;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		WayHolder viewHolder;
		SQLWay curWay = wayList.get(position);
		if(view == null){
			view = (RelativeLayout)songInf.inflate(R.layout.way_item, parent, false);
			viewHolder = new WayHolder();
			
			viewHolder.title = (TextView)view.findViewById(R.id.title);
			viewHolder.date = (TextView)view.findViewById(R.id.date);
			viewHolder.way = (TextView)view.findViewById(R.id.way);
			
			view.setTag(viewHolder);
		}else{
			viewHolder = (WayHolder)view.getTag();
		}
		viewHolder.title.setText(curWay.getTitle());
		viewHolder.date.setText(curWay.getFormatedDate());
		viewHolder.way.setText(wayText + "  " + curWay.getFormatedWay());
		
		viewHolder.sqlWay = curWay;
		viewHolder.position = position;
		return view;
	}
}