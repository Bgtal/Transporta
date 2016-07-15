package blq.ssnb.trive.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import blq.ssnb.trive.R;
import blq.ssnb.trive.activity.MainActivity;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;

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


	public static void Update(Context context){
		long time = System.currentTimeMillis();

		if(time<=(DateConvertUtil.DateTodayLong()+20*CommonConstant.ONE_HOUR_LONG)){
			//如果当前时间小于更新时间久不弹出
			return ;
		}

		/*if(pzInfo.readBoolean(Common.UPDATE_OK)){
		 	//如果显示已经更新过了，那么就不用显示了
			return ;
		}*/

		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = context.getResources().getString(R.string.notification_update);

		Notification myNotify = new Notification();  
		myNotify.icon = icon;  
		myNotify.tickerText = tickerText;  
		myNotify.when = time; 
		myNotify.flags = Notification.FLAG_AUTO_CANCEL;//会被清除
		myNotify.defaults = Notification.DEFAULT_VIBRATE;

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),  
				R.layout.notify_status_bar_latest_event_view); 

		remoteView.setImageViewResource(R.id.icon, icon);
		remoteView.setTextViewText(R.id.title, MyApplication.getInstance().getUserInfo().getEmail());//这里需要注意下换成相应的文字
		remoteView.setTextViewText(R.id.text, tickerText);
		remoteView.setLong(R.id.time, "setTime", time);
		myNotify.contentView = remoteView;

		Intent intent = new Intent(context,MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		myNotify.contentIntent = pending;
		getNotifiactionManager(context).notify(NotificationType.Update.getID(), myNotify);
	}
	public static void cancle(Context context,NotificationType type){
		getNotifiactionManager(context).cancel(type.getID());
	}

	private static NotificationManager getNotifiactionManager(Context context){
		return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
}
