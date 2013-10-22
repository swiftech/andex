package andex;

import java.io.Serializable;
import java.util.Map;

import andex.model.DataList;
import andex.model.DataRow;
import andex.view.SimpleDialog.DialogCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public interface Extendable {

	/**
	 * 按照Activity类的名字启动（相同的classpath下面）
	 * 
	 * @param actName
	 */
	public void startActivityByName(String actName);

	/**
	 * 按照Activity的Class启动
	 * 
	 * @param clazz
	 */
	public void startActivityWithoutTrace(Class<? extends Activity> clazz);

	/**
	 * 按照Activity的Class启动
	 * 
	 * @param clazz
	 */
	public void startActivity(Class<? extends Activity> clazz);

	/**
	 * 按照Activity的Class启动
	 * 
	 * @param clazz
	 * @param forResult
	 */
	public void startActivity(Class<? extends Activity> clazz, boolean forResult);

	/**
	 * 启动Activity，附带选项ID，并监听返回。 Start activity with option ID that represent a selection from multi-options.
	 * 
	 * @param clazz
	 * @param id
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, int id, boolean forResult);

	/**
	 * Start activity with biz ID that represent a data row's PK usually. use getIdFromPreActivity() to retrieve ID.
	 * 
	 * @param clazz
	 * @param id
	 */
	public void startActivityWith(Class<? extends Activity> clazz, long id, boolean forResult);

	/**
	 * 启动一个Activity，无ID，附带一对键值参数。 在新Activity中用getArgFromPreActivity()方法获取参数值。
	 * 
	 * @param clazz
	 * @param key
	 * @param value
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, String key, Serializable value, boolean forResult);

	/**
	 * 启动一个Activity，附带ID和一对键值参数。 在新Activity中用getArgFromPreActivity()方法获取参数值。
	 * 
	 * @param clazz
	 * @param id
	 * @param key
	 * @param value
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, long id, String key, Serializable value,
			boolean forResult);

	/**
	 * Start activity with arguments.
	 * 
	 * @param clazz
	 * @param args
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, Bundle args, boolean forResult);

	/**
	 * Start activity with biz ID and arguments. use getIdFromPreActivity() to retrieve ID. use getArgsFromPreActivity()
	 * to retrieve arguments.
	 * 
	 * @param clazz
	 * @param id
	 *            Integer类型表示是选项，Long和String类型表示是ID，其他类型则为参数。
	 * @param args
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, Object id, Bundle args, boolean forResult);

	public void startActivityWith(Class<? extends Activity> clazz, DataList<?> data);

	public void startActivityWith(Class<? extends Activity> clazz, Map<?, ?> data);


	public void finishWithId(long id);

	public void finishWithData(DataRow row);

	public void finishWithData(DataRow row, Bundle args);

	public long getLongIdFromPrevious();


	public void showConfirmDialog(String msg, DialogCallback callback);

	public void showProgressDialog(String msg, DialogCallback callback);

	public void showProgressDialog(String msg, long timeout, DialogCallback callback);

	/**
	 * Show dialog that allows any text to input.
	 * 
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback);

	/**
	 * Show dialog that only allows integer number to input.
	 * 
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback);

	/**
	 * Show dialog that only allows float number to input.
	 * 
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback);

	public AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
			final DialogCallback callback);

	public AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter,
			final DialogCallback callback);

	public void showInfoDialog(int msgId);

	public void showInfoDialog(String msg);

	public void showInfoDialog(String msg, DialogCallback callback);

	public void showListSelectDialog(String title, String[] items, DialogCallback callback);

	public void showListSelectDialog(String title, Map items, DialogCallback callback);

	public void dismissDialogOnTop();


	/**
	 * 获取LinearLayout
	 * 
	 * @param resId
	 * @return
	 */
	public LinearLayout getLinearLayout(int resId);

	public RelativeLayout getRelativeLayout(int resId);

	public FrameLayout getFrameLayout(int resId);

	public TableLayout getTableLayout(int resId);

	/**
	 * Get view by it's name which is defined in XML.
	 * 
	 * @param name
	 * @return
	 */
	public View getViewByName(String name);

	public View setViewBackground(int viewResId, int bgResId);

	/**
	 * 
	 * @param resId
	 * @return
	 */
	public TextView getTextView(int resId);

	/**
	 * 设置指定资源ID的TextView的文本为指定文本资源ID
	 * 
	 * @param resId
	 *            TextView的资源ID
	 * @param strResId
	 *            需要设置文本的资源ID
	 * @return
	 */
	public TextView setTextViewText(int resId, int strResId);

	/**
	 * 设置指定资源ID的TextView的文本
	 * 
	 * @param resId
	 * @param str
	 * @return
	 */
	public TextView setTextViewText(int resId, String str);

	/**
	 * 设置指定资源ID的Button的文本
	 * 
	 * @param resId
	 * @param str
	 * @return
	 */
	public Button setButtonText(int resId, String str);

	/**
	 * 
	 * @param resId
	 * @return
	 */
	public Button getButton(int resId);

	public CheckBox getCheckBox(int resId);

	public EditText getEditText(int resId);

	public String getEditTextString(int resId);

	public EditText setEditTextString(int resId, String str);

	// public int getEditTextInt(int resId) {
	// String str = getEditTextString(resId);
	// if(Utils.isEmpty(str)) {
	// return 0;
	// }
	// Integer.parseInt(str);
	// }

	public Spinner getSpinner(int resId);

	public ViewGroup getViewGroup(int resId);
	
	public AbsListView getAbsListView(int resId);

	public GridView getGridView(int resId);

	public ListView getListView(int resId);

	public ProgressBar getProgressBar(int resId);

	public RadioButton getRadioButton(int resId);

	public RadioGroup getRadioGroup(int resId);

	public SeekBar getSeekBar(int resId);

	public ToggleButton getToggleButton(int resId);

	public RatingBar getRatingBar(int resId);

	public ExpandableListView getExpandableListView(int resId);

	public ScrollView getScrollView(int resId);

	public ImageView getImageView(int resId);

	public ImageButton getImageButton(int resId);

	public WebView getWebView(int resId);

	public SurfaceView getSurfaceView(int resId);

	/**
	 * 取出并处理嵌入式的字符资源，嵌入格式: {编号}
	 * 
	 * @param sentence
	 * @param words
	 *            字符串值或者字符串资源ID可以混合使用
	 * @return
	 */
	public String getNestedString(int sentence, Object... words);

	/**
	 * 显示多个视图组件
	 * 
	 * @param ids
	 */
	public void showViews(int... ids);

	/**
	 * 显示多个视图组件
	 * 
	 * @param views
	 */
	public void showViews(View... views);

	/**
	 * 隐藏多个视图组件
	 * 
	 * @param ids
	 */
	public void hideViews(int... ids);

	/**
	 * 隐藏多个视图组件
	 * 
	 * @param views
	 */
	public void hideViews(View... views);

	/**
	 * 暂时移除多个视图组件
	 * 
	 * @param ids
	 */
	public void unblockViews(int... ids);

	/**
	 * 暂时移多个视图组件
	 * 
	 * @param views
	 */
	public void unblockViews(View... views);

	/**
	 * Make views disabled by resource ids.
	 * 
	 * @param ids
	 */
	public void disableViews(int... ids);

	/**
	 * Make views disabled.
	 * 
	 * @param views
	 */
	public void disableViews(View... views);

	/**
	 * Make views enabled by resource ids.
	 * 
	 * @param ids
	 */
	public void enableViews(int... ids);

	/**
	 * Make view enabled.
	 * 
	 * @param views
	 */
	public void enableViews(View... views);


	/**
	 * Simple handle click event for any View component.
	 * 
	 * @param resId
	 * @param handler
	 */
	public View onViewClicked(int resId, final Callback handler);

	public CheckBox onCheckBoxChecked(int resId, final OnCheckedChangeListener listener);

	public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler);
}
