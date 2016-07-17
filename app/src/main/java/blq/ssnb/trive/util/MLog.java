package blq.ssnb.trive.util;

import android.util.Log;

public class MLog {

	private static int LEVEL = 0;
	private static int V = 1;
	private static int D = 2;
	private static int I = 3;
	private static int W = 4;
	private static int E = 5;

	public static void v(String tag ,String msg){
		msg = isEmpty(msg);
		if(LEVEL>=V){
			Log.v(tag, msg);			
		}
	}

	public static void d(String tag ,String msg){
		msg = isEmpty(msg);
		if(LEVEL>=V){
			Log.d(tag, msg);			
		}
	}

	public static void i(String tag ,String msg){
		msg = isEmpty(msg);
		if(LEVEL>=V){
			Log.i(tag, msg);			
		}
	}

	public static void w(String tag ,String msg){
		msg = isEmpty(msg);
		if(LEVEL>=V){
			Log.w(tag, msg);			
		}
	}
	public static void e(String tag ,String msg){
		msg = isEmpty(msg);
		if(LEVEL>=V){
			Log.e(tag, msg);			
		}
	}
	private static String isEmpty(String msg){
		
		if(msg==null||msg.isEmpty()){
			msg="data is null";
		}
		
		return msg;
	}
}
