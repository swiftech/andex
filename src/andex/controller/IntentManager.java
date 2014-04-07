package andex.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 *
 */
public class IntentManager {

	/**
	 * 发送邮件
	 * @param ctx
	 * @param email
	 * @return
	 */
	public static boolean sendMail(Context ctx, String email) {
		try {
			Uri uri = Uri.parse("mailto:" + email);
			Intent mailIntent = new Intent(Intent.ACTION_SENDTO, uri);
			ctx.startActivity(mailIntent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 呼叫电话。
	 * @param ctx
	 * @param phoneNumber
	 * @return
	 */
	public static boolean callSomebody(Context ctx, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		try {
			ctx.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 剪切图片。
	 * TODO 待测试
	 * @param ctx
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param width
	 * @param height
	 * @return
	 */
	public static boolean cropImage(Context ctx, String inputFilePath, String outputFilePath, int width, int height) {
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			return false;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(inputFile), "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputFilePath)));
		ctx.startActivity(intent);
		return true;
	}
}
