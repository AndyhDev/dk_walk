package com.dk.walk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper{
	public static final String TABLE_WAY = "way";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_GPS = "gps";
	public static final String COLUMN_WAY = "way";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_STEPS = "steps";
	public static final String COLUMN_CALORIES= "calories";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_WEEK= "week";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_YEAR = "year";
	
	private static final String DATABASE_NAME = "songs.db";
	private static final int DATABASE_VERSION = 2;


	private static final String DATABASE_SONGS_WAY = "create table " + TABLE_WAY + "("
			+ COLUMN_ID + " INTEGER primary key autoincrement, "
			+ COLUMN_TITLE + " VARCHAR(250) not null,"
			+ COLUMN_GPS + " TEXT not null,"
			+ COLUMN_WAY + " FLOAT not null,"
			+ COLUMN_TIME + " INTEGER not null,"
			+ COLUMN_DATE + " INTEGER not null,"
			+ COLUMN_STEPS + " INTEGER not null,"
			+ COLUMN_CALORIES + " INTEGER not null,"
			+ COLUMN_DAY + " INTEGER not null,"
			+ COLUMN_WEEK + " INTEGER not null,"
			+ COLUMN_MONTH + " INTEGER not null,"
			+ COLUMN_YEAR + " INTEGER not null"
			+");";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void reCreateDB(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAY);
		onCreate(db);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_SONGS_WAY);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAY);
		onCreate(db);
	}

}
