package com.example.exampleproject.app.util;

import java.security.MessageDigest;

public class MD5Util {
	public final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public final static char lowerCase[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String MD5encode(String src) {
		if (src == null || "".equals(src)) {
			return null;
		}
		try {
			byte[] strTemp = src.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = lowerCase[byte0 >>> 4 & 0xf];
				str[k++] = lowerCase[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
