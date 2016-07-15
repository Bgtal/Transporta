package blq.ssnb.trive.db;

import blq.ssnb.trive.constant.DbConstant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CacheDB {
	private DbHelper dbHelper;
	
	public CacheDB(Context context){
		dbHelper = DbHelper.getInstance(context);
	}
	
	public void addCache(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql="insert into"+DbConstant.TABLE_NAME_CACHE
				+"("+DbConstant.CACHE_FIEID_PROVIDER+")";
	}
	public void updateCache(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	}
	public void cleanCache(){
		
	}
}
