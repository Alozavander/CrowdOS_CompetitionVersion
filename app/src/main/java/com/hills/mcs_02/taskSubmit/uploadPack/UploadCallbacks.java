package com.hills.mcs_02.taskSubmit.uploadPack;

public interface UploadCallbacks {
    //单个文件上传的内存
    void onProgressUpdate(long uploaded);

    void onError();

    void onFinish();
}
