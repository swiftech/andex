package andex.builder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 构造时间格式。
 * 最后用 format() 方法来格式化时间。
 * Created by yuxing on 14-7-22.
 */
public class TimeFormatBuilder {

	// yyyy-MM-dd
	public static TimeFormatBuilder TIME_FORMAT_BUILDER_DATE_MINUS
			= new TimeFormatBuilder().year().minus().month().minus().day();

	// yyyy-MM-dd HH:mm
	public static TimeFormatBuilder TIME_FORMAT_BUILDER_DATE_TIME_MINUS
			= new TimeFormatBuilder().year().minus().month().minus().day().blank().hour().colon().minute().colon().second();

	// yyyy_MMdd_HHmm TODO 待验证
	public static TimeFormatBuilder TIME_FORMAT_BUILDER_TIME_STAMP
			= new TimeFormatBuilder().year().underscore().month().day().underscore().hour().minute();

	private StringBuilder timeFormat = new StringBuilder();


	/**
	 * 自定义分隔符
	 * @param separator
	 * @return
	 */
	public TimeFormatBuilder separator(String separator) {
		timeFormat.append(separator);
		return this;
	}

	/**
	 * ':'
	 *
	 * @return
	 */
	public TimeFormatBuilder colon() {
		timeFormat.append(":");
		return this;
	}

	/**
	 * '_'
	 *
	 * @return
	 */
	public TimeFormatBuilder underscore() {
		timeFormat.append("_");
		return this;
	}

	/**
	 * '-'
	 *
	 * @return
	 */
	public TimeFormatBuilder minus() {
		timeFormat.append("-");
		return this;
	}

	public TimeFormatBuilder blank() {
		timeFormat.append(" ");
		return this;
	}

	public TimeFormatBuilder year() {
		timeFormat.append("yyyy");
		return this;
	}

	public TimeFormatBuilder month() {
		timeFormat.append("MM");
		return this;
	}

	public TimeFormatBuilder day() {
		timeFormat.append("dd");
		return this;
	}

	public TimeFormatBuilder hour() {
		timeFormat.append("HH");
		return this;
	}

	public TimeFormatBuilder minute() {
		timeFormat.append("mm");
		return this;
	}

	public TimeFormatBuilder second() {
		timeFormat.append("ss");
		return this;
	}

	public String format(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(String.format("%s", timeFormat.toString()), Locale.CHINA);
		return sdf.format(time);
	}

	public String format(Date date) {
		return format(date.getTime());
	}

	public String format(Calendar calendar){
		return format(calendar.getTimeInMillis());
	}
}
