package andex.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 *
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

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public ActivityBuilder with(String key, Serializable value) {
//		if (args == null) {
//			args = new Bundle();
//		}
//		args.putSerializable(key, value);
		this.intent.putExtra(key, value);
		return this;
	}

	/**
	 * @param args
	 * @return
	 */
	public ActivityBuilder with(Bundle args) {
//		if (this.args == null) {
//			this.args = args;
//		}
//		else {
//			this.args.putAll(args);
//		}
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
