package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.Rule;
import com.librarysystem.model.UserAdapter;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by g on 2017/2/27.
 * 管理员搜索用户
 */

public class ManagerUser extends Activity {
    private EditText searchUser;
    private Button searchButton;
    private ListView userListView;
    private List<PersonMessage> usersList = new ArrayList<PersonMessage>();
    private LibraryDB libraryDB;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_users);
        libraryDB = LibraryDB.getInstance(this);
        searchUser = (EditText) findViewById(R.id.manager_search_user);
        searchButton = (Button) findViewById(R.id.manager_searchuser_button);
        userListView = (ListView) findViewById(R.id.manager_user_list);
/**
 * 从账户和姓名两重搜索
 */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch = true;
                seacherUsers();
            }
        });
    }

    public void seacherUsers() {
        userListView = (ListView) findViewById(R.id.manager_user_list);
        final String input = searchUser.getText().toString();
        DialogMessage.showDialog(ManagerUser.this);
        BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<PersonMessage>();
        personMessageBmobQuery.addWhereNotEqualTo("isRootManager", "管理员");
        personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
            @Override
            public void done(List<PersonMessage> list, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null) {
                    if (list.size() <=0) {
                        ToastMessage.useToast(ManagerUser.this, "没有符合搜索要求的用户！");
                    } else {
                        libraryDB.savePersonalMeassage(list);
                        libraryDB.getUsers(input, usersList);
                        if (usersList.size() <= 0) {
                            ToastMessage.useToast(ManagerUser.this, "没有该用户");
                        }
                        UserAdapter userAdapter = new UserAdapter(ManagerUser.this, R.layout.user_item, usersList);
                        userListView.setAdapter(userAdapter);

                    }
                } else {
                    ToastMessage.useToast(ManagerUser.this, "获取失败");
                }
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent changeUserIntent = new Intent(ManagerUser.this, ChangeUsers.class);
                PersonMessage personMessage = usersList.get(position);
                changeUserIntent.putExtra("user_id", personMessage);
                BmobQuery<Rule> rule = new BmobQuery<Rule>();
                rule.getObject("c9cf23b8fb", new QueryListener<Rule>() {
                    @Override
                    public void done(Rule rule, BmobException e) {
                        if (e == null) {
                            changeUserIntent.putExtra("rule", rule);
                            startActivity(changeUserIntent);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSearch) {
            seacherUsers();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }
}
