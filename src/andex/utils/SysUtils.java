package andex.utils;

import andex.builder.TimeFormatBuilder;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import org.apache.commons.lang3.StringUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 系统相关的工具类。
 */
public class SysUtils {
	public static String DEFAULT_MAC_TEMPLATE = "%02X:%02X:%02X:%02X:%02X:%02X";

	private static String NO_MAC_ADDRESS = "00:00:00:00:00:00";

	/**
	 * 获取设备的IMEI编号。
	 * 有些设备（比如Nexus 7）没有不能返回IMEI编号。
	 * @param ctx
	 * @return
	 */
	public static String getDeviceIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
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
		return diagonalInPixel / dm.densityDpi;
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

	/**
	 * 根据手机的像素密度从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的像素密度从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取应用程序最近一次安装更新的时间。
	 * @param ctx
	 * @return
	 */
	public static long getAppLastUpdateTime(Context ctx) {
		try {
			PackageInfo pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			return pkgInfo.lastUpdateTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得APP编译的时间戳。
	 * @param ctx
	 * @return
	 */
	public static String getAppBuildTimestamp(Context ctx) {
		return TimeFormatBuilder.TIME_FORMAT_BUILDER_TIME_STAMP.format(new Date(getAppBuildTime(ctx)));
//		return Utils.formatToTimestamp(new Date(getAppBuildTime(ctx)));
	}

	/**
	 * 获得APP编译的时间。
	 * @param ctx
	 * @return
	 */
	public static long getAppBuildTime(Context ctx) {
		try {
			ApplicationInfo ai = ctx.getApplicationInfo();
			if (ai == null) {
				return 0;
			}
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
			return ze.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 播放系统默认铃声
	 * @param context
	 */
	public static void playSystemDefaultRingtone(Context context) {
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}


	/**
	 * 不管什么情况，总是能够获得MAC地址。
	 * @param ctx
	 * @return
	 */
	public static String getMacAddressSafely(Context ctx) {
		return getMacAddressSafely(ctx, DEFAULT_MAC_TEMPLATE);
	}

	/**
	 * 不管什么情况，总是能够获得MAC地址。
	 * @param ctx
	 * @param template
	 * @return
	 */
	public static String getMacAddressSafely(Context ctx, String template) {
		String ret;
		ret = getFirstMacAddress("wlan", template);
		if (ret.equals(NO_MAC_ADDRESS)) {
			ret = getFirstMacAddress("eth", template);
			if (ret.equals(NO_MAC_ADDRESS)) {
				ret = getMac(ctx);
			}
		}
		return ret;
	}

	/**
	 * 从WIFI管理器获得MAC地址（模拟器无效）
	 * @param ctx
	 * @return
	 */
	public static String getMac(Context ctx) {
		WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			String macAddress = info.getMacAddress();
			if (org.apache.commons.lang3.StringUtils.isEmpty(macAddress)) {
				return NO_MAC_ADDRESS;
			}
			return macAddress;
		}
		return NO_MAC_ADDRESS;
	}

	/**
	 * 获取名称符合前缀规则的第一个网卡MAC地址（在网络未开启的情况下无效）
	 * @param namePrefix 空则不做限定
	 * @param macTemplate 返回的MAC地址模板
	 * @return
	 */
	public static String getFirstMacAddress(String namePrefix, String macTemplate) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {

				Log.v("andex", String.format("MAC: Try %s with %s ", intf.getName(), namePrefix));

				// 排除非物理网卡
				if (intf.isLoopback() || intf.isPointToPoint() || intf.isVirtual()) {
					continue;
				}

				if (!org.apache.commons.lang3.StringUtils.isEmpty(namePrefix)) {
					if (!intf.getName().startsWith(namePrefix)) {
						continue;
					}
				}

				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					continue;
				if (StringUtils.isEmpty(macTemplate)) {
					macTemplate = DEFAULT_MAC_TEMPLATE;
				}
				return String.format(macTemplate, mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NO_MAC_ADDRESS;
	}

}
