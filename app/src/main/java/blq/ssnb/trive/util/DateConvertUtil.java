package blq.ssnb.trive.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换工具，可以讲时间戳转换为格式化日期，可以讲格式化日期转换为时间戳
 * @author xucj
 *
 */
@SuppressLint("SimpleDateFormat")
public class DateConvertUtil {
	/**
	 * 日期格式的类型
	 * @author xucj
	 *
	 */
	public enum DateFormatStyle{
		yyyy_MM_dd("yyyy-MM-dd"),
		yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
		yyyyMMdd("yyyyMMdd"),
		yyyyMMddHHmmss("yyyyMMddHHmmss"),
		MM_dd_HH_mm("MM-dd HH:mm");
		private String style;
		DateFormatStyle(String style){
			this.style = style;
		}
		private String getStyle(){
			return style;
		}
	}

	public static String MM_dd_HH_mm(long time){
		return MFormat(DateFormatStyle.MM_dd_HH_mm.getStyle(), time);
	}

	/**
	 * 返回格式为  2015-08-09
	 * @param time 需要转换的时间
	 * @return
	 */
	public static String yyyy_MM_dd(long time){
		return MFormat(DateFormatStyle.yyyy_MM_dd.getStyle(), time);

	}

	/**
	 * 返回格式为 2015-08-09 12:02:15
	 * @param time 需要转换的时间
	 * @return
	 */
	public static String yyyy_MM_dd_HH_mm_ss(long time){
		return MFormat(DateFormatStyle.yyyy_MM_dd_HH_mm_ss.getStyle(), time);
	}
	/**
	 * 返回格式为 20151215
	 * @param time 需要转换的时间
	 * @return
	 */
	public static String YYYYMMDD(long time){
		return MFormat(DateFormatStyle.yyyyMMdd.getStyle(), time);
	}

	/**
	 * 获取本地格式的时间
	 * @param time 需要转换的时间戳
	 * @return 返回本地格式的时间格式
	 */
	public static String LocationFormat(long time){
		return SimpleDateFormat.getDateInstance().format(new Date(time));
	}

	private static String MFormat(String format,long time){
		Date date = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	/**
	 * 将格式化时间转换为long类的时间戳
	 * @param time 时间 20150515（2015-05-15 ...）
	 * @param style 传入时间的类型（类型需要对应上，不然解析会出错）
	 * @return
	 * @throws ParseException 当类型无法解析的时候会报错
	 */
	public static long TimeStamp(String time,DateFormatStyle style) throws ParseException{
		String format = null;
		format = style.getStyle();
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date date = new Date();
		date =sf.parse(time);
		return date.getTime();
	}

	/**
	 * 返回当前时间的时间戳（单位为秒）
	 * @return
	 */
	public static int TimeStamp(){
		long time = System.currentTimeMillis();
		return TimeStamp(time);
	}
	/**
	 * 将Long 时间戳（毫秒）转换为 int 时间戳（秒）
	 * @param time 单位为毫秒的时间戳
	 * @return 单位为秒的时间戳
	 */
	public static int TimeStamp(long time){
		time = time /1000;
		return Long.valueOf(time).intValue();
	}
	/**
	 * 将int 时间戳（秒）转换为 Long 时间戳（毫秒）
	 * @param time 单位为秒的时间戳
	 * @return 单位为毫秒的时间戳
	 */
	public static long TimeStamp(int time){
		return time*1000L;
	}

	/**
	 * 返回当天的日期格式 20150505
	 * @return String 类型的日期格式
	 */
	public static String DateTodayString(){
		return YYYYMMDD(System.currentTimeMillis());
	}
	/**
	 * 返回当天零点的时间戳 
	 * @return 单位毫秒的时间戳
	 * @throws ParseException
	 */
	public static long DateTodayLong(){
		try {
			return TimeStamp(DateTodayString(), DateFormatStyle.yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 返回当天的零点的时间戳
	 * @return 单位为秒的时间戳
	 * @throws ParseException
	 */
	public static int DateTodayInt() {
		long time=0;
		try {
			time = TimeStamp(DateTodayString(), DateFormatStyle.yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return TimeStamp(time);
	}

}

