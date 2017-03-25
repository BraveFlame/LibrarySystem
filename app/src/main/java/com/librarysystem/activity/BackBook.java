package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.model.PastBooks;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentBooks;
import com.librarysystem.model.Rule;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2016/12/25.
 * 还书或续借的activity
 */

public class BackBook extends Activity {
    private TextView detailed_name, detailed_author, book_press, book_version,
            detailed_id, detailed_message, borrow_date, back_date;
    private Button detailed_button, back_continue;
    private Toast mToast;
    private PersonMessage person;
    private Books books;
    private Date date1;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_book);
        final PresentBooks book = (PresentBooks) getIntent().getSerializableExtra("bookmessage");
        person = (PersonMessage) getIntent().getSerializableExtra("person");
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
 * 改变用户
 */
                        DialogMessage.showDialog(BackBook.this);
                        back_continue.setEnabled(false);
                        detailed_button.setEnabled(false);
                        person.setNowBorrow(person.getNowBorrow() - 1);
                        person.setUserLevel(person.getUserLevel() + 1);
                        person.update(person.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {

                                } else {
                                    ToastMessage.useToast(BackBook.this,"网络异常！");
                                }
                            }
                        });
                        /**
                         * 改变该用户过去借阅
                         */

                        PastBooks psb = new PastBooks();
                        psb.setBookId(book.getBookId());
                        psb.setBookAuthor(book.getBookAuthor());
                        psb.setBookName(book.getBookName());
                        psb.setPress(book.getPress());
                        psb.setVersion(book.getVersion());
                        psb.setUserDescription(book.getUserDescription());
                        psb.setUserId(person.getUserId());
                        psb.setUserName(person.getUserName());
                        psb.setBorrowTime(book.getLentTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String borrowdate = sdf.format(new Date());
                        psb.setBackTime(borrowdate);
                        psb.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {

                                } else {
                                    useToast("网络异常！");
                                }
                            }
                        });
                        /**
                         * 将书还回或给预约者
                         */
                        final BmobQuery<Books> b = new BmobQuery<Books>();
                        b.addWhereEqualTo("bookId", book.getBookId());
                        b.findObjects(new FindListener<Books>() {
                            @Override
                            public void done(List<Books> list, BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {

                                    books = list.get(0);
                                    String s = book.getBookName();

                                    /**
                                     * 如果没有预约则返回书库
                                     */

                                    if (books.getIsSubscribe().equals("无")) {
                                        books.setIsSubscribe("无");
                                        books.setIsContinue("无");
                                        books.setBackTime("");
                                        books.setLentTime("");
                                        books.setIsLent("可借");
                                        books.update(books.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                DialogMessage.closeDialog();
                                                if (e == null) {

                                                } else {
                                                    useToast("书库更新异常！");
                                                }
                                            }
                                        });
                                        book.delete(book.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                DialogMessage.closeDialog();
                                                if (e == null) {


                                                } else {
                                                    useToast("当前借阅删除异常！");
                                                }
                                            }
                                        });

                                    }
                                    /**
                                     * 有预约则给预约者借
                                     */
                                    else {
                                        String ss = books.getIsSubscribe().substring(0, books.getIsSubscribe().length() - 2);
                                        final int userId = Integer.valueOf(ss);

                                        /**
                                         * 获取人信息
                                         */
                                        BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<PersonMessage>();
                                        personMessageBmobQuery.addWhereEqualTo("userId", userId);
                                        personMessageBmobQuery.findObjects(new FindListener<PersonMessage>() {
                                            @Override
                                            public void done(List<PersonMessage> list, BmobException e) {
                                                person = list.get(0);
                                                if (e == null) {
                                                    /**
                                                     * 获取借阅规则信息
                                                     */
                                                    BmobQuery<Rule> ruleBmobQuery = new BmobQuery<Rule>();
                                                    ruleBmobQuery.getObject("c9cf23b8fb", new QueryListener<Rule>() {
                                                        @Override
                                                        public void done(Rule rule, BmobException e) {
                                                            DialogMessage.closeDialog();
                                                            if (e == null) {
                                                                books.setIsContinue("无");
                                                                books.setIsSubscribe("无");
                                                                books.setIsLent(person.getUserName() + "借出");
                                                                Date date1 = new Date();
                                                                Date date2 = new Date();
                                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                                final String borrowdate = sdf.format(date1);
                                                                Calendar calendar = new GregorianCalendar();
                                                                calendar.setTime(date2);
                                                                //pref.getInt("firstborrow", 30)
                                                                calendar.add(calendar.DATE, rule.getFirstDay());//把日期往后增加60天整数往后推,负数往前移动
                                                                date2 = calendar.getTime();   //这个时间就是日期往后推60天的结果
                                                                final String backdate = sdf.format(date2);
                                                                books.setLentTime(borrowdate);
                                                                books.setBackTime(backdate);
                                                                /**
                                                                 *预约者得借
                                                                 */
                                                                books.update(books.getObjectId(), new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        DialogMessage.closeDialog();
                                                                        if (e == null) {

                                                                        } else {
                                                                            useToast("预约者书库异常！");
                                                                        }
                                                                    }
                                                                });
                                                                /**
                                                                 * 预约者当前借阅增加
                                                                 */
                                                                book.setUserId(person.getUserId());
                                                                book.setUserName(person.getUserName());
                                                                book.setIsSubscribe("无");
                                                                book.setIsContinue("无");
                                                                book.setLentTime(borrowdate);
                                                                book.setBackTime(backdate);
                                                                book.update(book.getObjectId(), new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        DialogMessage.closeDialog();
                                                                        if (e == null) {

                                                                        } else {
                                                                            useToast("预约者当前借阅异常！");

                                                                        }
                                                                    }
                                                                });
                                                                /**
                                                                 * 预约者当前借阅增加
                                                                 */
                                                                person.setNowBorrow(person.getNowBorrow() + 1);
                                                                person.update(person.getObjectId(), new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        DialogMessage.closeDialog();
                                                                        if (e == null)
                                                                            finish();
                                                                        else
                                                                            useToast("更新预约者信息异常！");
                                                                    }
                                                                });

                                                            } else {
                                                                useToast("获取规则异常！");
                                                            }

                                                        }
                                                    });

                                                } else {
                                                    useToast("获取预约者信息异常！");
                                                }
                                            }
                                        });


                                    }
                                } else {
                                    useToast("获取书库异常！");
                                }

                            }
                        });


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
                        DialogMessage.showDialog(BackBook.this);
                        BmobQuery<Rule> r = new BmobQuery<>();
                        r.getObject("c9cf23b8fb", new QueryListener<Rule>() {
                            @Override
                            public void done(Rule rule, BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {
                                    book.setIsContinue("续借");
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        date1 = sdf.parse(book.getBackTime());
                                        Calendar calendar = new GregorianCalendar();
                                        calendar.setTime(date1);
                                        calendar.add(calendar.DATE, rule.getSecondDay());//把日期往后增加n天整数往后推,负数往前移动
                                        date1 = calendar.getTime();   //这个时间就是日期往后推几天的结果
                                        book.setBackTime(sdf.format(date1));
                                        book.update(book.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                DialogMessage.closeDialog();
                                                if (e == null) {
                                                    back_date.setText("应还日期：" + book.getBackTime());
                                                    useToast("续借成功！");
                                                } else
                                                    useToast("续借异常");
                                            }
                                        });
                                        final BmobQuery<Books> b = new BmobQuery<Books>();
                                        b.addWhereEqualTo("bookId", book.getBookId());
                                        b.findObjects(new FindListener<Books>() {
                                            @Override
                                            public void done(List<Books> list, BmobException e) {
                                                DialogMessage.closeDialog();
                                                if (e == null) {
                                                    books = list.get(0);
                                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                    books.setBackTime(sdf.format(date1));
                                                    books.update(books.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if (e == null) {

                                                            } else {
                                                                useToast("更新书库异常");
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    useToast("获取书库异常");
                                                }
                                            }
                                        });
                                    } catch (Exception ee) {
                                        useToast("续借失败！");
                                    }
                                } else {
                                    useToast("获取规则异常！");
                                }
                            }
                        });

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
            if (days < 0)
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
        BackBook.this.finish();
        DialogMessage.closeDialog();
        cancelToast();
    }

}
