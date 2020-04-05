package andex;

import java.util.Calendar;

import android.util.Log;

public class Profiler {

    private static long startTime = 0;
    private static long endTime = 0;

    public static void start() {
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    public static void end(String tag) {
        endTime = Calendar.getInstance().getTimeInMillis();
        Log.d("andex", String.format("  PRF: %s  %d -> %d = %dms", tag, startTime, endTime, endTime - startTime));
    }
}
