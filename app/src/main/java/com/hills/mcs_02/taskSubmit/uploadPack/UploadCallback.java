package com.hills.mcs_02.taskSubmit.uploadPack;

public interface UploadCallback {
    /** Single File Upload record*/
    void onProgressUpdate(long uploaded);

    void onError();

    void onFinish();
}
