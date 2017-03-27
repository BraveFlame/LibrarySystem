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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.librarysystem.R;
import com.librarysystem.model.ActivityCollector;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentBooks;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
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
    private List<Books> repertoryBooks = new ArrayList<Books>();
    private List<PresentBooks> presentBooks = new ArrayList<PresentBooks>();
    private LibraryDB libraryDB;
    private boolean isSearch;
    private SharedPreferences pref;
    private PersonMessage ps;
    private int j = 0, k = 0;
    private PersonMessage person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        ActivityCollector.addActivity(this);
        // 初始化BmobSDK
        Bmob.initialize(this, "32d94fb0a064700f838f59bc0083ad70");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);
        libraryDB = LibraryDB.getInstance(this);
        selectButton = (Button) findViewById(R.id.select_button);
        searchButton = (Button) findViewById(R.id.search_book);
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
                                if (pref.getString("root", "").equals("管理员")) {
                                    startActivity(intentRoot);
                                } else {
                                    ToastMessage.useToast(MainPage.this, "您不是管理员！");
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
                DialogMessage.showDialog(MainPage.this);
                searchBooks();
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
            searchBooks();
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
            public void done(final PersonMessage personMessage, BmobException e) {
                person = personMessage;
                final BmobQuery<PresentBooks> pb = new BmobQuery<>();
                pb.addWhereEqualTo("userId", pref.getInt("userId", 0));
                pb.findObjects(new FindListener<PresentBooks>() {
                    @Override
                    public void done(List<PresentBooks> list, BmobException e) {
                        if (e == null) {
                            if(list.size()>0){
                                personMessage.setNowBorrow(list.size());
                            }else{
                                personMessage.setNowBorrow(0);
                            }
                            presentBooks = list;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date1;
                            for (int i = 0; i < presentBooks.size(); i++) {
                                try {
                                    date1 = sdf.parse(presentBooks.get(i).getBackTime());
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
                            notifyBooks(k, j, person);
                            k = 0;
                            j = 0;

                        } else {
                            ToastMessage.useToast(MainPage.this, "网络异常");
                        }
                    }
                });
            }
        });
    }

    public void notifyBooks(final int k, final int j, PersonMessage person) {
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

                } else ToastMessage.useToast(MainPage.this, "个人更新失败！");
            }
        });
    }

    public void searchBooks() {
        bookList = (ListView) findViewById(R.id.list_search_book);
        bookName = inputSearchBook.getText().toString();
        BmobQuery<Books> bs = new BmobQuery<Books>();
        bs.findObjects(new FindListener<Books>() {
            @Override
            public void done(List<Books> list, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null) {
                    if (list.size() <= 0) {
                        ToastMessage.useToast(MainPage.this, "没有该书籍！");
                    } else {
                        libraryDB.saveBookMeassage(list);
                        libraryDB.getBookMeassage(bookName, repertoryBooks);
                        if (repertoryBooks.size() == 0) {
                            ToastMessage.useToast(MainPage.this, "没有该书籍！");
                        }
                    }
                    BookAdapter adapter = new BookAdapter(MainPage.this, R.layout.book_item, repertoryBooks);
                    bookList.setAdapter(adapter);
                } else ToastMessage.useToast(MainPage.this, "网络异常！");
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
                Books book = repertoryBooks.get(position);
                bookIntent.putExtra("bookmessage", book);

                final BmobQuery<PersonMessage> p = new BmobQuery<PersonMessage>();
                p.getObject(pref.getString("objectid", ""), new QueryListener<PersonMessage>() {
                    @Override
                    public void done(PersonMessage personMessage, BmobException e) {
                        if (e == null) {
                            ps = personMessage;
                            bookIntent.putExtra("person", ps);
                            startActivity(bookIntent);
                            startActivity(bookIntent);
                        } else {
                            ToastMessage.useToast(MainPage.this, "获取个人信息失败！");
                        }
                    }
                });


            }
        });
    }
//
//    /**
//     * 显示进度对话框
//     */
//    public void showDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("正在加载...");
//            progressDialog.setCanceledOnTouchOutside(false);
//        }
//    }

//    /**
//     * 取消进度对话框
//     */
//    public void closeDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//
//    /**
//     * 解救Toast频繁显示
//     *
//     * @param text
//     */
//
//    public void useToast(String text) {
//        if (mToast == null) {
//            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        } else {
//            mToast.setText(text);
//            mToast.setDuration(Toast.LENGTH_SHORT);
//        }
//        mToast.show();
//    }
//
//    public void cancelToast() {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
        ToastMessage.cancelToast();
    }

    /**
     * 后台运行
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            moveTaskToBack(true);

        }

        return super.onKeyDown(keyCode, event);
    }
}