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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
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

public abstract class BaseActionBarActivity extends ActionBarActivity implements ActivityExtendable {

	protected final int REQUEST_CODE_DEFAULT = 1000;
	
	protected Activity thisActivity;

	protected Context context;

	// Resources from context.
	protected Resources rs;

	// Handler UI update
	protected final Handler handler = new Handler();

	// Simple Dialogs
	protected SimpleDialog simpleDialog;
	
	// 
	protected ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		thisActivity = this;

		context = this;
		
		rs = this.getResources();

		simpleDialog = new SimpleDialog(context);

		// 没设置参数的情况为true，只有明确设置不是debug模式的情况才是false。
		Constants.debugMode = !"false".equals(System.getProperty(Constants.SYS_PROP_DEBUG_MODE));

		i18n.init(context);
		
		initLayout();

		actionBar = getSupportActionBar();
	}
	
	/**
	 * 返回layout文件的ID，用处是：
	 * <p>
	 * 1. 避免在子类中设置布局文件。
	 * <p>
	 * 2. 父类中某些初始化操作需要在设置布局文件后执行，而某些操作需要在设置之前执行。
	 * 
	 * @return
	 */
	protected abstract boolean initLayout();

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

	@Override
	public void startActivityByName(String actName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		ComponentName cn = new ComponentName(this.getClass().getPackage().getName(), actName);
		intent.setComponent(cn);
		startActivity(intent);
	}

	@Override
	public void startActivityWithoutTrace(Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void startActivity(Class<? extends Activity> clazz) {
		startActivity(clazz, false);
	}

	@Override
	public void startActivity(Class<? extends Activity> clazz, boolean forResult) {
		if(forResult) {
			startActivityForResult(new Intent(context, clazz), Constants.REQUEST_CODE_DEFAULT);
		}
		else {
			startActivity(new Intent(context, clazz));
		}
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, int id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, long id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, String key, Serializable value, boolean forResult) {
		startActivityWith(clazz, -1L, key, value, forResult);
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, long id, String key, Serializable value,
			boolean forResult) {
		Bundle args = new Bundle();
		args.putSerializable(key, value);
		startActivityWith(clazz, id, args, forResult);	
	}

	@Override
	public void startActivityWith(Class<? extends Activity> clazz, Bundle args, boolean forResult) {
		startActivityWith(clazz, 0, args, forResult);	
	}

	@Override
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
			startActivityForResult(intent,  Constants.REQUEST_CODE_DEFAULT);
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

	@Override
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.replace(resId, frag);
		ft.commit();
		return frag;
	}

	@Override
	public Basev4Fragment showFragment(Basev4Fragment frag, int resId, String argKey, Serializable argValue) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		Bundle args = new Bundle();
		args.putSerializable(argKey, argValue);
		frag.setArguments(args);
		ft.replace(resId, frag);
		ft.commit();
		return frag;
	}

	@Override
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

	@Override
	public void finishWithData(DataRow row) {
		finishWithData(row, null);
	}

	@Override
	public void finishWithData(DataRow row, Bundle args) {
		Intent intent = new Intent();
//		debug("finishWithData() " + row.getClass());
		intent.putExtra(Constants.INTENT_DATA_ROW_KEY, row);
		intent.putExtra(Constants.INTENT_DATA_ARGS_KEY, args);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public Object getIdObjectFromPrevious() {
		if (this.getIntent().getExtras() == null) {
			return 0L; //需要转换成Long
		}
		Object v = this.getIntent().getExtras().get(Constants.INTENT_DATA_ID_KEY);
		if (v == null)
			return 0L; //需要转换成Long
		return v;
	}

	@Override
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

	@Override
	public String getArgStrFromIntent(String argName) {
		Object o = getArgFromIntent(argName);
		if (o == null) {
			Log.v("andex", String.format("参数值%s不存在", argName));
			return null;
//			throw new RuntimeException(String.format("参数值%s不存在", argName));
		}
		return (String) o;
	}

	@Override
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

	@Override
	public void showProgressDialog(String msg, long timeout, DialogCallback callback) {
		simpleDialog.showProgressDialog(msg, timeout, callback);
	}

	@Override
	public AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_CLASS_TEXT, inputInit, callback);
	}

	@Override
	public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_SIGNED ,inputInit, callback);
	}

	@Override
	public AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_DECIMAL, inputInit, callback);
	}

	@Override
	public AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
			final DialogCallback callback) {
		return simpleDialog.showRadioGroupDialog(title, msg, labels, checked, callback);
	}

	@Override
	public AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter, DialogCallback callback) {
		return simpleDialog.showCheckBoxsDialog(title, checkboxListViewAdapter, callback);
	}

	@Override
	public void showInfoDialog(int msgId) {
		simpleDialog.showInfoDialog(rs.getString(msgId));
	}	

	@Override
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

	@Override
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

	@Override
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

	@Override
	public TextView getTextView(int resId) {
		return (TextView)this.findViewById(resId);
	}

	@Override
	public TextView setTextViewText(int resId, int strResId) {
		return this.setTextViewText(resId, rs.getString(strResId));
	}

	@Override
	public TextView setTextViewText(int resId, String str) {
		TextView tv = this.getTextView(resId);
		if (tv != null) {
			tv.setText(str);
		}
		return tv;
	}

	@Override
	public Button setButtonText(int resId, String str) {
		Button btn = getButton(resId);
		btn.setText(str);
		return btn;
	}

	@Override
	public Button getButton(int resId) {
		return (Button)this.findViewById(resId);
	}

	@Override
	public CheckBox getCheckBox(int resId) {
		return (CheckBox)this.findViewById(resId);
	}

	@Override
	public EditText getEditText(int resId) {
		return (EditText)this.findViewById(resId);
	}

	@Override
	public String getEditTextString(int resId) {
		return getEditText(resId).getText().toString();
	}

	@Override
	public EditText setEditTextString(int resId, String str) {
		EditText et = getEditText(resId);
		if (et != null) {
			et.setText(str);
		}
		return et;
	}

	@Override
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

	public DrawerLayout getDrawerLayout(int resId) {
		return (DrawerLayout)this.findViewById(resId);
	}
	
	@Override
	public String getNestedString(int sentence, Object... words) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void hideViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void unblockViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setVisibility(View.GONE);
		}
	}

	@Override
	public void unblockViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setVisibility(View.GONE);
		}
	}

	@Override
	public void disableViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setEnabled(false);
		}
	}

	@Override
	public void disableViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setEnabled(false);
		}
	}

	@Override
	public void enableViews(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (findViewById(ids[i]) == null) {
				continue;
			}
			findViewById(ids[i]).setEnabled(true);
		}
	}

	@Override
	public void enableViews(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] == null) {
				continue;
			}
			views[i].setEnabled(true);
		}
	}

	@Override
	public void beforeLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if (wait == null) {
			Log.w("andex", "Not set waitting progress bar in XML layout file");
			return;
		}
		wait.setVisibility(View.VISIBLE);
	}

	@Override
	public void afterLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if(wait == null) return;
		wait.setVisibility(View.INVISIBLE);
	}

	@Override
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
