package andex.view.composite;

import android.content.Context;
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

	protected int iconResId;

	public IconItemViewBuilder(Context context) {
		super(context);
	}

	public IconItemViewBuilder icon(int resId) {
		this.iconResId = resId;
		return this;
	}

	@Override
	public void build(View itemLayout, I item) {
		super.build(itemLayout, item);
		if (item.icon() != 0) {
			ImageView icon = (ImageView) itemLayout.findViewById(iconResId);
			icon.setImageResource(item.icon());
		}
	}
}
