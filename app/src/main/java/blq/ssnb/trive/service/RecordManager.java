package blq.ssnb.trive.service;

import com.google.android.gms.maps.model.LatLng;

import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.MapUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

public class RecordManager {
	
	private RecordCallBackHandler call = null ;
	private static RecordManager recordManager;
	private Location lastStopLocation;
	private Location lastPointLocation;
	private TripPointDB tripDB;
	private Location temporaryStopLocation;
	
	private RecordManager(){}
	/**
	 * 懒加载，获取单例
	 * @return
	 */
	public synchronized static RecordManager getInstance(){
		if(recordManager == null){
			recordManager = new RecordManager();
		}
		return recordManager;
	}
	/**
	 * 获取最后一个停止点的对象
	 * @return
	 */
	public Location getLastStopLocation() {
		if(lastStopLocation ==null){
			lastStopLocation = MapUtil.getLastStopLocation(MyApplication.getInstance().getApplicationContext());
		}
		return lastStopLocation;
	}
	/**
	 * 存储最后一个点的对象
	 * @param lastStopLocation
	 */
	public void setLastStopLocation(Location lastStopLocation) {
		MapUtil.setLastStopLocation(MyApplication.getInstance().getApplicationContext(), lastStopLocation);
		this.lastStopLocation = lastStopLocation;
	}
	/**
	 * 更新最后停止点的时间属性
	 * @param time
	 */
	public void setLastStopLocationTime(Long time){
		MapUtil.setLastStopLocationTime(MyApplication.getInstance().getApplicationContext(), time);
		this.lastStopLocation.setTime(time);
	}

	/**
	 * 获取最后记录的一个point点
	 * @return
	 */
	public Location getLastPointLocation() {
		if(lastPointLocation==null){
			lastPointLocation = MapUtil.getLastPointLocation(MyApplication.getInstance().getApplicationContext());
		}
		return lastPointLocation;
	}
	/**
	 * 设置最后记录的一个point点
	 * @param lastPointLocation
	 */
	public void setLastPointLocation(Location lastPointLocation) {
		MapUtil.setLastPointLocation(MyApplication.getInstance().getApplicationContext(), lastPointLocation);
		this.lastPointLocation = lastPointLocation;
	}
	/**
	 * 数据库的懒加载
	 * @return
	 */
	private TripPointDB getTripDB() {		
		if(tripDB==null){
			tripDB = new TripPointDB(MyApplication.getInstance().getApplicationContext());
		}
		return tripDB;
	}

	public void WriteToDB(Location location,DrawStyle style){
		TripPointInfo point = new TripPointInfo();
		point.setAddress(new LatLng(location.getLatitude(), location.getLongitude()));
		point.setStamp(DateConvertUtil.TimeStamp(location.getTime()));
		point.setUserID(MyApplication.getInstance().getUserInfo().getEmail());
		point.setStyle(style);
		getTripDB().addTripPoint(point);
	}
	
	/**
	 * 获取临时停止点的对象
	 * @return
	 */
	public Location getTemporaryStopLocation() {
		if(temporaryStopLocation == null && MapUtil.isTemporaryStopNotNull(MyApplication.getInstance().getApplicationContext())){
			
			temporaryStopLocation = MapUtil.getTemporaryStopLocation(MyApplication.getInstance().getApplicationContext());
		}
		return temporaryStopLocation;
	}
	/**
	 * 设置临时停止点的对象
	 * @param temporaryStopLocation
	 */
	public void setTemporaryStopLocation(Location temporaryStopLocation) {
		if(temporaryStopLocation ==null){
			MapUtil.setTemporaryStopStatus(MyApplication.getInstance().getApplicationContext(), false);
		}else{
			MapUtil.setTemporaryStopStatus(MyApplication.getInstance().getApplicationContext(), true);
		}
		this.temporaryStopLocation = temporaryStopLocation;
	}

	public void setActivityRequestCallBack(RecordCallBackHandler callHandler) {
		this.call = callHandler;
	}
	
	public void  callBackActivity(Location location,DrawStyle style){
		if(call!=null){
			Message msg = new Message();
			Bundle bundle = new Bundle();
			switch (style) {
			case LINE:
				bundle.putString("style", "line");
				break;
			case MARK:
				bundle.putString("style", "mark");
				break;
			default:
				break;
			}
			bundle.putParcelable("location", (Parcelable)location);
			msg.setData(bundle);
			call.sendMessage(msg);
		}
	}
}
