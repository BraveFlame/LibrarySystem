package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * Created by g on 2017/2/22.
 * 账户信息以及跳到修改密码界面
 */

public class UserCcount extends Activity {
    private Toast mToast;
    private SharedPreferences pref;
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private TextView accountId, accountName, accountSex, accountpro, accounthobby, accounttel,
            accountlevel, accountpast, accountwpast, userProperty;
    private Button alterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ccount);
        libraryDB = LibraryDB.getInstance(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        alterPassword = (Button) findViewById(R.id.usercpassword);
        init();
        // libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 200000));
        BmobQuery<PersonMessage>personMessage=new BmobQuery<PersonMessage>();
        //pref.getString("objectid", "")    ea173120a2
        String dd=pref.getString("objectid", "");
        personMessage.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
            @Override
            public void done(PersonMessage personMessage, BmobException e) {
                if(e==null){
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
                    alterpassword.putExtra("alterpassword",personMessage);
                    alterPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(alterpassword);
                        }
                    });
                }else{
                    useToast("未联网！");
                    alterPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            useToast("请先联网！");
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
