package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * Created by g on 2017/2/22.
 * 账户信息以及跳到修改密码界面
 */

public class UserCcount extends Activity {
    private SharedPreferences pref;
    private TextView accountId, accountName, accountSex, accountpro, accounthobby, accounttel,
            accountlevel, accountpast, accountwpast, userProperty;
    private Button alterPassword;
    private ImageView renew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ccount);
        alterPassword = (Button) findViewById(R.id.usercpassword);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        init();
        renew();
        renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renew();
            }
        });

    }

    private void renew() {
        DialogMessage.showDialog(UserCcount.this);
        BmobQuery<PersonMessage> personMessage = new BmobQuery<PersonMessage>();
        personMessage.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
            @Override
            public void done(PersonMessage personMessage, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null) {
                    accountName.setText("姓名：" + personMessage.getUserName().toString());
                    accountSex.setText("性别：" + personMessage.getUserSex().toString());
                    accountId.setText("账户：" + String.valueOf(personMessage.getUserId()));
                    accounthobby.setText("书籍爱好：" + personMessage.getUserDescription().toString());
                    accountpro.setText("专业：" + personMessage.getUserProfession().toString());
                    accountlevel.setText("借阅等级：" + String.valueOf(personMessage.getUserLevel()));
                    accountpast.setText("逾期书本：" + String.valueOf(personMessage.getPastBooks()));
                    accountwpast.setText("即将到期：" + String.valueOf(personMessage.getWpastBooks()));
                    accountlevel.setText("借阅等级：" + personMessage.getUserLevel().toString());
                    accountpast.setText("逾期书本：" + personMessage.getPastBooks().toString());
                    accountwpast.setText("即将到期：" + personMessage.getWpastBooks().toString());
                    accounttel.setText("联系方式：" + personMessage.getUserTel().toString());
                    userProperty.setText("属性：" + personMessage.getIsRootManager().toString());
                    final Intent alterpassword = new Intent(UserCcount.this, AlterPassword.class);
                    alterpassword.putExtra("alterpassword", personMessage);
                    alterPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(alterpassword);
                        }
                    });
                } else {
                    ToastMessage.useToast(UserCcount.this, "未联网！");
                    alterPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastMessage.useToast(UserCcount.this, "请先联网！");
                        }
                    });
                }
            }
        });


    }

    public void init() {
        accountName = (TextView) findViewById(R.id.usercname);
        accountSex = (TextView) findViewById(R.id.usercsex);
        accountId = (TextView) findViewById(R.id.usercId);
        accountpro = (TextView) findViewById(R.id.usercprofession);
        accounthobby = (TextView) findViewById(R.id.userchobby);
        accounttel = (TextView) findViewById(R.id.userctel);
        accountlevel = (TextView) findViewById(R.id.userclevel);
        accountpast = (TextView) findViewById(R.id.userclate);
        accountwpast = (TextView) findViewById(R.id.usercwpastbook);
        userProperty = (TextView) findViewById(R.id.user_property);
        renew = (ImageView) findViewById(R.id.renew_ccount);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }
}
