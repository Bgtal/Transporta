package blq.ssnb.trive.db.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.util.MLog;
import blq.ssnb.trive.util.MapUtil;

public class AllTripToJson {
	private Context context;
	
	public AllTripToJson(Context context){
		this.context = context;
	}

	public JSONArray getTripoint(){
		JSONArray allTripJson =new JSONArray();
		TripPointDB db = new TripPointDB(MyApplication.getInstance().getApplicationContext());
		List<TripPointInfo> allTripsInfo =db.selectTodayTripoint("1234567890");
		allTripJson = tripJSON(allTripsInfo);
		MLog.e("json", allTripJson.toString());
		return allTripJson;
	}

	/*
	 * [{"STOPTIME":1433840781,
	 * "STOPPOINT":"29.9137297,121.6071669",
	 * "DESTPURP":14,
	 * "STOPCONTENT":"空军建军节",
	 * "POINTS":"29.9137297,121.6071669",
	 * "STOPMODE":10},
	 * {"STOPTIME":1433845477,
	 * "STOPPOINT":"29.9153921,121.6066933",
	 * "DESTPURP":16,"STOPCONTENT":"阿鲁V领",
	 * "POINTS":"29.9137297,121.6071669|29.9147072,121.6061504|29.915341,121.6070129|29.9153921,121.6066933",
	 * "STOPMODE":12}]
	 */

	public JSONArray allTripInfoToJSON(String userID){
		TripPointDB db = new TripPointDB(context);
		List<TripPointInfo> allTripsInfo =db.selectTodayTripoint(userID);
		return tripJSON(allTripsInfo);
	}
	
	public JSONArray allTripInfoToJsonByTime(String userID, int startTime){

		TripPointDB db = new TripPointDB(context);
		List<TripPointInfo> allTripsInfo =db.selectTripPoint(startTime, startTime+CommonConstant.ONE_DAY_INT, userID);
		return tripJSON(allTripsInfo);
	
		
	}
	
	private JSONArray tripJSON(List<TripPointInfo> allTripsInfo){
		JSONArray allTripJson =new JSONArray();
		StringBuffer lineBf = new StringBuffer();
		JSONObject object = new JSONObject();
		for (TripPointInfo tripPointInfo : allTripsInfo) {
			if(tripPointInfo.getStyle()==DrawStyle.MARK){
				try {
					if(lineBf.length()>1){
						lineBf.deleteCharAt(lineBf.length()-1);
					}
					object.put("POINTS", lineBf.toString());
					object.put("STOPTIME", tripPointInfo.getStamp());
					object.put("STOPPOINT", MapUtil.LatLng2String(tripPointInfo.getAddress()));
					object.put("STOPCONTENT", tripPointInfo.getContent());
					object.put("STOPREASON", tripPointInfo.getReason());
					object.put("STOPWAY", tripPointInfo.getWay());
					allTripJson.put(object);
					object = new JSONObject();
					lineBf = new StringBuffer();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			lineBf.append(MapUtil.LatLng2String(tripPointInfo.getAddress()))
			.append("&")
			.append(tripPointInfo.getStamp())
			.append("|");
		}

		MLog.e("json", allTripJson.toString());
		return allTripJson;
	}

}
