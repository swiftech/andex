package andex;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import andex.Callback.CallbackAdapter;
import andex.model.DataList;
import andex.model.DataRow;
import andex.view.SimpleDialog;
import andex.view.SimpleDialog.DialogCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

/**
 * 提供常用功能的基础Activity类<td/>
 * 文字资源，Toast
 * 
 * @author 
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements ActivityExtendable{
	
//	protected static String LOG_TAG = "andex";
	
	//
	protected final int REQUEST_CODE_DEFAULT = 1000;
	
	protected Activity thisActivity;

	protected Context context;
	
	// DEBUG模式（默认） changed by setting system arguments "andex.debug"
	// @deprecated
//	protected boolean debugMode = true; 

	// Resources from context.
	protected Resources rs;
	
	// Handler UI update
	protected final Handler handler = new Handler();
	
	// Simple Dialogs
	protected SimpleDialog simpleDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		thisActivity = this;

		context = this;

		simpleDialog = new SimpleDialog(context);

		// 没设置参数的情况为true，只有明确设置不是debug模式的情况才是false。
		Constants.debugMode = !"false".equals(System.getProperty(Constants.SYS_PROP_DEBUG_MODE));

		i18n.init(context);

		rs = this.getResources();
	}

	/**
	 * 设置Activity为全屏无标题栏模式。
	 */
	protected void setWindowFullscreenNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	protected void setWindowFullscreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	protected FragmentTransaction beginFragmentTransaction() {
		return this.getSupportFragmentManager().beginTransaction();
	}

	/**
	 * 按照Activity类的名字启动（相同的classpath下面）
	 * @param actName
	 */
	public void startActivityByName(String actName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		ComponentName cn = new ComponentName(this.getClass().getPackage().getName(), actName);
		intent.setComponent(cn);
		startActivity(intent);
	}
	
	/**
	 * 按照Activity的Class启动
	 * @param clazz
	 */
	public void startActivityWithoutTrace(Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	/**
	 * 按照Activity的Class启动
	 * @param clazz
	 */
	public void startActivity(Class<? extends Activity> clazz) {
		startActivity(clazz, false);
	}
	
	/**
	 * 按照Activity的Class启动
	 * @param clazz
	 * @param forResult
	 */
	public void startActivity(Class<? extends Activity> clazz, boolean forResult) {
		if(forResult) {
			startActivityForResult(new Intent(context, clazz), REQUEST_CODE_DEFAULT);
		}
		else {
			startActivity(new Intent(context, clazz));
		}
	}

	/**
	 * 启动Activity，附带选项ID，并监听返回。
	 * Start activity with option ID that represent a selection from multi-options.
	 * @param clazz
	 * @param id
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, int id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}
	
	/**
	 * Start activity with biz ID that represent a data row's PK usually.
	 * use getIdFromPreActivity() to retrieve ID.
	 * @param clazz
	 * @param id
	 */
	public void startActivityWith(Class<? extends Activity> clazz, long id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}
	
	/**
	 * 启动一个Activity，无ID，附带一对键值参数。
	 * 在新Activity中用getArgFromPreActivity()方法获取参数值。
	 * @param clazz
	 * @param key
	 * @param value
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, String key, Serializable value, boolean forResult) {
		startActivityWith(clazz, -1L, key, value, forResult);
	}

	/**
	 * 启动一个Activity，附带ID和一对键值参数。
	 * 在新Activity中用getArgFromPreActivity()方法获取参数值。
	 * @param clazz
	 * @param id
	 * @param key
	 * @param value
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, long id, String key, Serializable value, boolean forResult) {
		Bundle args = new Bundle();
		args.putSerializable(key, value);
		startActivityWith(clazz, id, args, forResult);	
	}
	
	/**
	 * Start activity with arguments.
	 * @param clazz
	 * @param args
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, Bundle args, boolean forResult) {
		startActivityWith(clazz, 0, args, forResult);	
	}
	
	/**
	 * Start activity with biz ID and arguments.
	 * use getIdFromPreActivity() to retrieve ID.
	 * use getArgsFromPreActivity() to retrieve arguments.
	 * @param clazz
	 * @param id Integer类型表示是选项，Long和String类型表示是ID，其他类型则为参数。
	 * @param args
	 * @param forResult
	 */
	public void startActivityWith(Class<? extends Activity> clazz, Object id, Bundle args, boolean forResult) {
		Intent intent = new Intent(context, clazz);
		if(id instanceof Integer) {
			intent.putExtra(Constants.INTENT_DATA_ID_KEY, (Integer)id);
		}
		else if(id instanceof Long) {
			intent.putExtra(Constants.INTENT_DATA_ID_KEY, (Long)id);
		}
		else if(id instanceof String){
			intent.putExtra(Constants.INTENT_DATA_ID_KEY, (String)id);
		}
		if (args != null)
			intent.putExtra(Constants.INTENT_DATA_ARGS_KEY, args);
		if(forResult) {
			startActivityForResult(intent, REQUEST_CODE_DEFAULT);
		}
		else {
			startActivity(intent);			
		}
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, DataList<?> data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(Constants.INTENT_DATA_LIST_KEY, data);
		startActivity(intent);
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, Map<?, ?> data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("TEST", 999);
		intent.putExtra(Constants.INTENT_DATA_ROW_KEY, new DataRow(data));
		startActivity(intent);
	}
	
	
	/**
	 * 在资源ID指定的位置显示Fragment
	 * @param frag
	 * @param resId
	 * @return
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.replace(resId, frag);
		ft.commit();
		return frag;
	}
	
	/**
	 * 在资源ID指定的位置显示Fragment，附带一对键值参数。
	 * @param frag
	 * @param resId
	 * @param argKey
	 * @param argValue
	 * @return 
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId, String argKey, Serializable argValue) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		Bundle args = new Bundle();
		args.putSerializable(argKey, argValue);
		frag.setArguments(args);
		ft.replace(resId, frag);
		ft.commit();
		return frag;
	}
	
	/**
	 * 在资源ID指定的位置显示Fragment，附带Bundle参数。
	 * @param frag
	 * @param resId
	 * @param args 启动Fragment附带的参数列表，用getArguments()获取。
	 */
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId, Bundle args) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		frag.setArguments(args);
		ft.replace(resId, frag);
		ft.commit();
		return frag;
	}

	@Override
	public void finishWithId(long id) {
		getIntent().getExtras().putLong(Constants.INTENT_DATA_ID_KEY, id);
		finish();
	}
	
	public void finishWithData(DataRow row){
		finishWithData(row, null);
	}

	public void finishWithData(DataRow row, Bundle args){
		Intent intent = new Intent();
//		debug("finishWithData() " + row.getClass());
		intent.putExtra(Constants.INTENT_DATA_ROW_KEY, row);
		intent.putExtra(Constants.INTENT_DATA_ARGS_KEY, args);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public Object getIdObjectFromPrevious() {
		if (this.getIntent().getExtras() == null) {
			return 0L; //需要转换成Long
		}
		Object v = this.getIntent().getExtras().get(Constants.INTENT_DATA_ID_KEY);
		if (v == null)
			return 0L; //需要转换成Long
		return v;
	}
	
	public long getLongIdFromPrevious() {
		return (Long) getIdObjectFromPrevious();
	}

	@Override
	public int getIntIdFromPrevious() {
		return (Integer) getIdObjectFromPrevious();
	}

	@Override
	public String getIdStrFromIntent() {
		if (this.getIntent().getExtras() == null) {
			return "";
		}
		Object v = this.getIntent().getExtras().get(Constants.INTENT_DATA_ID_KEY);
		if (v == null)
			return "";
		return (String) v;
	}

	@Override
	public int getArgIntFromIntent(String argName) {
		Object o = getArgFromIntent(argName);
		if (o == null) {
			throw new RuntimeException(String.format("参数值%s不存在", argName));
		}
		return (Integer) o;
	}

	/**
	 * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
	 * 
	 * @param argName
	 * @return
	 */
	public String getArgStrFromIntent(String argName) {
		Object o = getArgFromIntent(argName);
		if (o == null) {
			Log.v("andex", String.format("参数值%s不存在", argName));
			return null;
//			throw new RuntimeException(String.format("参数值%s不存在", argName));
		}
		return (String) o;
	}
	
	/**
	 * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
	 * @param argName
	 * @return
	 */
	public Object getArgFromIntent(String argName) {
		Bundle extras = this.getIntent().getExtras();
		if(extras == null) {
			return null;
		}
		Bundle bundle = (Bundle)extras.get(Constants.INTENT_DATA_ARGS_KEY);
		if(bundle == null) {
			return null;
		}
		if (Constants.debugMode) {
			for (Iterator it = bundle.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Log.v("andex", String.format("  ARG: %s = %s", key, bundle.get(key.toString())));
			}
		}
		return bundle.get(argName);
	}

	@Override
	public DataList getDataListFromIntent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public DataRow getDataRowFromIntent() {
		return (DataRow)this.getIntent().getSerializableExtra(Constants.INTENT_DATA_ROW_KEY);
//		throw new UnsupportedOperationException();
	}

	@Override
	public void showConfirmDialog(String msg, DialogCallback callback) {
		simpleDialog.showConfirmDialog(msg, callback);
	}

	@Override
	public void showProgressDialog(String msg, DialogCallback callback) {
		simpleDialog.showProgressDialog(msg, 0, callback);
	}
	
	public void showProgressDialog(String msg, long timeout, DialogCallback callback) {
		simpleDialog.showProgressDialog(msg, timeout, callback);
	}

	/**
	 * Show dialog that allows any text to input.
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_CLASS_TEXT, inputInit, callback);
	}
	
	/**
	 * Show dialog that only allows integer number to input.
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_SIGNED ,inputInit, callback);
	}
	
	/**
	 * Show dialog that only allows float number to input.
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	public AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_DECIMAL, inputInit, callback);
	}

	@Override
	public AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
			final DialogCallback callback) {
		return simpleDialog.showRadioGroupDialog(title, msg, labels, checked, callback);
	}

	public AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter,
			final DialogCallback callback) {
		return simpleDialog.showCheckBoxsDialog(title, checkboxListViewAdapter, callback);
	}

	@Override
	public void showInfoDialog(int msgId) {
		simpleDialog.showInfoDialog(rs.getString(msgId));
	}	

	public void showInfoDialog(String msg) {
		simpleDialog.showInfoDialog(msg);
	}

	@Override
	public void showInfoDialog(String msg, DialogCallback callback) {
		simpleDialog.showInfoDialog(msg, callback);
	}

	@Override
	public void showListSelectDialog(String title, String[] items, DialogCallback callback) {
		simpleDialog.showListSelectDialog(title, items, callback);
	}

	@Override
	public void showListSelectDialog(String title, Map items, DialogCallback callback) {
		simpleDialog.showListSelectDialog(title, items, callback);
	}

	@Override
	public void dismissDialogOnTop() {
		simpleDialog.dismissDialogOnTop();
	}

	@Override
	public void showToast(String msg) {
		AndroidUtils.showToast(context, msg);
	}

	@Override
	public void showToast(String msg, Object... params) {
		// TODO
		
	}

	/**
	 * 获取LinearLayout
	 * @param resId
	 * @return
	 */
	public LinearLayout getLinearLayout(int resId) {
		return (LinearLayout)this.findViewById(resId);
	}

	@Override
	public RelativeLayout getRelativeLayout(int resId) {
		return (RelativeLayout)this.findViewById(resId);
	}

	@Override
	public FrameLayout getFrameLayout(int resId) {
		return (FrameLayout)this.findViewById(resId);
	}

	@Override
	public TableLayout getTableLayout(int resId) {
		return (TableLayout)this.findViewById(resId);
	}
	
	/**
	 * Get view by it's name which is defined in XML.
	 * @param name
	 * @return
	 */
	public View getViewByName(String name) {
		int id = rs.getIdentifier(name, "id", getPackageName());
		if(id == 0) {
			return null;
		}
		return this.findViewById(id);
	}

	@Override
	public View setViewBackground(int viewResId, int bgResId) {
		View v = this.findViewById(viewResId);
		v.setBackgroundResource(bgResId);
		return v;
	}
	
	/**
	 * 
	 * @param resId
	 * @return
	 */
	public TextView getTextView(int resId) {
		return (TextView)this.findViewById(resId);
	}
	
	/**
	 * 设置指定资源ID的TextView的文本为指定文本资源ID
	 * @param resId TextView的资源ID
	 * @param strResId 需要设置文本的资源ID
	 * @return
	 */
	public TextView setTextViewText(int resId, int strResId) {
		return this.setTextViewText(resId, rs.getString(strResId));
	}
	
	/**
	 * 设置指定资源ID的TextView的文本
	 * @param resId
	 * @param str
	 * @return
	 */
	public TextView setTextViewText(int resId, String str) {
		TextView tv = this.getTextView(resId);
		if (tv != null) {
			tv.setText(str);
		}
		return tv;
	}
	
	/**
	 * 设置指定资源ID的Button的文本
	 * @param resId
	 * @param str
	 * @return
	 */
	public Button setButtonText(int resId, String str) {
		Button btn = getButton(resId);
		btn.setText(str);
		return btn;
	}
	
	/**
	 * 
	 * @param resId
	 * @return
	 */
	public Button getButton(int resId) {
		return (Button)this.findViewById(resId);
	}
	
	public CheckBox getCheckBox(int resId) {
		return (CheckBox)this.findViewById(resId);
	}
	
	public EditText getEditText(int resId) {
		return (EditText)this.findViewById(resId);
	}
	
	public String getEditTextString(int resId) {
		return getEditText(resId).getText().toString();
	}
	
	public EditText setEditTextString(int resId, String str) {
		EditText et = getEditText(resId);
		if (et != null) {
			et.setText(str);
		}
		return et;
	}
	
//	public int getEditTextInt(int resId) {
//		String str = getEditTextString(resId);
//		if(Utils.isEmpty(str)) {
//			return 0;
//		}
//		Integer.parseInt(str);
//	}
	
	public Spinner getSpinner(int resId) {
		return (Spinner)this.findViewById(resId);
	}

	@Override
	public ViewGroup getViewGroup(int resId) {
		return (ViewGroup)this.findViewById(resId);
	}
	
	@Override
	public AbsListView getAbsListView(int resId) {
		return (AbsListView)this.findViewById(resId);
	}

	@Override
	public GridView getGridView(int resId) {
		return (GridView)this.findViewById(resId);
	}

	@Override
	public ListView getListView(int resId) {
		return (ListView)this.findViewById(resId);
	}

	@Override
	public ProgressBar getProgressBar(int resId) {
		return (ProgressBar)this.findViewById(resId);
	}

	@Override
	public RadioButton getRadioButton(int resId) {
		return (RadioButton)this.findViewById(resId);
	}

	@Override
	public RadioGroup getRadioGroup(int resId) {
		return (RadioGroup) this.findViewById(resId);
	}

	@Override
	public SeekBar getSeekBar(int resId) {
		return (SeekBar) this.findViewById(resId);
	}

	@Override
	public ToggleButton getToggleButton(int resId) {
		return (ToggleButton)this.findViewById(resId);
	}

	@Override
	public RatingBar getRatingBar(int resId) {
		return (RatingBar) this.findViewById(resId);
	}

	@Override
	public ExpandableListView getExpandableListView(int resId) {
		return (ExpandableListView)this.findViewById(resId);
	}

	@Override
	public ScrollView getScrollView(int resId) {
		return (ScrollView)this.findViewById(resId);
	}

	@Override
	public ImageView getImageView(int resId) {
		return (ImageView)this.findViewById(resId);
	}

	@Override
	public ImageButton getImageButton(int resId) {
		return (ImageButton) this.findViewById(resId);
	}

	@Override
	public WebView getWebView(int resId) {
		return (WebView) this.findViewById(resId);
	}

	@Override
	public SurfaceView getSurfaceView(int resId) {
		return (SurfaceView)this.findViewById(resId);
	}
	
	@Override
	public DrawerLayout getDrawerLayout(int resId) {
		return (DrawerLayout)this.findViewById(resId);
	}

	@Override
	public String getNestedString(int sentence, Object... words) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 显示多个视图组件
	 * @param ids
	 */
	public void showViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 显示多个视图组件
	 * @param views
	 */
	public void showViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏多个视图组件
	 * @param ids
	 */
	public void hideViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 隐藏多个视图组件
	 * @param views
	 */
	public void hideViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 暂时移除多个视图组件
	 * @param ids
	 */
	public void unblockViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.GONE);
		}
	}
	
	/**
	 * 暂时移多个视图组件
	 * @param views
	 */
	public void unblockViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.GONE);
		}
	}
	
	/**
	 * Make views disabled by resource ids.
	 * 
	 * @param ids
	 */
	public void disableViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setEnabled(false);
		}
	}

	/**
	 * Make views disabled.
	 * 
	 * @param views
	 */
	public void disableViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setEnabled(false);
		}
	}
	
	/**
	 * Make views enabled by resource ids.
	 * 
	 * @param ids
	 */
	public void enableViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setEnabled(true);
		}
	}

	/**
	 * Make view enabled.
	 * 
	 * @param views
	 */
	public void enableViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setEnabled(true);
		}
	}

	/**
	 * Show progress bar if long time operation will be performed. 
	 * resource "pgb_wait" is required
	 */
	public void beforeLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if (wait == null) {
			warn("Not set waitting progress bar in XML layout file");
			return;
		}
		wait.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hide progress bar after long time operation.
	 * resource "pgb_wait" is required 
	 */
	public void afterLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if(wait == null) return;
		wait.setVisibility(View.INVISIBLE);
	}
	
	
	/**
	 * Simple handle click event for any View component.
	 * @param resId
	 * @param handler
	 */
	public View onViewClicked(int resId, final Callback handler) {
		final View view = this.findViewById(resId);
		if(view == null) {
			Log.w("andex", "No view found：" + rs.getResourceName(resId));
			return view;
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.setEnabled(false);
				handler.invoke();
				handler.invoke(v);
				view.setEnabled(true);
			}
		});
		return view;
	}

	@Override
	public CheckBox onCheckBoxChecked(int resId, final OnCheckedChangeListener listener) {
		CheckBox ckb = getCheckBox(resId);
		ckb.setOnCheckedChangeListener(listener);
		return ckb;
	}

	@Override
	public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler) {
		final CompoundButton view = (CompoundButton) this.findViewById(resId);
		if(view == null) {
			Log.w("andex", "No view found：" + rs.getResourceName(resId));
			return view;
		}
		view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				view.setEnabled(false);
				handler.invoke(isChecked);
				view.setEnabled(true);
			}
		});
		return view;
	}

