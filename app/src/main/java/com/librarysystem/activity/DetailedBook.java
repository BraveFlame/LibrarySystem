package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentBooks;
import com.librarysystem.model.Rule;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2016/12/25.
 * 从书库获取的书籍具体信息
 */

public class DetailedBook extends Activity {
    private TextView detailed_name, detailed_author, detailed_id, detailed_message, detailed_status, detailed_date;
    private TextView detailed_version, detailed_press;
    private Button detailed_button, detailed_subscribe;
    private SharedPreferences pref;
    private Books book;
    private PersonMessage personMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedbook);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        personMessage = (PersonMessage) getIntent().getSerializableExtra("person");
        book = (Books) getIntent().getSerializableExtra("bookmessage");

        init();
        detailed_id.setText("编号：" + book.getBookId());
        detailed_name.setText("书名：" + book.getBookName());
        detailed_author.setText("作者：" + book.getBookAuthor());
        detailed_version.setText("版本：" + book.getVersion());
        detailed_press.setText("出版社：" + book.getPress());
        detailed_message.setText("主要信息：" + book.getUserDescription());
        if (book.getIsLent().equals("可借"))
            detailed_status.setText("状态：" + book.getIsLent());
        else {
            detailed_status.setText("状态：" + book.getIsLent().substring(2, book.getIsLent().length()));
        }
        detailed_subscribe = (Button) findViewById(R.id.book_subscribe);
        detailed_button = (Button) findViewById(R.id.detailed_button);


        /**
         * 可借时不需要预约，而且借此书者不得预约该书
         */


        if (book.getIsLent().equals("可借")) {
            detailed_subscribe.setEnabled(false);
        } else {

            /**
             * 借出时显示应还日期
             */

            detailed_button.setEnabled(false);
            detailed_date.setText("应还日期：" + book.getBackTime());
        }
        /**
         *   有人预约时不得预约（预约时以“预约者ID号+预约”存储）
         */

        if (book.getIsSubscribe().length() > 2) {
            String userSub = book.getIsSubscribe().substring(0, book.getIsSubscribe().length() - 2);
            detailed_subscribe.setEnabled(false);
            if (Integer.valueOf(userSub) == personMessage.getUserId()) {
                detailed_subscribe.setText("吾约");
            } else {
                detailed_subscribe.setText("其约");
            }
        }


        /**
         * 借此书者不得预约该书
         */


        /**
         * 借书时需满足无过期图书和未达最大借阅量
         */
        detailed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showDialog(DetailedBook.this);
                if (book.getIsLent().equals("可借")) {
                    BmobQuery<Rule> r = new BmobQuery<>();
                    r.getObject("c9cf23b8fb", new QueryListener<Rule>() {
                        @Override
                        public void done(Rule rule, BmobException e) {
                            if (e == null) {
                                if (personMessage.getNowBorrow() < rule.getMaxBooks() && personMessage.getPastBooks() == 0) {
                                    personMessage.setNowBorrow(personMessage.getNowBorrow() + 1);
                                    book.setIsLent(personMessage.getUserName() + "借出");
                                    Date date1 = new Date();
                                    Date date2 = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String borrowdate = sdf.format(date1);
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(date2);
                                    calendar.add(calendar.DATE, rule.getFirstDay());//把日期往后增加60天整数往后推,负数往前移动
                                    date2 = calendar.getTime();   //这个时间就是日期往后推60天的结果
                                    String backdate = sdf.format(date2);
                                    book.setLentTime(borrowdate);
                                    book.setBackTime(backdate);
                                    book.update(book.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            DialogMessage.closeDialog();
                                            if (e == null) {
                                                detailed_status.setText("状态：借出");
                                                detailed_button.setEnabled(false);
                                                detailed_date.setText("应还日期：" + book.getBackTime());
                                                detailed_subscribe.setEnabled(false);
                                            } else {
                                                ToastMessage.useToast(DetailedBook.this, "书库更改异常");
                                            }
                                        }
                                    });
                                    PresentBooks pb = new PresentBooks();
                                    pb.setBookId(book.getBookId());
                                    pb.setBookName(book.getBookName());
                                    pb.setBookAuthor(book.getBookAuthor());
                                    pb.setPress(book.getPress());
                                    pb.setVersion(book.getVersion());
                                    pb.setIsContinue(book.getIsContinue());
                                    pb.setIsSubscribe(book.getIsSubscribe());
                                    pb.setBackTime(book.getBackTime());
                                    pb.setLentTime(book.getLentTime());
                                    pb.setUserDescription(book.getUserDescription());
                                    pb.setUserId(personMessage.getUserId());
                                    pb.setUserName(personMessage.getUserName());
                                    pb.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            DialogMessage.closeDialog();
                                            if (e == null) {
                                            } else {
                                                ToastMessage.useToast(DetailedBook.this, "过去存储异常");
                                            }
                                        }
                                    });
                                    personMessage.update(pref.getString("objectid", ""), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e != null) {
                                                ToastMessage.useToast(DetailedBook.this, "人信息更新异常");
                                            }
                                        }
                                    });

                                } else if (personMessage.getNowBorrow() >= rule.getMaxBooks()) {
                                    ToastMessage.useToast(DetailedBook.this, "已达最大续借量！");
                                } else if (personMessage.getPastBooks() > 0) {
                                    ToastMessage.useToast(DetailedBook.this, "请先归还过期图书！");
                                }

                            } else {
                                ToastMessage.useToast(DetailedBook.this, "获取规则异常");
                            }
                        }
                    });
                }

            }
        });

        detailed_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showDialog(DetailedBook.this);
                BmobQuery<Rule> r = new BmobQuery<>();
                r.getObject("c9cf23b8fb", new QueryListener<Rule>() {
                    @Override
                    public void done(Rule rule, BmobException e) {
                        DialogMessage.closeDialog();
                        if (e == null) {
                            String userName = book.getIsLent().substring(0, book.getIsLent().length() - 2);
                            if (userName.equals(personMessage.getUserName())) {
                                ToastMessage.useToast(DetailedBook.this, "借阅者不得预约！");
                            } else if (personMessage.getNowBorrow() > rule.getMaxBooks()) {
                                ToastMessage.useToast(DetailedBook.this, "最大借阅时不可预约");
                            } else if (personMessage.getPastBooks() > 0) {
                                ToastMessage.useToast(DetailedBook.this, "有未归还图书无法预约！");
                            } else {
                                book.setIsSubscribe(pref.getInt("userId", 0) + "预约");
                                book.update(book.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        detailed_subscribe.setEnabled(false);
                                        detailed_subscribe.setText("吾约");
                                    }
                                });
                            }
                        } else {
                            ToastMessage.useToast(DetailedBook.this, "获取规则异常");
                        }
                    }
                });
            }
        });

    }


    public void init() {
        detailed_id = (TextView) findViewById(R.id.detailed_id);
        detailed_name = (TextView) findViewById(R.id.detailed_name);
        detailed_version = (TextView) findViewById(R.id.detailed_version);
        detailed_press = (TextView) findViewById(R.id.detailed_press);
        detailed_author = (TextView) findViewById(R.id.detailed_author);
        detailed_message = (TextView) findViewById(R.id.detailed_message);
        detailed_status = (TextView) findViewById(R.id.detailed_status);
        detailed_date = (TextView) findViewById(R.id.detailed_date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
        DialogMessage.closeDialog();
    }
}
