package blq.ssnb.trive.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式 工具，主要有判断手机号，id等是否符合规则等
 * @author xucj
 *
 */
public class RegularUtil {

	/**
	 * 判断手机号码是否为10位数字
	 * @param mobiles 待判断的手机号码
	 * @return true 表示符合规则 false表示手机号码不正确
	 */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^()\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 判断输入的HHID是否为5为数字
	 * @param hhid
	 * @return true 表示符合规则 false表示HHID 不符合规则
	 */
	public static boolean isHhid(String hhid){
		Pattern p = Pattern.compile("^()\\d{5}$");
		Matcher m = p.matcher(hhid);
		return m.matches();
	}
	
	public static boolean isEmail(String email){
		Pattern p = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
