package blq.ssnb.trive.constant;

/**
 * 用于存储xml文件的标签名 常量
 * @author ssnb
 *
 */
public class PlistConstant {
	/**
	 * 存储登录用户的信息
	 */
	public static final String FILE_NAME_AUTO_LOGIN = "auto_login_info";
	public static final String AUTO_LOGIN_ISAUTO = "isauto";
	public static final String AUTO_LOGIN_EMAIL = "user_email";
	public static final String AUTO_LOGIN_TIME="login_time";

	/**
	 * 用于存储最后一个stop点
	 */
	public static final String FILE_NAME_LAST_STOP_POINT="tripinfo";

	/**
	 * 精度:float
	 */
	public static final String POINT_ACCURACY="Accuracy";
/*	*//**
	 * 海拔:double
	 *//*
	public static final String POINT_ALTITUDE="Altitude";*/
	/**
	 * 方向:float
	 */
	public static final String POINT_BEARING="Bearing";
	/**
	 * 纬度:double
	 */
	public static final String POINT_LATITUDE="Latitude";
	/**
	 * 经度:double
	 */
	public static final String POINT_LONGITUDE="Longitude";
	/**
	 * 供应商:String
	 */
	public static final String POINT_PROVIDER="Provider";
	/**
	 * 速度：float
	 */
	public static final String POINT_SPEED="Speed";
	/**
	 * 时间戳：long
	 */
	public static final String POINT_TIME="Time";

	/**
	 * 用于存储最后一个记录点的数据
	 */
	public static final String FILE_NAME_LAST_POINT="lastpoint";
	/**
	 * 用于储存临时记录点
	 */

	public static final String FILE_NAME_TEMPORARY_POINT = "Temporary";
	public static final String TEMPORARY_ISNull ="isnull";

	/**
	 * 用于记录过程中的配置文件
	 */
//	public static final String FILE_NAME_RECORD_CONFIGURE="recordConfigure";


	/**
	 * 用于app的配置文件
	 */
	public static final String FILE_NAME_APP_CONFIGURE="appConfigure";

}
