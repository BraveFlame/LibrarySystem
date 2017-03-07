package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by g on 2016/12/2.
 * 注册填写用户信息界面以及更改资料（但此处将修改资料功能取消，仅能改密码）
 */

public class PersonalSet extends Activity implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private EditText userId, userPassword, userName, userprofession;
    private EditText userDescription, userTel;
    private Button personSave, personSet;
    private RadioGroup groupSex;
    private String whereActivity, userSex;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_data);
        Intent intent = getIntent();
        whereActivity = intent.getStringExtra("activity");
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
        libraryDB = LibraryDB.getInstance(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 200000));

//        if (whereActivity.equals("personSet")) {
//            userName.setText(personMessage.getUserName());
//            userName.setEnabled(false);
//            userPassword.setText(personMessage.getUserPassword());
//            userPassword.setEnabled(false);
//            userId.setText(String.valueOf(personMessage.getUserId()));
//            userId.setEnabled(false);
//            //userSex.setText(personMessage.getUserSex());
//            //userSex.setEnabled(false);
//            userprofession.setText(personMessage.getUserProfession());
//            userprofession.setEnabled(false);
//            userDescription.setText(personMessage.getUserDescription());
//            userDescription.setEnabled(false);
//        } else
        if (whereActivity.equals("register")) {
            personSave.setText("确定", null);
            personSet.setText("取消", null);

        }

    }


    @Override
    public void onClick(View v) {
        final Intent intentLogin = new Intent(this, Login.class);
        switch (v.getId()) {
            case R.id.personsave:
//                if (whereActivity.equals("personSet")) {
//                    personMessage.setUserName(userName.getText().toString());
//                    personMessage.setUserSex(userSex);
//                    personMessage.setUserProfession(userprofession.getText().toString());
//                    personMessage.setUserDescription(userDescription.getText().toString());
//                    libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
//                    userId.setEnabled(false);
//                    userName.setEnabled(false);
//                    // userSex.setEnabled(false);
//                    userprofession.setEnabled(false);
//                    userDescription.setEnabled(false);
//                } else
                /**
                 * 账号不能相同，信息（除兴趣书籍外）不能空白，手机格式要正确
                 */
                if (whereActivity.equals("register")) {
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
                                personMessage.setUserLevel("0");
                                personMessage.setPastBooks("0");
                                personMessage.setWpastBooks("0");
                                personMessage.setUserTel(userTel.getText().toString());
                                personMessage.setIsRootManager("普通用户");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalSet.this);
                                dialog.setTitle("用户注册").setMessage("是否确定注册？").setCancelable(false);
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (libraryDB.savePersonalMeassage(personMessage)) {
                                            Toast.makeText(PersonalSet.this, "注册成功", Toast.LENGTH_LONG).show();
                                            startActivity(intentLogin);
                                            finish();
                                        } else
                                            useToast("该账户已存在！");
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

                }
                break;
            case R.id.personset:
//                if (whereActivity.equals("personSet")) {
//                    userName.setEnabled(true);
//                    // userSex.setEnabled(true);
//                    userprofession.setEnabled(true);
//                    userDescription.setEnabled(true);
//                } else
                if (whereActivity.equals("register")) {
                    startActivity(intentLogin);
                    useToast("已取消注册！");
                    finish();
                }
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
