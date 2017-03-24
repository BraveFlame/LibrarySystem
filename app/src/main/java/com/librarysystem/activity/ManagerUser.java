package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.UserAdapter;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by g on 2017/2/27.
 * 管理员搜索用户
 */

public class ManagerUser extends Activity {
    private EditText searchUser;
    private Button searchButton;
    private ListView userListView;
    private List<PersonMessage> usersList= new ArrayList<PersonMessage>();
    private LibraryDB libraryDB;
    private boolean isSearch;
    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_users);
        libraryDB = LibraryDB.getInstance(this);
        searchUser=(EditText)findViewById(R.id.manager_search_user);
        searchButton=(Button)findViewById(R.id.manager_searchuser_button);
        userListView=(ListView)findViewById(R.id.manager_user_list);
/**
 * 从账户和姓名两重搜索
 */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch=true;
                userListView=(ListView) findViewById(R.id.manager_user_list);
                final String input=searchUser.getText().toString();
                BmobQuery<PersonMessage>personMessageBmobQuery=new BmobQuery<PersonMessage>();
                personMessageBmobQuery.addWhereNotEqualTo("userId",12345);
                personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                    @Override
                    public void done(List<PersonMessage> list, BmobException e) {
                        if(e==null){
                            if(list.size()==0){
                                useToast("没有符合搜索要求的用户！");
                            }else {
                                libraryDB.savePersonalMeassage(list);
                                libraryDB.getUsers(input,usersList);
                                if(usersList.size()>0) {

                                }else {
                                    useToast("没有该用户");
                                }
                                usersList=list;
                                UserAdapter userAdapter = new UserAdapter(ManagerUser.this, R.layout.user_item, usersList);
                                userListView.setAdapter(userAdapter);

                            }
                        }else {
                            useToast("获取失败");
                        }
                    }
                });

                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Intent changeUserIntent=new Intent(ManagerUser.this,ChangeUsers.class);
                        PersonMessage personMessage=usersList.get(position);
                        changeUserIntent.putExtra("user_id",personMessage);
                        startActivity(changeUserIntent);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isSearch){
            try{
                final String input=searchUser.getText().toString();
                userListView=(ListView) findViewById(R.id.manager_user_list);
                BmobQuery<PersonMessage>personMessageBmobQuery=new BmobQuery<PersonMessage>();
                personMessageBmobQuery.addWhereNotEqualTo("userId",12345);
                personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                    @Override
                    public void done(List<PersonMessage> list, BmobException e) {
                        if(e==null){

                            if(list.size()==0){
                                useToast("没有符合搜索要求的用户！");
                            }else {

                                libraryDB.savePersonalMeassage(list);
                                libraryDB.getUsers(input,usersList);
                                if(usersList.size()>0) {


                                }else {
                                    useToast("没有该用户");
                                }

                            }
                            usersList=list;
                            UserAdapter userAdapter = new UserAdapter(ManagerUser.this, R.layout.user_item, usersList);
                            userListView.setAdapter(userAdapter);
                        }else {
                            useToast("获取异常");
                        }
                    }
                });
                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Intent changeUserIntent=new Intent(ManagerUser.this,ChangeUsers.class);
                        PersonMessage personMessage=usersList.get(position);
                        changeUserIntent.putExtra("user_id",personMessage);
                        startActivity(changeUserIntent);
                    }
                });

            }catch (Exception e){

            }



        }
    }
    public void useToast(String text){
        if(mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelToast();
    }
}
