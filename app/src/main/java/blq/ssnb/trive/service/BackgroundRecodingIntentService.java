package blq.ssnb.trive.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.PlistConstant;
import blq.ssnb.trive.util.RecordUtil;

import com.google.android.gms.location.LocationResult;
/**
 * 
 * @author xucj
 * 用于在后台记录位置的信息
 */
public class BackgroundRecodingIntentService extends IntentService{
	//private static final String TAG = BackgroundRecodingIntentService.class.getSimpleName();

	public BackgroundRecodingIntentService() {
		super(BackgroundRecodingIntentService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		//如果在记录的时间范围内，那么就应该被允许记录，否则就抛弃掉
		if(RecordUtil.needRecord()){
			if(MyApplication.getInstance().getUserInfo()==null){
					return;
			}
//			WriteFileUtil.writeByNameAndContent("流程记录","IntentService");
			RecordManager manager = RecordManager.getInstance();
			//如果没有地址就返回
			if(!LocationResult.hasResult(intent)){
//				WriteFileUtil.writeByNameAndContent("流程记录","没有地址返回，结束");
				return;
			}
//			WriteFileUtil.writeByNameAndContent("流程记录","有地址返回");

			//当有地址返回的时候
			LocationResult result = LocationResult.extractResult(intent);
			Location location =result.getLastLocation();
			RecordUtil.writeToNTxt(location);
			
			RecordUtil.RecordStart(manager, location);
		}
	}
	

}
