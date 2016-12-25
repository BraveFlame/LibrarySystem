package com.librarysystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/10/15.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText userEdit, passwordEdit;
    private CheckBox rememberPassword;
    private Button register, login;
    private TextView forgetPassword;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LibraryDB libraryDB;
    private PersonMessage personMessage=new PersonMessage();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        initOnclick();
        libraryDB=LibraryDB.getInstance(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        boolean change_user=getIntent().getBooleanExtra("change",false);

            if (isRemember) {

                int user = pref.getInt("user", 0);
                String password = pref.getString("password", "");
                userEdit.setText(""+user);
                passwordEdit.setText(password);
                rememberPassword.setChecked(true);
                if(!change_user) {
                startActivity(new Intent(this, MainPage.class));


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
        // user.setOnClickListener(this);
        //password.setOnClickListener(this);
        // rememberPassword.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                String data="register";
                Intent registerIntent=new Intent(this,PersonalSet.class);
                registerIntent.putExtra("activity",data);
                startActivity(registerIntent);
                break;
            case R.id.login_button:
                if (userLogin()) {
                    Intent loginIntent = new Intent(this, MainPage.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.forget_password:
                break;
            default:
                break;
        }
    }

    public boolean userRegister() {
        return true;
    }

    public boolean userLogin() {



        int userInput =Integer.valueOf(userEdit.getText().toString());
        String passwordInput = passwordEdit.getText().toString();
        libraryDB.getPersonalMeassage(personMessage,userInput);
        int userId=personMessage.getUserId();
        String password=personMessage.getUserPassword();
        if ((userInput==userId) && passwordInput.equals(password)) {

            editor = pref.edit();
            editor.putInt("userId",userInput);
            if (rememberPassword.isChecked()) {
                editor.putBoolean("remember_password", true);
                editor.putInt("user", userInput);
                editor.putString("password", passwordInput);

            } else {
                editor.clear();
            }
            editor.commit();
            return true;
        } else return false;
    }

    public boolean isForgetPassword() {
        return true;
    }
}
