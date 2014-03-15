package andex.utils;

import java.util.Map;

/**
 *
 */
public class MapUtils {
	/**
	 * Return default string value if not exist.
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFrom(Map map, String key, String defaultValue) {
		return (org.apache.commons.lang3.StringUtils.isEmpty((CharSequence) map.get(key)) ? defaultValue : map.get(key).toString());
	}
}
