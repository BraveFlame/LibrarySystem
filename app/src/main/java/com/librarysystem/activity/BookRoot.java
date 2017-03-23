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
import com.librarysystem.model.Rule;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

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
        BmobQuery<Rule> ruleBmobQuery = new BmobQuery<>();
        ruleBmobQuery.getObject("c9cf23b8fb", new QueryListener<Rule>() {
            @Override
            public void done(Rule rule, BmobException e) {
                if (e == null) {
                    maxBooks.setText("" + rule.getMaxBooks());
                    firstBook.setText("" + rule.getFirstDay());
                    thanBook.setText("" + rule.getSecondDay());
                } else {
                    useToast("网络异常");
                }
            }
        });

        set_Books = (Button) findViewById(R.id.max_borrow);
        set_Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Rule rule = new Rule();
                    rule.setMaxBooks(Integer.valueOf(maxBooks.getText().toString()));
                    rule.setFirstDay(Integer.valueOf(firstBook.getText().toString()));
                    rule.setSecondDay(Integer.valueOf(thanBook.getText().toString()));
                    rule.update("c9cf23b8fb", new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                useToast("修改成功");
                                finish();
                            } else {
                                useToast("网络异常");
                            }
                        }
                    });
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
