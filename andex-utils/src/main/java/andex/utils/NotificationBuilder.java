package andex.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

/**
 *
 */
public class NotificationBuilder {

	public static String KEY_NOTI_ID = "notification_id";

	public static String KEY_NOTI_CANCEL_WITH_CLICK = "notification_cancel_with_click";

	private Context context;

	private Intent notiIntent;

	private int notificationId;

	private int flags;

	private final NotificationCompat.Builder notiBuilder;

	public NotificationBuilder(Context context) {
		this.context = context;
		notiBuilder = new NotificationCompat.Builder(context);
	}

	public NotificationBuilder id(int notificationId) {
		this.notificationId = notificationId;
		return this;
	}

	public NotificationBuilder randomId() {
		this.notificationId = new Random(Long.MAX_VALUE).nextInt();
		return this;
	}

	public NotificationBuilder timestampId() {
		this.notificationId = (int) Calendar.getInstance().getTimeInMillis();
		return this;
	}

	public NotificationBuilder title(String title) {
		notiBuilder.setContentTitle(title);
		return this;
	}

	public NotificationBuilder description(String desc) {
		notiBuilder.setContentText(desc);
		return this;
	}

	public NotificationBuilder icon(int icon) {
		notiBuilder.setSmallIcon(icon);
		return this;
	}

	public NotificationBuilder sticky(boolean sticky) {
		flags(Notification.FLAG_ONGOING_EVENT);
		return this;
	}

	public NotificationBuilder flags(int flags) {
		this.flags |= flags;
		return this;
	}

	public NotificationBuilder activity(Class clazz) {
		notiIntent = new Intent(context, clazz);
		return this;
	}

	public NotificationBuilder with(String key, int value) {
		if (notiIntent == null) {
			throw new IllegalStateException("Call activity() first");
		}
		else {
			notiIntent.putExtra(key, value);
		}
		return this;
	}

	public NotificationBuilder with(String key, Serializable value) {
		if (notiIntent == null) {
			throw new IllegalStateException("Call activity() first");
		}
		else {
			notiIntent.putExtra(key, value);
		}
		return this;
	}

	/**
	 * 标注点击通知后取消通知，由 BaseActivity.handleNotification() 处理。
	 *
	 * @return
	 */
	public NotificationBuilder cancelWithClick() {
		return with(KEY_NOTI_CANCEL_WITH_CLICK, true);
	}

	public NotificationBuilder start() {
		with(KEY_NOTI_ID, this.notificationId);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (notiIntent != null) {
			PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId,
					notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notiBuilder.setContentIntent(contentIntent);
		}
		Notification notification = notiBuilder.build();
		notification.flags = this.flags;
		nm.notify(this.notificationId, notification);
		return this;
	}

	/**
	 * 处理由 Notification 跳转的过来所附带的参数。
	 *
	 * @see NotificationBuilder
	 */
	public static int handleNotification(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		int notiId = 0;
		if (extras != null) {
			notiId = extras.getInt(NotificationBuilder.KEY_NOTI_ID);
			boolean cancelWithClick = extras.getBoolean(NotificationBuilder.KEY_NOTI_CANCEL_WITH_CLICK);
			if (cancelWithClick) {
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(notiId);
			}
		}
		return notiId;
	}
}
