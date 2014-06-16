package com.dk.walk.database;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dk.walk.App;
import com.dk.walk.R;

public class SQLWay {
	private static final String TAG = "SQLWay";
	
	private Long id;
	private String title;
	private JSONArray gps;
	private Float way;
	private Integer time;
	private Long date;
	private Integer steps;
	private Integer calories;
	private Integer day;
	private Integer week;
	private Integer month;
	private Integer year;
	
	private Double speed;
	private SharedPreferences prefs;
	
	private DecimalFormat format = new DecimalFormat("0.00");
	private DateFormat formatter = new SimpleDateFormat();
	
	public SQLWay(){
		title = "";
		gps = new JSONArray();
		way = 0f;
		time = 0;
		date = System.currentTimeMillis();		
		steps = 0;
		calories = 0;
		
		Calendar now = Calendar.getInstance();
		
		day = now.get(Calendar.DAY_OF_MONTH);
		week = now.get(Calendar.WEEK_OF_YEAR);
		month = now.get(Calendar.MONTH);
		year = now.get(Calendar.YEAR);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(App.getContextStatic());
	}
	
	public SQLWay(SQLWay way){
		this.id = way.getId();
		this.title = way.getTitle();
		this.gps = way.getGps();
		this.way = way.getWay();
		this.time = way.getTime();
		this.date = way.getDate();
		this.steps = way.getSteps();
		this.calories = way.getCalories();
		this.day = way.getDay();
		this.week = way.getWeek();
		this.month = way.getMonth();
		this.year = way.getYear();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(App.getContextStatic());
	}
	public SQLWay(String title, JSONArray gps, float way, int time, long date, int steps, int calories, int day, int week, int month, int year){
		this.title = title;
		this.gps = gps;
		this.way = way;
		this.time = time;
		this.date = date;
		this.steps = steps;
		this.calories = calories;
		this.day = day;
		this.week = week;
		this.month = month;
		this.year = year;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(App.getContextStatic());
	}
	
	public SQLWay(Cursor cursor){
		try{
			this.id = cursor.getLong(0);
			this.title = cursor.getString(1);
			this.gps = new JSONArray(cursor.getString(2));
			this.way = cursor.getFloat(3);
			this.time = cursor.getInt(4);
			this.date = cursor.getLong(5);
			this.steps = cursor.getInt(6);
			this.calories = cursor.getInt(7);
			this.day = cursor.getInt(8);
			this.week = cursor.getInt(9);
			this.month = cursor.getInt(10);
			this.year = cursor.getInt(11);
			
		}catch (JSONException e){
			e.printStackTrace();
		}
		
		prefs = PreferenceManager.getDefaultSharedPreferences(App.getContextStatic());
	}
	public long SQLInsert(SQLiteDatabase database){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TITLE, title);
		values.put(SQLiteHelper.COLUMN_GPS, gps.toString());
		values.put(SQLiteHelper.COLUMN_WAY, way);
		values.put(SQLiteHelper.COLUMN_TIME, time);
		values.put(SQLiteHelper.COLUMN_DATE, date);
		values.put(SQLiteHelper.COLUMN_STEPS, steps);
		values.put(SQLiteHelper.COLUMN_CALORIES, calories);
		values.put(SQLiteHelper.COLUMN_DAY, day);
		values.put(SQLiteHelper.COLUMN_WEEK, week);
		values.put(SQLiteHelper.COLUMN_MONTH, month);
		values.put(SQLiteHelper.COLUMN_YEAR, year);
		long insertId = database.insert(SQLiteHelper.TABLE_WAY, null, values);
		id = insertId;
		
