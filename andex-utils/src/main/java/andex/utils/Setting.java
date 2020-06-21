package andex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class Setting {

	/**
	 * 是否存在名为 name 的设置项。
	 *
	 * @param ctx
	 * @param name
	 * @return
	 */
	public static boolean hasGlobalSetting(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		return setting.contains(name);
	}

	/**
	 * 保存全局设置
	 *
	 * @param ctx
	 * @param name
	 * @param value
	 */
	public static void saveGlobalSetting(Context ctx, String name, Object value) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);

		if (value == null) {
			setting.edit().remove(name).apply();
		}
		else {
			if (value instanceof Boolean) {
				setting.edit().putBoolean(name, (Boolean) value).apply();
			}
			else if (value instanceof Integer) {
				setting.edit().putInt(name, (Integer) value).apply();
			}
			else if (value instanceof Long) {
				setting.edit().putLong(name, (Long) value).apply();
			}
			else {
				setting.edit().putString(name, value.toString()).apply();
			}
		}
	}

	/**
	 * Get global setting from system, return specified default value if not exists.
	 *
	 * @param ctx
	 * @param name
	 * @param defaultValue
	 * @return
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
				return Objects.requireNonNull(setting.getString(name, defaultValue.toString())).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue.toString();
		}
	}

	/**
	 * 获取全局设定，如果不存在则返回 null
	 *
	 * @param ctx
	 * @param name
	 * @return
	 */
	public static String getGlobalSetting(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			if (setting.getString(name, null) == null) {
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
	 *
	 * @param ctx
	 * @param name
	 * @return
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
	 *
	 * @param ctx
	 * @param name
	 * @param defaultValue
	 * @return
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

	public static int getGlobalSettingInt(Context ctx, String name) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getInt(name, 0);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getGlobalSettingInt(Context ctx, String name, int defaultValue) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		try {
			return setting.getInt(name, defaultValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

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
	 * @param ctx
	 * @param prefix
	 * @return
	 */
	public static Map<String, Object> getGlobalSettingsWithPrefix(Context ctx, String prefix) {
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		Map<String, Object> result = new HashMap<>();
		Map m = setting.getAll();
		if (m != null) {
			for (Object o : m.keySet()) {
				String key = (String) o;
				if (key.startsWith(prefix) && m.get(key) != null) {
					result.put(key, m.get(key));
				}
			}
		}
		return result;
	}

	/**
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static boolean removeGlobalSetting(Context ctx, String key) {
		Log.v("", "Try to remove setting: " + key);
		SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
		return setting.edit().remove(key).commit();
	}
}
