package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Rule;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

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
    private ImageView rerule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_booksroot);
        maxBooks = (EditText) findViewById(R.id.max_eborrow);
        firstBook = (EditText) findViewById(R.id.first_eborrow);
        thanBook = (EditText) findViewById(R.id.than_eborrow);
        rerule = (ImageView) findViewById(R.id.renew_rule);
        getRule();
        rerule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRule();
            }
        });

        set_Books = (Button) findViewById(R.id.max_borrow);
        set_Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showDialog(BookRoot.this);
                try {
                    Rule rule = new Rule();
                    rule.setMaxBooks(Integer.valueOf(maxBooks.getText().toString()));
                    rule.setFirstDay(Integer.valueOf(firstBook.getText().toString()));
                    rule.setSecondDay(Integer.valueOf(thanBook.getText().toString()));
                    rule.update("c9cf23b8fb", new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            DialogMessage.closeDialog();
                            if (e == null) {
                                ToastMessage.useToast(BookRoot.this, "修改成功");
                                finish();
                            } else {
                                ToastMessage.useToast(BookRoot.this, "修改失败");
                            }
                        }
                    });
                } catch (Exception e) {
                    ToastMessage.useToast(BookRoot.this, "格式错误！");
                }
            }
        });
    }

    public void getRule() {
        DialogMessage.showDialog(BookRoot.this);
        BmobQuery<Rule> ruleBmobQuery = new BmobQuery<>();
        ruleBmobQuery.getObject("c9cf23b8fb", new QueryListener<Rule>() {
            @Override
            public void done(Rule rule, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null) {
                    maxBooks.setText("" + rule.getMaxBooks());
                    firstBook.setText("" + rule.getFirstDay());
                    thanBook.setText("" + rule.getSecondDay());
                } else {
                    ToastMessage.useToast(BookRoot.this, "获取失败");
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }

}
