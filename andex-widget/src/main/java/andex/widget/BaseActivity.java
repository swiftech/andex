package andex.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
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
import andex.mvc.BaseFlowActivity;
import andex.mvc.constant.FlowConstants;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;

/**
 * 提供常用功能的基础Activity类<td/>
 * 文字资源，Toast
 *
 * @author
 */
public abstract class BaseActivity extends BaseFlowActivity implements ActivityExtendable {


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

        // 使用 ActivityBuilder.fullscreen() 方法即可设置这个标志来设定是否要全屏显示
        if (getIntent().getExtras() != null) {
            boolean isFullscreen = getIntent().getExtras().getBoolean(FlowConstants.FLAG_FULL_SCREEN);
            if (isFullscreen) {
                setWindowFullscreenNoTitle();
            }
        }

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
     * @param dlgFrag
     * @return
     * @deprecated TODO 也用 builder模式实现
     */
    public BaseDialogFragment showDialogFragment(BaseDialogFragment dlgFrag) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        dlgFrag.show(ft, "andex");
        return dlgFrag;
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

    public void showProgressDialog(String msg, long timeout, DialogCallback callback) {
        simpleDialog.showProgressDialog(msg, timeout, callback);
    }

    /**
     * Show dialog that allows any text to input.
     *
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
     *
     * @param title
     * @param msg
     * @param inputInit
     * @param callback
     * @return
     */
    public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
        return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_SIGNED, inputInit, callback);
    }

    /**
     * Show dialog that only allows float number to input.
     *
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
        return simpleDialog.showCheckBoxesDialog(title, checkboxListViewAdapter, callback);
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
     * Get view by it's name which is defined in XML.
     *
     * @param name
     * @return
     */
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

    /**
     * @param resId
     * @return
     */
    public TextView getTextView(int resId) {
        return (TextView) this.findViewById(resId);
    }

    /**
     * 设置指定资源ID的TextView的文本为指定文本资源ID
     *
     * @param resId    TextView的资源ID
     * @param strResId 需要设置文本的资源ID
     * @return
     */
    public TextView setTextViewText(int resId, int strResId) {
        return this.setTextViewText(resId, rs.getString(strResId));
    }

    /**
     * 设置指定资源ID的TextView的文本
     *
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
     *
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
     * @param resId
     * @return
     */
    public Button getButton(int resId) {
        return (Button) this.findViewById(resId);
    }

    public EditText getEditText(int resId) {
        return (EditText) this.findViewById(resId);
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

    @Override
    public String getNestedString(int sentence, Object... words) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 显示多个视图组件
     *
     * @param ids
     */
    public void showViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示多个视图组件
     *
     * @param views
     */
    public void showViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏多个视图组件
     *
     * @param ids
     */
    public void hideViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏多个视图组件
     *
     * @param views
     */
    public void hideViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 暂时移除多个视图组件
     *
     * @param ids
     */
    public void unblockViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setVisibility(View.GONE);
        }
    }

    /**
     * 暂时移多个视图组件
     *
     * @param views
     */
    public void unblockViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Make views disabled by resource ids.
     *
     * @param ids
     */
    public void disableViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setEnabled(false);
        }
    }

    /**
     * Make views disabled.
     *
     * @param views
     */
    public void disableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(false);
        }
    }

    /**
     * Make views enabled by resource ids.
     *
     * @param ids
     */
    public void enableViews(int... ids) {
        for (int id : ids) {
            if (findViewById(id) == null) {
                continue;
            }
            findViewById(id).setEnabled(true);
        }
    }

    /**
     * Make view enabled.
     *
     * @param views
     */
    public void enableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(true);
        }
    }

    /**
     * Show progress bar if long time operation will be performed.
     * resource "pgb_wait" is required
     */
    public void beforeLoadingData(int resId) {
        ProgressBar wait = findViewById(resId);
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
        ProgressBar wait = findViewById(resId);
        if (wait == null) return;
        wait.setVisibility(View.INVISIBLE);
    }


    //	boolean isClickSound = false;
//	
//	public void enableDefaultClickSound() {
//		isClickSound = true;
//	}

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
     * 调试输出当前线程信息
     */
    protected void debugThread() {
        Log.d("andex", String.format("Thread: %d-%s", Thread.currentThread().getId(), Thread.currentThread().getName()));
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
        bdHead.setTileModeXY(TileMode.MIRROR, TileMode.MIRROR);
        bdHead.setDither(true);
        view.setBackgroundDrawable(bdHead);
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
