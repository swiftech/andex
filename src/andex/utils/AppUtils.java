package andex.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Utils for App
 */
public class AppUtils {

	/**
	 * 分段获取应用程序的版本号（版本号必须由数字组成，否则会抛出异常）
	 * @param ctx
	 * @param packageName
	 * @return 版本数组（长度取决于版本信息）
	 */
	public static int[] getAppVersionName(Context ctx, String packageName) {
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
	public static String getAppVersionNameString(Context ctx, String packageName) {
		List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
		for (PackageInfo pi : pkgs) {
			if (pi.packageName.equals(packageName)) {
				return pi.versionName;
			}
		}
		return null;
	}

	/**
	 * 比较某个包所在的 APP 的版本与目标版本的差异。
	 * Compare target version with APP version.
	 * @param ctx
	 * @param packageName
	 * @param targetVersion
	 * @return Return 1 if target version higher than app version, return -1 if lower, return 0 if equals.
	 */
	public static int compareAppVersion(Context ctx, String packageName, String targetVersion) {
		int[] appVersion = getAppVersionName(ctx, packageName);
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
	 * 获取当前 App 的版本
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
	 * 获取当前 App 的版本码
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

}
