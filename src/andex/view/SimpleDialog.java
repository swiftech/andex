package andex.view;

import java.util.Map;
import java.util.Stack;

import org.andex.R;

import andex.Utils;
import andex.Callback.CallbackAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Show all kinds of dialogs in a simple way.
 * 
 * @author 
 *
 */
public class SimpleDialog {

	protected String tagOk = "OK";
	protected String tagCancel = "Cancel";
	protected String tagYes = "Yes";
	protected String tagNo = "No";
	protected String tagSave = "Save";
	protected String tagClose = "Close";
	
	protected int dialogBgColor = Color.LTGRAY;
	
	protected int dialogTxtColor = Color.BLACK;
	
	// The current on top of any other dialogs.
	private Stack<AlertDialog> dialogStack = new Stack<AlertDialog>();
	
	private Context context;
	
	private Resources rs;

	public SimpleDialog(Context context) {
		super();
		this.context = context;
		this.rs = context.getResources();
		
		tagOk = rs.getString(android.R.string.ok);
		tagCancel = rs.getString(android.R.string.cancel);
		tagClose = rs.getString(R.string.common_close);
		tagYes = rs.getString(android.R.string.yes);
		tagNo = rs.getString(android.R.string.no);
	}

	/**
	 * 
	 * @param msg
	 * @param callback
	 */
	public void showConfirmDialog(String msg, final DialogCallback callback) {
		showConfirmDialog(msg, new String[] { rs.getString(R.string.common_yes), rs.getString(R.string.common_no) },
				callback);
	}
	
