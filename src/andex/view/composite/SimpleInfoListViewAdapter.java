package andex.view.composite;

import andex.model.BaseListAdapter2;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.andex.R;

/**
 * 显示提示信息
 *
 * @author
 */
public class SimpleInfoListViewAdapter extends BaseListAdapter2<ListableItem> {
	private int gravity;

	/**
	 * 用给定的默认提示信息显示只有一项的列表。
	 *
	 * @param context
	 * @param info
	 */
	public SimpleInfoListViewAdapter(Context context, String info) {
		super(context);
		super.itemViewBuilder = new ItemViewBuilder(context)
				.title(R.id.tv_alii_label).itemType(0, R.layout.ax_lv_item_info);
		super.addItem(new ListableItem(-1, info, 0));
	}

	/**
	 * @param context
	 * @param info
	 * @param gravity 设定文字在水平方向的重心
	 */
	public SimpleInfoListViewAdapter(Context context, String info, int gravity) {
		this(context, info);
		this.gravity = gravity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = super.getView(position, convertView, parent);
		TextView tv = (TextView) layout.findViewById(R.id.tv_alii_label);
		if (gravity != 0) {
			tv.setGravity(gravity);
		}
		tv.setText(getItem(position).title());
		return layout;
	}
}
