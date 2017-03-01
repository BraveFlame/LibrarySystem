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

/**
 * Created by g on 2017/2/27.
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


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch=true;
                String input=searchUser.getText().toString();
                libraryDB.getUsers(input,usersList);
                if(usersList.size()==0){
                    useToast("没有符合搜索要求的用户！");
                }
                UserAdapter userAdapter=new UserAdapter(ManagerUser.this,R.layout.user_item,usersList);
                userListView=(ListView) findViewById(R.id.manager_user_list);
                userListView.setAdapter(userAdapter);
                final Intent changeUserIntent=new Intent(ManagerUser.this,ChangeUsers.class);
                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PersonMessage personMessage=usersList.get(position);
                        changeUserIntent.putExtra("user_id",personMessage.getUserId());
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
                String input=searchUser.getText().toString();
                libraryDB.getUsers(input,usersList);
                UserAdapter userAdapter=new UserAdapter(ManagerUser.this,R.layout.user_item,usersList);
                userListView=(ListView) findViewById(R.id.manager_user_list);
                userListView.setAdapter(userAdapter);
                final Intent changeUserIntent=new Intent(ManagerUser.this,ChangeUsers.class);
                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PersonMessage personMessage=usersList.get(position);
                        changeUserIntent.putExtra("user_id",personMessage.getUserId());
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
