package com.example.exampleproject.util;

import android.content.Context;

/**
 * 根据资源的名字获取其ID值
 * 
 * @author chang
 * 
 */
public class MResource {
	public static int getIdByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName+".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if (desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return id;
	}

	public static int[] getIdsByName(Context context, String className,
			String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int[] ids = null;
		try {
			r = Class.forName(packageName+".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if ((desireClass != null)
					&& (desireClass.getField(name).get(desireClass) != null)
					&& (desireClass.getField(name).get(desireClass).getClass()
							.isArray()))
				ids = (int[]) desireClass.getField(name).get(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return ids;
	}
	
	public static int getStyleByName(Context context,String name){
		return getIdByName(context, "style", name);
	}
	
	public static int[] getStylesByName(Context context,String name){
		return getIdsByName(context, "styleable", name);
	}
	
	public static int getIdByName(Context context,String name){
		return getIdByName(context, "id", name);
	}
	
	public static int getLayoutByName(Context context,String name){
		return getIdByName(context, "layout", name);
	}
	
	public static int getDimenByName(Context context,String name){
		return getIdByName(context, "dimen", name);
	}
	
	public static int getStringByName(Context context,String name){
		return getIdByName(context, "string", name);
	}
}
