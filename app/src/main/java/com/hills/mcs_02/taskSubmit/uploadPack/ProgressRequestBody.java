package com.hills.mcs_02.taskSubmit.uploadPack;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private String mMediaType;
    private UploadCallbacks mListener;

    private int mEachBufferSize = 1024;

    public ProgressRequestBody(final File file, String mediaType, final UploadCallbacks listener) {
        mFile = file;
        mMediaType = mediaType;
        mListener = listener;
    }

    public ProgressRequestBody(final File file, String mediaType, int eachBufferSize, final UploadCallbacks listener) {
        mFile = file;
        mMediaType = mediaType;
        mEachBufferSize = eachBufferSize;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse(mMediaType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] buffer = new byte[mEachBufferSize];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded));
                uploaded += read;
                sink.write(buffer, 0, read);

            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;

        public ProgressUpdater(long uploaded) {
            mUploaded = uploaded;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate(mUploaded);
        }
    }
}
