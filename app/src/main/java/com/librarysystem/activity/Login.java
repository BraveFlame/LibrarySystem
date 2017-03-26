package com.librarysystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.ActivityCollector;
import com.librarysystem.model.LoginView;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by g on 2016/10/15.
 * 启动app的第一个活动，用于登录和注册和修改密码
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText userEdit, passwordEdit;
    private CheckBox rememberPassword;
    private Button register, login;
    private TextView forgetPassword,userKnow;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int userInput;
    private String passwordInput;
    public static float ScreenW, ScreenH;
    public static Login instans;
    private LoginView lv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        initOnclick();
        instans = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ScreenH = metrics.heightPixels;
        ScreenW = metrics.widthPixels;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        final boolean change_user = getIntent().getBooleanExtra("change", false);
/**
 * 如果记住密码则判断是否正确再决定自动登录与否
 */
        if (isRemember) {
            int user = pref.getInt("user", 0);
            String password = pref.getString("password", "");
            userEdit.setText("" + user);
            passwordEdit.setText(password);
            rememberPassword.setChecked(true);
            /**
             * 这里是判断当前活动是不是由主页跳转（即切换账户），是则不会自动登录
             *
             */
            if (!change_user) {
                lv = new LoginView(Login.this);
                setContentView(lv);
            }

        }
    }

    public void initOnclick() {
        userEdit = (EditText) findViewById(R.id.input_login_username);
        passwordEdit = (EditText) findViewById(R.id.input_login_password);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        register = (Button) findViewById(R.id.register_button);
        login = (Button) findViewById(R.id.login_button);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        userKnow=(TextView)findViewById(R.id.user_know);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        userKnow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                Intent registerIntent = new Intent(this, RegisterUser.class);
                startActivity(registerIntent);
                break;
            case R.id.login_button:
                if (userEdit.getText().toString().equals("")) {
                    ToastMessage.useToast(Login.this, "用户名或密码错误！");
                }else{
                DialogMessage.showDialog(Login.this);
                userLogin();}
                break;
            case R.id.forget_password:
                Intent alterpassword = new Intent(this, ForgetPassword.class);
                startActivity(alterpassword);
                break;
            case R.id.user_know:
                Intent knowIntent=new Intent(this,UsersKnow.class);
                startActivity(knowIntent);
            default:
                break;
        }
    }

    public void gotoMain() {
        Intent loginIntent = new Intent(Login.this, MainPage.class);
        startActivity(loginIntent);
        finish();
    }

    /**
     * 判断账户和密码是否符合
     *
     * @return
     */
    public void userLogin() {
        try {
            userInput = Integer.valueOf(userEdit.getText().toString());
            passwordInput = passwordEdit.getText().toString();
            /**
             *
             */
            BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<PersonMessage>();
            personMessageBmobQuery.addWhereEqualTo("userId", userInput);
            personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                @Override
                public void done(List<PersonMessage> list, BmobException e) {
                    DialogMessage.closeDialog();
                    if (e == null) {
                        if (list.size() == 1) {
                            PersonMessage personMessage = list.get(0);
                            int userId = personMessage.getUserId();
                            String password = personMessage.getUserPassword();
                            if ((userInput == userId) && passwordInput.equals(password)) {
                                editor = pref.edit();
/**
 * 记住密码时保存信息
 */
                                if (rememberPassword.isChecked()) {
                                    editor.putBoolean("remember_password", true);
                                    editor.putInt("user", userInput);
                                    editor.putString("password", passwordInput);
                                } else {
                                    editor.clear();
                                }
                                editor.commit();
                                editor.putString("objectid", personMessage.getObjectId());
                                editor.putInt("userId", userInput);
                                editor.putString("root", personMessage.getIsRootManager());
                                editor.commit();
                                /**
                                 * 登陆时轮播两张图共1.2秒
                                 */
                                lv = new LoginView(Login.this);
                                setContentView(lv);
                                /**
                                 * 再次登录时把之前的主页活动销毁，以免显示上个人的搜索以及提醒等信息
                                 */
                                ActivityCollector.finishAll();

                            } else {

                                ToastMessage.useToast(Login.this, "用户名或密码错误！");
                            }

                        } else {
                            ToastMessage.useToast(Login.this, "用户不存在");

                        }


                    } else {
                        ToastMessage.useToast(Login.this, "网络异常！");
                    }
                }
            });
        } catch (Exception e) {
            DialogMessage.closeDialog();
            ToastMessage.useToast(Login.this, "格式错误！");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }
}
