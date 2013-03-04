package andex;

import java.util.Map;

import org.andex.R;

import andex.model.DataList;
import andex.model.DataRow;
import andex.view.SimpleDialog;
import andex.view.SimpleDialog.DialogCallback;
import andex.view.TabsController;
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
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Gallery;
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
public abstract class BaseActivity extends Activity {

	public static final String SYS_PROP_DEBUG_MODE = "andex.debug";
	public static final String SYS_PROP_DB_VERSION = "andex.db.version";
	
	// Key of data in intent extra bundle.
	protected final String INTENT_DATA_ID_KEY = "INTENT_DATA_ID";
	protected final String INTENT_DATA_OPTION_KEY = "INTENT_DATA_OPTION_KEY"; 
	protected final String INTENT_DATA_ARGS_KEY = "INTENT_DATA_ARGS";
	protected final String INTENT_DATA_LIST_KEY = "INTENT_DATA_LIST";
	protected final String INTENT_DATA_ROW_KEY = "INTENT_DATA_ROW";
	
	protected final int REQUEST_CODE_DEFAULT = 1234;
	
	protected Activity thisActivity;

	protected Context context;
	
	// DEBUG模式（默认） changed by setting system arguments "andex.debug"
	protected boolean debugMode = true; 
	
	// Context resources(deprecated to rs)
	protected Resources rs;
	
	// Handler UI update
	protected final Handler handler = new Handler();
	
	// Simple Dialogs
	protected SimpleDialog simpleDialog;
	

	protected TabsController tabsController;
	
	// == 字符串资源 ==
	protected String tagOk = "OK";
	protected String tagCancel = "Cancel";
	protected String tagYes = "Yes";
	protected String tagNo = "No";
	protected String tagSave = "Save";
	protected String tagClose = "Close";
	
	protected DisplayMetrics dm;
	protected int sw;
	protected int sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		thisActivity = this;
		
		context = this;
		
		simpleDialog = new SimpleDialog(context);
		
		// 没设置参数的情况为true，只有明确设置不是debug模式的情况才是false。
		debugMode = !"false".equals(System.getProperty(SYS_PROP_DEBUG_MODE));
		
		rs = this.getResources();

