package andex.event;

/**
 * 对话框事件，例如：按后退键弹出确认框之后触发事件调用 Activity 父类的某个方法。
 * Created by yuxing on 16-10-7.
 */
public class DialogEvent {

	/**
	 * 是何种类型的对话框事件
	 */
	private Boolean positive;

	public DialogEvent(Boolean positive) {
		this.positive = positive;
	}

	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}
}
