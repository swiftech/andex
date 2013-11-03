package andex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.util.Log;

/**
 * Log to file
 * @author 
 *
 */
public class Flog {
	
	private static FileOutputStream fos;
	
	/**
	 * 打开dir下面以日期为名称的日志文件。
	 * @param dir
	 * @return
	 */
	public static synchronized boolean openFile(String dir) {
		if (fos != null) {
			i("Flog was already inited");
			return true;
		}
		String fname = dir + "/" + Utils.stringifyDate(Calendar.getInstance()) + ".log";
		File file = new File(fname);
		
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		i("Log to file: " + fname);
		return true;
	}
	
	public static void d(Object msg) {
		if(fos != null) {
			try {
				String wmsg = String.format("%s D %s \r\n", Utils.stringifyTime(Calendar.getInstance()), msg);
//				fos.write((Utils.stringifyDate(Calendar.getInstance()) + "D " + msg + "\r\n").getBytes());
				fos.write(wmsg.getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Log.w("", "Flog输出流错误");
		}
		Log.d("andex", msg + "");
	}
	
	public static void i(Object msg) {
		if(fos != null) {
			try {
				String wmsg = String.format("%s I %s \r\n", Utils.stringifyTime(Calendar.getInstance()), msg);
				fos.write(wmsg.getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Log.w("", "Flog输出流错误");
		}
		Log.i("andex", msg + "");
	}
	
	public static void w(Object msg) {
		if(fos != null) {
			try {
				String wmsg = String.format("%s W %s \r\n", Utils.stringifyTime(Calendar.getInstance()), msg);
				fos.write(wmsg.getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Log.w("", "Flog输出流错误");
		}
		Log.w("andex", msg + "");
	}
	
	
	public static void e(String msg) {
		e(msg, null);
	}
	
	public static void e(Exception ex) {
		e(ex.getLocalizedMessage(), ex);
	}
	
	public static void e(String msg, Exception ex) {
		if(fos != null) {
			try {
				fos.write(String.format("%s E %s \r\n", Utils.stringifyTime(Calendar.getInstance()), msg.getBytes())
						.getBytes());
				
				if (ex != null) {
					String emsg = String.format("%s E %s \r\n", Utils.stringifyTime(Calendar.getInstance()),
							ex.getMessage());
					fos.write(emsg.getBytes());
					StackTraceElement[] trace = ex.getStackTrace();
					for (int i = 0; i < trace.length; i++) {
						String traceMsg = String.format("    %s \r\n", trace[i].toString());
						fos.write(traceMsg.getBytes());
					}
				}
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Log.w("", "Flog输出流错误");
		}
		Log.e("andex", String.format("%s \r\n %s", msg, (ex == null ? "" : ex.getMessage())));
	}
	
	public static void closeFile() {
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
