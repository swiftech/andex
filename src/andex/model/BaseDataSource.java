package andex.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import andex.AndroidUtils;
import andex.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Access SQLite database from local file system(usually SDCard) or system storage.
 * Extend me to do more works on database.
 *   1. Override initTables() method to create database schema.
 *   2. Override getDB() if you want to provide other ways to SQLite database like system built-in.
 *   3. Call connect() before invoking any database operation method.
 *   4. Don't forget init table schema before everything.
 * @author 
 *
 */
public class BaseDataSource {
	
	public static final String TABLE_NAME_USER_META = "USER_META";

	public static final String SQL_CREATE_TABLE_USER_META = "create table "
			+ TABLE_NAME_USER_META
			+ " (ID integer not null PRIMARY KEY autoincrement, "
			+ " META_NAME text not null UNIQUE, " 
			+ " META_VALUE text not null, "
			+ " DEFAULT_VALUE text, "
			+ " MODIFY_TIME long not null, "
			+ " EXT_DATA text)";
	
	private static final String USER_META_SCHEMA_VERSION = "SCHEMA.VERSION"; 

	// Inject by setter
	protected Context context;
	
	protected String dbFilePath;
	protected String dbName;
	
	protected SQLiteDatabase db ;

	protected final String SQL_DROP_TABLE = "drop table ${tableName}";

	protected final String SQL_FIND_ALL = "select * from ${tableName}";
	
	
	// 是否自动关闭连接，默认是true；如果启用事务则设为false，所有方法实现时根据这个标识来决定是否在操作结束时关闭连接。
	protected boolean isAutoDisconnect = true;

	/**
	 * 
	 * @param filePath Path of DB file if SDCard available, set null or empty to force use internal storage.
	 * @param dbName Database name
	 */
	public BaseDataSource(String filePath, String dbName) {
		super();
		this.dbFilePath = filePath;
		this.dbName = dbName;
	}	
	
