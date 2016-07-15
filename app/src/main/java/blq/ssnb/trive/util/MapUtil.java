package blq.ssnb.trive.util;

import android.content.Context;
import android.location.Location;
import blq.ssnb.trive.constant.PlistConstant;

import com.google.android.gms.maps.model.LatLng;

/**
 * 地图工具，用来对地图上一些计算的操作
 * @author xucj
 *
 */
public class MapUtil {
	private static final String TAG = MapUtil.class.getName();
	/**
	 * 将LatLng "lat/lng(xxxxxxx,yyyyyy)" 转换为 "xxxxxx,yyyyy" 样式
	 * @param latlng
	 * @return
	 */
	public static String LatLng2String(LatLng latlng){
		String latLngStr = latlng.toString();
		String newStr = latLngStr.substring(latLngStr.indexOf("(")+1, latLngStr.lastIndexOf(")")); 
		MLog.e(TAG, newStr);
		return newStr;
		
	}
	
	/**
	 * 将"xxxxxx,yyyyyyy"的字符串转换为 LatLng对象
	 * @param latLngString  格式为  xxxxxx,yyyyyyy 的字符串
	 * @return 返回地理位置坐标 LatLng
	 */
	public static LatLng String2LatLng(String latLngString){
		String[] latAndLng = latLngString.split(",");
		LatLng latlng = new LatLng(Double.parseDouble(latAndLng[0]), Double.parseDouble(latAndLng[1]));
		MLog.e(TAG, latlng.toString());
		return latlng;
	}
	
