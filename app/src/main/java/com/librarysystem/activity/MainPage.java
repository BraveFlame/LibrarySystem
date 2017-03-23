package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.ActivityCollector;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentBooks;
import com.librarysystem.sqlite.LibraryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by g on 2016/10/16.
 * 本App的主页，包括搜索图书，和切换账号，个人信息，管理员权限，当前借阅，过去借阅，退出程序
 */

public class MainPage extends Activity implements View.OnClickListener {
    private Button selectButton, searchButton;
    private final int LISTSELECT = 1;
    private EditText inputSearchBook;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList = new ArrayList<Books>();
    private List<PresentBooks> books = new ArrayList<PresentBooks>();
    private LibraryDB libraryDB;
    private boolean isSearch;
    private SharedPreferences pref;
    private Toast mToast;
    private Button searchNet;
    private PersonMessage ps;
    private int j = 0, k = 0;
    private PersonMessage person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        ActivityCollector.addActivity(this);
        libraryDB = LibraryDB.getInstance(this);
        selectButton = (Button) findViewById(R.id.select_button);
        searchButton = (Button) findViewById(R.id.search_book);
        searchNet = (Button) findViewById(R.id.search_net);
        inputSearchBook = (EditText) findViewById(R.id.book_search_name);
        impart();
        selectButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(LISTSELECT);
            }
        };
        selectButton.setOnClickListener(listener);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case LISTSELECT:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择");
                /**
                 * 切换账号，个人信息，管理员权限，当前借阅，过去借阅
                 */
                final Intent intentChang = new Intent(this, Login.class);
                intentChang.putExtra("change", true);
                final Intent intentUser = new Intent(this, UserCcount.class);
                final Intent intentRoot = new Intent(this, ManagerRoot.class);
                final Intent intentPresent = new Intent(this, PresentBorrow.class);
                final Intent intentPass = new Intent(this, PassBorrow.class);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String data = "personSet";
                                intentUser.putExtra("activity", data);
                                startActivity(intentUser);
                                break;
                            case 1:
                                startActivity(intentPresent);
                                break;
                            case 2:
                                startActivity(intentPass);
                                break;

                            case 3:
                                startActivity(intentChang);
                                break;
                            case 4:
                                pref = PreferenceManager.getDefaultSharedPreferences(MainPage.this);
                                if (pref.getInt("userId", 0) == 12345) {
                                    startActivity(intentRoot);
                                } else {
                                    useToast("您不是管理员！");
                                }
                                break;
                            case 5:
                                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainPage.this);
                                dialog2.setTitle("退出").setMessage("是否退出程序？")
                                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                dialog2.show();

                                break;
                            default:
                                break;
                        }
                    }
                };
                builder.setItems(R.array.arrays, listener);
                dialog = builder.create();
                break;
        }

        return dialog;
    }

    /**
     * 用户搜索图书
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_book:
                isSearch = true;
                bookList = (ListView) findViewById(R.id.list_search_book);
                bookName = inputSearchBook.getText().toString();
                BmobQuery<Books> bs = new BmobQuery<Books>();
                bs.findObjects(new FindListener<Books>() {
                    @Override
                    public void done(List<Books> list, BmobException e) {
                        if (e == null) {
                            booksList = list;
                            BookAdapter adapter = new BookAdapter(MainPage.this, R.layout.book_item, booksList);
                            bookList.setAdapter(adapter);
                        } else useToast("网络异常！");
                    }
                });

                /**
                 *将搜索结果显示出来，查看每本书的信息
                 */

                final Intent bookIntent = new Intent(this, DetailedBook.class);

                bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //ea173120a2
                        pref = PreferenceManager.getDefaultSharedPreferences(MainPage.this);
                        Books book = booksList.get(position);
                        bookIntent.putExtra("bookmessage", book);
                        final BmobQuery<PersonMessage> p = new BmobQuery<PersonMessage>();
                        p.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
                            @Override
                            public void done(PersonMessage personMessage, BmobException e) {
                                if (e == null) {
                                    ps = personMessage;
                                    String sss = personMessage.getIsRootManager();
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
                                    useToast("未联网！");

                                }
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 刷新借阅界面，根据借出，即将过期，过期三种情况将书标识不一样颜色
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        impart();
        if (isSearch) {
            bookName = inputSearchBook.getText().toString();
            bookList = (ListView) findViewById(R.id.list_search_book);
            BmobQuery<Books> b = new BmobQuery<Books>();
            b.findObjects(new FindListener<Books>() {
                @Override
                public void done(List<Books> list, BmobException e) {
                    if (e == null) {
                        booksList = list;
                        BookAdapter adapter = new BookAdapter(MainPage.this, R.layout.book_item, booksList);
                        bookList.setAdapter(adapter);
                    } else useToast("网络异常！");
                }
            });
            final Intent bookIntent = new Intent(this, DetailedBook.class);
                /*
                点击查看每本书的信息
                 */
            bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Books book = booksList.get(position);
                    bookIntent.putExtra("bookmessage", book);
                    final BmobQuery<PersonMessage> p = new BmobQuery<PersonMessage>();
                    p.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
                        @Override
                        public void done(PersonMessage personMessage, BmobException e) {
                            if (e == null) {
                                ps = personMessage;
                                String sss = personMessage.getIsRootManager();
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
                                useToast("未联网！");
                            }
                        }
                    });

                }
            });
        }
    }
    /**
     * 有过期，将要过期的书时，notification通知
     */
    public void impart() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        BmobQuery<PersonMessage> personMessageBmobQuery = new BmobQuery<>();
        personMessageBmobQuery.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
            @Override
            public void done(PersonMessage personMessage, BmobException e) {
                person = personMessage;
                BmobQuery<PresentBooks> pb = new BmobQuery<>();
                pb.addWhereEqualTo("userId", pref.getInt("userId", 0));
                pb.findObjects(new FindListener<PresentBooks>() {
                    @Override
                    public void done(List<PresentBooks> list, BmobException e) {
                        if (e == null) {
                            books = list;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date1 = new Date();
                            for (int i = 0; i < books.size(); i++) {
                                try {
                                    date1 = sdf.parse(books.get(i).getBackTime());
                                    Date nowDate = new Date();
                                    Date date2 = sdf.parse(sdf.format(nowDate));
                                    long distance = date1.getTime() - date2.getTime();
                                    long days = distance / (1000 * 60 * 60 * 24);
                                    if (days <= 7 && days >= 1) {
                                        j++;
                                    } else if (days < 1) {
                                        k++;
                                    }
                                } catch (Exception ee) {
                                }
                            }
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (j > 0) {
                                person.setWpastBooks(j);
                                Notification.Builder notification = new Notification.Builder(MainPage.this).setTicker("图书通知！").setContentTitle("图书通知")
                                        .setContentText("您有" + j + "本书将要过期！").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
                                manager.notify(3, notification.build());
                                person.setWpastBooks(j);
                            } else {
                                person.setWpastBooks(0);
                                manager.cancel(3);
                            }
                            if (k > 0) {
                                person.setPastBooks(k);
                                Notification.Builder notification = new Notification.Builder(MainPage.this).setTicker("图书通知！").setContentTitle("图书通知")
                                        .setContentText("您有" + k + "本书已过期，请尽快归还！").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
                                manager.notify(4, notification.build());
                                person.setPastBooks(k);
                            } else {
                                manager.cancel(4);
                                person.setPastBooks(0);

                            }
                            person.update(pref.getString("objectid", ""), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                     j=0;k=0;
                                    }else    useToast("网络错误！");
                                }
                            });
                        } else {
                            useToast("网络异常");
                        }
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
