package com.hills.mcs_02.downloadPack;

public interface DownloadListener {
    void onStart();

    void onProgress(int currentLength);

    void onFinish(String localPath);

    void onFailure();
}
