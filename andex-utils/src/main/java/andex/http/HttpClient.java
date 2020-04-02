package andex.http;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {


    public boolean isWgetStop = false;

    /**
     * 获取远程（HTTP协议）文件
     *
     * @param downloadUrl
     * @param localSavePath
     * @param totalSize     Download file size, unknown if <=0
     * @return
     */
    public boolean wget(String downloadUrl, String localSavePath, long totalSize) {
        InputStream inputStream = null;
        FileOutputStream fileOutput = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File file = new File(localSavePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fileOutput = new FileOutputStream(file);
            inputStream = urlConnection.getInputStream();

            long downloadedSize = 0;
            long downloadSpeed = 0;
            float downloadPercent;
            byte[] buffer = new byte[4096];
            int bufferLength = 0;
            Log.v("wget()", "Download file size: " + totalSize);
            while ((bufferLength = inputStream.read(buffer)) > 0 && !isWgetStop) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // Log.v(TAG, downloadedSize + " bytes have been downloaded.");
                downloadSpeed = downloadedSize;
                downloadPercent = ((downloadedSize * 100) / totalSize) > 100 ? 100
                        : ((downloadedSize * 100) / totalSize);
            }

            // stop = !stop;
            // if (stop) {
            fileOutput.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutput.close();
                inputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }
}