		tagOk = rs.getString(android.R.string.ok);
		tagCancel = rs.getString(android.R.string.cancel);
		tagClose = rs.getString(R.string.common_close);
		tagYes = rs.getString(android.R.string.yes);
		tagNo = rs.getString(android.R.string.no);
		
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		sw = dm.widthPixels;
		sh = dm.heightPixels;
	}
	
	
	protected void setWindowFullscreenNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void startActivityByName(String actName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		ComponentName cn = new ComponentName(this.getClass().getPackage().getName(), actName);
		intent.setComponent(cn);
		startActivity(intent);
	}
	
	protected void startActivityWithoutTrace(Class clazz) {
		Intent intent = new Intent(context, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	protected void startActivity(Class clazz) {
		startActivity(clazz, false);
	}
	
	/**
	 * Simply start activity by it Class type.
	 * @param clazz
	 */
	protected void startActivity(Class clazz, boolean forResult) {
		if(forResult) {
			startActivityForResult(new Intent(context, clazz), REQUEST_CODE_DEFAULT);
		}
		else {
			startActivity(new Intent(context, clazz));
		}
	}

	/**
	 * Start activity with option ID that represent a selection from multi-options.
	 * @param clazz
	 * @param id
	 * @param forResult
	 */
	protected void startActivityWith(Class clazz, int id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}
	
	/**
	 * Start activity with biz ID that represent a data row's PK usually.
	 * use getIdFromPreActivity() to retrieve ID.
	 * @param clazz
	 * @param id
	 */
	protected void startActivityWith(Class clazz, long id, boolean forResult) {
		startActivityWith(clazz, id, null, forResult);	
	}
	
	/**
	 * Start activity with arguments.
	 * @param clazz
	 * @param args
	 * @param forResult
	 */
	protected void startActivityWith(Class clazz, Bundle args, boolean forResult) {
		startActivityWith(clazz, 0, args, forResult);	
	}
	
	/**
	 * Start activity with biz ID and arguments.
	 * use getIdFromPreActivity() to retrieve ID.
	 * use getArgsFromPreActivity() to retrieve arguments.
	 * @param clazz
	 * @param id Never be less than 0 or equal 0.
	 * @param args
	 */
	protected void startActivityWith(Class clazz, Object id, Bundle args, boolean forResult) {
		Intent intent = new Intent(context, clazz);
		if(id instanceof Integer) {
			intent.putExtra(INTENT_DATA_OPTION_KEY, (Integer)id);
		}
		else if(id instanceof Long) {
			intent.putExtra(INTENT_DATA_ID_KEY, (Long)id);
		}
		else if(id instanceof String){
			intent.putExtra(INTENT_DATA_ID_KEY, (String)id);
		}
		if (args != null)
			intent.putExtra(INTENT_DATA_ARGS_KEY, args);
		if(forResult) {
			startActivityForResult(intent, REQUEST_CODE_DEFAULT);
		}
		else {
			startActivity(intent);			
		}
	}
	
	protected void startActivityWith(Class clazz, DataList data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(INTENT_DATA_LIST_KEY, data);
		startActivity(intent);
	}
	
	protected void startActivityWith(Class clazz, Map data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("TEST", 999);
		intent.putExtra(INTENT_DATA_ROW_KEY, new DataRow(data));
		startActivity(intent);
	}
	
	protected void finishWithId(long id) {
		getIntent().getExtras().putLong(INTENT_DATA_ID_KEY, id);
		finish();
	}
	
	protected void finishWithData(DataRow row){
		finishWithData(row, null);
	}
	
	protected void finishWithData(DataRow row, Bundle args){
		Intent intent = new Intent();
//		debug("finishWithData() " + row.getClass());
		intent.putExtra(INTENT_DATA_ROW_KEY, row);
		intent.putExtra(INTENT_DATA_ARGS_KEY, args);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	/**
	 * Get ID from pre-activity
	 * @return >0
	 */
	protected long getIdFromPreActivity() {
		if (this.getIntent().getExtras() == null) {
			return 0;
		}
		Object v = this.getIntent().getExtras().get(INTENT_DATA_ID_KEY);
		if (v == null)
			return 0;
		return (Long) v;
	}
	
	protected String getIdStrFromPreActivity() {
		if (this.getIntent().getExtras() == null) {
			return "";
		}
		Object v = this.getIntent().getExtras().get(INTENT_DATA_ID_KEY);
		if (v == null)
			return "";
		return (String) v;
	}
	
	/**
	 * 
	 * @return
	 */
	protected int getOptionFromPreActivity() {
		if (this.getIntent().getExtras() == null) {
			return 0;
		}
		Object v = this.getIntent().getExtras().get(INTENT_DATA_OPTION_KEY);
		if (v == null)
			return 0;
		return (Integer) v;
	}
	
	protected String getArgStrFromPreActivity(String argName) {
		return (String)(getArgFromPreActivity(argName));
	}
	
	protected Object getArgFromPreActivity(String argName) {
		Bundle bundle = (Bundle)this.getIntent().getExtras().get(INTENT_DATA_ARGS_KEY);
		return bundle.get(argName);
	}
	
	protected DataList getDataListFromPreviousActivity() {
		throw new UnsupportedOperationException();
	}
	
	protected DataRow getDataRowFromPreviousActivity() {
		return (DataRow)this.getIntent().getSerializableExtra(INTENT_DATA_ROW_KEY);
//		throw new UnsupportedOperationException();
	}

	protected void showConfirmDialog(String msg, DialogCallback callback) {
		simpleDialog.showConfirmDialog(msg, callback);
	}

	protected void showProgressDialog(String msg, DialogCallback callback) {
		simpleDialog.showProgressDialog(msg, callback);
	}

	/**
	 * Show dialog that allows any text to input.
	 * @param title
	 * @param msg
	 * @param inputInit
	 * @param callback
	 * @return
	 */
	protected AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
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
	protected AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
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
	protected AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
		return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_DECIMAL, inputInit, callback);
	}

	protected AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
			final DialogCallback callback) {
		return simpleDialog.showRadioGroupDialog(title, msg, labels, checked, callback);
	}

	protected AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter,
			final DialogCallback callback) {
		return simpleDialog.showCheckBoxsDialog(title, checkboxListViewAdapter, callback);
	}

	protected void showInfoDialog(String msg) {
		simpleDialog.showInfoDialog(msg);
	}
	
	protected void showInfoDialog(String msg, DialogCallback callback) {
		simpleDialog.showInfoDialog(msg, callback);
	}

	protected void showListSelectDialog(String title, String[] items, DialogCallback callback) {
		simpleDialog.showListSelectDialog(title, items, callback);
	}

	public void dismissDialogOnTop() {
		simpleDialog.dismissDialogOnTop();
	}

	protected void showToast(String msg) {
		AndroidUtils.showToast(context, msg);
	}
	
	protected void showToast(String msg, Object... params) {
		// TODO
		
	}

	/**
	 * 获取LinearLayout
	 * @param resId
	 * @return
	 */
	protected LinearLayout getLinearyLayout(int resId) {
		return (LinearLayout)this.findViewById(resId);
	}
	
	protected RelativeLayout getRelativeLayout(int resId) {
		return (RelativeLayout)this.findViewById(resId);
	}
	
	protected FrameLayout getFrameLayout(int resId) {
		return (FrameLayout)this.findViewById(resId);
	}
	
	protected TableLayout getTableLayout(int resId) {
		return (TableLayout)this.findViewById(resId);
	}
	
	/**
	 * Get view by it's name which is defined in XML.
	 * @param name
	 * @return
	 */
	protected View getViewByName(String name) {
		int id = rs.getIdentifier(name, "id", getPackageName());
		if(id == 0) {
			return null;
		}
		return this.findViewById(id);
	}
	
	/**
	 * 
	 * @param resId
	 * @return
	 */
	protected TextView getTextView(int resId) {
		return (TextView)this.findViewById(resId);
	}
	
	protected TextView setTextViewText(int resId, String str) {
		TextView tv = this.getTextView(resId);
		if (tv != null) {
			tv.setText(str);
		}
		return tv;
	}
	
	/**
	 * 
	 * @param resId
	 * @return
	 */
	protected Button getButton(int resId) {
		return (Button)this.findViewById(resId);
	}
	
	protected CheckBox getCheckBox(int resId) {
		return (CheckBox)this.findViewById(resId);
	}
	
	protected EditText getEditText(int resId) {
		return (EditText)this.findViewById(resId);
	}
	
	protected String getEditTextString(int resId) {
		return getEditText(resId).getText().toString();
	}
	
	protected EditText setEditTextString(int resId, String str) {
		EditText et = getEditText(resId);
		if (et != null) {
			et.setText(str);
		}
		return et;
	}
	