	protected SQLiteDatabase getDB() {
		Log.d("db", "Try to get Database instance");
		// 如果有SDCARD則存在SD卡上面
		if(!Utils.isEmpty(dbFilePath) && AndroidUtils.isSDCardAvailable()) {
			Log.d("db", "Access database from SD card");
			File dbFile = null;
			dbFile = new File(dbFilePath + dbName + ".db");
			if (!dbFile.exists()) {
				Log.i("db", "Database file " + dbFilePath + " does not exist, cureate a new file.");
				try {
					if (!dbFile.getParentFile().exists()) {
						if (dbFile.getParentFile().mkdirs() == false) {
							Log.e("db", "Failed to create db directory : " + dbFile.getParent());
							return null;
						}
						else {
							Log.i("db", "Black list db directory created: " + dbFile.getParent());
						}
					}
					if(dbFile.createNewFile() == false) {
						Log.d("db", "Failed to create db file: " + dbFile);
						return null;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
			if(!db.isOpen()) {
				throw new RuntimeException("Failed to open database from: " + dbFilePath);
			}
			return db;
		}
		else {
			Log.d("db", "Access database from system storage");
			// 没有则存系统的数据库。
			if(context == null) {
				Log.e("andex.db", "System Context didn't set properly.");
				return null;
			}
			DefaultSQLiteOpenHelper dbHelper = new DefaultSQLiteOpenHelper(context, "andex");
			return dbHelper.getWritableDatabase();
		}
	}

	
	/**
	 * Connect to DB file, create it if not exist.
	 */
	public void connect() {
		if(db != null && db.isOpen()) {
			return;
		}
		db = getDB();
	}
	
	/**
	 * 
	 */
	public void disconnect() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
	/**
	 * Init tables by default: user meta infomation table.
	 * Override me if more tables to be created.
	 */
	public boolean initTables() {
		try {
			createTable(SQL_CREATE_TABLE_USER_META);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getSchemaVersion() {
		connect();
		return this.getMetaInt(USER_META_SCHEMA_VERSION);
	}
	
	public void setSchemaVersion(int version) {
		connect();
		this.setMeta(USER_META_SCHEMA_VERSION, version);
	}
	
	/**
	 * Get meta data by name. No need to open and close connection explicitly.
	 * @param metaName
	 * @return
	 */
	public String getMeta(String metaName) {
		if (!db.isOpen()) {
			this.connect();
		}
		try {
			Cursor cursor = db.query(TABLE_NAME_USER_META, null, " META_NAME = ? ", new String[] { metaName }, null,
					null, null);
			if (cursor.moveToNext()) {
				return cursor.getString(cursor.getColumnIndex("META_VALUE"));
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 取Long型值，如果小于0则返回默认值。
	 * @param metaName
	 * @param defaultValue
	 * @return
	 */
	public long getMetaLong(String metaName, long defaultValue) {
		long ret = getMetaLong(metaName);
		if (ret < 0) {
			return defaultValue;
		}
		else {
			return ret;
		}
	}

	public long getMetaLong(String metaName) {
		String meta = getMeta(metaName);
		if (Utils.isEmpty(meta)) {
			return -1L;
		}
		try {
			return Long.parseLong(meta);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1L;
		}
	}
	
	public int getMetaInt(String metaName, int defaultValue) {
		return (int)getMetaLong(metaName, defaultValue);
	}
	public int getMetaInt(String metaName) {
		return (int)getMetaLong(metaName);
	}
	
	/**
	 * Set meta data by name. No need to open and close connection explicitly.
	 * @param metaName
	 * @param metaValue
	 * @return
	 */
	public boolean setMeta(String metaName, Object metaValue) {
		if(getMeta(metaName) == null) {
			if (!db.isOpen()) {
				this.connect();
			}
			try {
				ContentValues values = new ContentValues();
				values.put("META_NAME", metaName);
				values.put("META_VALUE", metaValue.toString());
				values.put("MODIFY_TIME", Calendar.getInstance().getTimeInMillis());
				db.insert(TABLE_NAME_USER_META, null, values);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
//			} finally {
//				this.disconnect();
			}
		}
		else {
			if (!db.isOpen()) {
				this.connect();
			}
			try {
				db.execSQL("UPDATE " + TABLE_NAME_USER_META + " SET META_VALUE=? WHERE META_NAME=?", new String[]{metaValue.toString(), metaName});
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
//			} finally {
//				this.disconnect();
			}			
		}
		return true;
	}
	
	/**
	 * Create table by sql if not exist.
	 * @param sql
	 */
	public boolean createTable(String sql) {
		if(db == null || !db.isOpen()) {
			Log.e("db", "Database instance is not correctly initilized.");
			return false;
		}
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
//			e.printStackTrace();
			Log.w("db", e.getLocalizedMessage());
			return false;
		} finally {
			Log.i("db", "Table created");
//			db.close();
		}
		return true;
	}
	
	public void dropTable(String tableName) {
		if (db == null || !db.isOpen()) {
			Log.e("db", "Database instance is not correctly initilized.");
			return;
		}
		try {
			db.execSQL(SQL_DROP_TABLE.replace("${tableName}", tableName));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.i("db", "Table " + tableName + " has been droped");
			this.disconnect();
		}
	}
	
	public boolean isExists(String tableName, String uniqueCol, String colValue) {
		String sql = "select * from " + tableName + " where " + uniqueCol + "='" + colValue + "'";
//		Log.d("db", "SQL:" + sql);
		Cursor cur = null;
		try {
			cur = db.rawQuery(sql, null);
			if(cur.moveToNext()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally{
			if(cur != null )cur.close();	
		}
		return false;
	}
	
	public Map findUnique(String tableName, String uniqueColName, Object value) {
		List<Map> data = findAll(tableName, uniqueColName + "=" + value);
		if(data == null || data.size() == 0) {
			return null;
		}
		return data.get(0);
	}
	
	public List<Map> findAll(String tableName) {
		return findAll(tableName, null,  null);
	}
	
	public List<Map> findAll(String tableName, String whereClause) {
		return findAll(tableName, whereClause,  null);
	}
	
	/**
	 * 查询指定表的所有记录。
	 * @param tableName
	 * @param orderBy
	 * @return 一个List，每一项表示一条记录，每项用Map表示，key为字段名，value为字段值。
	 */
	public List<Map> findAll(String tableName, String whereClause,  String orderByClause) {
		if(db == null) {
			Log.e("db", "Database instance is not correctly initilized.");
			return null;
		}
		connect();
		Log.d("db", "Find all in table " + tableName);
		Cursor cursor = db.query(tableName, null, whereClause, null, null, null, orderByClause);
		try {
			return cursorToMapList(cursor);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		} finally {
			if(isAutoDisconnect) this.disconnect();
		}
	}
	
	public List<Map> find(String sql) {
		connect();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			return cursorToMapList(cursor);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		} finally {
			if(isAutoDisconnect) this.disconnect();
		}
	}
	
	private List<Map> cursorToMapList(Cursor cursor) {
		List<Map> result = new ArrayList();
		int n = 0;
		for(;cursor.moveToNext();) {
//			Log.d("db", "Row" + n++);
			int count = cursor.getColumnCount();
			Map row = new HashMap();
			for (int i = 0; i < count; i++) {
				String colName = cursor.getColumnName(i);
				if("ID".equals(colName)) {
					row.put(colName, cursor.getLong(i));	
				}
				else {
					row.put(colName, cursor.getString(i));
				}
			}
			result.add(row);
		}
		Log.d("db", "Result with " + result.size() + " records.");
		return result;
	}
	
	public long countTableAll(String table) {
		return countTable(table, null, null);
	}
	
	public long countTable(String table, String filter, String[] values) {
		//TODO 暂时用query替代实现，如果有性能问题，再改成用rawQuery实现
		prepareToConnect();
		Cursor cursor = db.query(table, null, filter, values, null, null, null, null);
		long i=0;
		for(;cursor.moveToNext();i++){}
		cursor.close();
		return i;
	}
	
	/**
	 * 
	 * @param tbName
	 * @param pkID
	 * @return
	 */
	public boolean deleteRow(String tbName, long pkID) {
		prepareToConnect();
		try {
			int rows = db.delete(tbName, "ID=?", new String[]{"" + pkID});
			Log.i("db", "" + rows  + " rows deleted.");
			return (rows > 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
//		} finally {
//			this.disconnect();
		}
	}
	
	// TODO use this method for every operation.
	// @deprecated?
	protected void prepareToConnect() {
//		if(db == null) {
//			Log.e("db", "Database instance is not correctly initilized.");
//			throw new RuntimeException("Database instance is not correctly initilized.");
//		}
		if(db == null || !db.isOpen()) {
			connect();
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}
	
	public void beginTransaction() {
		isAutoDisconnect = false; // 关闭自动模式
		this.connect();
		this.db.beginTransaction();
	}
	
	public void commit() {
		this.db.setTransactionSuccessful();
	}
	
	public void rollback() {
		// NOTHING NEED TO DO FOR ROLLBACK
	}
	
	public void endTransaction() {
		this.db.endTransaction();
		this.disconnect();
		isAutoDisconnect = true; // 恢复自动模式
	}


	public void setContext(Context context) {
		this.context = context;
	}
}
