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
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