//	protected int getEditTextInt(int resId) {
//		String str = getEditTextString(resId);
//		if(Utils.isEmpty(str)) {
//			return 0;
//		}
//		Integer.parseInt(str);
//	}
	
	protected Spinner getSpinner(int resId) {
		return (Spinner)this.findViewById(resId);
	}
	
	protected GridView getGridView(int resId) {
		return (GridView)this.findViewById(resId);
	}
	
	protected ListView getListView(int resId) {
		return (ListView)this.findViewById(resId);
	}
	
	protected ProgressBar getProgressBar(int resId) {
		return (ProgressBar)this.findViewById(resId);
	}
	
	protected RadioButton getRadioButton(int resId) {
		return (RadioButton)this.findViewById(resId);
	}
	
	protected RadioGroup getRadioGroup(int resId) {
		return (RadioGroup) this.findViewById(resId);
	}
	
	protected SeekBar getSeekBar(int resId) {
		return (SeekBar) this.findViewById(resId);
	}
	
	protected ToggleButton getToggleButton(int resId) {
		return (ToggleButton)this.findViewById(resId);
	}
	
	protected RatingBar getRatingBar(int resId) {
		return (RatingBar) this.findViewById(resId);
	}
	
	protected ExpandableListView getExpandableListView(int resId) {
		return (ExpandableListView)this.findViewById(resId);
	}
	
	protected ScrollView getScrollView(int resId) {
		return (ScrollView)this.findViewById(resId);
	}
	
	protected ImageView getImageView(int resId) {
		return (ImageView)this.findViewById(resId);
	}
	
	protected ImageButton getImageButton(int resId) {
		return (ImageButton) this.findViewById(resId);
	}
	
	protected Gallery getGallery(int resId) {
		return (Gallery)this.findViewById(resId);
	}

	
	/**
	 * 取出并处理嵌入式的字符资源，嵌入格式: {编号}
	 * @param sentence
	 * @param words 字符串值或者字符串资源ID可以混合使用
	 * @return
	 */
	protected String getNestedString(int sentence, Object... words){
		String resource = rs.getString(sentence);
		for (int i = 0; i < words.length; i++) {
			if(words[i] instanceof Integer) {
				resource = resource.replace("{" + i + "}", rs.getString((Integer)words[i]));	
			}
//			else if(words[i] instanceof String) {
			else{
				resource = resource.replace("{" + i + "}", words[i].toString());
			}
		}
		return resource;
	}
	
	/**
	 * Make views disabled by resource ids.
	 * @param ids
	 */
	protected void disableViews(int... ids) {
		for(int i=0;i<ids.length;i++) {
			findViewById(ids[i]).setEnabled(false);
		}
	}
	
	/**
	 * Make views disabled.
	 * @param views
	 */
	protected void disableViews(View... views) {
		for(int i=0;i<views.length;i++) {
			views[i].setEnabled(false);
		}
	}
	
	/**
	 * Make views enabled by resource ids.
	 * @param ids
	 */
	protected void enableViews(int... ids) {
		for(int i=0;i<ids.length;i++) {
			findViewById(ids[i]).setEnabled(true);
		}
	}
	
	/**
	 * Make view enabled.
	 * @param views
	 */
	protected void enableViews(View... views) {
		for(int i=0;i<views.length;i++) {
			views[i].setEnabled(true);
		}
	}
	

	/**
	 * Show progress bar if long time operation will be performed.
	 * resource "pgb_wait" is required
	 */
	protected void beforeLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if(wait == null) {
			warn("Not set waitting progress bar in XML layout file");
			return;
		}
		wait.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hide progress bar after long time operation.
	 * resource "pgb_wait" is required 
	 */
	protected void afterLoadingData(int resId) {
		ProgressBar wait = getProgressBar(resId);
		if(wait == null) return;
		wait.setVisibility(View.INVISIBLE);
	}
	
	
	/**
	 * Simple handle click event for any View component.
	 * @param resId
	 * @param handler
	 */
	protected View onViewClicked(int resId, final Callback handler) {
		final View view = this.findViewById(resId);
		if(view == null) {
			Log.d("android", "No view found：" + rs.getResourceName(resId));
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
	
	protected CheckBox onCheckBoxChecked(int resId, final OnCheckedChangeListener listener) {
		CheckBox ckb = getCheckBox(resId);
		ckb.setOnCheckedChangeListener(listener);
		return ckb;
	}

//	boolean isClickSound = false;
//	
//	public void enableDefaultClickSound() {
//		isClickSound = true;
//	}
	
	protected void debug(Object log) {
		if(log == null) log = "[null]";
		Log.d("andex", log.toString());
	}
	
	protected void warn(Object log) {
		if(log == null) log = "[null]";
		Log.w("andex", log.toString());		
	}
	
	protected void error(Object log) {
		if(log == null) log = "[null]";
		Log.e("andex", log.toString());
	}
	
	protected View inflatView(int viewId) {
		return LayoutInflater.from(context).inflate(viewId, null);
	}
	
	
	public void tileBackground(View view, int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(rs, resId);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		bd.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		bd.setDither(true);
		view.setBackgroundDrawable(bd);
	}
	
	
	public void mirrorBackground(View view, int resId) {
		Bitmap bmHead = BitmapFactory.decodeResource(rs, resId);
		BitmapDrawable bdHead = new BitmapDrawable(bmHead);
		bdHead.setTileModeXY(TileMode.MIRROR , TileMode.MIRROR);
		bdHead.setDither(true);		
		view.setBackgroundDrawable(bdHead);
	}

}
