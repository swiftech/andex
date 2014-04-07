package andex.controller;

import andex.Basev4Fragment;
import andex.model.DataRow;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;
import java.util.Map;

/**
 * For building activity or thisFragment results.
 */
public class ResultBuilder {

	Context context;

	Bundle args = new Bundle();

	// 当前的fragment
	Basev4Fragment thisFragment; // TODO 这个可能不是必须的。
	Basev4Fragment previousFragment;
	// 在finish的时候也调用父Activity的finish方法
	FragmentActivity parentActivity;

	// 当前的Activity
	FragmentActivity activity;

	public ResultBuilder(Context context) {
		this.context = context;
	}

	/**
	 * 结束当前的Fragment
	 * @param thisFragment
	 * @return
	 */
	public ResultBuilder fragment(Basev4Fragment thisFragment) {
		if (activity != null) {
			throw new IllegalStateException();
		}
		this.thisFragment = thisFragment;
		return this;
	}

	/**
	 * 结束当前的Fragment，并且也结束父Activity
	 * @param thisFragment
	 * @param parentActivity
	 * @return
	 */
	public ResultBuilder fragment(Basev4Fragment thisFragment, FragmentActivity parentActivity) {
		if (activity != null) {
			throw new IllegalStateException();
		}
		this.thisFragment = thisFragment;
		this.parentActivity = parentActivity;
		return this;
	}

	/**
	 * 结束当前的Fragment，并且返回数据给前面的Fragment
	 * 与activity()方法互斥
	 *
	 * @param thisFrag
	 * @param prevFragment
	 * @return
	 */
	public ResultBuilder fragment(Basev4Fragment thisFrag, Basev4Fragment prevFragment) {
		if (activity != null) {
			throw new IllegalStateException();
		}
		this.thisFragment = thisFrag;
		this.previousFragment = prevFragment;
		return this;
	}

	/**
	 * 与fragment()方法互斥
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

	public ResultBuilder with(String key, Serializable value) {
		this.args.putSerializable(key, value);
		return this;
	}

	public ResultBuilder with(Bundle args) {
		this.args.putAll(args);
		return this;
	}

	public ResultBuilder with(Map<String, Serializable> mapArgs) {
		for (String key : mapArgs.keySet()) {
			this.args.putSerializable(key, mapArgs.get(key));
		}
		return this;
	}

	public ResultBuilder with(DataRow dataRowArgs) {
		throw new NotImplementedException("");
//		return this;
	}

	/**
	 * 结束当前的Fragment或者Activity，返回至前面的Fragment，有参数附带返回参数。
	 * @return
	 */
	public ResultBuilder finish() {
		if (this.thisFragment != null) {
			if (this.previousFragment != null) {
				this.previousFragment.onFragmentResult(args);
			}
			Log.d("andex", String.format("finish fragment with data (%d)", args.size()));
			this.thisFragment.getFragmentManager().popBackStackImmediate();
			if (this.parentActivity != null) {
				this.parentActivity.finish();
			}
		}
		else if (this.activity != null) {
			if (!args.isEmpty()) {
				Intent intent = new Intent();
				intent.putExtras(args);
			}
			Log.d("andex", String.format("finish activity with data (%d)", args.size()));
			this.activity.finish();
		}
		else {
			throw new IllegalStateException("Don't know how to finish");
		}
		return this;
	}

}
