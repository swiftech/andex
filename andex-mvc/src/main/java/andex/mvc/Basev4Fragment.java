package andex.mvc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

import andex.AndroidUtils;
import andex.Callback;
import andex.Constants;
import andex.mvc.model.DataRow;
import andex.mvc.controller.ActivityBuilder;
import andex.mvc.controller.FragmentBuilder;
import andex.mvc.controller.ResultBuilder;
import andex.mvc.view.SimpleDialog;
import andex.mvc.view.DialogCallback;

/**
 * 扩展的基础Fragment类。
 * 注意：必须在使用前通过构造函数注入、或者onCreate()方法中设置布局资源ID。
 */
public abstract class Basev4Fragment<T extends FragmentActivity> extends Fragment implements Extendable {

    //
    public final int REQUEST_CODE_DEFAULT = 1000;

    // 从参数中获取标题
    public static final String ARG_TITLE = "arg_title";

    /**
     * 当前Fragment的引用
     */
    public Fragment thisFragment;

    /**
     * 前一个Fragment（目前只有需要返回值的情况下才有）
     */
    public Basev4Fragment previousFragment;

    /**
     * 当前Fragment所属的Activity
     */
    public T parentActivity;


    public Context context;

    /**
     * Resources from context.
     */
    public Resources rs;

    /**
     * Handler UI update
     */
    public final Handler handler = new Handler();

    /**
     * Simple Dialogs
     */
    public SimpleDialog simpleDialog;

    /**
     * View of Fragment
     */
    public View fragmentView;

    /**
     * Resource id for this fragment view.
     */
    public int layoutResourceId = 0;


    /**
     * Title of this fragment
     */
    private String title;

    public Basev4Fragment() {
        super();
        this.thisFragment = this;
    }

    public Basev4Fragment(int resourceId) {
        super();
        if (resourceId == 0) {
            throw new IllegalArgumentException(String.format("Layout resource ID is not available: %d, you may implement the getLayoutResourceId() method", layoutResourceId));
        }
        this.thisFragment = this;
        this.layoutResourceId = resourceId;
    }

    public int getLayoutResourceId() {
        return this.layoutResourceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.layoutResourceId = getLayoutResourceId();

        this.context = this.getActivity();
        this.parentActivity = (T) this.getActivity();
        this.rs = context.getResources();
        this.simpleDialog = new SimpleDialog(context);

        // 默认情况下Fragment的菜单都会加到Activity上。
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 此处如果不指定container，可能会出现子布局无法扩张到整个区域的问题
        if (layoutResourceId == 0) {
            throw new IllegalArgumentException(String.format("Layout Resource ID is not available: %d", layoutResourceId));
        }
        Object view = inflater.inflate(layoutResourceId, container, false);
        if (view == null) {
            throw new RuntimeException("可能是没有设置layoutResourceId");
        }
        fragmentView = (View) view;
        return fragmentView;
    }

