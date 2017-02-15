package com.example.exampleproject.api.db;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Map;

/**
 * 数据库基类
 * 
 * @author chang
 * 
 */
public abstract class DatabaseColumn implements BaseColumns {
	/** 外部应用调用ContentProvider */
	public static final String AUTHORITY = "";
	/** 数据库表类 */
	public static final String[] SUBCLASSES = new String[] {};

	/**
	 * 获取数据表创建语句
	 * 
	 * @return
	 */
	public String getTableCreateor() {
		return getTableCreator(getTableName(), getTableMap());
	}

	/**
	 * 获取所有数据表类
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Class<DatabaseColumn>[] getSubClasses() {
		ArrayList<Class<DatabaseColumn>> classes = new ArrayList<Class<DatabaseColumn>>();
		Class<DatabaseColumn> subClass = null;
		for (int i = 0; i < SUBCLASSES.length; i++) {
			try {
				subClass = (Class<DatabaseColumn>) Class.forName(SUBCLASSES[i]);
				classes.add(subClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		return classes.toArray(new Class[0]);
	}

	/**
	 * 获取数据表创建语句
	 * 
	 * @param tableName
	 *            数据表名
	 * @param map
	 *            数据表字段集合
	 * @return
	 */
	private static final String getTableCreator(String tableName,
			Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		String value = null;
		StringBuilder creator = new StringBuilder();
		creator.append("CREATE TABLE ").append(tableName).append("( ");
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.append(keys[i]).append(" ");
			creator.append(value);
			if (i < length - 1) {
				creator.append(",");
			}
		}
		creator.append(")");
		return creator.toString();
	}

	/**
	 * 获取更改数据表语句
	 * 
	 * @return
	 */
	public String getAlterTableCreator() {
		String tempTableName = getTableName() + "_temp";
		StringBuilder creator = new StringBuilder();
		creator.append("ALTER TABLE " + getTableName() + " RENAME TO "
				+ tempTableName);
		return creator.toString();
	}

	/**
	 * 获取更改数据语句
	 * 
	 * @return
	 */
	public String getAlterDataCreator() {
		String tempTableName = getTableName() + "_temp";
		StringBuilder creator = new StringBuilder();
		StringBuilder columns = new StringBuilder();
		String[] keys = getAlterColumns();
		for (int i = 0; i < keys.length; i++) {
			String column = keys[i];
			columns.append(column);
			if (i < keys.length - 1) {
				columns.append(",");
			}
		}
		creator.append("INSERT INTO " + getTableName() + " ("
				+ columns.toString() + ") " + " SELECT " + columns.toString()
				+ " FROM " + tempTableName);
		return creator.toString();
	}

	/**
	 * 删除更改表语句
	 * 
	 * @return
	 */
	public String dropAlterTable() {
		String tempTableName = getTableName() + "_temp";
		StringBuilder creator = new StringBuilder();
		creator.append("DROP TABLE IF EXISTS " + tempTableName);
		return creator.toString();
	}

	/**
	 * 获取字段名
	 * 
	 * @return
	 */
	abstract public String[] getColumns();

	/**
	 * 获取数据表名称
	 * 
	 * @return
	 */
	abstract public String getTableName();

	/**
	 * 获取数据表URI
	 * 
	 * @return
	 */
	abstract public Uri getTableContent();

	abstract public String[] getAlterColumns();

	/**
	 * 获取数据表字段集合
	 * 
	 * @return
	 */
	abstract protected Map<String, String> getTableMap();

}
