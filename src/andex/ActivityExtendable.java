package andex;

import java.io.Serializable;

import andex.model.DataList;
import andex.model.DataRow;
import android.os.Bundle;

public interface ActivityExtendable extends Extendable {

	/**
	 * 在资源ID指定的位置显示Fragment
	 * 
	 * @param frag
	 * @param resId
	 * @return
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId);

	/**
	 * 在资源ID指定的位置显示Fragment，附带一对键值参数。
	 * 
	 * @param frag
	 * @param resId
	 * @param argKey
	 * @param argValue
	 * @return
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId, String argKey, Serializable argValue);

	/**
	 * 在资源ID指定的位置显示Fragment，附带Bundle参数。
	 * 
	 * @param frag
	 * @param resId
	 * @param args
	 *            启动Fragment附带的参数列表，用getArguments()获取。
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId, Bundle args);
	
	
	/**
	 * 从前一Activity或Fragment获取ID。
	 * @return
	 */
	public Object getIdObjectFromPrevious();

	public int getIntIdFromPrevious();

	/**
	 * 
	 * @return
	 */
	public String getIdStrFromIntent();

	public DataList getDataListFromIntent();

	public DataRow getDataRowFromIntent();

	public void showToast(String msg);

	public void showToast(String msg, Object... params);
	

	/**
	 * Show progress bar if long time operation will be performed. resource "pgb_wait" is required
	 */
	public void beforeLoadingData(int resId);

	/**
	 * Hide progress bar after long time operation. resource "pgb_wait" is required
	 */
	public void afterLoadingData(int resId);
}
