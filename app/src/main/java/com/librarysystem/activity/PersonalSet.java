package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/12/2.
 */

public class PersonalSet extends Activity implements View.OnClickListener{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LibraryDB libraryDB;
    private PersonMessage personMessage=new PersonMessage();
    private EditText userId,userPassword,userName,userSex, userprofession;
    private EditText userDescription;
    private Button personSave,personSet;
    private String whereActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_data);
        Intent intent=getIntent();
        whereActivity=intent.getStringExtra("activity");
        personSave=(Button)findViewById(R.id.personsave);
        personSet=(Button)findViewById(R.id.personset);
        personSet.setOnClickListener(this);
        personSave.setOnClickListener(this);
        libraryDB=LibraryDB.getInstance(this);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPersonalMeassage(personMessage,pref.getInt("userId",200000));

        userId=(EditText) findViewById(R.id.userId);
        userPassword=(EditText)findViewById(R.id.userPassword);
        userName=(EditText)findViewById(R.id.userName);
        userSex=(EditText)findViewById(R.id.userSex);
        userprofession=(EditText)findViewById(R.id.userProfession);
        userDescription=(EditText)findViewById(R.id.userDescription);

        if(whereActivity.equals("personSet")) {
            userName.setText(personMessage.getUserName());
            userName.setEnabled(false);
            userPassword.setText(personMessage.getUserPassword());
            userPassword.setEnabled(false);
            userId.setText(String.valueOf(personMessage.getUserId()));
            userId.setEnabled(false);
            userSex.setText(personMessage.getUserSex());
            userSex.setEnabled(false);
            userprofession.setText(personMessage.getUserProfession());
            userprofession.setEnabled(false);
            userDescription.setText(personMessage.getUserDescription());
            userDescription.setEnabled(false);
        }
        else if(whereActivity.equals("register")){
           personSave.setText("确定",null);
            personSet.setText("取消",null);
       }

    }


    @Override
    public void onClick(View v) {
        Intent intentLogin=new Intent(this,Login.class);
        switch (v.getId()){
            case R.id.personsave:
                if(whereActivity.equals("personSet")){
                personMessage.setUserName(userName.getText().toString());
                personMessage.setUserSex(userSex.getText().toString());
                personMessage.setUserProfession(userprofession.getText().toString());
                personMessage.setUserDescription(userDescription.getText().toString());
                libraryDB.alterPersonalMessage(personMessage,personMessage.getUserId());
                userId.setEnabled(false);
                userName.setEnabled(false);
                userSex.setEnabled(false);
                userprofession.setEnabled(false);
                    userDescription.setEnabled(false);
                }else if(whereActivity.equals("register")){
                    personMessage.setUserId(Integer.valueOf(userId.getText().toString()));
                    personMessage.setUserPassword(userPassword.getText().toString());
                    personMessage.setUserName(userName.getText().toString());
                    personMessage.setUserSex(userSex.getText().toString());
                    personMessage.setUserProfession(userprofession.getText().toString());
                    personMessage.setUserDescription(userDescription.getText().toString());
                    if(libraryDB.savePersonalMeassage(personMessage))
                    { Toast.makeText(this,"注册成功",Toast.LENGTH_LONG);

                    startActivity(intentLogin);
                    finish();}
                    else
                    Toast.makeText(PersonalSet.this,"该账户已存在！",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.personset:
                if(whereActivity.equals("personSet")) {
                    userName.setEnabled(true);
                    userSex.setEnabled(true);
                    userprofession.setEnabled(true);
                    userDescription.setEnabled(true);
                }else if(whereActivity.equals("register")){
                    startActivity(intentLogin);
                    Toast.makeText(PersonalSet.this,"已取消注册！",Toast.LENGTH_SHORT);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
