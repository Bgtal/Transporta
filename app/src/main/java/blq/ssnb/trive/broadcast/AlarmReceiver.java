package blq.ssnb.trive.broadcast;

import blq.ssnb.trive.constant.AlarmConstant.AlarmObject;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.AlarmUtil;
import blq.ssnb.trive.util.NotificationUtil;
import blq.ssnb.trive.util.NotificationUtil.NotificationType;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String TAG = AlarmReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {

		//启动服务的广播
		if(intent.getAction().equals(AlarmObject.STARTSERVICE.getAction())){//（一般）只执行一次
			Intent inte = new Intent(context,RecordingService.class);
			inte.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			context.startService(inte);//启动服务
			AlarmUtil.setUpdat(context);
		}
		if(intent.getAction().equals(AlarmObject.UPDATE.getAction())){//八点的时候
			//pz.save(Common.UPDATE_OK, false);//update_ok变false
			NotificationUtil.Update(context);
			AlarmUtil.setUpConfig(context);
		}
		//24点时候更新
		if(intent.getAction().equals(AlarmObject.UPDATECONFIG.getAction())){
			//pz.save(Common.UPDATE_OK, true);//update_ok变false
			NotificationUtil.cancle(context,NotificationType.Update);

		}
	}

}
