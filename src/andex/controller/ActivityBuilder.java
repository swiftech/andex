package andex.controller;

import andex.Constants;
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

	Intent intent;
	Class activityClass;
	Fragment preFrag;
	Context context;

	public ActivityBuilder(Context context, Fragment preFrag, Class activityClass) {
		if (preFrag == null) {
			throw new IllegalArgumentException("Previous Fragment Needed");
		}
		this.activityClass = activityClass;
		this.preFrag = preFrag;
		this.context = context;
		this.intent = new Intent(context, activityClass);
	}

	public ActivityBuilder withId(long id) {
		with(Constants.INTENT_DATA_ID_KEY, id);
		return this;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public ActivityBuilder with(String key, Serializable value) {
		this.intent.putExtra(key, value);
		return this;
	}

	/**
	 * @param args
	 * @return
	 */
	public ActivityBuilder with(Bundle args) {
		intent.putExtras(args);
		return this;
	}

	public ActivityBuilder start() {
		if (this.intent == null) {
			throw new IllegalStateException("No Intent Provided");
		}
		preFrag.startActivity(this.intent);
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
