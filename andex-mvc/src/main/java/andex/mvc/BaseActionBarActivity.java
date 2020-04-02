package andex.mvc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

import andex.AndroidUtils;
import andex.Callback;
import andex.Callback.CallbackAdapter;
import andex.Constants;
import andex.i18n;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;
import andex.mvc.controller.ActivityBuilder;
import andex.mvc.controller.FragmentBuilder;
import andex.mvc.view.SimpleDialog;
import andex.mvc.view.DialogCallback;

/**
 *
 */
public abstract class BaseActionBarActivity extends AppCompatActivity implements ActivityExtendable {

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

        // 在某设备的4.0版本系统上会抛出异常，此处无法解决问题
        try {
            actionBar = getSupportActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
            AndroidUtils.showToast(context, "发生严重错误，即将退出: " + ex.getLocalizedMessage());
        }

    }

//
//	/**
//	 * 切换 ActionBar 上面的菜单按钮的可用状态
//	 * @param resId
//	 */
//	public void toggleMenuItem(int resId) {
//		if (actionBar == null) {
//			Log.i(LOG_TAG, "CAB mode was never started to dismiss");
//			return;
//		}
//		if (actionMode.getMenu() == null) {
//			Log.i(LOG_TAG, "No menu found to toggle");
//			return;
//		}
//		MenuItem item = .getMenu().getItem(resId);
//		item.setEnabled(!item.isEnabled());
//	}
//
//	public void disableMenuItem(int resId) {
//		if (actionMode.getMenu() == null) {
//			Log.i(LOG_TAG, "No menu found to toggle");
//			return;
//		}
//		MenuItem item = actionMode.getMenu().getItem(resId);
//		item.setEnabled(false);
//	}
//
//	public void enableMenuItem(int resId) {
//		if (actionMode.getMenu() == null) {
//			Log.i(LOG_TAG, "No menu found to toggle");
//			return;
//		}
//		MenuItem item = actionMode.getMenu().getItem(resId);
//		item.setEnabled(true);
//	}


    /**
     * 执行布局的初始化操作，用处是：
     * <p>
     * 1. 避免在子类中设置布局文件。
     * <p>
     * 2. 父类中某些初始化操作需要在设置布局文件后执行，而某些操作需要在设置之前执行。
     *
     * @return 初始化成功与否（目前不做处理）
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

    /**
     * {@inheritDoc}
     *
     * @param clazz
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(clazz, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param clazz
     * @param forResult
     */
    public void startActivity(Class<? extends Activity> clazz, boolean forResult) {
        if (forResult) {
            startActivityForResult(new Intent(context, clazz), Constants.REQUEST_CODE_DEFAULT);
        } else {
            startActivity(new Intent(context, clazz));
        }
    }

    /**
     * 构建一个 Activity 构建器。
     *
     * @param activityClass
     * @return
     */
    public ActivityBuilder buildActivity(Class activityClass) {
        return new ActivityBuilder(context, activityClass);
    }

    /**
     * 创建跳转至指定 Fragment 的构造器。
     *
     * @param fragment
     * @param resId
     * @return
     */
    public FragmentBuilder buildFragment(Basev4Fragment fragment, int resId) {
        return new FragmentBuilder(this, fragment).replace(resId);
    }

    /**
     * @param id
     */
    @Override
    public void finishWithId(long id) {
        getIntent().getExtras().putLong(Constants.INTENT_DATA_ID_KEY, id);
        finish();
    }

    @Override
    public void finishWithData(DataRow row) {
        finishWithData(row, null);
    }

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
        if (extras == null) {
            return null;
        }
        Bundle bundle = (Bundle) extras.get(Constants.INTENT_DATA_ARGS_KEY);
        if (bundle == null) {
            return null;
        }
        if (Constants.debugMode) {
            for (String key : bundle.keySet()) {
                Log.v("andex", String.format("  ARG: %s = %s", key, bundle.get(key)));
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
        return (DataRow) this.getIntent().getSerializableExtra(Constants.INTENT_DATA_ROW_KEY);
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
        return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_SIGNED, inputInit, callback);
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
        return simpleDialog.showCheckBoxesDialog(title, checkboxListViewAdapter, callback);
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
    public View getViewByName(String name) {
        int id = rs.getIdentifier(name, "id", getPackageName());
        if (id == 0) {
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
    public TextView setTextViewText(int resId, int strResId) {
        return this.setTextViewText(resId, rs.getString(strResId));
    }

    @Override
    public TextView setTextViewText(int resId, String str) {
        TextView tv = this.findViewById(resId);
        if (tv != null) {
            tv.setText(str);
        }
        return tv;
    }

    @Override
    public Button setButtonText(int resId, String str) {
        Button btn = findViewById(resId);
        btn.setText(str);
        return btn;
    }


    @Override
    public String getEditTextString(int resId) {
        return ((EditText)findViewById(resId)).getText().toString();
    }

    @Override
    public EditText setEditTextString(int resId, String str) {
        EditText et = findViewById(resId);
        if (et != null) {
            et.setText(str);
        }
        return et;
    }

    public DrawerLayout getDrawerLayout(int resId) {
        return (DrawerLayout) this.findViewById(resId);
    }

    public ViewPager getViewPager(int resId) {
        return (ViewPager) this.findViewById(resId);
    }

    public DatePicker getDatePicker(int resId) {
        return (DatePicker) this.findViewById(resId);
    }

    @Override
    public String getNestedString(int sentence, Object... words) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void showViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void unblockViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.GONE);
        }
    }

    @Override
    public void unblockViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void disableViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setEnabled(false);
        }
    }

    @Override
    public void disableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(false);
        }
    }

    @Override
    public void enableViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setEnabled(true);
        }
    }

    @Override
    public void enableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(true);
        }
    }

    @Override
    public void beforeLoadingData(int resId) {
        ProgressBar wait = findViewById(resId);
        if (wait == null) {
            Log.w("andex", "Not set waitting progress bar in XML layout file");
            return;
        }
        wait.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterLoadingData(int resId) {
        ProgressBar wait = findViewById(resId);
        if (wait == null) return;
        wait.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onViewClicked(int resId, final Callback handler) {
        final View view = this.findViewById(resId);
        if (view == null) {
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
        CheckBox ckb = findViewById(resId);
        ckb.setOnCheckedChangeListener(listener);
        return ckb;
    }

    @Override
    public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler) {
        final CompoundButton view = (CompoundButton) this.findViewById(resId);
        if (view == null) {
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
     *
     * @param log
     */
    protected void debug(Object log) {
        if (log == null) log = "[null]";
        Log.d("andex", log.toString());
    }

    /**
     * 警告输出
     *
     * @param log
     */
    protected void warn(Object log) {
        if (log == null) log = "[null]";
        Log.w("andex", log.toString());
    }

    /**
     * 错误输出
     *
     * @param log
     */
    protected void error(Object log) {
        if (log == null) log = "[null]";
        Log.e("andex", log.toString());
    }

    /**
     * 简单的创建一个Bundle
     *
     * @param key
     * @param value
     * @return
     */
    public static Bundle newBundle(String key, Serializable value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, value);
        return bundle;
    }


    /**
     * Prepared callback that just finish current activity
     */
    protected CallbackAdapter callbackFinish = new CallbackAdapter() {

        @Override
        public void invoke() {
            super.invoke();
            finish();
        }

    };
}
