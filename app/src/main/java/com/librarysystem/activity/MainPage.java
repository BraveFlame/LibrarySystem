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
import com.librarysystem.sqlite.LibraryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by g on 2016/10/16.
 */

public class MainPage extends Activity implements View.OnClickListener {
    private Button selectButton, searchButton;
    private final int LISTSELECT = 1;
    private EditText inputSearchBook;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList = new ArrayList<Books>();
    private LibraryDB libraryDB;
    private boolean isSearch;
    private SharedPreferences pref;
    private Toast mToast;
    private Button searchNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        ActivityCollector.addActivity(this);
        libraryDB = LibraryDB.getInstance(this);
        selectButton = (Button) findViewById(R.id.select_button);
        searchButton = (Button) findViewById(R.id.search_book);
        searchNet=(Button)findViewById(R.id.search_net);
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
                                if( pref.getInt("userId",0)==12345){
                                    startActivity(intentRoot);
                                }else {
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

    //用户搜索图书
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_book:
                isSearch = true;
                bookName = inputSearchBook.getText().toString();
                libraryDB.getBookMeassage(bookName, booksList);
                if(booksList.size()==0){
                    useToast("没有符合搜索要求的图书！");
                    searchNet.setVisibility(View.VISIBLE);
                    searchNet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          Intent net=new Intent(MainPage.this,NetBook.class);
                            net.putExtra("search",bookName);
                            startActivity(net);
                        }
                    });



                }else {
                    searchButton.setVisibility(View.GONE);
                }
                //将搜索结果显示出来
                BookAdapter adapter = new BookAdapter(MainPage.this, R.layout.book_item, booksList);
                bookList = (ListView) findViewById(R.id.list_search_book);
                bookList.setAdapter(adapter);
                final Intent bookIntent = new Intent(this, DetailedBook.class);
                /*
                点击查看每本书的信息
                 */
                bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Books book = booksList.get(position);
                        bookIntent.putExtra("bookmessage", book);
                        startActivity(bookIntent);


                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        impart();
        if (isSearch) {
            bookName = inputSearchBook.getText().toString();
            libraryDB.getBookMeassage(bookName, booksList);

            //将搜索结果显示出来
            BookAdapter adapter = new BookAdapter(MainPage.this, R.layout.book_item, booksList);
            bookList = (ListView) findViewById(R.id.list_search_book);
            bookList.setAdapter(adapter);
            final Intent bookIntent = new Intent(this, DetailedBook.class);
                /*
                点击查看每本书的信息
                 */
            bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Books book = booksList.get(position);
                    bookIntent.putExtra("bookmessage", book);
                    startActivity(bookIntent);


                }
            });
        }
    }

    public void impart() {
        SharedPreferences pref;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        int j = 0, k = 0;
        PersonMessage personMessage = new PersonMessage();
        Books book = new Books();
        List<Books> books = new ArrayList<Books>();

        libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 200000));
        libraryDB.getPresentBooks(pref.getInt("userId", 200000), books);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        for (int i = 0; i < books.size(); i++) {

            try {
                date1 = sdf.parse(books.get(i).getBackTime());
                Date nowDate = new Date();
                Date date2=sdf.parse(sdf.format(nowDate));
                long distance = date1.getTime() - date2.getTime();
                long days = distance / (1000 * 60 * 60 * 24);
                if (days <= pref.getInt("remain",7) && days >= 1) {
                    j++;
                } else if (days < 1) {
                    k++;
                }

            } catch (Exception e) {

            }

        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (j > 0) {
            personMessage.setWpastBooks("" + j);

            Notification.Builder notification = new Notification.Builder(this).setTicker("图书通知！").setContentTitle("图书通知")
                    .setContentText("您有" + j + "本书将要过期！").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
            manager.notify(3, notification.build());
            personMessage.setWpastBooks("" + j);
            libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
        }else {
            personMessage.setWpastBooks("0");
            libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
            manager.cancel(3);
        }
        if (k > 0) {
            personMessage.setPastBooks("" + k);

            Notification.Builder notification = new Notification.Builder(this).setTicker("图书通知！").setContentTitle("图书通知")
                    .setContentText("您有" + k + "本书已过期，请尽快归还！").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
            manager.notify(4, notification.build());
            personMessage.setPastBooks("" + k);
            libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());

        } else {
            manager.cancel(4);
            personMessage.setPastBooks("0");
            libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());
        }
    }
    public void useToast(String text){
        if(mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelToast();
    }
}
