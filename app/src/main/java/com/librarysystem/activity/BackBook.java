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
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by g on 2016/12/25.
 * 还书或续借的activity
 */

public class BackBook extends Activity {
    private TextView detailed_name, detailed_author, book_press, book_version,
            detailed_id, detailed_message, borrow_date, back_date;
    private Button detailed_button, back_continue;
    private LibraryDB libraryDB;
    private SharedPreferences pref;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_book);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final Books book = (Books) getIntent().getParcelableExtra("bookmessage");
        libraryDB = LibraryDB.getInstance(this);
        init();
        detailed_id.setText("编号：" + book.getBookId());
        detailed_name.setText("书名：" + book.getBookName());
        detailed_author.setText("作者：" + book.getBookAuthor());
        detailed_message.setText("主要信息：" + book.getUserDescription());
        borrow_date.setText("借阅日期：" + book.getLentTime());
        back_date.setText("应还日期：" + book.getBackTime());
        book_press.setText("出版社：" + book.getPress());
        book_version.setText("版本：" + book.getVersion());
        /**
         *  还书按键
         */
        detailed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BackBook.this);
                dialog.setTitle("还书操作！").setMessage("是否确定还书？").setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 还书成功时，更新借阅等级，每五本加一级
                         */
                        if (libraryDB.backBook(pref.getInt("userId", 0), book, pref.getInt("firstborrow", 30), pref.getInt("maxnumbook", 30))) {
                            List<Books> books = new ArrayList<Books>();
                            libraryDB.getPastBooks(pref.getInt("userId", 0), books);
                            PersonMessage personMessage = new PersonMessage();
                            libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 0));
                            personMessage.setNowBorrow(personMessage.getNowBorrow() - 1);
                            if (books.size() > 5) {
                                personMessage.setUserLevel("" + books.size() / 5);
                            }
                            libraryDB.alterPersonalMessage(personMessage, pref.getInt("userId", 0));
                            finish();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
/**
 * 续借，按照管理员设置的续借天数，把读者的应还日期延迟
 */
        back_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getIsContinue().equals("无")) {
                    if (impart(book.getBackTime())) {
                        book.setIsContinue("续借");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = new Date();
                        try {

                            date1 = sdf.parse(book.getBackTime());
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date1);
                            calendar.add(calendar.DATE, pref.getInt("thanborrow", 30));//把日期往后增加n天整数往后推,负数往前移动
                            date1 = calendar.getTime();   //这个时间就是日期往后推1天的结果
                            book.setBackTime(sdf.format(date1));
                            libraryDB.bookContinue(book);
                            back_date.setText("应还日期：" + book.getBackTime());
                            useToast("续借成功！");
                        } catch (Exception e) {
                            useToast("续借失败！");
                        }

                    } else {
                        useToast("已过期，无法续借！");
                    }
                } else {
                    useToast("已达续借最大天数！");
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    public void init() {
        detailed_id = (TextView) findViewById(R.id.present_id);
        detailed_name = (TextView) findViewById(R.id.present_name);
        detailed_author = (TextView) findViewById(R.id.present_author);
        detailed_message = (TextView) findViewById(R.id.present_message);
        borrow_date = (TextView) findViewById(R.id.start_date);
        back_date = (TextView) findViewById(R.id.back_date);
        book_version = (TextView) findViewById(R.id.present_version);
        book_press = (TextView) findViewById(R.id.press_id);
        detailed_button = (Button) findViewById(R.id.back_button);
        back_continue = (Button) findViewById(R.id.back_continue);
    }

    /**
     * 偌该书已过期，则无法续借
     *
     * @param date
     * @return
     */
    private boolean impart(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(date);
            Date nowDate = new Date();
            Date date2 = sdf.parse(sdf.format(nowDate));
            long distance = date1.getTime() - date2.getTime();
            long days = distance / (1000 * 60 * 60 * 24);
            if (days < 1)
                return false;
            else return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解决Toast频繁显示
     */
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
