package com.example.exampleproject.util;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class JacksonUtil {

	/**
	 * 对象转换json
	 * 
	 * @param object
	 *            要转换成json的目标对象
	 * @return
	 */
	public static String toJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		// by cm 2016-03-08
		// 有些变量转换失败，尝试使用配置
		// jackson 1.9 and before
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json转换对象
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param clazz
	 *            要转换的目标类
	 * @return
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		if (isEmpty(jsonString)) {
			return null;
		}

		try {
			return objectMapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			return null;
		}
	}

	private static boolean isEmpty(String inStr) {
		boolean reTag = false;
		if (inStr == null || "".equals(inStr)) {
			reTag = true;
		}
		return reTag;
	}
}
