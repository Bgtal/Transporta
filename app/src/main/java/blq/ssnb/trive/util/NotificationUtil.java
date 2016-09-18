package blq.ssnb.trive.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import blq.ssnb.trive.R;
import blq.ssnb.trive.activity.MainActivity;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.PlistConstant;

public class NotificationUtil {

	public enum NotificationType{
		Update(100);
		private int id ;
		private NotificationType(int id){
			this.id = id;
		}
		public int getID(){
			return id;
		}
	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void Update(Context context){
		if(System.currentTimeMillis()<=(DateConvertUtil.DateTodayLong()+20*CommonConstant.ONE_HOUR_LONG)){
			//如果当前时间小于更新时间久不弹出
			return ;
		}

		PreferenceUtil pf = new PreferenceUtil(context, PlistConstant.FILE_NAME_AUTO_LOGIN);
		if(!pf.readBoolean(PlistConstant.AUTO_LOGIN_ISAUTO)){
			return;
		}
		/*if(pzInfo.readBoolean(Common.UPDATE_OK)){
		 	//如果显示已经更新过了，那么就不用显示了
			return ;
		}*/
		if(MyApplication.getInstance().getUserInfo()==null){
			return;
		}
		CharSequence tickerText = context.getResources().getString(R.string.notification_update);
		Intent intent = new Intent(context,MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher_2)
				.setContentTitle(MyApplication.getInstance().getUserInfo().getEmail())
				.setContentText(tickerText)
				.setTicker(tickerText)
				.setAutoCancel(true)
				.setAutoCancel(true)
				.setOngoing(true)
				.setStyle(new Notification.BigTextStyle().bigText(tickerText))
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(pending)
				.build();
		getNotifiactionManager(context).notify(NotificationType.Update.getID(), notification);

	}
	public static void cancle(Context context,NotificationType type){
		getNotifiactionManager(context).cancel(type.getID());
	}

	private static NotificationManager getNotifiactionManager(Context context){
		return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
}
