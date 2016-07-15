package blq.ssnb.trive.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 用来获取当前网络连接状态
 * @author SSNB
 *判断网络是否可用，wifi是否可用，数据流量是否可用
 *
 */
public class NetUtil {

	static String TAG = NetUtil.class.getSimpleName();
	/**
	 * 判断是否有网络连接
	 * @param context
	 * @return true 表示网络已经连接可用
	 */
	public static boolean isNetworkConnected(Context context){
		if(context!=null){
			ConnectivityManager mcConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mcConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo!=null){
				return mNetworkInfo.isAvailable();
			}
		}
		return false;		
	}
		
	/**
	 * 判断是否wifi连接
	 * @param context
	 * @return true 表示网络已经连接可用
	 */
	public static boolean isWifiConnected(Context context){
		NetworkInfo mNetworkInfo = getNetWorkInfo(context, ConnectivityManager.TYPE_WIFI);
		if(mNetworkInfo !=null){
			return mNetworkInfo.isAvailable();
		}
		return false;		
	}
	
	/**
	 * 判断是否数据流量连接
	 * @param context
	 * @return true 表示网络已经连接可用
	 */
	public static boolean isMobileConnected(Context context){
		NetworkInfo mNetworkInfo =getNetWorkInfo(context, ConnectivityManager.TYPE_MOBILE);
		if(mNetworkInfo!=null){
			return mNetworkInfo.isAvailable();
		}
		return false;		
	}
	
	/**
	 * 通过 上下午对象和获取的连接类型来返回连接对象
	 * @param context 上下文对象
	 * @param networkType 连接类型 ConnectivityManager.TYPE_MOBILE/TYPE_WIFI等
	 * @return NetworkInfo 对象
	 * 如果 context 为 null 返回null，否则返回 NetworkInfo对象（可能为null）;
	 */
	private static NetworkInfo getNetWorkInfo(Context context, int networkType){
		if(context == null){
			return null;
		}
		ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return mConnectivityManager.getNetworkInfo(networkType);
	}
}
