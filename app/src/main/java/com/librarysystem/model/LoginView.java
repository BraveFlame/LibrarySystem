package com.librarysystem.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.librarysystem.R;
import com.librarysystem.activity.Login;

/**
 * Created by g on 2017/2/22.
 */

public class LoginView extends View implements Runnable {
    int logonID[] = {R.mipmap.loginview7, R.mipmap.loginview6};
    // 图片容器
    Bitmap[] pic;
    // 画笔
    Paint myPaint;
    // 控制循环变量
    int num;

    public LoginView(Context context) {
        super(context);
        myPaint = new Paint();
        // 消除锯齿
        myPaint.setAntiAlias(true);
        pic = new Bitmap[2];
        for (int i = 0; i < pic.length; i++) {
            pic[i] = BitmapFactory.decodeResource(context.getResources(), logonID[i]);
            pic[i] = Bitmap.createScaledBitmap(pic[i], (int) Login.ScreenW, (int) Login.ScreenH, true);
        }
        new Thread(this).start();
    }

    boolean threadStop;

    @Override
    public void run() {
        while (!threadStop) {
            num++;
            // 刷新界面方法
            // postInvalidate（）在工作线程中被调用
            // invalidate在UI线程中被调用
            postInvalidate();
            if (num > 19) {
                threadStop = true;
                Login.instans.gotoMain();
            }
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取一张图片，并每10*120毫秒变换一张
        canvas.drawBitmap(pic[num / 10 > 1 ? 1 : (num / 10)], 0, 0, myPaint);
    }
}
