package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentAdapter;
import com.librarysystem.model.PresentBooks;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by g on 2016/12/2.
 * 当前借阅的书籍
 */

public class PresentBorrow extends Activity {



    private ListView bookList;
    private List<PresentBooks> booksList = new ArrayList<PresentBooks>();
    private SharedPreferences pref;
    private Toast mToast;
    private PersonMessage ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_book);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        bookList = (ListView) findViewById(R.id.list_present_book);
        /**
         *  BmobQuery<PresentBooks> b = new BmobQuery<>();
         b.addWhereEqualTo("usrId",person.getUserId());
         b.findObjects(new FindListener<PresentBooks>() {
        @Override
        public void done(List<PresentBooks> list, BmobException e) {
         */
        BmobQuery<PresentBooks> pb = new BmobQuery<>();
        pb.addWhereEqualTo("userId", pref.getInt("userId", 0));
        pb.findObjects(new FindListener<PresentBooks>() {
            @Override
            public void done(List<PresentBooks> list, BmobException e) {
                if (e == null) {
                    booksList = list;
                    PresentAdapter adapter = new PresentAdapter(PresentBorrow.this, R.layout.book_item, booksList);
                    bookList.setAdapter(adapter);
                    if (booksList.size() == 0) {
                        useToast("当前没有借阅的书籍！");
                    }
                } else {
                    useToast("获取书籍异常！");
                }
            }
        });


        //将搜索结果显示出来

        final Intent bookIntent = new Intent(this, BackBook.class);
                /*
                点击查看每本书的信息
                 */
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PresentBooks book = booksList.get(position);
                bookIntent.putExtra("bookmessage", book);
                final BmobQuery<PersonMessage> p = new BmobQuery<PersonMessage>();
                p.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
                    @Override
                    public void done(PersonMessage personMessage, BmobException e) {
                        if (e == null) {
                            ps = personMessage;
                            if (ps.getNowBorrow() == null) {
                                ps.setNowBorrow(0);
                            }
                            if (ps.getPastBooks() == null) {
                                ps.setPastBooks(0);
                            }
                            if (ps.getWpastBooks() == null) {
                                ps.setWpastBooks(0);
                            }
                            if (ps.getUserLevel() == null) {
                                ps.setUserLevel(0);
                            }
                            bookIntent.putExtra("person", ps);
                            startActivity(bookIntent);
                        } else {
                            useToast("获取个人信息失败！");
                        }

                    }
                });
            }
        });
    }

    /**
     * 还书时刷新界面
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        BmobQuery<PresentBooks> pb = new BmobQuery<>();
        pb.addWhereEqualTo("userId", pref.getInt("userId", 0));
        pb.findObjects(new FindListener<PresentBooks>() {
            @Override
            public void done(List<PresentBooks> list, BmobException e) {
                if (e == null) {
                    booksList = list;
                    PresentAdapter adapter = new PresentAdapter(PresentBorrow.this, R.layout.book_item, booksList);
                    bookList.setAdapter(adapter);
                    if (booksList.size() == 0) {
                        useToast("当前没有借阅的书籍！");
                    }
                } else {
                    useToast("获取书籍失败！");
                }
            }
        });


        //将搜索结果显示出来

        final Intent bookIntent = new Intent(this, BackBook.class);
                /*
                点击查看每本书的信息
                 */
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PresentBooks book = booksList.get(position);
                bookIntent.putExtra("bookmessage", book);
                final BmobQuery<PersonMessage> p = new BmobQuery<PersonMessage>();
                p.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
                    @Override
                    public void done(PersonMessage personMessage, BmobException e) {
                        if (e == null) {
                            ps = personMessage;
                            if (ps.getNowBorrow() == null) {
                                ps.setNowBorrow(0);
                            }
                            if (ps.getPastBooks() == null) {
                                ps.setPastBooks(0);
                            }
                            if (ps.getWpastBooks() == null) {
                                ps.setWpastBooks(0);
                            }
                            if (ps.getUserLevel() == null) {
                                ps.setUserLevel(0);
                            }
                            bookIntent.putExtra("person", ps);
                            startActivity(bookIntent);
                        } else {
                            useToast("获取个人信息失败！");

                        }
                        startActivity(bookIntent);
                    }
                });
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
