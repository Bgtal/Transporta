package blq.ssnb.trive.util;

import android.location.Location;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.service.RecordManager;

public class RecordUtil {
	/**
	 * 最大的精度范围
	 */
	public static final float MAX_ACCURACY = 50;
	/**
	 * Stop点的最大的活动范围（在范围内不会被记录）
	 */
	public static final float MAX_ACTIVITIES_RANGE = 50;
	/**
	 * 最小的记录间距（两点小于这个间距就不会记录） 
	 */
	public static final float MIN_RECORD_DISTANCE = 10;

	/**
	 * 运动的最小判定速度(m/s);
	 */
	public static final double MAX_NO_MOVEMENT_SPEED = 0.224;
	/**
	 * 记录的起始时间
	 */
	public static final long RECORD_START_TIME = 6*CommonConstant.ONE_HOUR_LONG;
	/**
	 * 记录的结束时间
	 */
	public static final long RECORD_END_TIME = 24*CommonConstant.ONE_HOUR_LONG;


	/**
	 *  判断当前时间是否在记录的时间范围内（早上6点到下午20点）
	 * @return true 表示在记录的时间范围内
	 */
	public static boolean needRecord(){

		if((DateConvertUtil.DateTodayLong()+RECORD_START_TIME)<System.currentTimeMillis()
				&&System.currentTimeMillis()<(DateConvertUtil.DateTodayLong()+RECORD_END_TIME)){
			return true;
		}else{
			return false;
		}
	}

	public static void RecordStart(RecordManager manager, Location location){

/*		if(!isCanRecord(manager, location)){
			WriteFile.writeByNameAndContent("流程记录","当前点偏移太厉害了");
			return;
		}*/
		
		//如果精度大于50说明这个点是不可行的点，那么判定是否需要更新lastStop的时间
		if(location.getAccuracy()>RecordUtil.MAX_ACCURACY){
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点精度("+location.getAccuracy()+"米)大于"+RecordUtil.MAX_ACCURACY+"米");
			if(manager.getLastPointLocation()!=null&&manager.getTemporaryStopLocation()==null
					&&location.distanceTo(manager.getLastStopLocation())<MAX_ACTIVITIES_RANGE
					&&(location.getTime()-manager.getLastStopLocation().getTime())<10*CommonConstant.ONE_MINUTE_LONG){
				updateLastStopTime(manager, location.getTime());
			}else{
//				WriteFileUtil.writeByNameAndContent("流程记录","当前点不符合任何要求，结束");
			}
			return ;
		}
//		WriteFileUtil.writeByNameAndContent("流程记录","当前点精度("+location.getAccuracy()+"米)小于"+RecordUtil.MAX_ACCURACY+"米");

		//判断最后一个点是否有记录
		if(manager.getLastPointLocation()==null){
			//如果没有那么最新获得的点就是lastLocation 并且直接返回
//			WriteFileUtil.writeByNameAndContent("流程记录","最后一个记录点没有记录，赋值当前点为最后记录点,结束");
			manager.setLastPointLocation(location) ;
			return;
		}
//		WriteFileUtil.writeByNameAndContent("流程记录","最后一个点有记录");

		StateJudgment(manager,location);
	}

	private static void StateJudgment(RecordManager manager ,Location location){
		//如果这个点有速度，那么说明他在运动
		if(location.getSpeed()>MAX_NO_MOVEMENT_SPEED){
			moveJudgment(manager,location);
		}else{
			stopJudgment(manager,location);
		}	
	}

	private static void moveJudgment(RecordManager manager,Location location){
//		WriteFileUtil.writeByNameAndContent("流程记录","当前有速度："+location.getSpeed()+"米/秒，判定为运动点");
		//如果当前点与最后记录点的距离大于10那么就进行判断是否需要记录这个新的点
		if(location.distanceTo(manager.getLastPointLocation())>MIN_RECORD_DISTANCE){
			exceedRecordDistance(manager,location);
		}else if(location.distanceTo(manager.getLastStopLocation())<MAX_ACTIVITIES_RANGE
				&&(location.getTime()-manager.getLastStopLocation().getTime())<10*CommonConstant.ONE_MINUTE_LONG){
			//如果最后点这个点距离小于10 那么比较这个点和stop点的距离是否在活动范围内
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点与最后记录点的距离小于"+MIN_RECORD_DISTANCE
//					+"\n 当前点与最后停止点距离小于"+MAX_ACTIVITIES_RANGE
//					+"\n 当前点与最后停止点的时间小于10分钟");
			updateLastStopTime(manager,location.getTime());
		}

	}
	private static void updateLastStopTime(RecordManager manager,long time){
//		WriteFileUtil.writeByNameAndContent("流程记录","当前点在活动范围内，更新lastStop的时间，结束");
		manager.setLastStopLocationTime(time);
	}

