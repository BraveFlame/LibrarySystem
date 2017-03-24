package com.librarysystem.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;

import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/2/22.
 * 用于用户修改密码的activity
 * 其中验证码仅是在本活动中生成随机4位数，再通过NotificationManager ,Notification.Builder类进行
 * 通知，为了避免频繁触发，利用线程限制验证码获取间隔10s。
 */

public class AlterPassword extends Activity implements Runnable {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private PersonMessage personMessage = new PersonMessage();
    private Button rePassword, getCode;
    private EditText intoCode;
    private int i = 0, randomcode = 0;
    private Toast mToast;
    private static final int CODE1 = 1, CODE2 = 2;
    /**
     * 由于线程无法处理UI，故用Handler类进行处理，以实现验证码获取时间间隔
     */
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
        setContentView(R.layout.alter_password);
        init();
         personMessage=(PersonMessage)getIntent().getSerializableExtra("alterpassword");
        /**
         *更改密码
         */
                rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i作为标志，判断第几次输入，第一次为原密码或验证码，第二次为新密码。
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                i++;
                String code = intoCode.getText().toString();
                if (i == 1) {
                    if (code.equals(personMessage.getUserPassword()) || code.equals(String.valueOf(randomcode))) {
                        intoCode.setHint("请输入新密码");
                        intoCode.setText("");
                        manager.cancel(1);
                    } else {
                        //原密码或验证码不对时i要减掉1
                        i--;
                        useToast("密码或验证码错误！");
                    }
                } else if (i == 2) {
                    //第二次为新密码
                    personMessage.setUserPassword(code);
                    personMessage.update(pref.getString("objectid", ""), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null) {
                               useToast("修改成功");
                                editor = pref.edit();
                                editor.putBoolean("remember_password", false);
                                editor.commit();
                                finish();
                            }else {
                                useToast("修改失败");

                            }
                        }
                    });


                } else {
                    //改完重新为0，以便再改
                    i = 0;
                }
            }
        });
          /*
          忘记密码时，用验证码
           */
        getCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCode.setEnabled(false);
                new Thread(AlterPassword.this).start();
            }
        });
    }

    @Override
    public void run() {
        //停止的秒数
        int a = 11;
        Random random = new Random();
        randomcode = random.nextInt(9000) + 1000;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(AlterPassword.this).setTicker("收到验证码").setContentTitle("验证码")
                .setContentText("" + randomcode).setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, notification.build());

        while (true) {
            a--;
            //小于等于0时解除按键禁止
            Message message = new Message();
            if (a <= 0) {
                message.what = CODE2;
                handler.sendMessage(message);
                break;
            } else {
                //否则显示秒数
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

    public void init() {
        rePassword = (Button) findViewById(R.id.repassword);
        getCode = (Button) findViewById(R.id.getcode);
        intoCode = (EditText) findViewById(R.id.intocode);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * 解决Toast频繁显示
     */
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

}



