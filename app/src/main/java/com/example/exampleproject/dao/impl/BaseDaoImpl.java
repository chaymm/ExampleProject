package com.example.exampleproject.dao.impl;

import android.content.Context;

import com.example.exampleproject.dao.BaseDAO;
import com.example.exampleproject.util.DBHelper;

public class BaseDaoImpl implements BaseDAO{
	public DBHelper dbHelper;
	public final static Object syncObj = new Object();

	public BaseDaoImpl(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
}
