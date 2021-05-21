package com.hills.mcs_02.downloadPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;




public class DownloadFileUtils {
    private static final String TAG = "DownloadUtil";
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "WeSense" + File.separator + "DownloadFiles";   //注意此处路径应该与xml文件中path里download_path一致
    //视频下载相关
    protected PostRequestGetFile request;
    private Call<ResponseBody> mCall;
    private File mFile;
    private Thread mThread;
    private String mFilePath; //下载到本地的路径

    public DownloadFileUtils(String baseUrl) {
        if (request == null) {
            //初始化网络请求接口
            request = new Retrofit.Builder().baseUrl(baseUrl).build().create(PostRequestGetFile.class);
        }
    }

    public File downloadFile(String url) {
        //使用时间戳命名
        String name = url;
        File dirFile = new File(FILE_PATH);
        //通过Url得到文件并创建本地文件
        if (!dirFile.exists()) {
            //int i = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
            //if (i != -1) {
            //    name = name.substring(i);
            //    mFilePath = FILE_PATH +
            //            name;
            //}
            dirFile.mkdirs();
            Log.e(TAG, "CurrentFileDirPath: " + FILE_PATH);
        }
        mFilePath = FILE_PATH + File.separator + name;
        if (TextUtils.isEmpty(mFilePath)) {
            Log.e(TAG, "CurrentDownloadFilePath: " + mFilePath);
            Log.e(TAG, "downloadFile: The path is null.");
            return null;
        }
        //建立一个文件
        mFile = new File(mFilePath);
        if (mFile.exists()) mFile.delete();
        if (!FileUtils.isFileExists(mFile) && FileUtils.createOrExistsFile(mFile)) {
            if (request == null) {
                Log.e(TAG, "downloadFile: The download interface is null.");
                return null;
            }
            mCall = request.getFile("1");
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    //下载文件放在子线程
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //保存到本地
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

    //带进度listender的下载方法
    public File downloadFile(String url, final DownloadListener downloadListener) {
        //使用时间戳命名
        String name = url;
        File dirFile = new File(FILE_PATH);
        //通过Url得到文件并创建本地文件
        if (!dirFile.exists()) {
            //int i = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
            //if (i != -1) {
            //    name = name.substring(i);
            //    mFilePath = FILE_PATH +
            //            name;
            //}
            dirFile.mkdirs();
            Log.e(TAG, "CurrentFileDirPath: " + FILE_PATH);
        }
        mFilePath = FILE_PATH + File.separator + name;
        if (TextUtils.isEmpty(mFilePath)) {
            Log.e(TAG, "CurrentDownloadFilePath: " + mFilePath);
            Log.e(TAG, "downloadFile: The path is null.");
            return null;
        }
        //建立一个文件
        mFile = new File(mFilePath);
        if (mFile.exists()) mFile.delete();
        if (!FileUtils.isFileExists(mFile) && FileUtils.createOrExistsFile(mFile)) {
            if (request == null) {
                Log.e(TAG, "downloadFile: The download interface is null.");
                return null;
            }
            mCall = request.getFile("1");
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    //下载文件放在子线程
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //保存到本地

                            writeFile2Disk(response, mFile, downloadListener);

                        }
                    };
                    mThread.start();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    downloadListener.onFailure(); //下载失败
                }
            });
        } else {
            downloadListener.onFinish(mFilePath); //下载完成
        }
        return mFile;
    }


    //将下载的文件写入本地存储
    private void writeFile2Disk(Response<ResponseBody> response, File file) {
        long currentLength = 0;
        OutputStream outputS = null;
        System.out.println("response message:"+response.message());
        System.out.println("response headers:"+response.headers().toString());
        InputStream inputS = response.body().byteStream(); //获取下载输入流
        long totalLength = response.body().contentLength();
        try {
            outputS = new FileOutputStream(file); //输出流
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
                    outputS.close(); //关闭输出流
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
            if (inputS != null) {
                try {
                    inputS.close(); //关闭输入流
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    //带进度listener的文件写出方法
    private void writeFile2Disk(Response<ResponseBody> response, File file, DownloadListener downloadListener) {
        downloadListener.onStart();
        long currentLength = 0;
        OutputStream outputS = null;
        System.out.println("报文是否为空：" + (response.body() == null));
        InputStream inputS = response.body().byteStream(); //获取下载输入流
        long totalLength = response.body().contentLength();
        //System.out.println("totalLength:" + totalLength);
        try {
            outputS = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = inputS.read(buff)) != -1) {
                outputS.write(buff, 0, len);
                currentLength += len;
                //System.out.println("currentLength:" + currentLength);
                //Log.e(TAG, "当前进度: " + currentLength);
                //计算当前下载百分比，并经由回调传出
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
                //System.out.println("percentage:" + (int) (100 * currentLength / totalLength));
            }
            downloadListener.onFinish(mFilePath); //下载完成
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();

        } finally {
            if (outputS != null) {
                try {
                    outputS.close(); //关闭输出流
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
            if (inputS != null) {
                try {
                    inputS.close(); //关闭输入流
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}
