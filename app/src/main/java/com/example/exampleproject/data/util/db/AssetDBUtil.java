package com.example.exampleproject.data.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.exampleproject.data.util.cache.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * sqlLite操作类
 */
public class AssetDBUtil extends SQLiteOpenHelper {
    private SQLiteDatabase mDb;
    private Context mContext;
    private String mDatabaseName;

    private AssetDBUtil(Context context, String name, int version) {
        super(context, name, null, version);
        this.mContext = context;
        this.mDatabaseName = name;
        open();
    }

    private AssetDBUtil(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            return;
        }
    }

    /**
     * 批量执行创建数据表
     *
     * @param db
     * @param actionString
     */
    public void operateTable(SQLiteDatabase db, String actionString) {
        Class<DatabaseColumn>[] columnsClasses = DatabaseColumn.getSubClasses();
        DatabaseColumn columns = null;
        for (int i = 0; i < columnsClasses.length; i++) {
            try {
                columns = columnsClasses[i].newInstance();
                if ("".equals(actionString) || actionString == null) {
                    if (!isTableExist(columns.getTableName())) {
                        db.execSQL(columns.getTableCreateor());
                    }
                } else {
                    db.execSQL(actionString + columns.getTableName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启数据库写入
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        mDb = getWritableDatabase();
    }

    /**
     * 关闭数据库写入
     */
    public void close() {
        mDb.close();
    }

    /**
     * 判断是否开启数据库写入
     *
     * @return
     */
    public boolean isDBOpen() {
        return mDb.isOpen();
    }

    /**
     * 插入数据
     *
     * @param tableName     表名
     * @param initialValues 要插入的列对应值
     * @return
     */
    public long insert(String tableName, ContentValues initialValues) {
        return mDb.insert(tableName, null, initialValues);
    }

    /**
     * 删除数据
     *
     * @param tableName       表名
     * @param deleteCondition 删除的条件
     * @param deleteArgs      如果deleteCondition中有“？”号，将用此数组中的值替换
     * @return
     */
    public boolean delete(String tableName, String deleteCondition,
                          String[] deleteArgs) {
        return mDb.delete(tableName, deleteCondition, deleteArgs) > 0;
    }

    /**
     * 更新数据
     *
     * @param tableName     表名
     * @param initialValues 要更新的列
     * @param selection     更新的条件
     * @param selectArgs    如果selection中有“？”号，将用此数组中的值替换
     * @return
     */
    public boolean update(String tableName, ContentValues initialValues,
                          String selection, String[] selectArgs) {
        int returnValue = mDb.update(tableName, initialValues, selection,
                selectArgs);
        return returnValue > 0;
    }

    /**
     * 取得一个列表
     *
     * @param tableName     表名
     * @param columns       返回的列
     * @param selection     查询条件
     * @param selectionArgs 如果selection中有“？”号，将用此数组中的值替换
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public Cursor findList(String tableName, String[] columns,
                           String selection, String[] selectionArgs, String groupBy,
                           String having, String orderBy) {
        return mDb.query(tableName, columns, selection, selectionArgs, groupBy,
                having, orderBy);
    }

    /**
     * 取得单行记录
     *
     * @param tableName     表名
     * @param columns       返回的列
     * @param selection     查询条件
     * @param selectionArgs 如果selection中有“？”号，将用此数组中的值替换
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @param distinct
     * @return
     * @throws SQLException
     */
    public Cursor findInfo(String tableName, String[] columns,
                           String selection, String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit, boolean distinct)
            throws SQLException {
        Cursor mCursor = mDb.query(distinct, tableName, columns, selection,
                selectionArgs, groupBy, having, orderBy, limit);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * 执行sql
     *
     * @param sql 要执行的sql
     */
    public void execSQL(String sql) {
        mDb.execSQL(sql);
    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean isTableExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim() + "' ";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 判断某张表中是否存在某字段(注，该方法无法判断表是否存在，因此应与isTableExist一起使用)
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public boolean isColumnExist(String tableName, String columnName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim()
                    + "' and sql like '%"
                    + columnName.trim() + "%'";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 开启数据库读取
     *
     * @return
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        File dbFile = mContext.getDatabasePath(mDatabaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * 开启数据库写入
     *
     * @return
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        File dbFile = mContext.getDatabasePath(mDatabaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * 复制数据库文件
     *
     * @param dbFile
     * @throws IOException
     */
    private void copyDatabase(File dbFile) throws IOException {
        InputStream stream = mContext.getAssets().open(mDatabaseName);
        String filePath = dbFile.getAbsolutePath();
        if (FileUtil.makeDirs(FileUtil.getFolderName(filePath))) {
            FileUtil.writeFile(filePath, stream, false);
        }
    }

}
