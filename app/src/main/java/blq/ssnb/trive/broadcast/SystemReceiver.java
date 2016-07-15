package blq.ssnb.trive.broadcast;

import blq.ssnb.trive.R;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.AlarmUtil;
import blq.ssnb.trive.util.NetUtil;
import blq.ssnb.trive.util.TUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class SystemReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * 检测开机服务
		 */
		if(intent.getAction().equals(CommonConstant.BOOT_COMPETED)){
			
			//开机的时候启动服务
			Intent inte = new Intent(context,RecordingService.class);
			inte.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			context.startService(inte);//启动服务
			//启动
			AlarmUtil.setUpConfig(context);
			
			AlarmUtil.setUpdat(context);
		}
		
		/**
		 * 接受网络改变的广播，执行相应操作
		 */
		if (intent.getAction().equals(CommonConstant.CONNECTIVITY_ACTION)) {
			boolean isNetConnected = NetUtil.isNetworkConnected(context);
			if(!isNetConnected){//网络未连接
				TUtil.TLong(R.string.network_connection_fail);
			}else{
				TUtil.TLong(R.string.network_connection_success);
			}
		}
		
	}

	
}
