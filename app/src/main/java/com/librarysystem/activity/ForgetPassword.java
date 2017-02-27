package com.librarysystem.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.Random;

/**
 * Created by g on 2017/2/23.
 */

public class ForgetPassword extends Activity implements View.OnClickListener {
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private Button rePassword, getCode;
    private EditText inputId, intoCode;
    private int i = 0, randomcode = 0;

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
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        switch (v.getId()) {
            case R.id.forgetpassword:
                try {
                    int id = Integer.valueOf(inputId.getText().toString());
                    libraryDB.getPersonalMeassage(personMessage, id);
                    if (personMessage.getUserName() == null) {
                        inputId.setText("");
                        Toast.makeText(ForgetPassword.this, "账号不存在或已被注销！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } catch (Exception e) {
                    Toast.makeText(ForgetPassword.this, "账号格式错误！", Toast.LENGTH_SHORT).show();
                    break;
                }
                manager.cancel(2);
                i++;
                String code = intoCode.getText().toString();
                if (i == 1) {
                    if (code.equals(String.valueOf(randomcode))) {
                        intoCode.setHint("请输入新密码");
                        intoCode.setText("");
                    } else {
                        i--;
                        Toast.makeText(ForgetPassword.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                    }

                } else if (i == 2) {
                    personMessage.setUserPassword(code);
                    libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
                    Toast.makeText(ForgetPassword.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    i = 0;
                } else {
                    i = 0;
                }


                break;
            case R.id.forgetgetcode:
                Random random = new Random();
                randomcode = random.nextInt(9000) + 1000;

                Notification.Builder notification = new Notification.Builder(this).setTicker("收到验证码").setContentTitle("验证码")
                        .setContentText("" + randomcode).setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
                manager.notify(2, notification.build());


                break;
            default:
                break;
        }
    }
}
