package andex;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert string to any data type safely(return default if exception)
 *
 * @author
 */
public class NumberConverter {

    protected static Map<Class, Converter> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(Integer.class, (Converter<Integer>) value -> {
            try {
                return Integer.parseInt(value.toString());
            } catch (Exception ex) {
                return 0;
            }
        });
        CONVERTERS.put(Long.class, (Converter<Long>) value -> {
            try {
                return Long.parseLong(value.toString());
            } catch (Exception ex) {
                return 0L;
            }
        });
        CONVERTERS.put(Float.class, (Converter<Float>) value -> {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception ex) {
                return 0.0f;
            }
        });
        CONVERTERS.put(Double.class, (Converter<Double>) value -> {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception ex) {
                return 0.0d;
            }
        });
    }

    public static Converter getConverter(Class clazz) {
        return CONVERTERS.get(clazz);
    }

    public static int convertInt(String str) {
        return (Integer) convert(Integer.class, str);
    }

    public static long convertLong(String str) {
        return (Long) convert(Long.class, str);
    }

    public static float convertFloat(String str) {
        return (Float) convert(Float.class, str);
    }

    public static double convertDouble(String str) {
        return (Double) convert(Double.class, str);
    }

    public static Object convert(Class clazz, String str) {
        return CONVERTERS.get(clazz).convert(str);
    }

    @FunctionalInterface
    public interface Converter<T> {
        T convert(Object value);
    }
}
