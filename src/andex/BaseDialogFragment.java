package andex;

import andex.view.SimpleDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 *
 * @param <T> DialogFragment
 */
public abstract class BaseDialogFragment<T extends FragmentActivity> extends DialogFragment {

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = this.getActivity();
		this.rs = context.getResources();

		this.simpleDialog = new SimpleDialog(context);
	}

	protected void noTitleNoFrame() {
		setStyle(STYLE_NO_TITLE, 0);
		setStyle(STYLE_NO_FRAME, 0);
	}

	public abstract int getLayoutResourceId();


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layoutResourceId = getLayoutResourceId();
		if (layoutResourceId == 0) {
			throw new RuntimeException("没有设置layoutResourceId");
		}
		Object view = inflater.inflate(layoutResourceId, container, false);
		fragmentView = (View) view;
		return fragmentView;
	}

	public View onViewClicked(int resId, final Callback handler) {
		final View view = fragmentView.findViewById(resId);
		if (view == null) {
			Log.w("andex", "No view found：" + rs.getResourceName(resId));
			return view;
		}
		view.setOnClickListener(new OnClickListener() {

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
	 * 
	 * @param tag
	 */
	// public void dismissMe() {
	// DialogFragment frag = (DialogFragment) getFragmentManager().findFragmentByTag(this.getTag());
	// if (frag != null) {
	// frag.dismiss();
	// }
	// else {
	// Log.w("", "No dialog fragment to dismiss");
	// }
	// }

	/**
	 * 获取LinearLayout
	 * 
	 * @param resId
	 * @return
	 */

	public LinearLayout getLinearLayout(int resId) {
		return (LinearLayout) fragmentView.findViewById(resId);
	}

	public RelativeLayout getRelativeLayout(int resId) {
		return (RelativeLayout) fragmentView.findViewById(resId);
	}

	public FrameLayout getFrameLayout(int resId) {
		return (FrameLayout) fragmentView.findViewById(resId);
	}

	public TableLayout getTableLayout(int resId) {
		return (TableLayout) fragmentView.findViewById(resId);
	}

	/**
	 * 
	 * @param resId
	 * @return
	 */

	public TextView getTextView(int resId) {
		return (TextView) fragmentView.findViewById(resId);
	}

	/**
	 * 设置指定资源ID的TextView的文本为指定文本资源ID
	 * 
	 * @param resId
	 *            TextView的资源ID
	 * @param strResId
	 *            需要设置文本的资源ID
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
	 * 
	 * @param resId
	 * @return
	 */

	public Button getButton(int resId) {
		return (Button) fragmentView.findViewById(resId);
	}

	public CheckBox getCheckBox(int resId) {
		return (CheckBox) fragmentView.findViewById(resId);
	}

	public EditText getEditText(int resId) {
		return (EditText) fragmentView.findViewById(resId);
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

	public Spinner getSpinner(int resId) {
		return (Spinner) fragmentView.findViewById(resId);
	}

	public ViewGroup getViewGroup(int resId) {
		return (ViewGroup) fragmentView.findViewById(resId);
	}

	public AbsListView getAbsListView(int resId) {
		return (AbsListView) fragmentView.findViewById(resId);
	}

	public GridView getGridView(int resId) {
		return (GridView) fragmentView.findViewById(resId);
	}

	public ListView getListView(int resId) {
		return (ListView) fragmentView.findViewById(resId);
	}

	public ProgressBar getProgressBar(int resId) {
		return (ProgressBar) fragmentView.findViewById(resId);
	}

	public RadioButton getRadioButton(int resId) {
		return (RadioButton) fragmentView.findViewById(resId);
	}

	public RadioGroup getRadioGroup(int resId) {
		return (RadioGroup) fragmentView.findViewById(resId);
	}

	public SeekBar getSeekBar(int resId) {
		return (SeekBar) fragmentView.findViewById(resId);
	}

	public ToggleButton getToggleButton(int resId) {
		return (ToggleButton) fragmentView.findViewById(resId);
	}

	public RatingBar getRatingBar(int resId) {
		return (RatingBar) fragmentView.findViewById(resId);
	}

	public ExpandableListView getExpandableListView(int resId) {
		return (ExpandableListView) fragmentView.findViewById(resId);
	}

	public ScrollView getScrollView(int resId) {
		return (ScrollView) fragmentView.findViewById(resId);
	}

	public ImageView getImageView(int resId) {
		return (ImageView) fragmentView.findViewById(resId);
	}

	public ImageButton getImageButton(int resId) {
		return (ImageButton) fragmentView.findViewById(resId);
	}

	public WebView getWebView(int resId) {
		return (WebView) fragmentView.findViewById(resId);
	}

	public SurfaceView getSurfaceView(int resId) {
		return (SurfaceView) fragmentView.findViewById(resId);
	}

	protected String getNestedString(int sentence, Object... words) {
		return AndroidUtils.getNestedString(context, sentence, words);
	}
}
