package com.librarysystem.others;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by g on 2017/3/25.
 */

public class DialogMessage {
private static ProgressDialog progressDialog;
    /**
     * 显示进度对话框
     */
    public static void showDialog(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
    }
    /**
     * 取消进度对话框
     */
    public static void closeDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
