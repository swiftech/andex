package andex;

import java.text.SimpleDateFormat;
import java.util.*;


public class Utils {

    /**
     * @deprecated
     */
    public static SimpleDateFormat TIME_FORMATTER_DATA = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @deprecated TimeFormatBuilder.
     */
    public static SimpleDateFormat TIME_FORMATTER_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String quaterLogicStr(Object value, Object op0, String ret0, Object op1, String ret1) {
        return (String) quaterLogic(value, op0, ret0, op1, ret1);
    }

    public static Object quaterLogic(Object value, Object op0, Object ret0, Object op1, Object ret1) {
        return quaterLogic(value.equals(op0), ret0, value.equals(op1), ret1);
    }

//	public static <TI, TO> Object quaterLogic(TI value, TI op0, TO ret0, TI op1, TO ret1) {
//		return quaterLogic(value.equals(op0), ret0, value.equals(op1), ret1);
//	}

    public static String quaterLogicStr(boolean firstCond, String firstResult, boolean secondCond, String secondResult) {
        return (String) quaterLogic(firstCond, firstResult, secondCond, secondResult);
    }

    /**
     * Just used for simplify code to be less if - else statements.
     *
     * @param firstCond
     * @param firstResult
     * @param secondCond
     * @param secondResult
     * @return
     */
    public static Object quaterLogic(boolean firstCond, Object firstResult, boolean secondCond, Object secondResult) {
        if (firstCond) {
            return firstResult;
        } else if (secondCond) {
            return secondResult;
        } else {
            return "";
        }
    }


//	public static boolean isEmpty(Object str) {
//		return str == null || str.toString().length() == 0;
//	}
//
//	public static boolean isEmpty(String str) {
//		return str == null || str.length() == 0;
//	}

    /**
     * Convert key-value arrays to map.
     *
     * @param keys
     * @param values
     * @return
     */
    public static Map arrays2map(String[] keys, Object[] values) {
        if (keys == null || values == null || keys.length != values.length) {
            return null;
        }
        Map m = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            m.put(keys[i], values[i]);
        }
        return m;
    }

    public static Object[] append(Object[] arr, Object... newEl) {
        Object[] ret = new Object[arr.length + newEl.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        System.arraycopy(newEl, 0, ret, arr.length, newEl.length);
        return ret;
    }

    /**
     * @param date
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String stringifyDate(Calendar date) {
        return TIME_FORMATTER_DATA.format(date.getTime());
    }

    /**
     * @param date
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String stringifyDate(Date date) {
        return TIME_FORMATTER_DATA.format(date);
    }

    /**
     * @param time
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String stringifyTime(Date time) {
        return TIME_FORMATTER_TIME.format(time);
    }

    /**
     * @param time
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String stringifyTime(Calendar time) {
        return TIME_FORMATTER_TIME.format(time.getTime());
    }

    /**
     * @param time
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String stringifyTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return TIME_FORMATTER_TIME.format(cal.getTime());
    }

    /**
     * @param time
     * @return
     * @deprecated to TimeFormatBuilder
     */
    public static String formatToTimestamp(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_HHmm", Locale.CHINA);
        return sdf.format(time.getTime());
    }

    /**
     * 当前线程休眠一段时间
     *
     * @param minutes
     */
    public static void sleepFor(int minutes) {
        try {
            Thread.sleep(minutes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Class getClass(Object target) {
        if (target == null) {
            return null;
        }
        return target.getClass();
    }

    /**
     * 获得一个对象的类名
     *
     * @param target
     * @return
     */
    public static String getClassName(Object target) {
        if (target == null) {
            return null;
        }
        return ((Object) target).getClass().getName();
    }

}
