package blq.ssnb.trive.constant;

public class HttpConstant {

	/**
	 * 服务器地址
	 */
	//	public static final String HOST = "http://travle.sinaapp.com/Home";	

	public static final String HOST = "http://travle.applinzi.com/Home";

	//public static final String HOST="http://10.23.26.159:85/3/Home";
	//public static final String HOST="http://192.168.1.101:85/3/Home";

	public static final int CONNECTION_OUT_TIME = 0;//超时
	public static final int BIND_FAIL_NOHHID=-1;//没有hhid
	public static final int BIND_SUCCESS=1;//绑定成功

	public static final int CREATE_RETURN_FALSE=-1;//注册失败	

	public static final int LOGIN_RETURN_TRUE = 1;//成功并且有hhid
	public static final int LOGIN_RETURN_FALSE = -1;//成功但是没有hhid
	public static final int LOGIN_RETURN_NOID = 2;//没有ID

	public static final int REGIST_RETRUN_FALSE=-1;//账号存在
	public static final int REGIST_RETRUN_TRUE=1;//注册成功

	public static final int UPDATE_SUCCESS = 1;//跟新成功

	public static final String LOGIN = HOST+"/Login/login";
	public static final String REGISTER = HOST+"/Regiest/register";
	public static final String UPDATE = HOST+"/UpStopsAndTrips/update";

}
