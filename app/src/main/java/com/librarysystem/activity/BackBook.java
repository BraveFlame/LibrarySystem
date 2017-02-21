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

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/12/25.
 */

public class BackBook extends Activity {
    private TextView detailed_name, detailed_author, detailed_id, detailed_message, borrow_date,back_date;
    private Button detailed_button;
    private LibraryDB libraryDB;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_book);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        final Books book = (Books) getIntent().getParcelableExtra("bookmessage");
        detailed_id = (TextView) findViewById(R.id.present_id);
        detailed_name = (TextView) findViewById(R.id.present_name);
        detailed_author = (TextView) findViewById(R.id.present_author);
        detailed_message = (TextView) findViewById(R.id.present_message);
        borrow_date=(TextView)findViewById(R.id.start_date);
        back_date=(TextView)findViewById(R.id.back_date);
        detailed_id.setText("编号：" + book.getBookId());
        detailed_name.setText("书名：" + book.getBookName());
        detailed_author.setText("作者：" + book.getBookAuthor());
        detailed_message.setText("主要信息：" + book.getUserDescription());
        borrow_date.setText("借阅日期："+book.getLentTime());
        back_date.setText("应还日期:"+book.getBackTime());
        detailed_button = (Button) findViewById(R.id.back_button);
        libraryDB = LibraryDB.getInstance(this);

        detailed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BackBook.this);
                dialog.setTitle("还书操作！").setMessage("是否确定还书？").setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (libraryDB.backBook(pref.getInt("userId",0),book)) ;
                        {
                            finish();
                        }
                    }

                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();


            }
        });

    }
}
