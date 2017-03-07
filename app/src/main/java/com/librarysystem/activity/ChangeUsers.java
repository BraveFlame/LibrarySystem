package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

/**
 * Created by g on 2017/2/27.
 * 用于注销用户
 */

public class ChangeUsers extends Activity {
    private SharedPreferences pref;
    private LibraryDB libraryDB;
    private PersonMessage personMessage = new PersonMessage();
    private TextView accountId, accountName, accountSex, accountpro, accounthobby, accounttel,
            accountlevel, accountpast, accountwpast, accountProperty;
    private Button deleteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_users);
        libraryDB = LibraryDB.getInstance(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPersonalMeassage(personMessage, getIntent().getIntExtra("user_id", 0));
        init();

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeUsers.this);
                dialog.setTitle("注销").setMessage("是否删除用户？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 用户注销时需同时将其借阅的书籍归化（若有人预约其书籍，需要及时转接预约者），以及将其预约的书籍取消预约
                         * 然后删除其过去借阅，最后删除个人信息
                         */
                        if (libraryDB.forceBackBooks(personMessage.getUserId(), pref.getInt("firstborrow", 30), pref.getInt("maxnumbook", 30))
                                && libraryDB.deletePast(personMessage.getUserId()) && libraryDB.deleteUsers(personMessage.getUserId())) {
                            Toast.makeText(ChangeUsers.this, "用户已删除！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    public void init() {
        accountName = (TextView) findViewById(R.id.userchname);
        accountSex = (TextView) findViewById(R.id.userchsex);
        accountId = (TextView) findViewById(R.id.userchId);
        accountpro = (TextView) findViewById(R.id.userchprofession);
        accounthobby = (TextView) findViewById(R.id.userchhobby);
        accounttel = (TextView) findViewById(R.id.userchtel);
        accountlevel = (TextView) findViewById(R.id.userchlevel);
        accountpast = (TextView) findViewById(R.id.userchlate);
        accountwpast = (TextView) findViewById(R.id.userchwpastbook);
        deleteUser = (Button) findViewById(R.id.user_delete);
        accountProperty = (TextView) findViewById(R.id.user_chproperty);

        accountName.setText("姓名：" + personMessage.getUserName().toString());
        accountSex.setText("性别：" + personMessage.getUserSex().toString());
        accountId.setText("账户：" + String.valueOf(personMessage.getUserId()));
        accounthobby.setText("书籍爱好：" + personMessage.getUserDescription().toString());
        accountpro.setText("专业：" + personMessage.getUserProfession().toString());
        accountlevel.setText("借阅等级：" + personMessage.getUserLevel().toString());
        accountpast.setText("逾期书本：" + personMessage.getPastBooks().toString());
        accountwpast.setText("即将到期：" + personMessage.getWpastBooks().toString());
        accounttel.setText("联系方式：" + personMessage.getUserTel().toString());
        accountProperty.setText("属性：" + personMessage.getIsRootManager().toString());


    }
}
