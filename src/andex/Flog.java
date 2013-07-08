package andex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Flog {
	
	private static FileOutputStream fos;
	
	public static synchronized boolean openFile(String dir) {
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
		return true;
	}
	
	public static void d(Object msg) {
		if(fos != null) {
			try {
				String wmsg = String.format("%s D %s \r\n", Utils.stringifyDate(Calendar.getInstance()), msg);
//				fos.write((Utils.stringifyDate(Calendar.getInstance()) + "D " + msg + "\r\n").getBytes());
				fos.write(wmsg.getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void i(Object msg) {
		if(fos != null) {
			try {
				String wmsg = String.format("%s I %s \r\n", Utils.stringifyDate(Calendar.getInstance()), msg);
				fos.write(wmsg.getBytes());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
