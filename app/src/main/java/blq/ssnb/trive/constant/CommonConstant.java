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

/*
	public enum Sex{
		WOMEN(0,R.string.women),MEN(1,R.string.men);
		private int tag ;
		private int string;
		private Sex(int tag,int string) {
			this.setTag(tag);
			this.setString(string);
		}
		public int getTag() {
			return tag;
		}
		private void setTag(int tag) {
			this.tag = tag;
		}
		public int getString() {
			return string;
		}
		private void setString(int string) {
			this.string = string;
		}
		public static Sex getSexByTag(int tag){
			if(tag==WOMEN.getTag()){
				return WOMEN;
			}else{
				return MEN;
			}
		}
	}*/


	public static final String[] WAY={"NULL",
			"Car as driver",
			"Car as passenger",
			"Motorbike",
			"Truck as driver",
			"Truck as passenger",
			"Other private vehicle",
			"Walk",
			"Bicycle",
			"Taxi",
			"Train",
			"Ferry",
			"School Bus",
			"Transperth Bus",
			"Other Bus",
			"Other method"};


	public static final String[] RESON={
			"NULL",
			"Got on or off PT",
			"Something picked-up or delivered ",
			"Someone picked-up or delivered",
			"Accompanied someone","Ate or drank",
			"Bought something (including petrol)",
			"Education","Work purposes","At home",
			"Visited someone","	Other (NEC)",
			"Change mode (NEC)",
			"Childcare (being cared for)",
			"Volunteer/Community activity",
			"Religious activity",
			"Personal business (NEC)",
			"Medical/Dental purposes",
			"Stayed overnight",
			"Socialising (Pubs, Clubs etc)",
			"Participated in sport",
			"Watched sport",
			"Participated in concert,musical,band etc",
			"Watched concert, musical, band etc",
			"Other recreational (eg. exercise)",
			"Browsing, window-shopping","Returned to other house",
			"Met/waited for someone",
			"Walked the dog",
			"Childcare (providing)",
			"Animal care/ feeding",
			"Vehicle repair and maintainence",
			"Gardening",
			"Beauty",
			"Financial Purposes (eg banking)",
			"To get on aeroplane/ship (to leave region)",
			"To be out of the region (NFD)"};


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
