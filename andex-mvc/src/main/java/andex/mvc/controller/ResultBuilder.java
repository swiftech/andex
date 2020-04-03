package andex.mvc.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;
import java.util.Map;

import andex.constants.LogConstants;
import andex.mvc.BaseFlowV4Fragment;
import andex.mvc.model.DataRow;

/**
 * For building activity or fragment results.
 */
public class ResultBuilder implements LogConstants {

    private Context context;

    private Bundle args = new Bundle();

    // 当前的fragment
    private BaseFlowV4Fragment thisFragment; // TODO 这个可能不是必须的。
    private BaseFlowV4Fragment previousFragment;

    // 在finish的时候也调用父Activity的finish方法
    private FragmentActivity parentActivity;

    // 当前的Activity
    private FragmentActivity activity;

    public ResultBuilder(Context context) {
        this.context = context;
    }

    /**
     * 标注结束当前的Fragment
     *
     * @param thisFragment
     * @return
     */
    public ResultBuilder fragment(BaseFlowV4Fragment thisFragment) {
        if (activity != null) {
            throw new IllegalStateException();
        }
        this.thisFragment = thisFragment;
        return this;
    }

    /**
     * 标注结束当前的Fragment，并且也结束父Activity
     *
     * @param thisFragment
     * @param parentActivity
     * @return
     */
    public ResultBuilder fragment(BaseFlowV4Fragment thisFragment, FragmentActivity parentActivity) {
        if (activity != null) {
            throw new IllegalStateException();
        }
        this.thisFragment = thisFragment;
        this.parentActivity = parentActivity;
        return this;
    }

    /**
     * 标注结束当前的Fragment，并且返回数据给前面的Fragment
     * 与activity()方法互斥
     *
     * @param thisFrag
     * @param prevFragment
     * @return
     */
    public ResultBuilder fragment(BaseFlowV4Fragment thisFrag, BaseFlowV4Fragment prevFragment) {
        if (activity != null) {
            throw new IllegalStateException();
        }
        this.thisFragment = thisFrag;
        this.previousFragment = prevFragment;
        return this;
    }

    /**
     * 标注结束当前的Activity，与fragment()方法互斥
     *
     * @param activity
     * @return
     */
    public ResultBuilder activity(FragmentActivity activity) {
        if (thisFragment != null) {
            throw new IllegalStateException();
        }
        this.activity = activity;
        return this;
    }

    /**
     * 添加返回参数
     *
     * @param key
     * @param value
     * @return
     */
    public ResultBuilder with(String key, Serializable value) {
        this.args.putSerializable(key, value);
        return this;
    }

    /**
     * 批量添加返回参数
     *
     * @param args
     * @return
     */
    public ResultBuilder with(Bundle args) {
        this.args.putAll(args);
        return this;
    }

    /**
     * 批量添加返回参数
     *
     * @param mapArgs
     * @return
     */
    public ResultBuilder with(Map<String, Serializable> mapArgs) {
        for (String key : mapArgs.keySet()) {
            this.args.putSerializable(key, mapArgs.get(key));
        }
        return this;
    }

    /**
     * 批量添加返回参数
     *
     * @param dataRowArgs
     * @return
     */
    public ResultBuilder with(DataRow dataRowArgs) {
        throw new NotImplementedException("");
//		return this;
    }

    /**
     * 结束当前的 Fragment 或者 Activity，返回至前面的 Fragment，有参数附带返回参数。
     *
     * @return
     */
    public ResultBuilder finish() {
        // 是 Fragment
        if (this.thisFragment != null) {

            Log.d(LogConstants.LOG_TAG, String.format("finish fragment with data (%d)", args.size()));
            if (this.previousFragment != null) {
                this.previousFragment.onFragmentResult(args); // onFragmentResult 放在后面是因为有可能其中有跳转至另外的 Fragment 的情况
            }
            try {
                FragmentManager fragmentManager = this.thisFragment.getFragmentManager();
                fragmentManager.popBackStackImmediate();
            } catch (Exception e) {
                e.printStackTrace();
                FragmentManager fragManager = this.thisFragment.getChildFragmentManager();
                fragManager.popBackStackImmediate();
            }
            if (this.previousFragment != null) {
                this.previousFragment.afterFragmentResult(args);
            }

            if (this.parentActivity != null) {
                this.parentActivity.finish();
            }
        }
        // 是 Activity
        else if (this.activity != null) {
            if (!args.isEmpty()) {
                Intent intent = new Intent();
                intent.putExtras(args);
            }
            Log.d(LogConstants.LOG_TAG, String.format("finish activity with data (%d)", args.size()));
            this.activity.finish();
        } else {
            throw new IllegalStateException("Don't know how to finish");
        }
        return this;
    }

}
