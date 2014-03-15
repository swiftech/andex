package andex.utils;

import android.content.Context;
import android.util.Log;

/**
 *
 */
public class StringUtils {

	/**
	 * 在字符串右侧添加指定数量的字符
	 *
	 * @param str
	 * @param count
	 * @param c
	 * @return
	 */
	public static String rightPad(String str, int count, char c) {
		char[] pad = new char[count];
		for (int i = 0; i < count; i++) {
			pad[i] = c;
		}
		return str.concat(new String(pad));
	}

	/**
	 * 根据屏幕大小，字体大小计算出一个合适的显示字符数量。
	 * 注意：重复调用可能性能不佳。
	 * @param ctx
	 * @param original
	 * @param headLen Head width in DP
	 * @param tailLen Tail width in DP
	 * @param fontSize Font size in PX
	 * @return
	 */
	public static String toFitableString(Context ctx, String original, int headLen, int tailLen, float fontSize) {
		float screenWidth = SysUtils.getScreenWidth(ctx);
		float screenWidthInDip = SysUtils.px2dip(ctx, screenWidth);
		int maxWordsCount = (int) ((screenWidthInDip - headLen - tailLen) / SysUtils.px2dip(ctx,fontSize));
		Log.v("", String.format("Max Words Count: %d", maxWordsCount));
		try {
			return org.apache.commons.lang3.StringUtils.abbreviate(original, maxWordsCount);
		} catch (IllegalArgumentException e) {
			return original;
		}
	}
}
