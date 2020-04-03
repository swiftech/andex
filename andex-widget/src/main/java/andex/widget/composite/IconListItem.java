package andex.widget.composite;

import android.graphics.drawable.Drawable;

/**
 * 带图标的列表项构建器。
 */
public class IconListItem extends ListableItem {

    private static final String KEY_ICON_RES = "icon_res";

    private static final String KEY_ICON_DRAWABLE = "icon_drawable";

    /**
     * 无描述信息的项
     *
     * @param id       列表项 ID
     * @param title    列表项标题
     * @param itemType
     */
    public IconListItem(int id, String title, int itemType) {
        this(id, title, null, itemType);
        setIcon(0);
    }

    /**
     * @param id       列表项 ID
     * @param title    列表项标题
     * @param desc
     * @param itemType
     */
    public IconListItem(int id, String title, String desc, int itemType) {
        super(id, title, desc, itemType);
        setIcon(0);
    }

    /**
     * 图标资源。不可以是颜色
     *
     * @return
     */
    public Drawable iconDrawable() {
        return (Drawable) get(KEY_ICON_DRAWABLE);
    }

    public int iconResId() {
        return (Integer) get(KEY_ICON_RES);
    }

    /**
     * @return
     * @deprecated to IconResId
     */
    public int icon() {
        return iconResId();
    }

    /**
     * 设置图标资源，未设置的话默认为0
     *
     * @param icon
     * @return
     */
    public IconListItem setIcon(int icon) {
        put(KEY_ICON_RES, icon);
        return this;
    }

    public IconListItem setIcon(Drawable icon) {
        put(KEY_ICON_DRAWABLE, icon);
        return this;
    }
}