//	boolean isClickSound = false;
//	
//	public void enableDefaultClickSound() {
//		isClickSound = true;
//	}
	
	/**
	 * 调试输出
	 * @param log
	 */
	protected void debug(Object log) {
		if(log == null) log = "[null]";
		Log.d("andex", log.toString());
	}
	
	/**
	 * 警告输出
	 * @param log
	 */
	protected void warn(Object log) {
		if(log == null) log = "[null]";
		Log.w("andex", log.toString());		
	}
	
	/**
	 * 错误输出
	 * @param log
	 */
	protected void error(Object log) {
		if(log == null) log = "[null]";
		Log.e("andex", log.toString());
	}
	
	/**
	 * 调试输出当前线程信息
	 */
	protected void debugThread() {
		Log.d("andex", "Thread: " + Thread.currentThread().getId() + "-" + Thread.currentThread().getName());
	}
	
	protected View inflatView(int viewId) {
		return LayoutInflater.from(context).inflate(viewId, null);
	}
	
	
	protected void tileBackground(View view, int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(rs, resId);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		bd.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		bd.setDither(true);
		view.setBackgroundDrawable(bd);
	}
	
	
	protected void mirrorBackground(View view, int resId) {
		Bitmap bmHead = BitmapFactory.decodeResource(rs, resId);
		BitmapDrawable bdHead = new BitmapDrawable(bmHead);
		bdHead.setTileModeXY(TileMode.MIRROR , TileMode.MIRROR);
		bdHead.setDither(true);		
		view.setBackgroundDrawable(bdHead);
	}
	
	/**
	 * 简单的创建一个Bundle
	 * @param key
	 * @param value
	 * @return
	 */
	public static Bundle newBundle(String key, Serializable value) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(key, value);
		return bundle;
	}

	/** Prepared callback that just finish current activity */
	protected CallbackAdapter callbackFinish = new CallbackAdapter(){

		@Override
		public void invoke() {
			super.invoke();
			finish();
		}
		
	};
}
