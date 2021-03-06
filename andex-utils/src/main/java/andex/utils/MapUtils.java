package andex.utils;

import java.util.*;

/**
 *
 */
public class MapUtils {

	/**
	 * Return default string value if not exist.
	 *
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFrom(Map map, String key, String defaultValue) {
		return (org.apache.commons.lang3.StringUtils.isEmpty((CharSequence) map.get(key)) ? defaultValue : map.get(key).toString());
	}

	/**
	 * 按照值的大小排序一个Map
	 *
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
