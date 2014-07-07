package andex;

import andex.utils.SysUtils;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

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
		for (PackageInfo pi : pkgs) {
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
	 * 获取指定 APP 的版本名称。
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static String getAppVersionString(Context ctx, String packageName) {
		List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
		for (PackageInfo pi : pkgs) {
			if (pi.packageName.equals(packageName)) {
				return pi.versionName;
			}
		}
		return null;
	}
	
	/**
	 * Version of current APP.
	 * TODO 与 getAppVersionName 重复
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
	 * 获取当前版本
	 * TODO 与 getAppVersion 重复
	 * @param ctx
	 * @return
	 */
	public static String getAppVersionName(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		if (packageManager != null) {
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
				return packageInfo.versionName;
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 获取当前版本
	 *
	 * @param ctx
	 * @return
	 */
	public static int getAppVersionCode(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		if (packageManager != null) {
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
				return packageInfo.versionCode;
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return 0;
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
	 * 在通知栏显示一个可以跳转至Activity的通知消息。
	 * @param context
	 * @param id
	 * @param icon
	 * @param title
	 * @param msg
	 * @param notificationIntent
	 * @param sticky
	 * @deprecated to NotificationBuilder
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
	 * 取出并处理嵌入式的字符资源，嵌入格式: {编号}
	 * @param ctx
	 * @param sentence
	 * @param words 字符串值或者字符串资源ID可以混合使用
	 * @return
	 */
	public static String getNestedString(Context ctx, int sentence, Object... words) {
		Resources rs = ctx.getResources();
		String resource = rs.getString(sentence);
		if (StringUtils.isEmpty(resource)) {
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
