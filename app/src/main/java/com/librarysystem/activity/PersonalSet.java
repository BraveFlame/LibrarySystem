package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText userId;
    private EditText userName;
    private EditText userSex;
    private EditText userprofession;
    private EditText userDescription;
    private Button personSave,personSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_data);
        personSave=(Button)findViewById(R.id.personsave);
        personSet=(Button)findViewById(R.id.personset);
        personSet.setOnClickListener(this);
        personSave.setOnClickListener(this);

        libraryDB=LibraryDB.getInstance(this);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPersonalMeassage(personMessage,pref.getInt("userId",200000));

        userId=(EditText) findViewById(R.id.userId);
        userName=(EditText)findViewById(R.id.userName);
        userSex=(EditText)findViewById(R.id.userSex);
        userprofession=(EditText)findViewById(R.id.userProfession);
        userDescription=(EditText)findViewById(R.id.userDescription);


        userName.setText(personMessage.getUserName());
        userName.setEnabled(false);
        userId.setText(String.valueOf(personMessage.getUserId()));
        userId.setEnabled(false);
        userSex.setText(personMessage.getUserSex());
        userSex.setEnabled(false);
        userprofession.setText(personMessage.getUserProfession());
        userprofession.setEnabled(false);
        userDescription.setText(personMessage.getUserDescription());
        userDescription.setEnabled(false);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personsave:

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
                break;
            case R.id.personset:

                userName.setEnabled(true);
                userSex.setEnabled(true);
                userprofession.setEnabled(true);
                userDescription.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
