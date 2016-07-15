package blq.ssnb.trive.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import blq.ssnb.trive.R;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.util.TUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * 用于记录服务在后台的使用
 * @author xucj
 *
 */
public class RecordingService extends Service implements OnConnectionFailedListener, ConnectionCallbacks {

	//private static final String TAG = RecordingService.class.getSimpleName();

	private static GoogleApiClient mGoogleApiClient;

	/**
	 * GoogleApiClient 的懒加载
	 * @return
	 */
	private synchronized GoogleApiClient getmGoogleApiClient() {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this).
					addApi(LocationServices.API).
					addConnectionCallbacks(this).
					addOnConnectionFailedListener(this).
					build();

		}
		return mGoogleApiClient;
	}

	private static final LocationRequest REQUEST = LocationRequest.create().
			setFastestInterval(2 * CommonConstant.ONE_SECOND_LONG).
			setInterval(10 * CommonConstant.ONE_SECOND_LONG).
			setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


	private PendingIntent backRecordPendingIntent;

	private PendingIntent getBackRecordPendingIntent() {
		if (backRecordPendingIntent == null) {
			backRecordPendingIntent = PendingIntent.getService(
					getApplicationContext(),
					120,
					new Intent(getApplicationContext(), BackgroundRecodingIntentService.class),
					PendingIntent.FLAG_CANCEL_CURRENT);
		}
		return backRecordPendingIntent;
	}


	@Override
	public IBinder onBind(Intent intent) {
//		WriteFileUtil.writeByNameAndContent("Service", "绑定onBind");
		checkGoogleApiClientConnected();
		return new MyBind();
	}

	public class MyBind extends Binder {
		public RecordingService getService() {
			return RecordingService.this;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
//		WriteFileUtil.writeByNameAndContent("Service", "解除绑定onUnbind");
		RecordManager.getInstance().setActivityRequestCallBack(null);
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
//		WriteFileUtil.writeByNameAndContent("Service", "启动onCreate");
		super.onCreate();
		getmGoogleApiClient().connect();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		WriteFileUtil.writeByNameAndContent("Service", "启动onStartCommand");
		checkGoogleApiClientConnected();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Clean();
//		WriteFileUtil.writeByNameAndContent("Service", "死亡onDestroy");
		super.onDestroy();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		TUtil.TLong(R.string.google_service_connectioned);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				getmGoogleApiClient(),
				REQUEST,
				getBackRecordPendingIntent());
	}

	@Override
	public void onConnectionSuspended(int cause) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if(!result.isSuccess()){
			TUtil.TLong(R.string.google_service_connection_fail);
		}
	}

	private void checkGoogleApiClientConnected(){
		if(!mGoogleApiClient.isConnected()){//如果没有连接
			if(!mGoogleApiClient.isConnecting()){//如果没有连接就进行连接
				getmGoogleApiClient().connect();
			}
			TUtil.TShort(R.string.google_service_connectioning);
		}
	}
	public void Clean(){
		mGoogleApiClient.disconnect();
		mGoogleApiClient = null;
	};

}

