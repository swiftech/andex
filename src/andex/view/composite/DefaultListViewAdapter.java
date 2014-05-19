package andex.view.composite;

import andex.model.BaseListAdapter2;
import android.content.Context;
import org.andex.R;

/**
 * 默认布局的适配器。
 */
public class DefaultListViewAdapter extends BaseListAdapter2<IconListItem> {

	/**
	 * 默认的布局
	 *
	 * @param context
	 */
	public DefaultListViewAdapter(Context context) {
		super(context);
		super.itemViewBuilder = new ItemViewBuilder(context);
		super.itemViewBuilder.title(R.id.ax_tv_lv_item_title);
		super.itemViewBuilder.description(R.id.ax_tv_lv_item_desc);
	}

	/**
	 * 自定义布局的
	 *
	 * @param context
	 * @param itemViewBuilder
	 */
	public DefaultListViewAdapter(Context context, ItemViewBuilder itemViewBuilder) {
		super(context, itemViewBuilder);
		super.itemViewBuilder = itemViewBuilder;
	}

}
