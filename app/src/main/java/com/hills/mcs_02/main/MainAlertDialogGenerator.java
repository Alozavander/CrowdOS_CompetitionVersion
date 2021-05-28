package com.hills.mcs_02.main;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.hills.mcs_02.R;

public class MainAlertDialogGenerator {

    public static AlertDialog.Builder getDownAlertDialog(Context pContext) {
        /** The prompt box for download pops up */
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setCancelable(false);
        builder.setTitle(R.string.App_Update);
        builder.setMessage(R.string.update_message);
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static AlertDialog.Builder getProgressbarDownAlertDialog(Context pContext){
        /** Create a dialog with progress */
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setCancelable(false);
        builder.setTitle(R.string.App_Update);
        builder.setMessage(" ");
        builder.setView(R.layout.progressbar_layout);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static AlertDialog.Builder getInstallPermissionDialog(Context pContext){
        System.out.println("can not request installs");
        /** The prompt box for download pops up */
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setCancelable(false);
        builder.setTitle("权限授予");           /** Subsequent changes to the string file */
        builder.setMessage("请给予安装权限");

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static AlertDialog.Builder getGPSPermissionDialog(Context pContext){
        return new AlertDialog.Builder(pContext).setTitle("打开GPS")
            .setMessage("前去设置")
            /** Uncheck options */
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.e("MainAlertDilalogGenerator", "GPS开启过程关闭");
                    /** close the dialog */
                    dialogInterface.dismiss();
                }
            })
            .setCancelable(false);
    }
}
