package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/12/25.
 */
/*
书的主要信息
 */
public class DetailedBook extends Activity {
    private TextView detailed_name, detailed_author, detailed_id, detailed_message, detailed_status, detailed_date;
    private TextView detailed_version, detailed_press;
    private Button detailed_button, detailed_subscribe;
    private LibraryDB libraryDB;
    private SharedPreferences pref;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedbook);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final Books book = (Books) getIntent().getParcelableExtra("bookmessage");
        detailed_id = (TextView) findViewById(R.id.detailed_id);
        detailed_name = (TextView) findViewById(R.id.detailed_name);
        detailed_version = (TextView) findViewById(R.id.detailed_version);
        detailed_press = (TextView) findViewById(R.id.detailed_press);
        detailed_author = (TextView) findViewById(R.id.detailed_author);
        detailed_message = (TextView) findViewById(R.id.detailed_message);
        detailed_status = (TextView) findViewById(R.id.detailed_status);
        detailed_date = (TextView) findViewById(R.id.detailed_date);
        detailed_id.setText("编号：" + book.getBookId());
        detailed_name.setText("书名：" + book.getBookName());
        detailed_author.setText("作者：" + book.getBookAuthor());
        detailed_version.setText("版本：" + book.getVersion());
        detailed_press.setText("出版社：" + book.getPress());
        detailed_message.setText("主要信息：" + book.getUserDescription());
        detailed_status.setText("状态：" + book.getIsLent());
        detailed_subscribe = (Button) findViewById(R.id.book_subscribe);
        if(book.getIsSubscribe().length()>2) {
            String userSub = book.getIsSubscribe().substring(book.getIsSubscribe().length() - 2);
            if (userSub.equals("预约")) {
                detailed_subscribe.setEnabled(false);
                detailed_subscribe.setText("已被预约");
            }
        }

        detailed_button = (Button) findViewById(R.id.detailed_button);

        libraryDB = LibraryDB.getInstance(this);
        if ((book.getIsLent().equals("借出"))) {
            detailed_button.setEnabled(false);
            detailed_date.setText("应还日期：" + book.getBackTime());
        }
        detailed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getIsLent().equals("可借")) {


                    if (libraryDB.borrowBook(pref.getInt("userId", 0), book,pref.getInt("firstborrow",30)
                            ,pref.getInt("maxnumbook",30))) {
                        if(book.getIsLent().equals("可借")){
                            useToast("已达当前最大借阅量！");
                        }else {
                            detailed_status.setText("状态：借出");
                            detailed_button.setEnabled(false);
                            detailed_date.setText("应还日期：" + book.getBackTime());
                            detailed_subscribe.setEnabled(false);
                            book.setIsLent("借出");
                        }

                    } else {
                        useToast("您有过期图书未归还，无法借书！");
                    }


                }
            }
        });
        detailed_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book.getIsLent().equals("可借")){
                    useToast("可直接借书！");
                }else if(book.getIsLent().equals("借出")) {
                    PersonMessage personMessage=new PersonMessage();
                    libraryDB.getPersonalMeassage(personMessage,pref.getInt("userId",0));
                    if(Integer.valueOf(personMessage.getPastBooks())>0){
                        useToast("有未归还图书无法预约！");
                    }else {
                        detailed_subscribe.setEnabled(false);
                        book.setIsSubscribe(pref.getInt("userId", 0) + "预约");
                        libraryDB.bookSubscribe(book);
                        detailed_subscribe.setEnabled(false);
                        detailed_subscribe.setText("已预约");
                    }
                }
            }
        });
    }

    public void useToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(DetailedBook.this, text, Toast.LENGTH_SHORT);
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
