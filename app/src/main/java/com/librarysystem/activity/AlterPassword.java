package com.librarysystem.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.Random;

/**
 * Created by g on 2017/2/22.
 */

public class AlterPassword extends Activity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private Button rePassword, getCode;
    private EditText intoCode;
    private int i = 0, randomcode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_password);

        rePassword = (Button) findViewById(R.id.repassword);
        getCode = (Button) findViewById(R.id.getcode);
        intoCode = (EditText) findViewById(R.id.intocode);
        libraryDB = LibraryDB.getInstance(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 200000));
        /*
        改密码
         */
        rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(1);
                i++;
                String code = intoCode.getText().toString();
                if (i == 1) {
                    if (code.equals(personMessage.getUserPassword()) || code.equals(String.valueOf(randomcode))) {
                        intoCode.setHint("请输入新密码");
                        intoCode.setText("");
                    } else {
                        i--;
                        Toast.makeText(AlterPassword.this, "密码或验证码错误！", Toast.LENGTH_SHORT).show();
                    }

                } else if (i == 2) {
                    personMessage.setUserPassword(code);
                    libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
                    Toast.makeText(AlterPassword.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                    pref.edit().putBoolean("remember_password",false);
                    pref.edit().clear();
                    pref.edit().commit();
                    finish();
                } else {
                    i = 0;
                }

            }
        });
          /*
          忘记密码，用验证码
           */
        getCode.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Random random = new Random();
                randomcode = random.nextInt(9000) + 1000;
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder notification=new Notification.Builder(AlterPassword.this).setTicker("收到验证码").setContentTitle("验证码")
                        .setContentText(""+randomcode).setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
                manager.notify(1,notification.build());


            }
        });

    }
}
