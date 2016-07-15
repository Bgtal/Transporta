package blq.ssnb.trive.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
/**
 * 服务类工具，用于判断服务是否开启等
 * @author xucj
 *
 */
public class ServiceUtil {
	private static final String TAG = ServiceUtil.class.getSimpleName();
	/**
	 * 判断服务是否在运行
	 * @param mContext 上下文对象
	 * @param serviceName 需要判断的服务的名字
	 * @return boolean true 在运行，false 不在运行
	 * 
	 */
	public static boolean isServiceRunning(Context mContext,String serviceName){
		
		ActivityManager activityManager = (ActivityManager)mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = 
				activityManager.getRunningServices(100);
		if(!(serviceList.size()>0)){
			MLog.e(TAG, "没有服务");
			return false;
		}
		for(int i = 0 ; i < serviceList.size() ; i++){
			//Log.e(TAG, serviceList.get(i).service.getClassName());
			if(serviceList.get(i).service.getClassName().equals(serviceName)){
				MLog.e(TAG, "服务已启动");
				return true;
			}
		}
		MLog.e(TAG, "服务没启动");
		return false;
	}
}
