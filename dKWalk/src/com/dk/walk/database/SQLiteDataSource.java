package com.dk.walk.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	@SuppressWarnings("unused")
	private Context ctx;
	
	public String[] allWayColumns = { SQLiteHelper.COLUMN_ID,
		      SQLiteHelper.COLUMN_TITLE,
		      SQLiteHelper.COLUMN_GPS,
		      SQLiteHelper.COLUMN_WAY,
		      SQLiteHelper.COLUMN_TIME,
		      SQLiteHelper.COLUMN_DATE,
		      SQLiteHelper.COLUMN_STEPS,
		      SQLiteHelper.COLUMN_CALORIES,
		      SQLiteHelper.COLUMN_DAY,
		      SQLiteHelper.COLUMN_WEEK,
		      SQLiteHelper.COLUMN_MONTH,
		      SQLiteHelper.COLUMN_YEAR};
	
	public SQLiteDataSource(Context context){
		ctx = context;
		dbHelper = new SQLiteHelper(context);
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	public void close() {
		dbHelper.close();
	}
	public SQLiteDatabase getDatabase(){
		return database;
	}
	public void reCreateDatabase(){
		dbHelper.reCreateDB(database);
	}
	public SQLWay getNewSQLWay(){
		SQLWay way = new SQLWay();
		way.SQLInsert(database);
		return way;
	}
	public void updateSQLWay(SQLWay way){
		way.SQLUpdate(database);
	}
	public void deleteSQLWay(SQLWay way){
		way.SQLDelete(database);
	}
	
}
