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

import com.librarysystem.R;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/12/2.
 */
/*
当前借阅
 */
public class PresentBorrow extends Activity {

    private LibraryDB libraryDB;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList=new ArrayList<Books>();
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.present_book);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB=LibraryDB.getInstance(this);
        libraryDB.getPresentBooks(pref.getInt("userId",0),booksList);
        //将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
        final Intent bookIntent=new Intent(this,BackBook.class);
                /*
                点击查看每本书的信息
                 */
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book=booksList.get(position);
                bookIntent.putExtra("bookmessage",book);
                startActivity(bookIntent);


            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        libraryDB.getPresentBooks(pref.getInt("userId",0),booksList);
        //将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
        final Intent bookIntent=new Intent(this,BackBook.class);
                /*
                点击查看每本书的信息
                 */
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book=booksList.get(position);
                bookIntent.putExtra("bookmessage",book);
                startActivity(bookIntent);

            }
        });
    }
}
