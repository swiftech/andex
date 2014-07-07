package andex.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 与网络相关的操作
 */
public class NetworkUtils {

	public static String DEFAULT_MAC_TEMPLATE = "%02X:%02X:%02X:%02X:%02X:%02X";

	public static String DEFAULT_UNAVAILABLE_MAC = "00:00:00:00:00:00";

	public static boolean isWgetStop = false;
	
	/**
	 * TODO Need re-arrange!
	 * @param downloadUrl
	 * @param localSavePath
	 * @param totalSize Download file size, unknown if <=0
	 * @return
	 */
	public static boolean wget(String downloadUrl, String localSavePath, long totalSize) {
		InputStream inputStream = null;
		FileOutputStream fileOutput = null;
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			File file = new File(localSavePath);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fileOutput = new FileOutputStream(file);
			inputStream = urlConnection.getInputStream();

			long downloadedSize = 0;
			long downloadSpeed = 0;
			float downloadPercent;
			byte[] buffer = new byte[4096];
			int bufferLength = 0;
			Log.v("wget()", "Download file size: " + totalSize);
			while ((bufferLength = inputStream.read(buffer)) > 0 && !isWgetStop) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				// Log.v(TAG, downloadedSize + " bytes have been downloaded.");
				downloadSpeed = downloadedSize;
				downloadPercent = ((downloadedSize * 100) / totalSize) > 100 ? 100
						: ((downloadedSize * 100) / totalSize);
			}

			// stop = !stop;
			// if (stop) {
			fileOutput.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutput.close();
				inputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Retrieve the first found local IP address(No 127.0.0.1)
	 * @return
	 */
	public static String getFirstLocalIPAddress() {
		Enumeration<NetworkInterface> enu = null;
		try {
			enu = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (enu == null) {
			System.out.println("No network interface found in this machine.");
			return null;
		}
		String appropriateIpAddress = null;
		while (enu.hasMoreElements()) {
			NetworkInterface ni = enu.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress ia = addresses.nextElement();
				// Skip loopback
				if (ia.isSiteLocalAddress() && !"127.0.0.1".equals(ia.getHostAddress())) {
					// Prefer 192.* and 10.*
					// if(ia.getHostAddress().startsWith("192") || ia.getHostAddress().startsWith("10")) {
					appropriateIpAddress = ia.getHostAddress();
					// }
				}
			}
		}
		if (org.apache.commons.lang3.StringUtils.isEmpty(appropriateIpAddress)) {
			appropriateIpAddress = "127.0.0.1";
		}
		return appropriateIpAddress;
	}
	
	public static String[] getAllLocalIPAddress() {
		List<String> ret = new ArrayList();
		Enumeration<NetworkInterface> enu = null;
		try {
			enu = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if(enu == null) {
			System.out.println("No network interface found in this machine.");
			return null;
		}
		String appropriateIpAddress = null;
		while(enu.hasMoreElements()) {
			NetworkInterface ni = enu.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while(addresses.hasMoreElements()) {
				InetAddress ia = addresses.nextElement();
				// Skip loopback
				if(ia.isSiteLocalAddress() && !"127.0.0.1".equals(ia.getHostAddress())) {
					// Prefer 192.* and 10.*
//					if(ia.getHostAddress().startsWith("192") || ia.getHostAddress().startsWith("10")) {
						ret.add(ia.getHostAddress());
//					}
				}
			}
		}
		return (String[])ret.toArray();
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
		if (ret.equals(DEFAULT_UNAVAILABLE_MAC)) {
			ret = getFirstMacAddress("eth", template);
			if (ret.equals(DEFAULT_UNAVAILABLE_MAC)) {
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
				return DEFAULT_UNAVAILABLE_MAC;
			}
			return macAddress;
		}
		return DEFAULT_UNAVAILABLE_MAC;
	}

	/**
	 * 获取第一个读取到的网卡的MAC地址（可能会有虚拟网卡，而不是真实的网卡地址）
	 * @return
	 */
	public static String getFirstMacAddress() {
		return getFirstMacAddress(null, DEFAULT_MAC_TEMPLATE);
	}

	public static String getFirstMacAddress(String namePrefix) {
		return getFirstMacAddress(namePrefix, DEFAULT_MAC_TEMPLATE);
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
				if (org.apache.commons.lang3.StringUtils.isEmpty(macTemplate)) {
					macTemplate = DEFAULT_MAC_TEMPLATE;
				}
				return String.format(macTemplate, mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return DEFAULT_UNAVAILABLE_MAC;
	}
	
}
