package andex.mvc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

import andex.Callback;
import andex.Constants;
import andex.mvc.controller.ActivityBuilder;
import andex.mvc.controller.FragmentBuilder;
import andex.mvc.controller.ResultBuilder;
import andex.mvc.model.DataRow;

public class BaseFlowV4Fragment<T extends FragmentActivity> extends Fragment implements Flowable {
    // 从参数中获取标题
    public static final String ARG_TITLE = "arg_title";
    //
    public final int REQUEST_CODE_DEFAULT = 1000;
    /**
     * Handler UI update
     */
    public final Handler handler = new Handler();
    /**
     * 当前Fragment的引用
     */
    public Fragment thisFragment;
    /**
     * 前一个Fragment（目前只有需要返回值的情况下才有）
     */
    public BaseFlowV4Fragment previousFragment;
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

    public BaseFlowV4Fragment() {
        super();
        this.thisFragment = this;
    }

    public int getLayoutResourceId() {
        return this.layoutResourceId;
    }

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     */
    public View onViewClicked(int resId, final Callback handler) {
        final View view = fragmentView.findViewById(resId);
        if (view == null) {
            Log.w(LOG_TAG, "No view found：" + rs.getResourceName(resId));
            return null;
        }
        view.setOnClickListener(v -> {
            view.setEnabled(false);
            handler.invoke();
            handler.invoke(v);
            view.setEnabled(true);
        });
        return view;
    }

    public CheckBox onCheckBoxChecked(int resId, final CompoundButton.OnCheckedChangeListener listener) {
        CheckBox ckb = fragmentView.findViewById(resId);
        ckb.setOnCheckedChangeListener(listener);
        return ckb;
    }

    public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler) {
        final CompoundButton view = fragmentView.findViewById(resId);
        if (view == null) {
            Log.w(LOG_TAG, "No view found：" + rs.getResourceName(resId));
            return null;
        }
        view.setOnCheckedChangeListener((buttonView, isChecked) -> {
            view.setEnabled(false);
            handler.invoke(isChecked);
            view.setEnabled(true);
        });
        return view;
    }

    /**
     * 按照Activity的Class启动
     *
     * @param clazz
     */
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
    public FragmentBuilder buildFragment(BaseFlowV4Fragment fragment, int resId) {
        return new FragmentBuilder(this, fragment).replace(resId);
    }

    /**
     * 从前一个调用该Fragment的地方获得传递过来的Long类型的ID。
     *
     * @return
     */
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

    /**
     * 直接返回至前一个Fragment（将当前的Fragment退出堆栈），如果没有更多...
     *
     * @deprecated
     */
    public void backToPrevious() {
        handler.post(() -> {
            if (getFragmentManager() == null) {
                Log.d(LOG_TAG, "No fragment manager!");
            } else {
                if (!getFragmentManager().popBackStackImmediate()) {
//						if (parentActivity != null) {
//							parentActivity.finish();
//						}
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
            Log.w(LOG_TAG, "No fragment manager!");
        } else {
            fragManager.popBackStack();
//			FragmentTransaction ft = this.getFragmentManager().beginTransaction();
//			ft.remove(this);
//			if (previousFragment != null) {
//				Log.d(LOG_TAG, "Back to previous fragment");
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
        Log.d(LOG_TAG, log.toString());
    }

    /**
     * 警告输出
     *
     * @param log
     */
    protected void warn(Object log) {
        if (log == null) log = "[null]";
        Log.w(LOG_TAG, log.toString());
    }

    /**
     * 错误输出
     *
     * @param log
     */
    protected void error(Object log) {
        if (log == null) log = "[null]";
        Log.e(LOG_TAG, log.toString());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
