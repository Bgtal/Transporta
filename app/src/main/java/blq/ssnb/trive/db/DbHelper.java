package blq.ssnb.trive.db;

import blq.ssnb.trive.constant.DbConstant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 创建数据库
 * @author ssnb
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	private static DbHelper instance;
	public static DbHelper getInstance(Context context){
		if(instance==null){
			instance = new DbHelper(context);
		}
		return instance;
	}
	
	private  DbHelper(Context context) {
		super(context, DbConstant.DB_NAME, null, DbConstant.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DbConstant.CREATE_TABLE_USER);
		
		db.execSQL(DbConstant.CREATE_TABLE_TRIPS);
		
		db.execSQL(DbConstant.CREATE_TABLE_POINTS);
		
		db.execSQL(DbConstant.CREATE_TABLE_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL(DbConstant.CREATE_TABLE_USER);
	}

}
