package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by g on 2016/12/2.
 * 注册填写用户信息界面以及更改资料（但此处将修改资料功能取消，仅能改密码）
 */

public class RegisterUser extends Activity implements View.OnClickListener {

    private PersonMessage personMessage = new PersonMessage();
    private EditText userId, userPassword, userName, userprofession;
    private EditText userDescription, userTel;
    private Button personSave, personSet;
    private RadioGroup groupSex;
    private String  userSex;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_data);
        personSave = (Button) findViewById(R.id.personsave);
        personSet = (Button) findViewById(R.id.personset);
        groupSex = (RadioGroup) findViewById(R.id.radioGroup);
        groupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                userSex = rb.getText().toString();
            }
        });
        init();
        personSet.setOnClickListener(this);
        personSave.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        final Intent intentLogin = new Intent(this, Login.class);
        switch (v.getId()) {
            case R.id.personsave:
                /**
                 * 账号不能相同，信息（除兴趣书籍外）不能空白，手机格式要正确
                 */
                    try {
                        personMessage.setUserId(Integer.valueOf(userId.getText().toString()));
                        if (checkMobileNumber(userTel.getText().toString())) {
                            if (!userName.getText().toString().equals("") && !userPassword.getText().toString().equals("") &&
                                    !userSex.equals("") && !userprofession.getText().toString().equals("")
                                    ) {
                                personMessage.setUserPassword(userPassword.getText().toString());
                                personMessage.setUserName(userName.getText().toString());
                                personMessage.setUserSex(userSex);
                                personMessage.setUserProfession(userprofession.getText().toString());
                                personMessage.setUserDescription(userDescription.getText().toString());
                                personMessage.setUserLevel(0);
                                personMessage.setPastBooks(0);
                                personMessage.setWpastBooks(0);
                                personMessage.setNowBorrow(0);
                                personMessage.setUserTel(userTel.getText().toString());
                                personMessage.setIsRootManager("普通用户");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterUser.this);
                                dialog.setTitle("用户注册").setMessage("是否确定注册？").setCancelable(false);
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<PersonMessage>();
                                        personMessageBmobQuery.addWhereEqualTo("userId", personMessage.getUserId());
                                        personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                                            @Override
                                            public void done(List<PersonMessage> list, BmobException e) {
                                                if (e == null) {
                                                    if (list.size() == 0) {
                                                        personMessage.save(new SaveListener<String>() {
                                                            @Override
                                                            public void done(String s, BmobException e) {
                                                                useToast("注册成功！");
                                                                finish();
                                                            }
                                                        });
                                                    } else {
                                                        useToast("该账户已存在！");
                                                    }
                                                } else {
                                                    useToast("网络异常");
                                                }
                                            }
                                        });
                                    }
                                });
                                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog.show();
                            }
                        } else {
                            useToast("手机格式错误！");
                        }
                    } catch (Exception e) {
                        useToast("账号格式错误！");
                    }

                break;
            case R.id.personset:
                    startActivity(intentLogin);
                    useToast("已取消注册！");
                    finish();
                break;
            default:
                break;
        }
    }

    public void init() {

        userId = (EditText) findViewById(R.id.userId);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userName = (EditText) findViewById(R.id.userName);
        userprofession = (EditText) findViewById(R.id.userProfession);
        userDescription = (EditText) findViewById(R.id.userDescription);
        userTel = (EditText) findViewById(R.id.user_tel);

    }

    /***
     * 检验手机号
     *
     * @param mobileNumber
     * @return
     */
    public boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            // Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Pattern regex = Pattern.compile("^1[345789]\\d{9}$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            useToast("手机格式错误！");
            flag = false;

        }
        return flag;
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
}