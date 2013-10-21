package andex;

import andex.AndroidUtils;
import andex.view.SimpleDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * 
 * @author yuxing
 *
 * @param <T> FragmentActivity
 */
public class BaseDialogFragment<T extends FragmentActivity> extends DialogFragment {

	protected Context context;
	
	protected T parentActivity;
	
	// Resources from context.
	protected Resources rs;

	protected View fragmentView;
	
	protected int layoutResourceId;
	
	// Simple Dialogs
	protected SimpleDialog simpleDialog;
	
	// Handler UI update
	protected Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.context = (Context) this.getActivity();
		this.rs = context.getResources();
		
		this.simpleDialog = new SimpleDialog(context);
	}
	
	protected void noTitleNoFrame() {
		setStyle(STYLE_NO_TITLE, 0);
		setStyle(STYLE_NO_FRAME, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Object view = inflater.inflate(layoutResourceId, container, false);
		if(view == null) {
			throw new RuntimeException("可能是没有设置layoutResourceId");
		}
		fragmentView = (View)view;
		return fragmentView;
	}
	
	public View onViewClicked(int resId, final Callback handler) {
		final View view = fragmentView.findViewById(resId);
		if(view == null) {
			Log.w("andex", "No view found：" + rs.getResourceName(resId));
			return view;
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
	
	/**
	 * 解散对话框
	 * @param tag
	 */
//	public void dismissMe() {
//		DialogFragment frag = (DialogFragment) getFragmentManager().findFragmentByTag(this.getTag());
//		if (frag != null) {
//			frag.dismiss();
//		}
//		else {
//			Log.w("", "No dialog fragment to dismiss");
//		}
//	}
	
	
	
	protected String getNestedString(int sentence, Object... words){
		return AndroidUtils.getNestedString(context, sentence, words);
	}
}
