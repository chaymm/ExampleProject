package com.example.exampleproject.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * xml解析工具类
 * 
 * @author chang
 * 
 * 例子：
 * public class User{  
	private String name;  
	private String age;  
	}
	<Users>  
	  <User>  
	    <name>张三</name>  
	    <age>20</age>  
	  </User>  
	  <User>  
	    <name>李四</name>  
	    <age>21</age>  
	  </User>
	<Users>
	List<String> fields = new ArrayList<String>(); 
	fields.add("name"); 
	fields.add("age");
	调用parseToList(is,User.class,fields,"User")
	
	<User>  
	    <name>李四</name>  
	    <age>21</age>  
	</User>
	调用parseToObject(is,User.class,fields,"User")
 */
public class XmlUtil {
	
	 /** 
     * 解析XML转换成对象 
     *  
     * @param is 
     *            输入流 
     * @param clazz 
     *            对象Class 
     * @param fields 
     *            字段集合一一对应节点集合 
     * @param itemElement 
     *            根节点标签 
     * @return 
     */  
    public static List<Object> parseToList(InputStream is, Class<?> clazz,  
            List<String> fields, String itemElement) {  
        List<Object> list = new ArrayList<Object>();  
        try {  
        	//========创建XmlPullParser,有两种方式=======  
            //方式一:使用工厂类XmlPullParserFactory  
            //XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();  
            //XmlPullParser parser = pullFactory.newPullParser();  
            //方式二:使用Android提供的实用工具类android.util.Xml  
            XmlPullParser xmlPullParser = Xml.newPullParser();  
            xmlPullParser.setInput(is, "UTF-8");  
            int event = xmlPullParser.getEventType();  
  
            Object obj = null;  
  
            while (event != XmlPullParser.END_DOCUMENT) {  
                switch (event) {  
                case XmlPullParser.START_TAG:  
                    if (itemElement.equals(xmlPullParser.getName())) {  
                        obj = clazz.newInstance();  
                    }  
                    if (obj != null  
                            && fields.contains(xmlPullParser.getName())) {  
                        setFieldValue(obj, fields.get(fields  
                                .indexOf(xmlPullParser.getName())),  
                                xmlPullParser.nextText());  
                    }  
                    break;  
                case XmlPullParser.END_TAG:  
                    if (itemElement.equals(xmlPullParser.getName())) {  
                        list.add(obj);  
                        obj = null;  
                    }  
                    break;  
                }  
                event = xmlPullParser.next();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException("解析XML异常：" + e.getMessage());  
        }  
        return list;  
    }  
    
    /** 
     * 解析XML转换成对象 
     *  
     * @param is 
     *            输入流 
     * @param clazz 
     *            对象Class 
     * @param fields 
     *            字段集合一一对应节点集合 
     * @param itemElement 
     *            根节点标签  
     * @return 
     */  
    public static Object parseToObject(InputStream is, Class<?> clazz,  
            List<String> fields, String itemElement) {  
    	Object obj = null;
        try {  
        	//========创建XmlPullParser,有两种方式=======  
            //方式一:使用工厂类XmlPullParserFactory  
            //XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();  
            //XmlPullParser parser = pullFactory.newPullParser();  
            //方式二:使用Android提供的实用工具类android.util.Xml
            XmlPullParser xmlPullParser = Xml.newPullParser();  
            xmlPullParser.setInput(is, "UTF-8");  
            int event = xmlPullParser.getEventType();  
  
            while (event != XmlPullParser.END_DOCUMENT) {  
                switch (event) {  
                case XmlPullParser.START_TAG:  
                    if (itemElement.equals(xmlPullParser.getName())) {  
                        obj = clazz.newInstance();  
                    }  
                    if (obj != null  
                            && fields.contains(xmlPullParser.getName())) {  
                        setFieldValue(obj, fields.get(fields  
                                .indexOf(xmlPullParser.getName())),  
                                xmlPullParser.nextText());  
                    }  
                    break;  
                }  
                event = xmlPullParser.next();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException("解析XML异常：" + e.getMessage());  
        }  
        return obj;  
    } 
      
    /** 
     * 设置字段值 
     *  
     * @param propertyName 
     *            字段名 
     * @param obj 
     *            实例对象 
     * @param value 
     *            新的字段值 
     * @return 
     */  
    public static void setFieldValue(Object obj, String propertyName,  
            Object value) {  
        try {  
            Field field = obj.getClass().getDeclaredField(propertyName);  
            field.setAccessible(true);  
            field.set(obj, value);  
        } catch (Exception ex) {  
            throw new RuntimeException();  
        }  
    } 
}
