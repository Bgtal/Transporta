package blq.ssnb.trive.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 存储个人偏好信息
 * @author SSNB
 * 
 */
public class PreferenceUtil {

	private SharedPreferences sharedPreferences;
	private Editor editor;
	/**
	 * 
	 * @param context 上下文对象
	 * @param name 存储数据xml的命名
	 */
	@SuppressLint("CommitPrefEdits")
	public PreferenceUtil(Context context ,String name) {
		sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		editor= sharedPreferences.edit();

	}

	/**
	 * 保存参数
	 * 
	 * @param key
	 *            保存参数的名称
	 * @param value
	 *           String 保存参数的值
	 */
	public void saveString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 读取保存的参数，
	 * 
	 * @param key
	 *            需要读取的参数的名称
	 * @return  String类型的参数值
	 * 如果该参数不存在返回 ""
	 */
	public String readString(String key) {
		String nuber = sharedPreferences.getString(key, "");
		return nuber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 		long 类型的内容
	 */
	public void saveLong(String key, long value){
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return 如果不存在返回 -1
	 */
	public long readLong(String key) {
		long nuber = sharedPreferences.getLong(key,-1);
		return nuber;
	}

	/**
	 * 保存参数
	 * 
	 * @param key
	 *            保存参数的名称
	 * @param value
	 *            保存参数的值
	 */
	public void saveBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 读取保存的参数，
	 * 
	 * @param key
	 *            需要读取的参数的名称
	 * @return  boolean类型的参数值
	 * 如果该参数不存在返回false
	 */
	public boolean readBoolean(String key) {
		boolean nuber = sharedPreferences.getBoolean(key,false);
		return nuber;
	}
	/**
	 * 保存参数
	 * 
	 * @param key
	 *            保存参数的名称
	 * @param value
	 *            保存参数的值
	 */
	public void saveInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 读取保存的参数，
	 * 
	 * @param key
	 *            需要读取的参数的名称
	 * @return  int 类型的参数值
	 * 如果该参数不存在返回 -1
	 */
	public int readInt(String key) {
		int nuber = sharedPreferences.getInt(key,-1);
		return nuber;
	}

	/**
	 * 保存参数
	 * @param key
	 * @param value
	 */
	public void saveFloat(String key, float value) {
		editor.putFloat(key, value);
		editor.commit();
	}
	/**
	 * 读取参数
	 * @param key
	 * @return
	 * 	如果不存在返回 -1
	 */
	public float readFloat(String key){
		float nuber = sharedPreferences.getFloat(key,0);
		return nuber;
	}

	/**
	 * 读取参数 待默认参数
	 * @param key
	 * @param defValue 当读取的数据不存在返回的数据
	 * @return
	 */
	public float readFloat(String key,float defValue){
		float nuber =  sharedPreferences.getFloat(key, defValue);
		return nuber;
	}

}