	/**
	 * Show dialog with message to confirm something.
	 * 
	 * @param msg
	 * @param callback
	 */
	public void showConfirmDialog(String msg, String[] buttonsTitle, final DialogCallback callback) {
		Log.d("androidx", "showConfirmDialog()");
		
		String tagPositive = null;
		String tagNegative = null;
		if (buttonsTitle != null && buttonsTitle.length >= 2) {
			tagPositive = Utils.isEmpty(buttonsTitle[0]) ? tagOk : buttonsTitle[0];
			tagNegative = Utils.isEmpty(buttonsTitle[1]) ? tagOk : buttonsTitle[1];
		}
		
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setMessage(msg);
		dBuilder.setIcon(android.R.drawable.ic_menu_help);
		dBuilder.setPositiveButton(tagPositive, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("Confirm Dialog", "OK clicked");
				callback.onPositive(dialog);
				dismissDialogOnTop();
				callback.afterSelected();
			}
		});
		dBuilder.setNegativeButton(tagNegative, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("Confirm Dialog", "Calcel clicked");
				callback.onNegative(dialog);
				dismissDialogOnTop();
				callback.afterSelected();
			}
		});

		AlertDialog confirmDialog = dBuilder.create();
		confirmDialog.setTitle(rs.getString(R.string.common_dialog_confirm_title));
		confirmDialog.show();
		Log.d("androidx", "  show");
		dialogStack.push(confirmDialog);
	}

	/**
	 * Show progress dialog until dismissing by others.
	 * @param msg
	 */
	public void showProgressDialog(String msg) {
		final View progressView = LayoutInflater.from(context).inflate(R.layout.ax_progress_dialog, null);
		TextView textView = (TextView) progressView.findViewById(R.id.axpd_msg);
		textView.setText(msg);
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setView(progressView);
		dBuilder.setIcon(android.R.drawable.ic_dialog_info);
		AlertDialog progressDialog = dBuilder.create();
		progressDialog.setTitle(rs.getString(R.string.common_dialog_progress_title));
		progressDialog.show();
		dialogStack.push(progressDialog);		
	}
	
	
	/**
	 * Show un-interrupted progress dialog with message.
	 * 
	 * @param msg
	 * @param callback
	 */
	public void showProgressDialog(String msg, final DialogCallback callback) {
		final View progressView = LayoutInflater.from(context).inflate(R.layout.ax_progress_dialog, null);
		TextView textView = (TextView) progressView.findViewById(R.id.axpd_msg);
		textView.setText(msg);

		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setView(progressView);
		dBuilder.setIcon(android.R.drawable.ic_dialog_info);
		dBuilder.setNegativeButton(tagCancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("ProgressDialog", "Calcel clicked");
				dismissDialogOnTop();
				callback.onNegative(dialog);
			}
		});

		AlertDialog progressDialog = dBuilder.create();
		progressDialog.setTitle(rs.getString(R.string.common_dialog_progress_title));
		progressDialog.show();
		dialogStack.push(progressDialog);
	}

	/**
	 * Show dialog with single input.
	 * 
	 * @param title
	 * @param msg
	 * @param inputInit
	 *            Init the input edit text.
	 * @param callback
	 *            Callback with user inputs when click OK button.
	 * @return
	 */
	public AlertDialog showInputDialog(String title, String msg, int inputType, Object inputInit, final DialogCallback callback) {
		View inputView = LayoutInflater.from(context).inflate(R.layout.ax_dialog_single_input, null);
		final EditText etInput = (EditText) inputView.findViewById(R.id.editTxtInput);
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setView(inputView);
		dBuilder.setIcon(android.R.drawable.ic_dialog_info);
		dBuilder.setMessage(msg);
		dBuilder.setPositiveButton(tagYes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissDialogOnTop();
				callback.onPositive(etInput.getText().toString().trim());
				
			}
		});
		dBuilder.setNegativeButton(tagNo, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissDialogOnTop();
				callback.onNegative(dialog);
			}
		});

		etInput.setInputType(inputType);
		etInput.setText(inputInit.toString());
		AlertDialog searchDialog = dBuilder.create();
		searchDialog.setTitle(title);
		searchDialog.show();
		dialogStack.push(searchDialog);
		return searchDialog;
	}

	/**
	 * Show radio group dialog, return selected index in group.
	 * 
	 * @param title
	 * @param msg
	 * @param labels
	 * @param checked
	 * @param callback
	 * @return
	 */
	public AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
			final DialogCallback callback) {
		View inputView = LayoutInflater.from(context).inflate(R.layout.ax_dialog_radiogroup, null);
		inputView.setBackgroundColor(dialogBgColor);
		final RadioGroup radioGroup = (RadioGroup) inputView.findViewById(R.id.cdr_rg_selection);
		// radioGroup.setBackgroundColor(dialogBgColor);
		radioGroup.removeAllViews();
		for (int i = 0; i < labels.length; i++) {
			// Log.d("", "Add new radio to group " + labels[i]);
			RadioButton radio = new RadioButton(context);
			radio.setId(i);
			radio.setText(labels[i]);
			radio.setChecked(checked == i ? true : false);
			radio.setTextColor(dialogTxtColor);
			radioGroup.addView(radio);
		}

		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setView(inputView);
		dBuilder.setIcon(android.R.drawable.ic_menu_more);
		dBuilder.setMessage(msg);
		dBuilder.setPositiveButton(tagYes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onPositive(radioGroup.getCheckedRadioButtonId());
				dismissDialogOnTop();
			}
		});
		dBuilder.setNegativeButton(tagNo, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onNegative(dialog);
				dismissDialogOnTop();
			}
		});

		AlertDialog radioGroupDialog = dBuilder.create();
		radioGroupDialog.setTitle(title);
		radioGroupDialog.show();
		dialogStack.push(radioGroupDialog);
		return radioGroupDialog;
	}

	/**
	 * Show dialog with Checkbox list view
	 * 
	 * @param title
	 * @param msg
	 * @param labels
	 * @param checkboxListViewAdapter
	 *            Adapter to init the list view with checkboxs.
	 * @param callback
	 *            Callback to invoker.
	 * @return
	 */
	public AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter,
			final DialogCallback callback) {
		View inputView = LayoutInflater.from(context).inflate(R.layout.ax_dialog_list_select, null);
		final ListView listView = (ListView) inputView.findViewById(R.id.cdr_rg_selection);

		listView.setAdapter(checkboxListViewAdapter);

		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setView(inputView);
		dBuilder.setIcon(android.R.drawable.ic_menu_more);
		dBuilder.setPositiveButton(tagYes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onPositive(listView);
				dismissDialogOnTop();
			}
		});
		dBuilder.setNegativeButton(tagNo, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onNegative(dialog);
				dismissDialogOnTop();
			}
		});

		AlertDialog listSelectDialog = dBuilder.create();
		listSelectDialog.setTitle(title);
		listSelectDialog.show();
		dialogStack.push(listSelectDialog);
		return listSelectDialog;
	}

	/**
	 * 显示提示对话框。
	 * @param msg
	 */
	public void showInfoDialog(final String msg) {
		showInfoDialog(msg, null);
	}
	
	/**
	 * 显示提示对话框。
	 * 
	 * @param msg
	 */
	public void showInfoDialog(final String msg, final DialogCallback callback) {
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setMessage(msg);
		dBuilder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("showInfoDialog", "Positive Clicked");
				if (callback != null)
					callback.onPositive();
				dismissDialogOnTop();
			}
		});
		AlertDialog infoDialog = dBuilder.create();
		infoDialog.setTitle(android.R.string.dialog_alert_title);
		infoDialog.setIcon(android.R.drawable.ic_menu_info_details);
		infoDialog.show();
		dialogStack.push(infoDialog);
	}

	/**
	 * 显示一个列表对话框，比如“对话框式弹出菜单“。不自动关闭对话框，需要调用者手动关闭。
	 * 
	 * @param title
	 * @param items
	 * @param callback
	 *            列表选中一项时调用，参数为选项位置。
	 */
	public void showListSelectDialog(final String title, final String[] items, final DialogCallback callback) {
		if(items == null || items.length == 0) {
			Log.w("androidsx", "Invalid items");
		}
		View fileActionView = LayoutInflater.from(context).inflate(R.layout.ax_dialog_list_select, null);
		ListView listSelect = (ListView) fileActionView.findViewById(R.id.cdl_list);
		listSelect.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, items));
		listSelect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				callback.onPositive(position);
			}

		});
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setTitle(title);
		dBuilder.setIcon(android.R.drawable.ic_menu_more);
		dBuilder.setView(fileActionView);
		dBuilder.setNegativeButton(tagCancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onNegative(which);
				dismissDialogOnTop();
			}
		});
		AlertDialog listSelectDialog = dBuilder.create();
		listSelectDialog.show();
		dialogStack.push(listSelectDialog);
	}
	
	/**
	 * Show dialog with key-value list, when click, callback with selected key.
	 * @param title
	 * @param items
	 * @param callback
	 */
	public void showListSelectDialog(final String title, final Map items, final DialogCallback callback) {
		View layout = LayoutInflater.from(context).inflate(R.layout.ax_dialog_list_select, null);
		ListView listSelect = (ListView) layout.findViewById(R.id.cdl_list);
		SimpleListView slv = new SimpleListView(context, listSelect);
		slv.addAllItems(items);
		slv.onItemClick(new CallbackAdapter() {
			@Override
			public void invoke(Object key) {
				callback.onPositive(key);
			}
		});
		slv.render();

		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setTitle(title);
		dBuilder.setIcon(android.R.drawable.ic_menu_more);
		dBuilder.setView(layout);
		dBuilder.setNegativeButton(tagCancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissDialogOnTop();
				callback.onNegative(dialog);
			}
		});
		AlertDialog listSelectDialog = dBuilder.create();
		listSelectDialog.show();
		dialogStack.push(listSelectDialog);
	}
	
	/**
	 * Show customized dialog, if complex input such as Spinner, use {@link CustomDialogInit} to init; 
	 * @param title
	 * @param layoutResId
	 * @param resIds
	 * @param init
	 * @param callback
	 */
	public void showCustomizedDialog(final String title, int layoutResId, final int[] resIds, final Object[] init,
			final DialogCallback callback) {
		final View layout = LayoutInflater.from(context).inflate(layoutResId, null);

		for (int i = 0; i < resIds.length; i++) {
			View v = layout.findViewById(resIds[i]);
			if (v == null) {
				continue;
			}
			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				tv.setText(init[i].toString());
			}
			else if (v instanceof EditText) {
				EditText et = (EditText) v;
				ViewUtils.initEditTextByType(et, init[i].getClass());
				et.setText(init[i].toString());
			}
			else if (v instanceof Spinner) {
				if (init[i] instanceof CustomDialogInit) {
					CustomDialogInit h = (CustomDialogInit) init[i];
					int size = h.init(layout, resIds[i]);
					v.setEnabled(size == 0 ? false : true);
					new SimpleSpinner((Spinner) v).setSelection(h.setValue());
				}
			}
		}
		
		AlertDialog.Builder dBuilder = new Builder(context);
		dBuilder.setTitle(title);
		dBuilder.setIcon(android.R.drawable.ic_menu_info_details);
		dBuilder.setView(layout);
		dBuilder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("showCustomizedDialog", "Positive Button Clicked");
				dismissDialogOnTop();
				Object[] ret = new Object[resIds.length];
				for (int i = 0; i < resIds.length; i++) {
					View v = layout.findViewById(resIds[i]);
					if(v == null) {
						continue;
					}
					if(v instanceof TextView) {
						TextView tv = (TextView)v;
						ret[i] = tv.getText().toString();
					}
					else if(v instanceof EditText) {
						EditText et = (EditText)v;
						ret[i] = et.getText().toString();
					}
					else if(v instanceof Spinner) {
						SimpleSpinner spinner = new SimpleSpinner(context);
						// TODO
					}
				}
				callback.onPositive(ret);
			}
		});
		dBuilder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissDialogOnTop();
				callback.onNegative(dialog);
			}
		});
		AlertDialog customDialog = dBuilder.create();
		customDialog.show();
		dialogStack.push(customDialog);
	}
	
	/**
	 * Dismiss latest dialog showed up.
	 */
	public void dismissDialogOnTop() {
		if(dialogStack == null || dialogStack.isEmpty()) {
			Log.d("androidx", "No dialog on the top");
			return;
		}
		AlertDialog dlg = dialogStack.pop();
		if (dlg == null)
			Log.d("androidx", "No dialog on the top");
		else{
			Log.d("androidx", "Dismiss top dialog");
			dlg.dismiss();			
		}
	}

	
	/**
	 * Use this to init complex dialog item like Spinner.
	 * @author yuxing
	 *
	 */
	public static class CustomDialogInit {
		
		/**
		 * 
		 * @param layout
		 * @param resId
		 * @return size of data
		 */
		public int init(View layout, int resId){return 0;};
		
		public Object setValue(){return null;};
	}
	
	/**
	 * Callback for dialog.
	 * 
	 * @author 
	 * 
	 */
	public static class DialogCallback<T> {
		/**
		 * 
		 */
		public void onPositive(){};
		
		/**
		 * Positive button clicked.
		 * @param value
		 */
		public void onPositive(T value){};
		
		/**
		 * Positive button clicked with multi-values returned.
		 * @param values
		 */
		public void onPositive(T... values) {};

		/**
		 * Negative button clicked.
		 */
		public void onNegative(T value){};
		
		/**
		 * Invoked after any choices that make.
		 */
		public void afterSelected(){}
	}

}
