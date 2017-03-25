package com.librarysystem.others;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by g on 2017/3/25.
 */

public class ToastMessage {
    private static Toast mToast;
    /**
     * 解救Toast频繁显示
     *
     * @param text
     */

    public static void useToast(Context context,String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


}
