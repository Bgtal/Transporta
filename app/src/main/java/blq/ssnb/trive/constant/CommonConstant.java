package blq.ssnb.trive.constant;

import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 用于存放常用的常量
 * @author ssnb
 *
 */
public class CommonConstant {
	public static final boolean DEBUG = true;
	public static final boolean LOG_SHOW = DEBUG;
	/**
	 * 一秒（1000毫秒）
	 */
	public static final long ONE_SECOND_LONG = 1000L;
	public static final long ONE_MINUTE_LONG = 60*ONE_SECOND_LONG;
	public static final long ONE_HOUR_LONG = 60*ONE_MINUTE_LONG;
	public static final long ONE_DAY_LONG = 24*ONE_HOUR_LONG;

	public static final int ONE_SECOND_INT=1;
	public static final int ONE_MINUTE_INT=60*ONE_SECOND_INT;
	public static final int ONE_HOUR_INT = 60*ONE_MINUTE_INT;
	public static final int ONE_DAY_INT = 24*ONE_HOUR_INT;


	public static final String BOOT_COMPETED = Intent.ACTION_BOOT_COMPLETED;//开机启动广播
	public static final String CONNECTIVITY_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//网络连接改变广播

	private static Integer[] AGE;

	public static final Integer[] getAges(){
		if(AGE==null){
			AGE =new Integer[80];
			for(int i = 10;i<90;i++){
				AGE[i-10]=i;
			}
		}
		return AGE;
	}

}