    /**
     * 设置指定资源ID的TextView的文本为指定文本资源ID
     *
     * @param resId    TextView的资源ID
     * @param strResId 需要设置文本的资源ID
     * @return
     */
    @Override
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
    @Override
    public TextView setTextViewText(int resId, String str) {
        TextView tv = fragmentView.findViewById(resId);
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
    @Override
    public Button setButtonText(int resId, String str) {
        Button btn = fragmentView.findViewById(resId);
        btn.setText(str);
        return btn;
    }

    @Override
    public String getEditTextString(int resId) {
        return ((EditText)fragmentView.findViewById(resId)).getText().toString();
    }

    @Override
    public EditText setEditTextString(int resId, String str) {
        EditText et = fragmentView.findViewById(resId);
        if (et != null) {
            et.setText(str);
        }
        return et;
    }


    public ViewPager getViewPager(int resId) {
        return (ViewPager) fragmentView.findViewById(resId);
    }

    public DatePicker getDatePicker(int resId) {
        return (DatePicker) fragmentView.findViewById(resId);
    }

    @Override
    public String getNestedString(int sentence, Object... words) {
        return AndroidUtils.getNestedString(context, sentence, words);
    }

    /**
     * Make views disabled by resource ids.
     *
     * @param ids
     */
    @Override
    public void disableViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setEnabled(false);
        }
    }

    /**
     * Make views disabled.
     *
     * @param views
     */
    @Override
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
    @Override
    public void enableViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setEnabled(true);
        }
    }

    /**
     * Make view enabled.
     *
     * @param views
     */
    @Override
    public void enableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(true);
        }
    }

    /**
     * 显示多个视图组件
     *
     * @param ids
     */
    @Override
    public void showViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示多个视图组件
     *
     * @param views
     */
    @Override
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
    @Override
    public void hideViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏多个视图组件
     *
     * @param views
     */
    @Override
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
    @Override
    public void unblockViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.GONE);
        }
    }

    /**
     * 暂时移多个视图组件
     *
     * @param views
     */
    @Override
    public void unblockViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.GONE);
        }
    }


    /**
     * Get view by it's name which is defined in XML.
     *
     * @param name
     * @return
     */
    @Override
    public View getViewByName(String name) {
        if (this.getActivity() == null) {
            return null;
        }
        int id = rs.getIdentifier(name, "id", this.getActivity().getPackageName());
        if (id == 0) {
            return null;
        }
        return fragmentView.findViewById(id);
    }

    @Override
    public View setViewBackground(int viewResId, int bgResId) {
        View v = fragmentView.findViewById(viewResId);
        v.setBackgroundResource(bgResId);
        return v;
    }

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     */
    @Override
    public View onViewClicked(int resId, final Callback handler) {
        final View view = fragmentView.findViewById(resId);
        if (view == null) {
            Log.w("andex", "No view found：" + rs.getResourceName(resId));
            return null;
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
        CheckBox ckb = fragmentView.findViewById(resId);
        ckb.setOnCheckedChangeListener(listener);
        return ckb;
    }

    @Override
    public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler) {
        final CompoundButton view = (CompoundButton) fragmentView.findViewById(resId);
        if (view == null) {
            Log.w("andex", "No view found：" + rs.getResourceName(resId));
            return null;
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
     * 隐藏当前的Fragment
     */
    public void hide() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.hide(this);
        ft.commit();
    }

    /**
     * 按照Activity的Class启动
     *
     * @param clazz
     */
    @Override
    public void startActivity(Class<? extends Activity> clazz) {
        buildActivity(clazz).start();
    }


    /**
     * 按照Activity的Class启动
     *
     * @param clazz
     * @param forResult
     */
    public void startActivity(Class<? extends Activity> clazz, boolean forResult) {
        if (forResult) {
            startActivityForResult(new Intent(context, clazz), REQUEST_CODE_DEFAULT);
        } else {
            startActivity(new Intent(context, clazz));
        }
    }

    /**
     * 创建掉转至指定Activity的构造器。
     *
     * @param activityClass
     * @return
     */
    public ActivityBuilder buildActivity(Class activityClass) {
        return new ActivityBuilder(context, activityClass).from(this);
    }


    /**
     * 创建跳转至指定Fragment的构造器。
     *
     * @param fragment
     * @param resId
     * @return
     */
    public FragmentBuilder buildFragment(Basev4Fragment fragment, int resId) {
        return new FragmentBuilder(this, fragment).replace(resId);
    }

    /**
     * 从前一个调用该Fragment的地方获得传递过来的Long类型的ID。
     *
     * @return
     */
    @Override
    public long getLongIdFromPrevious() {
        Bundle args = getArguments();
        if (args == null || args.size() == 0) {
            return 0;
        }
        return args.getLong(Constants.FRAGMENT_DATA_ID_KEY);
    }


    /**
     * 从前面（Fragment）获得默认的选项参数值（用Constants.FRAGMENT_DATA_OPTION_KEY标识）
     *
     * @return
     */
    public int getOptionFromPrevious() {
        Bundle args = getArguments();
        if (args == null) {
            return 0;
        }
        Object v = args.get(Constants.FRAGMENT_DATA_OPTION_KEY);
        if (v == null)
            return 0;
        return (Integer) v;
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


    /**
     * 直接返回至前一个Fragment（将当前的Fragment退出堆栈），如果没有更多...
     *
     * @deprecated
     */
    public void backToPrevious() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getFragmentManager() == null) {
                    Log.d("andex", "No fragment manager!");
                } else {
                    if (!getFragmentManager().popBackStackImmediate()) {
//						if (parentActivity != null) {
//							parentActivity.finish();
//						}
                    }
                }
            }
        });
    }

    /**
     * 创建无返回值的结果。
     *
     * @return
     */
    public ResultBuilder buildResult() {
        return new ResultBuilder(context).fragment(this);
    }

    /**
     * 创建要返回给前一Fragment的结果。
     *
     * @return
     */
    public ResultBuilder buildResultToPrevFragment() {
        return new ResultBuilder(context).fragment(this, previousFragment);
    }

    /**
     * 创建要finish父Activity的结果。
     *
     * @return
     */
    public ResultBuilder buildResultNoActivity() {
        return new ResultBuilder(context).fragment(this, parentActivity);
    }

    /**
     * 结束当前的Fragment，返回至前一个Fragment（如果设定有返回值的话）
     *
     * @deprecated
     */
    public void finish() {
        FragmentManager fragManager = this.getFragmentManager();
        if (fragManager == null) {
            Log.w("andex", "No fragment manager!");
        } else {
            fragManager.popBackStack();
//			FragmentTransaction ft = this.getFragmentManager().beginTransaction();
//			ft.remove(this);
//			if (previousFragment != null) {
//				Log.d("andex", "Back to previous fragment");
//				ft.add(previousFragment, Utils.getClassName(previousFragment));
//				ft.addToBackStack(Utils.getClassName(previousFragment));
//			}
//			ft.commit();
        }
    }

    /**
     * TODO 待测试
     *
     * @deprecated ?
     */
    @Override
    public void finishWithId(long id) {
        if (previousFragment != null) {
            previousFragment.onFragmentResult(id);
        }
        finish();
    }

    /**
     * 结束当前Fragment中的业务逻辑，前面一个Fragment（有的话）的onFragmentResult()方法会被调用并传递数据。
     *
     * @deprecated ?
     */
    @Override
    public void finishWithData(DataRow data) {
        if (previousFragment != null) {
            previousFragment.onFragmentResult(data);
        }
        finish();
    }

    /**
     * 结束当前Fragment中的业务逻辑，前面一个Fragment（有的话）的onFragmentResult()方法会被调用并传递数据。
     *
     * @param data
     * @deprecated ?
     */
    public void finishWithData(Map data) {
        if (previousFragment != null) {
            previousFragment.onFragmentResult(data);
        }
        finish();
    }

    /**
     * @param data
     * @deprecated ?
     */
    public void finishWithData(Object data) {
        if (previousFragment != null) {
            previousFragment.onFragmentResult(data);
        }
        finish();
    }

    /**
     * 当从一个Fragment返回时调用，并且附带数据（可以为NULL），此操作在 Fragment 事务完成前执行。
     *
     * @param data
     */
    protected void onFragmentResult(DataRow data) {
        // NOTHING NEED TO DO FOR NOW, INHERIT ME.
        throw new NotImplementedException(DataRow.class.getSimpleName());
    }

    /**
     * 当从一个Fragment返回时调用，并且附带数据（可以为NULL），此操作在 Fragment 事务完成前执行。
     *
     * @param data
     */
    protected void onFragmentResult(Map data) {
        // NOTHING NEED TO DO FOR NOW, INHERIT ME.
        throw new NotImplementedException(Map.class.getSimpleName());
    }

    /**
     * 当从一个Fragment返回时调用，并且附带数据（可以为NULL），此操作在 Fragment 事务完成前执行。
     *
     * @param data
     */
    protected void onFragmentResult(Object data) {
        // NOTHING NEED TO DO FOR NOW, INHERIT ME.
        throw new NotImplementedException(Object.class.getSimpleName());
    }

    /**
     * 当从一个Fragment返回时调用，并且附带数据（可以为NULL），此操作在 Fragment 事务完成前执行。
     *
     * @param args
     */
    public void onFragmentResult(Bundle args) {
        // NOTHING NEED TO DO FOR NOW, INHERIT ME.
        throw new NotImplementedException(Bundle.class.getSimpleName());
    }

    /**
     * 当从一个Fragment返回时调用，并且附带数据（可以为NULL），此操作在 Fragment 事务完成后执行。
     * 用于在返回前一个 Fragment 之后立即跳转至另外一个 Fragment
     *
     * @param args
     */
    public void afterFragmentResult(Bundle args) {
        // NOTHING NEED TO DO FOR NOW, INHERIT ME.
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

    protected Callback.CallbackAdapter callbackFinish = new Callback.CallbackAdapter() {

        @Override
        public void invoke() {
            super.invoke();
            buildResultToPrevFragment().finish();
        }

    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
