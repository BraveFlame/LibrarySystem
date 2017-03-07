package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.librarysystem.R;

/**
 * Created by g on 2017/2/21.
 * 管理员权限，包括用户管理，图书管理，借阅规则
 */

public class ManagerRoot extends Activity {
    private TextView bookManager,userManager,bookRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_root);
        bookManager=(TextView)findViewById(R.id.manager_book);
        userManager=(TextView)findViewById(R.id.manager_user);
        bookRoot=(TextView)findViewById(R.id.manager_bookroot);
        bookManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookManagerIntent=new Intent(ManagerRoot.this,ChangeBooks.class);
                startActivity(bookManagerIntent);
            }
        });
        userManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userManagerIntent=new Intent(ManagerRoot.this,ManagerUser.class);
                startActivity(userManagerIntent);
            }
        });
        bookRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent bookRootIntent=new Intent(ManagerRoot.this,BookRoot.class);
                startActivity(bookRootIntent);
            }
        });
    }
}
