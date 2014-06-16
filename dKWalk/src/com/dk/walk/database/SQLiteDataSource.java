package com.dk.walk.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
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

	public SQLWay getSQLWaybyId(long id){
		SQLWay way = null;

		Cursor cursor = database.query(SQLiteHelper.TABLE_WAY, allWayColumns, SQLiteHelper.COLUMN_ID + " LIKE ?" ,new String[]{id + "%"}, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount() == 1){
			way = new SQLWay(cursor);
		}
		cursor.close();

		return way;
	}

	public List<SQLWay> getAllSQLWays(){
		List<SQLWay> ways = new ArrayList<SQLWay>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_WAY, allWayColumns, null, null, null, null, SQLiteHelper.COLUMN_DATE);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SQLWay way = new SQLWay(cursor);
			ways.add(way);
			cursor.moveToNext();
		}
		cursor.close();

		return ways;
	}

	public SQLWay getLastWay(){
		Cursor cursor = database.query(SQLiteHelper.TABLE_WAY, allWayColumns, null, null, null, null, SQLiteHelper.COLUMN_DATE);
		if(cursor.getCount() > 0){
			cursor.moveToLast();
			SQLWay way = new SQLWay(cursor);
			cursor.close();
			
			return way;
		}
		return null;
	}

}
