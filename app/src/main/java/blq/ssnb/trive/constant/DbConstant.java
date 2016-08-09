package blq.ssnb.trive.constant;

/**
 * 用于存放数据库相关的数据，数据库名、版本、表名等
 * @author ssnb
 *
 */
public class DbConstant {

	public static final String DB_NAME = "user2.db";
	public static final int DB_VERSION = 6;

	public static final String TABLE_NAME_USER="user";

	public static final String USER_FIEID_EMAIL = "email";//用户手机号
	public static final String USER_FIEID_GENDER = "gender";//性别
	public static final String USER_FIEID_RESIDENT = "resident";//是否本地人
	public static final String USER_FIEID_AGE = "age";//年龄

	public static final String USER_FIEID_OTHER = "birth_country";//
	public static final String USER_FIEID_DRIVER_LICENCE = "driver_licence";//
	public static final String USER_FIEID_EMPLOYMENT = "employment";//
	public static final String USER_FIEID_STUDYING = "studying";//
	public static final String USER_FIEID_WORKSPACE = "workspace";//
	public static final String USER_FIEID_OCCUPATION = "occupation";//职业
	public static final String USER_FIEID_INDUSTRY = "industry";//行业
	public static final String USER_FIEID_STUDY_LEVEL = "study_level";//
	public static final String USER_FIEID_OTHER_ACTIVITY = "other_activity";//


	public static final String CREATE_TABLE_USER =
			"CREATE TABLE IF NOT EXISTS "+
					TABLE_NAME_USER+
					" ( "+USER_FIEID_EMAIL+" varchar(256) NOT NULL , "+
					USER_FIEID_GENDER+" tinyint(4) NOT NULL, "+
					USER_FIEID_RESIDENT+" tinyint(4) NOT NULL, "+
					USER_FIEID_AGE+" int(11) NOT NULL, "+
					USER_FIEID_OTHER +" varchar(256) NOT NULL, "+
					USER_FIEID_DRIVER_LICENCE+" int(11) NOT NULL, "+
					USER_FIEID_EMPLOYMENT+" int(11) NOT NULL, "+
					USER_FIEID_STUDYING+" int(11) NOT NULL, "+
					USER_FIEID_WORKSPACE+" int(11) NOT NULL DEFAULT '-1', "+
					USER_FIEID_OCCUPATION+" varchar(256) NULL DEFAULT NULL, "+
					USER_FIEID_INDUSTRY+" int(11) NOT NULL DEFAULT '-1', "+
					USER_FIEID_STUDY_LEVEL+" int(11) NOT NULL DEFAULT '-1', "+
					USER_FIEID_OTHER_ACTIVITY+" int(11) NOT NULL DEFAULT '-1', "+
					" PRIMARY KEY ( "+USER_FIEID_EMAIL+" ));";




	//	public static final String DB_TABLE_NAME_HOUSEHOLDS = "household";
	public static final String TABLE_NAME_TRIPS = "trips";//每天update的信息

	public static final String TRIPS_FIEID_ACCTTDATE = "ACTTDATE";//日期
	public static final String TRIPS_FIEID_USERID = USER_FIEID_EMAIL;//person id 作为外键
	public static final String TRIPS_FIEID_TRIPINFO = "TRIPINFO";//信息
	public static final String TRIPS_FIEID_SYNC = "SYNCHRONIZATION";
	public static final String CREATE_TABLE_TRIPS=
			"CREATE TABLE IF NOT EXISTS "
					+DbConstant.TABLE_NAME_TRIPS
					+" ("+TRIPS_FIEID_ACCTTDATE+" INTEGER PRIMARY KEY AUTOINCREMENT,"
					+TRIPS_FIEID_USERID+" VARCHAR(256) NOT NULL ,"
					+TRIPS_FIEID_TRIPINFO+" TEXT NOT NULL ,"
					+TRIPS_FIEID_SYNC +" INTEGER DEFAULT '0'"
					+")";

