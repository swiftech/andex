package andex.view.composite;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * 带图标的构建器
 * 例子：
 * IconItemViewBuilder builder = new IconItemViewBuilder(context);
 * builder.icon(R.id.iv_menu_icon).title(R.id.tv_menu_title);
 * builder.itemType(typeHead, R.layout.lv_item_menu_head);
 * builder.itemType(typeItem, R.layout.lv_item_menu_item);
 */
public class IconItemViewBuilder<I extends IconListItem> extends ItemViewBuilder<I> {

	static final String LOG_TAG = IconItemViewBuilder.class.getSimpleName();

	protected int iconResId;

	public IconItemViewBuilder(Context context) {
		super(context);
	}

	/**
	 * 图标
	 *
	 * @param resId
	 * @return
	 */
	public IconItemViewBuilder icon(int resId) {
		this.iconResId = resId;
		return this;
	}

	/**
	 *
	 * @param itemLayout
	 * @param item
	 */
	@Override
	public void build(View itemLayout, I item) {
		super.build(itemLayout, item);

		ImageView icon = (ImageView) itemLayout.findViewById(iconResId);
		if (item.iconDrawable() != null) {
			Log.d(LOG_TAG, "  Drawable Icon");
			icon.setImageDrawable(item.iconDrawable());
		}
		else if (item.iconResId() > 0) {
			Log.d(LOG_TAG, "  Icon Resource");
			icon.setImageResource(item.iconResId());
		}
		else {
			Log.w(LOG_TAG, "  No Icon");
		}
	}
}
