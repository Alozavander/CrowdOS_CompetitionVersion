package com.hills.mcs_02.downloadPack;

import com.blankj.utilcode.util.FileUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadImageUtils {
    private static final String TAG = "DownloadUtil";
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "WeSense" + File.separator +"DownloadFiles";
    /** video download */
    protected DownloadRequest request;
    private Call<ResponseBody> mCall;
    private File mFile;
    private Thread mThread;
    private String mFilePath; /** Download to the local image path */

    public DownloadImageUtils(String baseUrl) {
        if (request == null) {
            /** Initialize the network request interface */
            request = new Retrofit.Builder().baseUrl(baseUrl).build().create(DownloadRequest.class);
        }
    }

    public File downloadFile(String url) {
        String name = url;
        File dirFile =new File(FILE_PATH);
        /** Get the file from the URL and create the local file */
        if (! dirFile.exists()) {
            dirFile.mkdirs();
            Log.e(TAG, "CurrentFileDirPath: " + FILE_PATH);
        }
        mFilePath = FILE_PATH + File.separator + name;
        if (TextUtils.isEmpty(mFilePath)) {
            Log.e(TAG, "CurrentDownloadFilePath: " + mFilePath);
            Log.e(TAG, "downloadFile: The path is null.");
            return null;
        }
        /** Create a file */
        mFile = new File(mFilePath);
        if(mFile.exists()) mFile.delete();
        if (!FileUtils.isFileExists(mFile) && FileUtils.createOrExistsFile(mFile)) {
            if (request == null) {
                Log.e(TAG, "downloadFile: The download interface is null.");
                return null;
            }
            mCall = request.downloadFile(url);
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                   /** Download the file on the child thread */
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            /**  Save to local */
                            writeFile2Disk(response, mFile);
                        }
                    };
                    mThread.start();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                }
            });
        } else {
            return mFile;
        }
        return mFile;
    }

    /** Writes the downloaded file to local storage */
    private void writeFile2Disk(Response<ResponseBody> response, File file) {
        long currentLength = 0;
        OutputStream outputS = null;
        InputStream inputS = response.body().byteStream(); /** Get the download input stream */
        long totalLength = response.body().contentLength();
        try {
            outputS = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = inputS.read(buff)) != -1) {
                outputS.write(buff, 0, len);
                currentLength += len;
                Log.e(TAG, "当前进度: " + currentLength);
            }
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        } finally {
            if (outputS != null) {
                try {
                    outputS.close();
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
            if (inputS != null) {
                try {
                    inputS.close();
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}