	public static final String TABLE_NAME_TRIPPOINTS="tripspoints";//用于记录每一个点

	public static final String TRIPPOINT_FIEID_ID = "PID";//自动增长的一个点
	public static final String TRIPPOINT_FIEID_USERID = USER_FIEID_EMAIL;//person id 作为外键
	public static final String TRIPPOINT_FIEID_LATITUDE="LATITUDE";//纬度
	public static final String TRIPPOINT_FIEID_LONGITUDE="LONGITUDE";//经度
	public static final String TRIPPOINT_FIEID_TIME ="TIME";//时间点
	public static final String TRIPPOINT_FIEID_TYPE = "ISSTOP";//是否是停止点(0否,1是);
	public static final String TRIPPOINT_FIEID_REASON = "REASON";//到这个点的原因
	public static final String TRIPPOINT_FIEID_WAY = "WAY";//到这个点的方法
	public static final String TRIPPOINT_FIEID_CONTENT = "CONTENT";//到这个点的说明


	public static final String CREATE_TABLE_POINTS ="CREATE TABLE IF NOT EXISTS "
			+TABLE_NAME_TRIPPOINTS
			+" (" +TRIPPOINT_FIEID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+TRIPPOINT_FIEID_USERID+" VARCHAR(256) NOT NULL ,"
			+TRIPPOINT_FIEID_LATITUDE + " INTEGER ,"
			+TRIPPOINT_FIEID_LONGITUDE + " INTEGER ,"
			+TRIPPOINT_FIEID_TIME + " INTEGER ,"
			+TRIPPOINT_FIEID_REASON + " INTEGER DEFAULT '0',"
			+TRIPPOINT_FIEID_WAY + " INTEGER DEFAULT '0',"
			+TRIPPOINT_FIEID_CONTENT + " TEXT ,"
			+TRIPPOINT_FIEID_TYPE +" INTEGER DEFAULT '0'"
			+")";

	public static final String TABLE_NAME_HISTORY = "history";

	public static final String HISTORY_FIEID_ID="hid";
	public static final String HISTORY_FIEID_USERID = USER_FIEID_EMAIL;
	public static final String HISTORY_FIEID_DATE = "date";
	public static final String HISTORY_FIEID_UPDATE = "isupdate";

	public static final String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS "
			+TABLE_NAME_HISTORY
			+" ( "+HISTORY_FIEID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+HISTORY_FIEID_USERID + " VARCHAR(256) NOT NULL , "
			+HISTORY_FIEID_DATE + " INTEGER , "
			+HISTORY_FIEID_UPDATE + " INTEGER DEFAULT '0'"
			+")";

	public static final String TABLE_NAME_CACHE="mcache";

	public static final String CACHE_FILED_ID="c_id";
	public static final String CACHE_FIEID_PROVIDER="provider";
	public static final String CACHE_FIEID_ACCURACY="accuracy";
	public static final String CACHE_FIEID_LATITUDE="latitude";
	public static final String CACHE_FIEID_LONGITUDE="longitude";
	public static final String CACHE_FIEID_SPEED="speed";
	public static final String CACHE_FIEID_TIME="time";

	public static final String CREATE_TABLE_CACHE="CREATE TABLE IF NOT EXISTS "
			+TABLE_NAME_CACHE
			+" ( "+CACHE_FILED_ID + " VARCHAR(256) PRIMARY KEY , "
			+CACHE_FIEID_PROVIDER + " VARCHAR(256) NOT NULL , "
			+CACHE_FIEID_ACCURACY + " VARCHAR(256) DEFAULT '10000' , "
			+CACHE_FIEID_LATITUDE + " VARCHAR(256) DEFAULT '1000' , "
			+CACHE_FIEID_LONGITUDE + " VARCHAR(256) DEFAULT '1000' , "
			+CACHE_FIEID_SPEED + " VARCHAR(256) DEFAULT '0' , "
			+CACHE_FIEID_TIME + " VARCHAR(256) DEFAULT '0'"
			+")";

}
