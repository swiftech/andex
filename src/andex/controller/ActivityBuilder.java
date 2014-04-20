package andex.controller;

import andex.Constants;
import andex.Utils;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * 如果需要更多的处理（比如特定的参数）可以继承实现自己的Builder。
 */
public class ActivityBuilder {

	public final int REQUEST_CODE_DEFAULT = 1000;

	Context context;

	Intent intent;
	Fragment preFrag;
	Class activityClass;
	String activityName;

	/**
	 * 通过指定Activity名字来启动它。
	 * @param context
	 * @param activityName 想要启动的Actvity名字
	 */
	public ActivityBuilder(Context context, String activityName) {
		if (preFrag == null) {
			throw new IllegalArgumentException("Previous Fragment Needed");
		}
		this.context = context;
		this.activityName = activityName;
		this.intent = new Intent(Intent.ACTION_VIEW);
		ComponentName cn = new ComponentName(Utils.getClass(this).getPackage().getName(), activityName);
		intent.setComponent(cn);
	}

	/**
	 * 通过指定Activity类型启动它。
	 * @param context
	 * @param activityClass 想要启动的Actvity类型
	 */
	public ActivityBuilder(Context context, Class activityClass) {
//		if (preFrag == null) {
//			throw new IllegalArgumentException("Previous Fragment Needed");
//		}
		this.context = context;
		this.activityClass = activityClass;
		this.intent = new Intent(context, activityClass);
	}

	/**
	 * 从某个Fragment启动的。
	 * @param preFrag
	 * @return
	 */
	public ActivityBuilder from(Fragment preFrag) {
		this.preFrag = preFrag;
		return this;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public ActivityBuilder withId(long id) {
		with(Constants.INTENT_DATA_ID_KEY, id);
		return this;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public ActivityBuilder with(String key, Serializable value) {
		this.intent.putExtra(key, value);
		return this;
	}

	/**
	 *
	 * @param args
	 * @return
	 */
	public ActivityBuilder with(Bundle args) {
		intent.putExtras(args);
		return this;
	}

	/**
	 * Intent.FLAG_ACTIVITY_CLEAR_TOP 标志
	 * @return
	 */
	public ActivityBuilder clearTop() {
		intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return this;
	}

	public ActivityBuilder start() {
		if (this.intent == null) {
			throw new IllegalStateException("No Intent Provided");
		}
		if (preFrag == null) {
			context.startActivity(intent);
		}
		else {
			preFrag.startActivity(this.intent);
		}
		return this;
	}

	/**
	 * @return
	 */
	public ActivityBuilder startForResult() {
		if (this.intent == null) {
			throw new IllegalStateException("No Intent Provided");
		}
		preFrag.startActivityForResult(this.intent, REQUEST_CODE_DEFAULT);
		return this;
	}
}