		return insertId;
	}
	public void SQLDelete(SQLiteDatabase database){
		database.delete(SQLiteHelper.TABLE_WAY, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	public void SQLUpdate(SQLiteDatabase database){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TITLE, title);
		values.put(SQLiteHelper.COLUMN_GPS, gps.toString());
		values.put(SQLiteHelper.COLUMN_WAY, way);
		values.put(SQLiteHelper.COLUMN_TIME, time);
		values.put(SQLiteHelper.COLUMN_DATE, date);
		values.put(SQLiteHelper.COLUMN_STEPS, steps);
		values.put(SQLiteHelper.COLUMN_CALORIES, calories);
		values.put(SQLiteHelper.COLUMN_DAY, day);
		values.put(SQLiteHelper.COLUMN_WEEK, week);
		values.put(SQLiteHelper.COLUMN_MONTH, month);
		values.put(SQLiteHelper.COLUMN_YEAR, year);
		database.update(SQLiteHelper.TABLE_WAY, values, SQLiteHelper.COLUMN_ID + "=" + id, null);
	}
	
	public void addWay(float diff){
		way += diff;
	}
	public void addTime(int time){
		this.time += time;
	}
	public void addStep(){
		steps++;
	}
	private void actSpeed(){
		double km = way / 1000f;
		Log.d(TAG, "km:" + km);
		double hours = time / 3600000f;
		Log.d(TAG, "hours:" + hours + "  time:" + time);
		speed = km / hours;
	}
	public Double getSpeed(){
		actSpeed();
		return speed;
	}
	
	public String getFormatedSpeed(){
		actSpeed();
		return format.format(speed) + " km/h";
	}
	private void actCalories(){
		Log.d(TAG, "1");
		actSpeed();
		if(speed != null && time != null){
			Log.d(TAG, "2");
			double cor;
			int weight = Integer.parseInt(prefs.getString("weight", "60"));
			Log.d(TAG, "3");
			if(speed <= 8){
				cor = 8.9;
			}else if(speed <= 10){
				cor = 10.14;
			}else if(speed <= 12){
				cor = 11.8;
			}else if(speed <= 15){
				cor = 15.14;
			}else if(speed <= 18){
				cor = 19.20;
			}else if(speed <= 20){
				cor = 23;
			}else{
				cor = 25;
			}
			Log.d(TAG, "4");
			cor = ((cor / 3600000) * time);
			
			//calories = (int) (cor * weight); TODO
			double km = way / 1000f;
			calories = (int) (km * weight);
			Log.d(TAG, "5");
		}
	}

	public void addLocation(Location loc){
		gps.put(LocationToString(loc));
	}
	public String LocationToString(Location loc){
		JSONArray point = new JSONArray();
		try {
			point.put(loc.getLongitude());
			point.put(loc.getLatitude());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return point.toString();
		
	}
	public Location stringToLocation(String string){
		Location loc = new Location("point");
		try {
			JSONArray point = new JSONArray(string);
			loc.setLongitude(point.getDouble(0));
			loc.setLatitude(point.getDouble(1));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return loc;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		if(title.isEmpty()){
			return App.getContextStatic().getString(R.string.no_title);
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONArray getGps() {
		return gps;
	}

	public void setGps(JSONArray gps) {
		this.gps = gps;
	}
	
	public Float getWay() {
		return way;
	}
	
	public String getFormatedWay(){
		return format.format(way) + " m";
	}
	public void setWay(Float way) {
		this.way = way;
	}
	
	public Integer getTime() {
		return time;
	}
	
	public String getFormatedTime(){
		Integer sec = time / 1000;
		if(sec < 60){
			return sec.toString() + " sec.";
		}else if(sec < 3600){
			Float min = (float) (sec / 60.0);
			return format.format(min) + " min.";
		}else{
			Float hour = (float) (sec / 60.0 / 60.0);
			return format.format(hour)+ "St.";
		}
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	
	public Long getDate() {
		return date;
	}
	
	public String getFormatedDate() {
		Timestamp stamp = new Timestamp(date);
		Date date = new Date(stamp.getTime());
		return formatter.format(date);
	}
	
	public void setDate(long date) {
		this.date = date;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Integer getCalories() {
		actCalories();
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
}
