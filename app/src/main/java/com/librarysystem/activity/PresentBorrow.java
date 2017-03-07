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
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/12/2.
 * 当前借阅的书籍
 */

public class PresentBorrow extends Activity {

    private LibraryDB libraryDB;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList = new ArrayList<Books>();
    private SharedPreferences pref;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_book);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB = LibraryDB.getInstance(this);
        libraryDB.getPresentBooks(pref.getInt("userId", 0), booksList);
        if (booksList.size() == 0) {
            useToast("当前没有借阅的书籍！");
        }
        //将搜索结果显示出来
        BookAdapter adapter = new BookAdapter(this, R.layout.book_item, booksList);
        bookList = (ListView) findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
        final Intent bookIntent = new Intent(this, BackBook.class);
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

    /**
     * 还书时刷新界面
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        libraryDB.getPresentBooks(pref.getInt("userId", 0), booksList);
        //  将搜索结果显示出来
        BookAdapter adapter = new BookAdapter(this, R.layout.book_item, booksList);
        bookList = (ListView) findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
        if (booksList.size() == 0) {
            useToast("当前没有借阅的书籍！");
        }
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
