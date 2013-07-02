package andex;

import org.andex.R;

import android.content.Context;
import android.content.res.Resources;

public class i18n {
	// == 字符串资源 ==
	public static String tagOk = "OK";
	public static String tagCancel = "Cancel";
	public static String tagYes = "Yes";
	public static String tagNo = "No";
	public static String tagSave = "Save";
	public static String tagClose = "Close";
	
	public static void init(Context ctx) {
		Resources rs = ctx.getResources();

		tagOk = rs.getString(android.R.string.ok);
		tagCancel = rs.getString(android.R.string.cancel);
		tagClose = rs.getString(R.string.common_close);
		tagYes = rs.getString(R.string.common_yes);
		tagNo = rs.getString(R.string.common_no);
	}
}
