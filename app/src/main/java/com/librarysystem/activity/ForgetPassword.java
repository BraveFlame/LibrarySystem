package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/2/23.
 * 忘记密码时修改，该方法仅仅是输入账号就可修改（因为验证码获取不是利用手机号，故存在弊端）
 */

public class ForgetPassword extends Activity implements View.OnClickListener, Runnable {

    private PersonMessage personMessage = new PersonMessage();
    private Button rePassword, getCode;
    private EditText inputPhone, intoCode;
    private int i = 0;
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
        inputPhone = (EditText) findViewById(R.id.forgetphone);

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
                BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<>();
                personMessageBmobQuery.addWhereEqualTo("userTel", inputPhone.getText().toString());
                DialogMessage.showDialog(ForgetPassword.this);
                personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                    @Override
                    public void done(List<PersonMessage> list, BmobException e) {
                        if (e == null) {
                            String code = intoCode.getText().toString();
                            if (list.size() == 0) {
                                DialogMessage.closeDialog();
                                inputPhone.setText("");
                                ToastMessage.useToast(ForgetPassword.this, "手机号不存在或已被注销！");
                                intoCode.setText("");
                            } else {
                                personMessage = list.get(0);
                                i++;
                                if (i == 1) {
                                    BmobSMS.verifySmsCode(inputPhone.getText().toString(), code, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            DialogMessage.closeDialog();
                                            if (e == null) {
                                                intoCode.setHint("请输入新密码");
                                                intoCode.setText("");
                                            } else {
                                                i--;
                                                ToastMessage.useToast(ForgetPassword.this, "验证码错误！");
                                            }
                                        }
                                    });

                                } else if (i == 2) {
                                    personMessage.setUserPassword(code);
                                    personMessage.update(personMessage.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            DialogMessage.closeDialog();
                                            if (e == null) {
                                                ToastMessage.useToast(ForgetPassword.this, "密码修改成功！");
                                                i = 0;
                                                finish();
                                            } else {
                                                ToastMessage.useToast(ForgetPassword.this, "修改失败");
                                            }
                                        }
                                    });
                                } else {
                                    i = 0;
                                }
                            }
                        } else {
                            ToastMessage.useToast(ForgetPassword.this, "获取信息异常");
                        }
                    }
                });
                break;
            case R.id.forgetgetcode:
                DialogMessage.showDialog(ForgetPassword.this);
                getCode.setEnabled(false);
                BmobSMS.requestSMSCode(inputPhone.getText().toString(), "注册模板", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        DialogMessage.closeDialog();
                        if (e == null) {
                            ToastMessage.useToast(ForgetPassword.this, "验证码发送成功！");
                            new Thread(ForgetPassword.this).start();
                        } else {
                            ToastMessage.useToast(ForgetPassword.this, "验证码发送失败！");
                            getCode.setEnabled(true);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }

    @Override
    public void run() {
        int a=61;
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
