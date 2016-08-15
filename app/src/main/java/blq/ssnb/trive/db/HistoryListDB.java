package blq.ssnb.trive.db;

import java.util.ArrayList;
import java.util.List;

import blq.ssnb.trive.constant.DbConstant;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 历史记录的List表，只有在这张表中出现的时间节点才表明trippointDb中有数据
 * @author SSNB
 *
 */
public class HistoryListDB {

	private DbHelper dbHelper;
	private SQLiteDatabase db ;
	public HistoryListDB(Context context) {
		dbHelper = DbHelper.getInstance(context);
	}

	public List<Integer> getHistoryList(String userID){
		ArrayList<Integer> recordDate = new ArrayList<>();
		db =dbHelper.getReadableDatabase();
		String sql = "select * from "
				+DbConstant.TABLE_NAME_HISTORY
				+" where "
				+DbConstant.HISTORY_FIEID_USERID +" = ? order by "
				+DbConstant.HISTORY_FIEID_DATE + " desc";
		Cursor cursor = db.rawQuery(sql, new String[]{userID});

		while(cursor.moveToNext()){
			recordDate.add(
					cursor.getInt(
							cursor.getColumnIndex(DbConstant.HISTORY_FIEID_DATE)
					)
			);
		}
		cursor.close();
		db.close();
		return recordDate;
	}

	public void instertHistory(String userID ,int date){
		if(!haveData(userID, date)){
			db = dbHelper.getWritableDatabase();
			String sql = "insert into "
					+DbConstant.TABLE_NAME_HISTORY
					+" ("+DbConstant.HISTORY_FIEID_DATE+","
					+DbConstant.HISTORY_FIEID_USERID+" ) "
					+" values(?,?)";
			db.execSQL(sql, new Object[]{date,userID});
			db.close();
		}else{
			updateHistory(userID, date);
		}
	}

	private boolean haveData(String userID,int date){
		boolean yes = false;
		db = dbHelper.getReadableDatabase();
		String sql = "select * from "
				+DbConstant.TABLE_NAME_HISTORY
				+" where "
				+DbConstant.HISTORY_FIEID_USERID + " =? and "
				+DbConstant.HISTORY_FIEID_DATE + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{userID,date+""});
		if(cursor.moveToNext()){
			yes = true;
		}
		cursor.close();
		db.close();
		return yes;
	}

	public void updateHistory(String userID ,int date){
		db = dbHelper.getWritableDatabase();
		String sql = "UPDATE " + DbConstant.TABLE_NAME_HISTORY+" SET "
				+DbConstant.HISTORY_FIEID_UPDATE + " =?"
				+" where "+DbConstant.HISTORY_FIEID_USERID + " =? and "
				+DbConstant.HISTORY_FIEID_DATE +" =?";
		db.execSQL(sql, new Object[]{userID,date});
		db.close();
	}
}
