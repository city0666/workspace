/** 
 * 类说明： SQLite数据库抽象类  
 * @author  赵晨
 * @date    
 * @version 1.0
 */

package com.ustc.contactshelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUtil {

	private static final String TAG = "DatabaseUtil";

	/**
	 * Database Name
	 */
	private static final String DATABASE_NAME = "ContactsHelper_DB";//数据库名

	/**
	 * Database Version
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * Table Name
	 */
	private static final String DATABASE_TABLE = "relation";//表名

	/**
	 * Table columns
	 */
	public static final String KEY_CON = "contact";
	public static final String KEY_SOC = "sociality";
	public static final String KEY_SOCTYPE = "sociality_type";
	public static final String KEY_ROWID = "_id";

	/**
	 * Database creation SQL statement
	 */
	private static final String CREATE_RELATION_TABLE = "create table "//建表的SQL语句
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_CON
			+ " text not null, " + KEY_SOC + " text not null," + KEY_SOCTYPE
			+ " text not null);";

	/**
	 * Context
	 */
	private final Context mCtx;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Inner private class. Database Helper class for creating and updating
	 * database.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * onCreate method is called for the 1st time when database doesn't
		 * exists.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating DataBase: " + CREATE_RELATION_TABLE);
			db.execSQL(CREATE_RELATION_TABLE);
		}

		/**
		 * onUpgrade method is called when database version changes.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DatabaseUtil(Context ctx) {//构造函数
		this.mCtx = ctx;
		mDbHelper = new DatabaseHelper(mCtx);

	}

	/**
	 * This method is used for creating/opening connection
	 * 
	 * @return instance of DatabaseUtil
	 * @throws SQLException
	 */
	public DatabaseUtil open() throws SQLException {//打开数据库

		// mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		// DatabaseHelper.OnCreate(mDb);
		return this;
	}

	/**
	 * This method is used for closing the connection.
	 */
	public void close() {//关闭数据库
		mDbHelper.close();
	}

	/**
	 * This method is used to create/insert new record Relation record.
	 * 
	 * @param conntact
	 * @param sociality
	 * @param sociality_type
	 * @return long
	 */
	public long createRelation(String contact, String sociality,//插入
			String sociality_type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CON, contact);
		initialValues.put(KEY_SOC, sociality);
		initialValues.put(KEY_SOCTYPE, sociality_type);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * This method will delete Relation record.
	 * 
	 * @param rowId
	 * @return boolean
	 */
	public boolean deleteRelation(String soc_name, String name) {//删除
		return mDb.delete(DATABASE_TABLE, KEY_SOC + "='" + soc_name + "' AND "
				+ KEY_CON + "='" + name + "'", null) > 0;
	}

	/**
	 * This method will return Cursor holding all the Relation records.
	 * 
	 * @return Cursor
	 */
	public Cursor fetchAllRelation() {//查询所有项
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_CON,
				KEY_SOC, KEY_SOCTYPE }, null, null, null, null, null);
	}

	/**
	 * This method will return Cursor holding the specific Relation record.
	 * 
	 * @param id
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor fetchRelation(String name) throws SQLException {//按联系人姓名查询
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_CON, KEY_SOC, KEY_SOCTYPE }, KEY_CON + "="
				+ name, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * This method will update Student record.
	 * 
	 * @param id
	 * @param contact
	 * @param sociality
	 * @param sociality_type
	 * @return boolean
	 */
	public boolean updateRelation(int id, String contact, String sociality,//更新
			String sociality_type) {
		ContentValues args = new ContentValues();
		args.put(KEY_CON, contact);
		args.put(KEY_SOC, sociality);
		args.put(KEY_SOC, sociality_type);
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
	}
}
