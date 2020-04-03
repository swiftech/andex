package andex.widget.composite;

import android.content.Context;

import org.andex.R;

import andex.widget.BaseListAdapter2;

/**
 * 默认的带图标的布局
 */
public class DefaultIconListViewAdapter<B extends IconListItem> extends BaseListAdapter2<B> {

    public DefaultIconListViewAdapter(Context context) {
        super(context);

        super.itemViewBuilder = new IconItemViewBuilder(context).icon(R.id.ax_iv_lv_item_icon);

        super.itemViewBuilder.itemType(R.layout.ax_listview_item_icon_left)
                .title(R.id.ax_tv_lv_item_title)
                .description(R.id.ax_tv_lv_item_desc);
    }

    public DefaultIconListViewAdapter(Context context, IconItemViewBuilder itemViewBuilder) {
        super(context, itemViewBuilder);
    }
}
