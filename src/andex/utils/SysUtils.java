package andex.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 系统相关的工具类。
 */
public class SysUtils {

	/**
	 * 获取设备的IMEI编号。
	 * 有些设备（比如Nexus 7）没有不能返回IMEI编号。
	 * @param ctx
	 * @return
	 */
	public static String getDeviceIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

	/**
	 * 有很多手机分辨率很大，所以不能依靠分辨率来判断，而是根据像素密度获得屏幕大小来判断。
	 * @return
	 */
	public static boolean isLargeScreen(Context context) {
		double screenSize = SysUtils.getScreenSizeInch(context);
		Log.d("", String.format("屏幕大小（英寸）: %f", screenSize));
		return screenSize>= 6.5f;
	}

	/**
	 * 获得屏幕的尺寸（英寸），如果无法获得屏幕的像素密度，则返回0.
	 * @param ctx
	 * @return
	 */
	public static double getScreenSizeInch(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		double diagonalInPixel = Math.sqrt(dm.widthPixels * dm.widthPixels + dm.heightPixels * dm.heightPixels);
		if (dm.density == 0) {
			return 0;
		}
		double ret = (double)(diagonalInPixel / dm.densityDpi);
		return ret;
	}

	/**
	 * 获得屏幕像素数量。
	 * @param ctx
	 * @return
	 */
	public static int getScreenPixels(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.widthPixels * dm.heightPixels;
	}

	/**
	 * 获取屏幕像素宽度
	 * @param ctx
	 * @return
	 */
	public static int getScreenWidth(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕像素高度
	 * @param ctx
	 * @return
	 */
	public static int getScreenHeight(Context ctx) {
		DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

}
