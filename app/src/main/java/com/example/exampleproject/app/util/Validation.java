package com.example.exampleproject.app.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串验证工具类（网站域名 联系电话 手机号码 邮政编码 邮箱 身份证等）
 * 
 * @author chang
 * 
 */
public class Validation {
	public static final String EMPTY_STRING = ""; // 空字符串

	/** 中国移动号码 */
	private static final String REGEX_CHINA_MOBILE = "^(1(3[4-9]|5[01789]|8[78]))\\d{8}$";
	/** 中国联通号码 */
	private static final String REGEX_CHINA_UNICOM = "^(1(3[0-2]|5[256]|8[56]))\\d{8}$";
	/** 中国电信号码 */
	private static final String REGEX_CHINA_NET = "^(18[019])\\d{8}$";
	/** 中国电信(CDMA)号码 */
	private static final String REGEX_CDMA_CHINA_NET = "^(1[35]3)\\d{8}$";

	/**
	 * 正则验证方法
	 * 
	 * @param regexstr
	 * @param str
	 * @return
	 */
	public static boolean Match(String regexstr, String str) {
		Pattern regex = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher matcher = regex.matcher(str);
		return matcher.matches();
	}

	/**
	 * 解析短信推送内容
	 * 
	 * @param data
	 * @return
	 */
	public static Map<String, Object> getParameterMap(String data) {
		Map<String, Object> map = null;
		if (data != null) {
			map = new HashMap<String, Object>();
			String[] params = data.split("&");
			for (int i = 0; i < params.length; i++) {
				int idx = params[i].indexOf("=");
				if (idx >= 0) {
					map.put(params[i].substring(0, idx),
							params[i].substring(idx + 1));
				}
			}
		}
		return map;
	}

	/**
	 * 检测字符串是否符合用户名
	 * 
	 * @param len
	 * @return
	 */
	public static boolean checkingUserName(int len) {
		boolean isValid = true;
		if (5 < len && len < 17) {
			isValid = false;
		} else {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 检测字符串是否符合密码
	 * 
	 * @param len
	 * @return
	 */
	public static boolean checkingPwd(int len) {
		boolean isValid = true;
		if (5 < len && len < 17) {
			isValid = false;
		} else {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 检测字符串是否为中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinesrChar(String str) {
		if (str.length() < str.getBytes().length) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 邮箱验证
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean MatchMail(String mail) {
		String mailregex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return Match(mailregex, mail);
	}

	/**
	 * 手机验证
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean MatchMobile(String mobile) {
		if (Match(REGEX_CHINA_MOBILE, mobile)
				|| Match(REGEX_CHINA_UNICOM, mobile)
				|| Match(REGEX_CHINA_NET, mobile)
				|| Match(REGEX_CDMA_CHINA_NET, mobile)) {
			return true;
		}
		return false;
	}

	/**
	 * 电话验证
	 * 
	 * @param Tel
	 * @return
	 */
	public static boolean MatchTel(String Tel) {
		String telregex = "(^[0-9]{3,4}-[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{7,15}$)";
		return Match(telregex, Tel);
	}

	/**
	 * 域名验证
	 * 
	 * @param webdomain
	 * @return
	 */
	public static boolean Webdomain(String webdomain) {
		String webdomainregex = "http://([^/]+)/*";
		return Match(webdomainregex, webdomain);
	}

	/**
	 * 邮政编号验证
	 * 
	 * @param zipcode
	 * @return
	 */
	public static boolean ZipCode(String zipcode) {
		String zipcoderegex = "^[0-9]{6}$";
		return Match(zipcoderegex, zipcode);
	}

	/**
	 * 身份证验证
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean IdCardNo(String idcard) {
		return new IdcardValidator().isValidatedAllIdcard(idcard);
	}

	/**
	 * 判断字符串是否为空字符串。
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isEmpty(String aString) {
		return aString == null || EMPTY_STRING.equals(aString.trim());
	}

	/**
	 * 判断字符串是否是整数
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isInteger(String aString) {
		try {
			Integer.parseInt(aString);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 格式化手机号码
	 * 
	 * @param aPhoneNum
	 * @return
	 */
	public static String formatPhoneNum(String aPhoneNum) {
		String first = aPhoneNum.substring(0, 3);
		String end = aPhoneNum.substring(7, 11);
		String phoneNumber = first + "****" + end;
		return phoneNumber;
	}

	/**
	 * 将带格式的日期时间字符串dt转换为不带格式的日期时间字符串
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDateStrToShortDateStr(String dt) {
		try {
			return new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查字符串是否为纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isLetter(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!(s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
					&& !(s.charAt(i) >= 'a' && s.charAt(i) <= 'z')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 去除字符串中空格
	 * 
	 * @param aString
	 * @return
	 */
	public static String clearSpaces(String aString) {
		StringTokenizer aStringTok = new StringTokenizer(aString, " ", false);
		String aResult = "";
		while (aStringTok.hasMoreElements()) {
			aResult += aStringTok.nextElement();
		}
		return aResult;
	}

	/**
	 * 处理应用安装人数
	 * 
	 * @param number
	 * @return
	 */
	public static String numToFormat(int number) {
		String result = null;
		int y, w, q;
		if (number > 9999) {
			w = number / 10000;
			q = (number % 10000) / 1000;
			result = w + "." + q + "万";
		} else if (number > 99999999) {
			y = number / 100000000;
			w = (number % 100000000) / 10000;
			result = y + "." + w + "亿";
		} else {
			result = "" + number;
		}
		return result;
	}

	public static String formatToUTF8(String str)
			throws UnsupportedEncodingException {
		if (isEmpty(str)) {
			return "";
		}
		String result = new String(str.getBytes("ISO-8859-1"), "UTF8");
		return result;
	}

	// 判断是否为中文
	public static final boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			// 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
			if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
					|| ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public static final boolean isSpecialCharacter(String str) {
		// 是"�"这个特殊字符的乱码情况
		if (str.contains("ï¿½")) {
			return true;
		}
		return false;
	}
}
