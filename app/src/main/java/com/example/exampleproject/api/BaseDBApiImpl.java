package com.example.exampleproject.api;

import android.content.Context;

import com.example.exampleproject.api.db.DBHelper;

public class BaseDBApiImpl implements BaseDBApi {
	public DBHelper dbHelper;
	public final static Object syncObj = new Object();

	public BaseDBApiImpl(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
}
