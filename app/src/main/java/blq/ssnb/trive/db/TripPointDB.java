package blq.ssnb.trive.db;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.DbConstant;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.util.DateConvertUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 用于存储在记录过程中的每一个点
 * @author SSNB
 *
 */
public class TripPointDB {
	private DbHelper dbHelper;
	private SQLiteDatabase db ;
	public TripPointDB(Context context) {
		dbHelper = DbHelper.getInstance(context);
	}

	//-----------------------增加---------------------
	
	/**
	 * 添加TripPoint 对象
	 * @param point
	 */
	public void addTripPoint(TripPointInfo point){
		if(point.getStyle()==DrawStyle.LINE){
			addTripPointByLine(point);
		}else{
			addTripPointByMark(point);
		}
	}
	/**
	 * 添加线
	 * @param linePoint
	 */
	private void addTripPointByLine(TripPointInfo linePoint){
		db = dbHelper.getWritableDatabase();
		String sql = "replace into "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" ( "+DbConstant.TRIPPOINT_FIEID_USERID+","
				+DbConstant.TRIPPOINT_FIEID_LATITUDE+","
				+DbConstant.TRIPPOINT_FIEID_LONGITUDE+","
				+DbConstant.TRIPPOINT_FIEID_TIME+","
				+DbConstant.TRIPPOINT_FIEID_TYPE+" )"
				+"values(?,?,?,?,?)";
		db.execSQL(sql, new Object[]{
				linePoint.getUserID(),
				linePoint.getLatitude(),
				linePoint.getLongitude(),
				linePoint.getStamp(),
				0});
		db.close();
	}