	private static void exceedRecordDistance(RecordManager manager,Location location){
//		WriteFileUtil.writeByNameAndContent("流程记录","当前点与最后记录点的距离大于"+MIN_RECORD_DISTANCE);
		//如果这个点与最后stop点的距离大于50 那么是一个可记录的点,如果小于50就要判断是否是从新进入的点
		if(location.distanceTo(manager.getLastStopLocation())>MAX_ACTIVITIES_RANGE){
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点与最后停止点的距离大于"+MAX_ACTIVITIES_RANGE+",表明当前点在最后活动范围外");
			exceedActivities(manager, location);
		}else if(location.getTime()-manager.getLastStopLocation().getTime()<10*CommonConstant.ONE_MINUTE_LONG){
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点在最后活动范围内，且时间小于10分钟");
			updateLastStopTime(manager, location.getTime());
		}else{
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点在最后停止点内，但时间超过10分钟");
			exceedActivities(manager, location);
		}
	}

	private static void exceedActivities(RecordManager manager,Location location){
		//如果临时停止点为空的话就记录这个点写入数据库
		if(manager.getTemporaryStopLocation()==null){
//			WriteFileUtil.writeByNameAndContent("流程记录","临时停止点为空，记录该点，结束");
			manager.setLastPointLocation(location);
			manager.WriteToDB(location, DrawStyle.LINE);
			manager.callBackActivity(location, DrawStyle.LINE);
			writeToSxt(location);
			return;
		}else if(manager.getTemporaryStopLocation().distanceTo(location)>MAX_ACTIVITIES_RANGE){
			//如果临时停止点不为空，那么计算临时停靠点与这个点之间的距离里是否大于50米
//			WriteFileUtil.writeByNameAndContent("流程记录","临时停止点与该点距离大于"+MAX_ACTIVITIES_RANGE+",临时记录点作废，结束");
			manager.setTemporaryStopLocation(null);
			manager.setLastPointLocation(location);
			manager.WriteToDB(location, DrawStyle.LINE);
			manager.callBackActivity(location, DrawStyle.LINE);
			writeToSxt(location);
			return;
		}else{

//			WriteFileUtil.writeByNameAndContent("流程记录", "运动-在临时停止点内活动，结束");
		}
	}

	private static void stopJudgment(RecordManager manager,Location location){
//		WriteFileUtil.writeByNameAndContent("流程记录","当前有速度："+location.getSpeed()+"米/秒，判定为停止点");
		//如果在最后停止点的范围外地话
		if(location.distanceTo(manager.getLastStopLocation())>MAX_ACTIVITIES_RANGE){
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点与最后停止点的距离大于"+MAX_ACTIVITIES_RANGE+",在活动范围外地的停止");
			recordStopLocation(manager,location);
		}else if((location.getTime()-manager.getLastStopLocation().getTime())>10*CommonConstant.ONE_MINUTE_LONG){
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点重新进入最后点的范围内");
			//如果在最后 的停止点的范围内的话，但是时间超过10分钟了，说明这个点重新进入了。
			recordStopLocation(manager,location);
		}else{
//			WriteFileUtil.writeByNameAndContent("流程记录","当前点在最后stop的活动范围内");
			updateLastStopTime(manager, location.getTime());
		}
	}

	private static void recordStopLocation(RecordManager manager,Location location){
		//如果没有临时停止点
		if(manager.getTemporaryStopLocation()==null){
//			WriteFileUtil.writeByNameAndContent("流程记录","临时停止点为空,记录临时停止点，结束");
			manager.setTemporaryStopLocation(location);
			manager.setLastPointLocation(location);
			return;
		}
		//如果当前点与临时停止点的时间超过5分钟，那么说明他就是一个停止点
		if(Math.abs((location.getTime()-manager.getTemporaryStopLocation().getTime()))>5*CommonConstant.ONE_MINUTE_LONG){
			//if(Math.abs((location.getTime()-manager.getTemporaryStopLocation().getTime()))>5*CommonConstant.ONE_SECOND_LONG){
//			WriteFileUtil.writeByNameAndContent("流程记录","记录新的停止点，结束");
			manager.setLastStopLocation(location);
			manager.setLastPointLocation(location);
			manager.setTemporaryStopLocation(null);
			manager.WriteToDB(location, DrawStyle.MARK);
			manager.callBackActivity(location, DrawStyle.MARK);
			writeToSxt(location);
			return;
		}else if(manager.getTemporaryStopLocation().distanceTo(location)<50){
//			WriteFileUtil.writeByNameAndContent("流程记录","停止-在临时停止点内活动，结束");
		}else{
			manager.setTemporaryStopLocation(null);
			manager.setLastPointLocation(location);
			manager.WriteToDB(location, DrawStyle.LINE);
			manager.callBackActivity(location, DrawStyle.LINE);
			writeToSxt(location);
//			WriteFileUtil.writeByNameAndContent("流程记录", "停止-但是超出临时停止点，所以认为是新的可靠点");
		}
	}

	public static void writeToNTxt(Location location){
		//未筛选点的记录
//		WriteFileUtil.writeByNameAndContent("未筛选记录", MapUtil.getLocation(location));
	}
	public static void writeToSxt(Location location){
		//筛选过的点的记录
//		WriteFileUtil.writeByNameAndContent("筛选记录", MapUtil.getLocation(location));
	}
}
