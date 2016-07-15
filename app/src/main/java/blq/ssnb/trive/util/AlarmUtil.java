package blq.ssnb.trive.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import blq.ssnb.trive.constant.AlarmConstant.AlarmObject;
import blq.ssnb.trive.constant.CommonConstant;

/**
 * 用于控制闹钟的方法
 * @author xucj
 *
 */
public class AlarmUtil {
	
	/**
	 * 设置更新系统配置的闹钟
	 * @param context
	 */
	public static void setUpConfig(Context context){
		
		AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		calendar.set(Calendar.MILLISECOND, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
				calendar.getTimeInMillis(), 
				CommonConstant.ONE_DAY_LONG, 
				getPendingIntent(context, AlarmObject.UPDATECONFIG));
		
	}
	
	
	/**
	 * 启动每天20:10更新通知闹钟
	 * @param context
	 */
	public static void setUpdat(Context context){
		
		AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		calendar.set(Calendar.MILLISECOND, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
				calendar.getTimeInMillis(), 
				CommonConstant.ONE_DAY_LONG, 
				getPendingIntent(context, AlarmObject.UPDATE));
		
	}
	/**
	 * 用于启动RecordingService
	 * @param context
	 */
	public static void setStartService(Context context){
		
		AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		am.set(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				getPendingIntent(context, AlarmObject.STARTSERVICE));
	}
	
	/**
	 * 通过AlarmObject 对象来取消闹钟
	 * @param context 上下午对象
	 * @param obj 需要取消的对象
	 */
	public static void CancelAlarm(Context context,AlarmObject obj){
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(getPendingIntent(context, obj));
	}
	
	/**
	 * 
	 * @param context
	 * @param obj
	 * @return
	 */
	private static PendingIntent getPendingIntent(Context context , AlarmObject obj){
		Intent intent = new Intent(obj.getAction());
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		PendingIntent sendIntent = PendingIntent.getBroadcast(context,
				obj.getRequestCode(), 
				intent, 
				PendingIntent.FLAG_CANCEL_CURRENT);
		return sendIntent;
	}
}
