package andex;

import andex.utils.SysUtils;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.*;

/**
 * Utilities for Android.
 * TODO refactor to separated Utils classes.
 * @author 
 *
 */
public class AndroidUtils {
	
//	private static final String GLOBAL_SETTING = "org.andex";

	/**
	 * 杀掉一个进程
	 * @param ctx
	 * @param pkgName
	 */
	public static void killProcess(Context ctx,String pkgName) {
		ActivityManager actManager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
		// version 1.5 - 2.1
		if(android.os.Build.VERSION.SDK_INT <= 7) {
			actManager.restartPackage(pkgName);
		}
		// version 2.2+
		else {
			actManager.killBackgroundProcesses(pkgName);
		}
	}
	
	/**
	 * 比较某个包所在的APP的版本与目标版本的差异。
	 * Compare target version with APP version.
	 * @param ctx
	 * @param packageName
	 * @param targetVersion
	 * @return Return 1 if target version higher than app version, return -1 if lower, return 0 if equals.
	 */
	public static int compareAppVersion(Context ctx, String packageName, String targetVersion) {
		int[] appVersion = getAppVersion(ctx, packageName);
		StringTokenizer token = new StringTokenizer(targetVersion, ".");
		for (int i = 0; token.hasMoreTokens(); i++) {
			int v = Integer.parseInt(token.nextToken());
			if(v > appVersion[i]) {
				return 1;
			}
			else if(v < appVersion[i]) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 获取应用程序的版本号
	 * @param ctx
	 * @param packageName
	 * @return 版本数组（长度取决于版本信息
	 */
	public static int[] getAppVersion(Context ctx, String packageName) {
		List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
		for (Iterator it = pkgs.iterator(); it.hasNext();) {
			PackageInfo pi = (PackageInfo) it.next();
			if (pi.packageName.equals(packageName)) {
				StringTokenizer token = new StringTokenizer(pi.versionName, ".");
				int[] result = new int[token.countTokens()];
				for (int i = 0; token.hasMoreTokens(); i++) {
					result[i] = Integer.parseInt(token.nextToken());
				}
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static String getAppVersionString(Context ctx, String packageName) {
		List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
		for (Iterator it = pkgs.iterator(); it.hasNext();) {
			PackageInfo pi = (PackageInfo) it.next();
			if (pi.packageName.equals(packageName)) {
				return pi.versionName;
			}
		}
		return null;
	}
	
	/**
	 * Version of current APP.
	 * @param ctx
	 * @return
	 */
	public static String getAppVersion(Context ctx) {
		try {
			PackageInfo pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			return pkgInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "0.0";
		}
	}
	
	/**
	 * Divide screen height to grid.
	 * @param ctx
	 * @param gridHeight
	 * @param skipHeight
	 * @return
	 */
	public static int divideScreenHeight(Context ctx, int gridHeight, int skipHeight) {
		Log.v("andex", String.format("Screen: %dX%d", SysUtils.getScreenWidth(ctx), SysUtils.getScreenHeight(ctx)));
		int screenH = SysUtils.getScreenHeight(ctx);
		int contentHeight = screenH - skipHeight;
		double spacing = ((contentHeight / 160.0) * (contentHeight / 160.0));
		return (int) Math.round(contentHeight / (gridHeight + spacing));
	}
	
	/**
	 * Divide screen width to grid.
	 * @param ctx
	 * @param gridWidth
	 * @param skipWidth
	 * @param gridHeight
	 * @param skipHeight
	 * @param spacing
	 * @return
	 */
	public static int divideScreenWidth(Context ctx, int gridWidth, int skipWidth, int gridHeight, int skipHeight, int spacing){
		Log.v("andex", String.format("Screen: %dX%d", SysUtils.getScreenWidth(ctx), SysUtils.getScreenHeight(ctx)));

		int result = Math.round(SysUtils.getScreenWidth(ctx) / gridWidth)
				* Math.round((SysUtils.getScreenHeight(ctx) - skipHeight) / gridHeight);
		
		Log.v("andex", String.format("Cols: %d, Rows: %d",
				Math.round(SysUtils.getScreenWidth(ctx) / gridWidth),
				Math.round((SysUtils.getScreenHeight(ctx) - skipHeight) / gridHeight)));
		return result;
	}
	
	/**
	 * 获得屏幕像素数量。
	 * @param ctx
	 * @return
	 * @deprecated
	 */
	public static int getScreenPixels(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.widthPixels * dm.heightPixels;
	}
	
	/**
	 * 获取屏幕像素宽度
	 * @param ctx
	 * @return
	 * @deprecated
	 */
	public static int getScreenWidth(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕像素高度
	 * @param ctx
	 * @return
	 * @deprecated
	 */
	public static int getScreenHeight(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 保存全局设置
	 * 
	 * @param ctx
	 * @param name
	 * @param value
	 * @deprecated
	 */
	public static void saveGlobalSetting(Context ctx, String name, Object value) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);

		if (value instanceof Boolean) {
			setting.edit().putBoolean(name, (Boolean) value).commit();
		}
		else if (value instanceof Integer) {
			setting.edit().putInt(name, (Integer) value).commit();
		}
		else if (value instanceof Long) {
			setting.edit().putLong(name, (Long) value).commit();
		}
		else {
			setting.edit().putString(name, value.toString()).commit();
		}
	}

	/**
	 * Get global setting from system, return specified default value if not exists.
	 * 
	 * @param ctx
	 * @param name
	 * @param defaultValue
	 * @return
	 * @deprecated
	 */
	public static String getGlobalSetting(Context ctx, String name, Object defaultValue) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		if (defaultValue == null) {
			throw new RuntimeException("No default value provided.");
		}
		if (setting == null) {
			return defaultValue.toString();
		}
		try {
			if (setting.getString(name, defaultValue.toString()) == null) {
				return null;
			}
			else {
				return setting.getString(name, defaultValue.toString()).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue.toString();
		}
	}

	/**
	 * Get global setting.
	 * @param ctx
	 * @param name
	 * @return
	 * @deprecated
	 */
	public static String getGlobalSetting(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			if(setting.getString(name, null) == null) {
				return null;
			}
			else {
				return setting.getString(name, null).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取bool类型的设置，如果异常返回false
	 * @param ctx
	 * @param name
	 * @return
	 * @deprecated
	 */
	public static boolean getGlobalSettingBoolean(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getBoolean(name, false);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取bool类型的设置，如果异常返回指定的默认值。
	 * @param ctx
	 * @param name
	 * @param defaultValue
	 * @return
	 * @deprecated
	 */
	public static boolean getGlobalSettingBoolean(Context ctx, String name, boolean defaultValue) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getBoolean(name, defaultValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 *
	 * @deprecated
	 */
	public static int getGlobalSettingInt(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getInt(name, 0);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 *
	 * @deprecated
	 */
	public static long getGlobalSettingLong(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getLong(name, 0L);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param prefix
	 * @return
	 * @deprecated
	 */
	public static Map<String, Object> getGlobalSettingsWithPrefix(Context ctx, String prefix) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		Map<String, Object> result = new HashMap<String, Object>();
		Map m = setting.getAll();
		for (Object o : m.keySet()) {
			String key = (String) o;
			if (key.startsWith(prefix) && m.get(key) != null) {
				result.put(key, m.get(key));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param key
	 * @return
	 * @deprecated
	 */
	public static boolean removeGlobalSetting(Context ctx, String key) {
		Log.v("", "Try to remove setting: " + key);
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		return setting.edit().remove(key).commit();
	}

	/**
	 * 在通知栏显示通知消息通知消息
	 * @param context
	 * @param id Notification ID
	 * @param icon
	 * @param title
	 * @param msg
	 * @param activity 点击后调用的Activity
	 * @param sticky 是否常驻状态栏
	 */
	public static void showNotification_depreacted(Context context, int id, int icon,String title, String msg, Class activity, boolean sticky) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon,
				msg, System.currentTimeMillis());
		Intent notificationIntent = new Intent(context, activity);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, msg, contentIntent);
		if(sticky) {
			notification.flags = Notification.FLAG_ONGOING_EVENT;
		}
		notificationManager.notify(id, notification);
	}
	
	/**
	 * 通知栏
	 * @param context
	 * @param id Notification ID
	 * @param icon
	 * @param title
	 * @param msg
	 * @param clsActivity 点击后调用的Activity
	 * @param sticky 是否常驻状态栏
	 */
	public static void showNotification(Context context, int id, int icon, String title, String msg, Class clsActivity,
			boolean sticky) {
		Intent notificationIntent = new Intent(context, clsActivity);
		showNotification(context, id, icon, title, msg, notificationIntent, sticky);
	}
	
	/**
	 * 在通知栏显示一个可以跳转至Activity的通知消息。
	 * @param context
	 * @param id
	 * @param icon
	 * @param title
	 * @param msg
	 * @param notificationIntent
	 * @param sticky
	 */
	public static void showNotification(Context context, int id, int icon, String title, String msg,
			Intent notificationIntent, boolean sticky) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(msg)
				.setSmallIcon(icon).setContentIntent(contentIntent).build();

		if (sticky) {
			notification.flags = Notification.FLAG_ONGOING_EVENT;
		}
		nm.notify(id, notification);
	}
	
//	protected int findNotifiation() {
//		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.
//		return 0;
//	}
	
	
	/**
	 * 取消状态栏提示消息
	 * @param id
	 */
	public static void cancelNotification(Context context, int id) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}
	
	/**
	 * 显示一个长Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 * @param context
	 * @param msg
	 * @param params
	 */
	public static void showToast(Context context, String msg, Object... params) {
		// TODO
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 显示一个短Toast
	 * @param context
	 * @param msg
	 */
	public static void showToastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 检测SD卡是否可用。
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		File sdcard = Environment.getExternalStorageDirectory();
		return sdcard.exists() && sdcard.canWrite();
	}

	/**
	 * 获取联系人数量
	 * @param context
	 * @return
	 */
	public static int getContactsCount(Context context) {
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		return cursor.getCount();
	}
	
	/**
	 * 获取所有联系人的姓名。
	 * @param context
	 * @return
	 */
	public static List<String> getAllContactsNames(Context context) {
		List<String> ret = new ArrayList<String>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[]{PhoneLookup.DISPLAY_NAME};
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
		if (cursor == null) {
			return ret;
		}
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			if (!Utils.isEmpty(name)) {
				ret.add(name);
			}
		}
		return ret;
	}

	/**
	 * 从设备中获取所有拥有电话号码的联系人姓名。
	 * Read contacts names with phone number from device storage.
	 * @param context
	 * @return Map with phone number to contact name.
	 */
	public static Map<String, String> getContactsNamesFromDevice(Context context) {
		Map<String, String> ret = new HashMap<String, String>();
		ContentResolver cr = context.getContentResolver();
		// 先查所有联系人
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor == null) {
			return ret;
		}
		int idxName = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		while (cursor.moveToNext()) {
			String contact = cursor.getString(idxName);
			if (Utils.isEmpty(contact)) {
				continue;
			}
			Log.v("andex", "" + contact);

			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			// 第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤器
			Cursor phoneNumsCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					String.format("%s = %s", ContactsContract.CommonDataKinds.Phone.CONTACT_ID, contactId), null, null);
			if (phoneNumsCursor == null || phoneNumsCursor.isClosed()) {
				continue;
			}
			int idxNumber = phoneNumsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			while (phoneNumsCursor.moveToNext()) {
				String phoneNum = phoneNumsCursor.getString(idxNumber);
				ret.put(phoneNum.trim(), contact);
			}
			phoneNumsCursor.close();
		}
		cursor.close();
		return ret;
	}
	
	
	/**
	 * 取出并处理嵌入式的字符资源，嵌入格式: {编号}
	 * @param ctx
	 * @param sentence
	 * @param words 字符串值或者字符串资源ID可以混合使用
	 * @return
	 */
	public static String getNestedString(Context ctx, int sentence, Object... words) {
		Resources rs = ctx.getResources();
		String resource = rs.getString(sentence);
		if (Utils.isEmpty(resource)) {
			return "";
		}
		for (int i = 0; i < words.length; i++) {
			if (words[i] instanceof Integer) {
				resource = resource.replace("{" + i + "}", rs.getString((Integer) words[i]));
			}
			// else if(words[i] instanceof String) {
			else {
				resource = resource.replace("{" + i + "}", words[i].toString());
			}
		}
		return resource;
	}

	/**
	 *
	 * @return
	 */
	public static int getAPILevel() {
		return Build.VERSION.SDK_INT;
	}
	
}
