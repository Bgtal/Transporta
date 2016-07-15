package blq.ssnb.trive.util;

import blq.ssnb.trive.app.MyApplication;

import android.content.Context;
import android.widget.Toast;

public class TUtil {
	private static Context context = MyApplication.getInstance();
	private TUtil(){}
	public static void TShort(String mes){
		show(mes,Toast.LENGTH_SHORT);
	}
	
	public static void TLong(String mes){
		show(mes,Toast.LENGTH_LONG);
	}
	
	public static void TShort(int id){
		show(context.getString(id),Toast.LENGTH_SHORT);
	}
	public static void TLong(int id){
		show(context.getString(id),Toast.LENGTH_LONG);
	}
	
	private static void show(String mes, int lengthShort) {
		Toast.makeText(context, mes, lengthShort).show();
	}
}
