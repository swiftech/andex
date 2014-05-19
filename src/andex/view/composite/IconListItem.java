package andex.view.composite;

/**
 * 带图标的列表项构建器。
 */
public class IconListItem extends ListableItem {

	private static final String KEY_ICON = "icon";

	/**
	 * 无描述信息的项
	 * @param id
	 * @param title
	 * @param itemType
	 */
	public IconListItem(int id, String title, int itemType) {
		this(id, title, null, itemType);
		setIcon(0);
	}

	/**
	 *
	 * @param id
	 * @param title
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
	public int icon() {
		return (Integer) get(KEY_ICON);
	}

	/**
	 * 设置图标资源，未设置的话默认为0
	 * @param icon
	 * @return
	 */
	public IconListItem setIcon(int icon) {
		put(KEY_ICON, icon);
		return this;
	}
}
