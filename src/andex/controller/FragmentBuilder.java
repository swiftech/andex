package andex.controller;

import andex.Basev4Fragment;
import andex.Constants;
import andex.Utils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

/**
 * Fragment事务的构造器。如果需要更多的处理（比如特定的参数）可以继承实现自己的Builder。
 */
public class FragmentBuilder {

	Basev4Fragment preFrag;
	Basev4Fragment frag;
	int resourceId;
	boolean addToBackStack = false;
	FragmentTransaction ft;

	/**
	 * 从一个Fragment开始的。
	 * @param preFrag
	 * @param frag
	 */
	public FragmentBuilder(Basev4Fragment preFrag, Basev4Fragment frag) {
		if (preFrag == null || frag == null) {
			throw new IllegalArgumentException();
		}
		ft = preFrag.getFragmentManager().beginTransaction();
		this.frag = frag;
		this.preFrag = preFrag;
	}

	/**
	 * 从一个Activity开始的。
	 * @param act
	 * @param frag
	 */
	public FragmentBuilder(FragmentActivity act, Basev4Fragment frag) {
		if (frag == null) {
			throw new IllegalArgumentException();
		}
		ft = act.getSupportFragmentManager().beginTransaction();
		this.frag = frag;
	}

	/**
	 *
	 * @return
	 */
	public FragmentBuilder backstack() {
		ft.addToBackStack(Utils.getClassName(frag));
		addToBackStack = true;
		return this;
	}

	/**
	 *
	 * @param resId
	 * @return
	 */
	public FragmentBuilder add(int resId) {
		ft.add(resId, frag, Utils.getClassName(frag));
		this.resourceId = resId;
		return this;
	}

	public FragmentBuilder withId(long id) {
		with(Constants.FRAGMENT_DATA_ID_KEY, id);
		return this;
	}

	public FragmentBuilder withOption(int option) {
		with(Constants.FRAGMENT_DATA_OPTION_KEY, option);
		return this;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public FragmentBuilder with(String key, Serializable value) {
		Bundle args = frag.getArguments();
		if(args == null) {
			args = new Bundle();
			frag.setArguments(args);
		}
		args.putSerializable(key, value);
		return this;
	}

	/**
	 *
	 * @param args
	 * @return
	 */
	public FragmentBuilder with(Bundle args) {
		if (frag.getArguments() == null) {
			frag.setArguments(args);
		}
		else {
			frag.getArguments().putAll(args);
		}
		return this;
	}

	/**
	 *
	 * @param resId
	 * @return
	 */
	public FragmentBuilder replace(int resId) {
		ft.replace(resId, frag, Utils.getClassName(frag));
		this.resourceId = resId;
		return this;
	}

	/**
	 *
	 * @return
	 */
	public FragmentBuilder start() {
		ft.commit();
		return this;
	}

	/**
	 * TODO
	 * @return
	 */
	public FragmentBuilder result() {
		frag.previousFragment = preFrag;
		return this;
	}


}
