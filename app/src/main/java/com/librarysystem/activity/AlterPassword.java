package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
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
    private Button rePassword, alter_code, alter_sure;
    private EditText intoCode, preTel, getCode;
    private int i = 0, a = 61, j = 0;
    private static final int CODE1 = 1, CODE2 = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE1:
                    alter_code.setText(msg.arg1 + "秒后重获");
                    break;
                case CODE2:
                    alter_code.setText("获取验证码");
                    alter_code.setEnabled(true);
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
        personMessage = (PersonMessage) getIntent().getSerializableExtra("alterpassword");
        /**
         *更改密码
         */
        rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i作为标志，判断第几次输入，第一次为原密码或验证码，第二次为新密码。
                i++;
                String code = intoCode.getText().toString();
                if (i == 1) {
                    if (code.equals(personMessage.getUserPassword())) {
                        intoCode.setHint("请输入新密码");
                        intoCode.setText("");
                    } else {
                        //原密码不对时i要减掉1
                        i--;
                        ToastMessage.useToast(AlterPassword.this, "密码错误！");
                    }
                } else if (i == 2) {
                    //第二次为新密码
                    personMessage.setUserPassword(code);
                    DialogMessage.showDialog(AlterPassword.this);
                    personMessage.update(pref.getString("objectid", ""), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            DialogMessage.closeDialog();
                            if (e == null) {
                                ToastMessage.useToast(AlterPassword.this, "修改成功");
                                editor = pref.edit();
                                editor.putBoolean("remember_password", false);
                                editor.commit();
                                finish();
                            } else {
                                ToastMessage.useToast(AlterPassword.this, "修改失败");

                            }
                        }
                    });
                } else {
                    //改完重新为0，以便再改
                    i = 0;
                }
            }
        });
        /**
         *更改手机号时，原手机号的验证码
         */
        alter_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showDialog(AlterPassword.this);
                alter_code.setEnabled(false);
                BmobSMS.requestSMSCode(preTel.getText().toString(), "注册模板", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        DialogMessage.closeDialog();
                        if (e == null) {
                            ToastMessage.useToast(AlterPassword.this, "验证码发送成功！");
                            new Thread(AlterPassword.this).start();
                        } else {
                            ToastMessage.useToast(AlterPassword.this, "验证码发送失败！");
                            alter_code.setEnabled(true);
                        }
                    }
                });
            }
        });

        /**
         *确定更改手机号按键
         */

        alter_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j == 0) {
                    if (!personMessage.getUserTel().equals(preTel.getText().toString())) {
                        ToastMessage.useToast(AlterPassword.this, "手机号不是原来手机号！");
                    } else {
                        DialogMessage.showDialog(AlterPassword.this);
                        BmobSMS.verifySmsCode(preTel.getText().toString(), getCode.getText().toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {
                                    preTel.setHint("请输新手机号！");
                                    preTel.setText("");
                                    getCode.setText("");
                                    a = 0;
                                    getCode.setHint("新手机号验证码！");
                                    j++;
                                } else {
                                    ToastMessage.useToast(AlterPassword.this, "验证码错误！");
                                }
                            }
                        });
                    }
                } else if (j == 1) {
                    if (preTel.getText().toString().equals(personMessage.getUserTel())) {
                        ToastMessage.useToast(AlterPassword.this, "手机号未改变！");
                    } else {
                        DialogMessage.showDialog(AlterPassword.this);
                        BmobSMS.verifySmsCode(preTel.getText().toString(), getCode.getText().toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    personMessage.setUserTel(preTel.getText().toString());
                                    personMessage.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            DialogMessage.closeDialog();
                                            if (e == null) {
                                                j = 0;
                                                ToastMessage.useToast(AlterPassword.this, "手机号修改成功！");
                                                finish();
                                            } else {
                                                ToastMessage.useToast(AlterPassword.this, "手机号修改失败！");
                                            }
                                        }
                                    });
                                } else {
                                    DialogMessage.closeDialog();
                                    ToastMessage.useToast(AlterPassword.this, "手机号或验证码错误！");
                                }
                            }
                        });
                    }
                } else {
                    j = 0;
                }
            }
        });

    }

    public void init() {
        rePassword = (Button) findViewById(R.id.repassword);
        intoCode = (EditText) findViewById(R.id.intocode);
        preTel = (EditText) findViewById(R.id.pre_tel);
        getCode = (EditText) findViewById(R.id.tel_getcode);
        alter_code = (Button) findViewById(R.id.alter_tel_code);
        alter_sure = (Button) findViewById(R.id.sure_later_tel);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }

    @Override
    public void run() {
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



