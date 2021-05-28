package com.hills.mcs_02.main;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class OpenApk {
    private Context mContext;
    private File mFile;

    public OpenApk(Context pContext, File pFile) {
        mContext = pContext;
        mFile = pFile;
    }

    public void openApk(){
        if (mFile.isFile()) {
            /** Use Intent to jump to the downloaded file and open it */
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            Uri uri;
            /** Adapt the features of different versions */
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", mFile);
            intent1.setDataAndType(uri, "application/vnd.android.package-archive");
            try {
                if (mContext.getPackageManager().canRequestPackageInstalls()) {
                    System.out.println("JUMP TO APK");
                    intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent1);
                }
            } catch (ActivityNotFoundException exp) {
                exp.printStackTrace();
            }
            System.out.println("openAPK over");
        } else {
            Toast.makeText(mContext, "Download Failure.", Toast.LENGTH_SHORT).show();
        }
    }
}
