package andex;

import android.util.Log;

import com.github.swiftech.swifttime.TimeFormatBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import andex.constants.LogConstants;

/**
 * Log to file
 *
 * @author
 */
public class Flog {

    private static FileOutputStream fos;

    /**
     * 打开dir下面以日期为名称的日志文件。
     *
     * @param dir
     * @return
     */
    public static synchronized boolean openFile(String dir) {
        if (fos != null) {
            i("Flog was already inited");
            return true;
        }

        String fName = String.format("%s/%s.log", dir, TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_MINUS.format(Calendar.getInstance()));
        File file = new File(fName);

        return openFile(file);
    }

    public static synchronized boolean openFile(File file) {
        Log.i(LogConstants.LOG_TAG, String.format("Open file %s to log", file));
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
        i("Log to file: " + file);
        return true;
    }

    public static void d(Object msg) {
        if (fos != null) {
            try {
                String wmsg = String.format("%s D %s \r\n", TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_TIME_MINUS.format(Calendar.getInstance()), msg);
//				fos.write((Utils.stringifyDate(Calendar.getInstance()) + "D " + msg + "\r\n").getBytes());
                fos.write(wmsg.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(LogConstants.LOG_TAG, "Flog输出流错误");
        }
        Log.d(LogConstants.LOG_TAG, msg + "");
    }

    public static void i(Object msg) {
        if (fos != null) {
            try {
                String wmsg = String.format("%s I %s \r\n", TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_TIME_MINUS.format(Calendar.getInstance()), msg);
                fos.write(wmsg.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(LogConstants.LOG_TAG, "Flog输出流错误");
        }
        Log.i(LogConstants.LOG_TAG, msg + "");
    }

    public static void w(Object msg) {
        if (fos != null) {
            try {
                String wmsg = String.format("%s W %s \r\n", TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_TIME_MINUS.format(Calendar.getInstance()), msg);
                fos.write(wmsg.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(LogConstants.LOG_TAG, "Flog输出流错误");
        }
        Log.w(LogConstants.LOG_TAG, msg + "");
    }


    public static void e(String msg) {
        e(msg, null);
    }

    public static void e(Exception ex) {
        e(ex.getLocalizedMessage(), ex);
    }

    public static void e(String msg, Exception ex) {
        if (fos != null) {
            try {
                fos.write(String.format("%s E %s \r\n", TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_TIME_MINUS.format(Calendar.getInstance()), msg.getBytes())
                        .getBytes());

                if (ex != null) {
                    String emsg = String.format("%s E %s \r\n", TimeFormatBuilder.TIME_FORMAT_BUILDER_DATE_TIME_MINUS.format(Calendar.getInstance()),
                            ex.getMessage());
                    fos.write(emsg.getBytes());
                    StackTraceElement[] trace = ex.getStackTrace();
                    for (StackTraceElement aTrace : trace) {
                        String traceMsg = String.format("    %s \r\n", aTrace.toString());
                        fos.write(traceMsg.getBytes());
                    }
                }
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(LogConstants.LOG_TAG, "Flog输出流错误");
        }
        Log.e(LogConstants.LOG_TAG, String.format("%s \r\n %s", msg, (ex == null ? "" : ex.getMessage())));
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
