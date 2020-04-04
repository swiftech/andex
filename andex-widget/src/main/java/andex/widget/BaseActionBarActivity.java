package andex.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

import andex.AndroidUtils;
import andex.Callback.CallbackAdapter;
import andex.Constants;
import andex.i18n;
import andex.mvc.BaseFlowActionBarActivity;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;

/**
 *
 */
public abstract class BaseActionBarActivity extends BaseFlowActionBarActivity implements ActivityExtendable {

    protected Activity thisActivity;

    // Handler UI update
    protected final Handler handler = new Handler();

    // Simple Dialogs
    protected SimpleDialog simpleDialog;

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
        return ((EditText) findViewById(resId)).getText().toString();
    }

    @Override
    public EditText setEditTextString(int resId, String str) {
        EditText et = findViewById(resId);
        if (et != null) {
            et.setText(str);
        }
        return et;
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