	/**
	 * 添加停止点
	 * @param markPoint
	 */
	private void addTripPointByMark(TripPointInfo markPoint){
		db = dbHelper.getWritableDatabase();
		String sql = "insert into "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" ( "+DbConstant.TRIPPOINT_FIEID_USERID+","
				+DbConstant.TRIPPOINT_FIEID_LATITUDE+","
				+DbConstant.TRIPPOINT_FIEID_LONGITUDE+","
				+DbConstant.TRIPPOINT_FIEID_TIME+","
				+DbConstant.TRIPPOINT_FIEID_TYPE+","
				+DbConstant.TRIPPOINT_FIEID_REASON+","
				+DbConstant.TRIPPOINT_FIEID_WAY+","
				+DbConstant.TRIPPOINT_FIEID_CONTENT+" )"
				+"values(?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{
				markPoint.getUserID(),
				markPoint.getLatitude(),
				markPoint.getLongitude(),
				markPoint.getStamp(),
				1,
				markPoint.getReason(),
				markPoint.getWay(),
				markPoint.getContent()});
		db.close();

	}

	/**
	 * 更新停止点的信息
	 * @param markPoint
	 */
	public void updateStopInfo(TripPointInfo markPoint){
		db = dbHelper.getWritableDatabase();
		String sql = "UPDATE "+DbConstant.TABLE_NAME_TRIPPOINTS+" SET "
				+DbConstant.TRIPPOINT_FIEID_TYPE + " = ? ,"
				+DbConstant.TRIPPOINT_FIEID_REASON + " = ? ,"
				+DbConstant.TRIPPOINT_FIEID_WAY + " = ? ,"
				+DbConstant.TRIPPOINT_FIEID_CONTENT + " = ? "
				+" where "+DbConstant.TRIPPOINT_FIEID_ID +" = ? ";
		db.execSQL(sql, new Object[]{
				markPoint.getStyle().getTag(),
				markPoint.getReason(),
				markPoint.getWay(),
				markPoint.getContent(),
				markPoint.getPid()});
		db.close();
	}

	public void updateReson(int pid,int reason){
		db = dbHelper.getWritableDatabase();
		String sql = " UPDATE "+DbConstant.TABLE_NAME_TRIPPOINTS+" SET "
				+DbConstant.TRIPPOINT_FIEID_REASON+" =? ,"
				+" WHERE " + DbConstant.TRIPPOINT_FIEID_ID + " =? ";
		db.execSQL(sql,new Object[]{pid,reason});
	}
	public void updateWay(int pid,int way){
		db = dbHelper.getWritableDatabase();
		String sql = " UPDATE "+DbConstant.TABLE_NAME_TRIPPOINTS+" SET "
				+DbConstant.TRIPPOINT_FIEID_WAY+" =? ,"
				+" WHERE " + DbConstant.TRIPPOINT_FIEID_ID + " =? ";
		db.execSQL(sql,new Object[]{pid,way});
	}

	//-----------------------删除---------------------
	/**
	 * 通过pid移除mark点和信息
	 * @param tripPointPid 点在数据库中的具体位置
	 */
	public void deleteStopPoint(int tripPointPid){
		
		db=dbHelper.getWritableDatabase();
		String sql = "delete from "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" where "
				+DbConstant.TRIPPOINT_FIEID_ID
				+" = ?";
		db.execSQL(sql,new Object[]{ tripPointPid});
		db.close();
	}

	//-----------------------查询---------------------
	/**
	 * 查询uID用户当天的记录的数据
	 * @param uID
	 * @return
	 */
	public List<TripPointInfo> selectTodayTripoint(String uID){
		int fromTime = DateConvertUtil.DateTodayInt();
		return selectTripPoint(fromTime, fromTime+CommonConstant.ONE_DAY_INT, uID);
	}
	/**
	 * 从数据库获取特定时段的某个用户的trip点
	 * @param fromTime	起始时间
	 * @param toTime	到达时间
	 * @param uID	用户id
	 * @return
	 */
	public List<TripPointInfo> selectTripPoint(int fromTime,int toTime,String uID){

		ArrayList<TripPointInfo> tripPointList = new ArrayList<TripPointInfo>();

		db = dbHelper.getReadableDatabase();
		String sql = "select * from "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" where "
				+DbConstant.TRIPPOINT_FIEID_TIME
				+" > ? and "
				+DbConstant.TRIPPOINT_FIEID_TIME+" < ? and "
				+DbConstant.TRIPPOINT_FIEID_USERID +" = ? ";
		Cursor cursor = db.rawQuery(sql, new String[]{fromTime+"",toTime+"",uID});

		while(cursor.moveToNext()){
			TripPointInfo pointInfo = new TripPointInfo();
			double latitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LATITUDE));
			double longitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LONGITUDE));
			pointInfo.setAddress(new LatLng(latitude, longitude));
			pointInfo.setStamp(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TIME)));
			DrawStyle style = cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TYPE))==1?DrawStyle.MARK:DrawStyle.LINE;
			if(style == DrawStyle.MARK){
				pointInfo.setContent(cursor.getString(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_CONTENT)));
				pointInfo.setReason(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_REASON)));
				pointInfo.setWay(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_WAY)));				
				pointInfo.setPid(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_ID)));
				pointInfo.setStyle(style);
			}
			tripPointList.add(pointInfo);
		}
		cursor.close();
		db.close();
		return tripPointList;
	}

	/**
	 * 返回 user用户的最后一个点
	 * @param userID
	 * @return
	 */
	public TripPointInfo selectLastTripPoint(String userID){
		TripPointInfo pointInfo = null ;
		db = dbHelper.getReadableDatabase();
		String sql = "select * from "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" where "
				+DbConstant.TRIPPOINT_FIEID_USERID +" = ? "
				+"order by "
				+DbConstant.TRIPPOINT_FIEID_TIME +" desc limit 1";
		Cursor cursor = db.rawQuery(sql, new String[]{userID});
		
		if(cursor.moveToFirst()){
			pointInfo = new TripPointInfo();
			double latitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LATITUDE));
			double longitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LONGITUDE));
			pointInfo.setAddress(new LatLng(latitude, longitude));
			pointInfo.setStamp(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TIME)));
			DrawStyle style = cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TYPE))==1?DrawStyle.MARK:DrawStyle.LINE;
			pointInfo.setStyle(style);
		}
		cursor.close();
		db.close();
		return pointInfo;
	}
	/**
	 * 返回user用户的最后一个stop点
	 * @param userID
	 * @return
	 */
	public TripPointInfo selectLastStopPoint(String userID){
		TripPointInfo lastTripPointInfo = null ;
		db = dbHelper.getReadableDatabase();
		String sql = "select * from "
				+DbConstant.TABLE_NAME_TRIPPOINTS
				+" where "
				+DbConstant.TRIPPOINT_FIEID_USERID +" = ? "
				+" and "
				+DbConstant.TRIPPOINT_FIEID_TYPE + " =1 "
				+"order by "
				+DbConstant.TRIPPOINT_FIEID_TIME +" desc limit 1";
		Cursor cursor = db.rawQuery(sql, new String[]{userID});
		
		if(cursor.moveToFirst()){
			TripPointInfo pointInfo = new TripPointInfo();
			double latitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LATITUDE));
			double longitude = cursor.getDouble(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_LONGITUDE));
			pointInfo.setAddress(new LatLng(latitude, longitude));
			pointInfo.setStamp(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TIME)));
			DrawStyle style = cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_TYPE))==1?DrawStyle.MARK:DrawStyle.LINE;
			if(style == DrawStyle.MARK){
				pointInfo.setContent(cursor.getString(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_CONTENT)));
				pointInfo.setReason(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_REASON)));
				pointInfo.setWay(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_WAY)));				
				pointInfo.setPid(cursor.getInt(cursor.getColumnIndex(DbConstant.TRIPPOINT_FIEID_ID)));
				pointInfo.setStyle(style);
			}
			lastTripPointInfo = pointInfo;
		}
		cursor.close();
		db.close();
		return lastTripPointInfo;
	}
}
