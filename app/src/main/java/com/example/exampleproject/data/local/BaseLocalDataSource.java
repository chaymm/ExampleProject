package com.example.exampleproject.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.exampleproject.data.util.db.DBUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by chang on 2017/3/3.
 */

public class BaseLocalDataSource {

    @NonNull
    protected BriteDatabase mDatabaseHelper;

    protected BaseLocalDataSource(@NonNull Context context, String name, int version) {
        DBUtil dbUtil = new DBUtil(context, name, version);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbUtil, Schedulers.io());
    }
}
