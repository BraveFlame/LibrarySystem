package com.librarysystem.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/2/23.
 * 忘记密码时修改，该方法仅仅是输入账号就可修改（因为验证码获取不是利用手机号，故存在弊端）
 */

public class ForgetPassword extends Activity implements View.OnClickListener, Runnable {
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private Button rePassword, getCode;
    private EditText inputId, intoCode;
    private int i = 0, randomcode = 0;
    private Toast mToast;
    private static final int CODE1 = 1, CODE2 = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE1:
                    getCode.setText(msg.arg1 + "秒后重获");
                    break;
                case CODE2:
                    getCode.setText("获取验证码");
                    getCode.setEnabled(true);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        rePassword = (Button) findViewById(R.id.forgetpassword);
        getCode = (Button) findViewById(R.id.forgetgetcode);
        intoCode = (EditText) findViewById(R.id.forgetintocode);
        inputId = (EditText) findViewById(R.id.forgetid);
        libraryDB = LibraryDB.getInstance(this);
          /*
        改密码
         */
        rePassword.setOnClickListener(this);
          /*
          忘记密码，用验证码
           */
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgetpassword:
                try {
                    int id = Integer.valueOf(inputId.getText().toString());
                    BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<>();
                    personMessageBmobQuery.addWhereEqualTo("userId", id);
                    personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                        @Override
                        public void done(List<PersonMessage> list, BmobException e) {
                            if (e == null) {
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if (list.size() == 0) {
                                    inputId.setText("");
                                    useToast("账号不存在或已被注销！");
                                    intoCode.setText("");
                                    manager.cancel(2);
                                } else {
                                    personMessage = list.get(0);
                                    i++;
                                    String code = intoCode.getText().toString();
                                    if (i == 1) {
                                        if (code.equals(String.valueOf(randomcode))) {
                                            intoCode.setHint("请输入新密码");
                                            intoCode.setText("");
                                        } else {
                                            i--;
                                            useToast("验证码错误！");
                                            manager.cancel(2);
                                        }
                                    } else if (i == 2) {
                                        personMessage.setUserPassword(code);
                                        personMessage.update(personMessage.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    useToast("密码修改成功！");
                                                    i = 0;
                                                    finish();

                                                } else {
                                                    useToast("网络异常");
                                                }
                                            }
                                        });
                                        manager.cancel(2);
                                    } else {
                                        i = 0;
                                    }
                                }
                            } else {
                                useToast("网络异常");
                            }
                        }
                    });

                } catch (Exception ee) {
                    useToast("账号格式错误！");
                }
                break;
            case R.id.forgetgetcode:
                getCode.setEnabled(false);
                new Thread(ForgetPassword.this).start();
                break;
            default:
                break;
        }
    }

    public void useToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelToast();
    }

    @Override
    public void run() {
        int a = 11;
        Random random = new Random();
        randomcode = random.nextInt(9000) + 1000;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(ForgetPassword.this).setTicker("收到验证码").setContentTitle("验证码")
                .setContentText("" + randomcode).setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(2, notification.build());
        while (true) {
            a--;
            Message message = new Message();
            if (a <= 0) {
                message.what = CODE2;
                handler.sendMessage(message);
                break;
            } else {
                message.arg1 = a;
                message.what = CODE1;
                handler.sendMessage(message);
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }


        }
    }
}