	/**
	 * 传出Location 对象返回格式化后的对象
	 * @param location
	 * @return
	 */
	public static String getLocation(Location location){
/*		Location baseLocation = new Location("ssnb");
		baseLocation.setLatitude(23);
		baseLocation.setLongitude(122);

		location.hasAccuracy();//true 如果有精度
		location.hasAltitude();//true 如果有高度
		location.hasBearing();//true 如果有方向
		location.hasSpeed();//true 如果有速度

		location.bearingTo(baseLocation);//获取当前点和目标点的角度
		location.distanceTo(baseLocation);//获取当前点和目标点的距离

		location.getAccuracy();//获得精度
		location.getAltitude();//获得高度
		location.getBearing();//获得方位
		location.getLatitude();//返回纬度
		location.getLongitude();//返回经度
		location.getProvider();//返回供应商的名称
		location.getSpeed();//返回速度
		location.getTime();//返回时间戳 毫秒
*/
		StringBuffer bf = new StringBuffer(100);
		bf.append("\n");
		bf.append("精度:"+location.getAccuracy()).append("\n");
//		bf.append("海拔:"+location.getAltitude()).append("\n");
//		bf.append("方向:"+location.getBearing()).append("\n");
		bf.append("纬度:"+location.getLatitude()).append("\n");
		bf.append("经度:"+location.getLongitude()).append("\n");
//		bf.append("供应商:"+location.getProvider()).append("\n");
		bf.append("速度:"+location.getSpeed()).append("\n");
		bf.append("时间:"+DateConvertUtil.yyyy_MM_dd_HH_mm_ss(location.getTime()));

		return bf.toString();
	}
	
	
	public static Location getLastStopLocation(Context context){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_LAST_STOP_POINT);
		String provider = pf.readString(PlistConstant.POINT_PROVIDER);
		Location location =new Location(provider);
		location.setAccuracy(pf.readFloat(PlistConstant.POINT_ACCURACY));
		location.setBearing(pf.readFloat(PlistConstant.POINT_BEARING));
		location.setLatitude(pf.readFloat(PlistConstant.POINT_LATITUDE,1000f));
		location.setLongitude(pf.readFloat(PlistConstant.POINT_LONGITUDE,1000f));
		location.setSpeed(pf.readFloat(PlistConstant.POINT_SPEED));
		location.setTime(pf.readLong(PlistConstant.POINT_TIME));
		return location;
	}
	
	public static void setLastStopLocation(Context context,Location location){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_LAST_STOP_POINT);
		pf.saveFloat(PlistConstant.POINT_ACCURACY, location.getAccuracy());
		pf.saveFloat(PlistConstant.POINT_BEARING, location.getBearing());
		pf.saveFloat(PlistConstant.POINT_LATITUDE, (float)location.getLatitude());
		pf.saveFloat(PlistConstant.POINT_LONGITUDE,(float)location.getLongitude());
		pf.saveString(PlistConstant.POINT_PROVIDER, location.getProvider());
		pf.saveFloat(PlistConstant.POINT_SPEED, location.getSpeed());
		pf.saveLong(PlistConstant.POINT_TIME, location.getTime());
	}
	
	public static void setLastStopLocationTime(Context context,long time){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_LAST_STOP_POINT);
		pf.saveLong(PlistConstant.POINT_TIME, time);
	}
	
	public static Location getLastPointLocation(Context context){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_LAST_POINT);
		String provider = pf.readString(PlistConstant.POINT_PROVIDER);
		Location location =new Location(provider);
		location.setAccuracy(pf.readFloat(PlistConstant.POINT_ACCURACY));
		location.setBearing(pf.readFloat(PlistConstant.POINT_BEARING));
		location.setLatitude(pf.readFloat(PlistConstant.POINT_LATITUDE,1000f));
		location.setLongitude(pf.readFloat(PlistConstant.POINT_LONGITUDE,1000f));
		location.setSpeed(pf.readFloat(PlistConstant.POINT_SPEED));
		location.setTime(pf.readLong(PlistConstant.POINT_TIME));
		return location;
	}
	public static void setLastPointLocation(Context context,Location location){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_LAST_POINT);
		pf.saveFloat(PlistConstant.POINT_ACCURACY, location.getAccuracy());
		pf.saveFloat(PlistConstant.POINT_BEARING, location.getBearing());
		pf.saveFloat(PlistConstant.POINT_LATITUDE, (float)location.getLatitude());
		pf.saveFloat(PlistConstant.POINT_LONGITUDE,(float)location.getLongitude());
		pf.saveString(PlistConstant.POINT_PROVIDER, location.getProvider());
		pf.saveFloat(PlistConstant.POINT_SPEED, location.getSpeed());
		pf.saveLong(PlistConstant.POINT_TIME, location.getTime());
	}
	
	
	public static Location getTemporaryStopLocation(Context context){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_TEMPORARY_POINT);
		String provider = pf.readString(PlistConstant.POINT_PROVIDER);
		Location location =new Location(provider);
		location.setAccuracy(pf.readFloat(PlistConstant.POINT_ACCURACY));
		location.setBearing(pf.readFloat(PlistConstant.POINT_BEARING));
		location.setLatitude(pf.readFloat(PlistConstant.POINT_LATITUDE,1000f));
		location.setLongitude(pf.readFloat(PlistConstant.POINT_LONGITUDE,1000f));
		location.setSpeed(pf.readFloat(PlistConstant.POINT_SPEED));
		location.setTime(pf.readLong(PlistConstant.POINT_TIME));
		return location;
	}
	
	public static void setTemporaryStopLocation(Context context,Location location){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_TEMPORARY_POINT);
		pf.saveFloat(PlistConstant.POINT_ACCURACY, location.getAccuracy());
		pf.saveFloat(PlistConstant.POINT_BEARING, location.getBearing());
		pf.saveFloat(PlistConstant.POINT_LATITUDE, (float)location.getLatitude());
		pf.saveFloat(PlistConstant.POINT_LONGITUDE,(float)location.getLongitude());
		pf.saveString(PlistConstant.POINT_PROVIDER, location.getProvider());
		pf.saveFloat(PlistConstant.POINT_SPEED, location.getSpeed());
		pf.saveLong(PlistConstant.POINT_TIME, location.getTime());
	}
	public static boolean isTemporaryStopNotNull(Context context){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_TEMPORARY_POINT);
		return pf.readBoolean(PlistConstant.TEMPORARY_ISNull);
	}
	public static void setTemporaryStopStatus(Context context,boolean isNull){
		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_TEMPORARY_POINT);
		pf.saveBoolean(PlistConstant.TEMPORARY_ISNull, isNull);
	}
}
