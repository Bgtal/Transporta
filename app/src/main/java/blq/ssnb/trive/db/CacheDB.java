package blq.ssnb.trive.db;

import blq.ssnb.trive.constant.DbConstant;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

public class CacheDB {
	private DbHelper dbHelper;
	
	public CacheDB(Context context){
		dbHelper = DbHelper.getInstance(context);
	}
	
	public void addCache(String type,Location location){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql="replace into "+DbConstant.TABLE_NAME_CACHE
				+" ( "
				+DbConstant.CACHE_FILED_ID
				+","+DbConstant.CACHE_FIEID_PROVIDER
				+","+DbConstant.CACHE_FIEID_ACCURACY
				+","+DbConstant.CACHE_FIEID_LATITUDE
				+","+DbConstant.CACHE_FIEID_LONGITUDE
				+","+DbConstant.CACHE_FIEID_SPEED
				+","+DbConstant.CACHE_FIEID_TIME
				+") VALUES (?,?,?,?,?,?,?)";
		db.execSQL(sql,new Object[]{type,location.getProvider(),location.getAccuracy(),
				location.getLatitude(),location.getLongitude(),location.getSpeed(),
				location.getTime()});
		db.close();
	}
	public void updateCache(String type,String time){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = " UPDATE "+DbConstant.TABLE_NAME_CACHE+" SET "
		+DbConstant.CACHE_FIEID_TIME + " =? WHERE "
		+DbConstant.CACHE_FILED_ID + " =? ";
		db.execSQL(sql,new Object[]{time,type});
		db.close();
	}
	public void cleanCache(String type){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = " DELETE FROM "+DbConstant.TABLE_NAME_CACHE
				+ " WHERE "+DbConstant.CACHE_FILED_ID +" =? ";
		db.execSQL(sql, new Object[]{type});
		db.close();
	}
	
	public Location readCache(String type){
		Location location = null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "SELECT * FROM "+DbConstant.TABLE_NAME_CACHE
				+" WHERE "
				+DbConstant.CACHE_FILED_ID + " =? ";
		Cursor cursor = db.rawQuery(sql, new String[]{type});
		while(cursor.moveToNext()){
			location = new Location(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_PROVIDER)));
			location.setAccuracy(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_ACCURACY))));
			location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_LATITUDE))));
			location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_LONGITUDE))));
			location.setSpeed(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_SPEED))));
			location.setTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(DbConstant.CACHE_FIEID_TIME))));
		}
		cursor.close();
		db.close();
		return location;
	}
}
