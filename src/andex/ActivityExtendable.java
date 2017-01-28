package andex;

import andex.model.DataList;
import andex.model.DataRow;

public interface ActivityExtendable extends Extendable {

	/**
	 * 从前一Activity或Fragment获取ID。
	 *
	 * @return
	 */
	Object getIdObjectFromPrevious();

	int getIntIdFromPrevious();

	/**
	 * @return
	 */
	String getIdStrFromIntent();


	/**
	 * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
	 *
	 * @param argName
	 * @return
	 */
	int getArgIntFromIntent(String argName);

	/**
	 * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
	 *
	 * @param argName
	 * @return
	 */
	String getArgStrFromIntent(String argName);

	/**
	 * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
	 *
	 * @param argName
	 * @return
	 */
	Object getArgFromIntent(String argName);

	DataList getDataListFromIntent();

	DataRow getDataRowFromIntent();

	void showToast(String msg);

	void showToast(String msg, Object... params);


	/**
	 * Show progress bar if long time operation will be performed. resource "pgb_wait" is required
	 */
	void beforeLoadingData(int resId);

	/**
	 * Hide progress bar after long time operation. resource "pgb_wait" is required
	 */
	void afterLoadingData(int resId);
}
