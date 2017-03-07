package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;

/**
 * Created by g on 2017/2/27.
 * 管理员设置借阅参数首借天数，续借图书，可借最大图书本数
 */

public class BookRoot extends Activity {
    private EditText maxBooks, firstBook, thanBook;
    private Button set_Books;
    private Toast mToast;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_booksroot);
        maxBooks = (EditText) findViewById(R.id.max_eborrow);
        firstBook = (EditText) findViewById(R.id.first_eborrow);
        thanBook = (EditText) findViewById(R.id.than_eborrow);
        pref = PreferenceManager.getDefaultSharedPreferences(BookRoot.this);
        maxBooks.setText("" + pref.getInt("maxnumbook", 30));
        firstBook.setText("" + pref.getInt("firstborrow", 60));
        thanBook.setText("" + pref.getInt("thanborrow", 30));
        set_Books = (Button) findViewById(R.id.max_borrow);


        set_Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int max = Integer.valueOf(maxBooks.getText().toString());
                    int first = Integer.valueOf(firstBook.getText().toString());
                    int than = Integer.valueOf(thanBook.getText().toString());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("firstborrow", first);
                    editor.putInt("thanborrow", than);
                    editor.putInt("maxnumbook", max);
                    editor.commit();
                    useToast("修改成功");
                    finish();
                } catch (Exception e) {
                    useToast("格式错误！");
                }
            }
        });
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
