package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/12/25.
 */

public class PassBorrow extends Activity {
    private LibraryDB libraryDB;
    private String bookName;
    private ListView bookList;
    private TextView btitle;
    private List<Books> booksList=new ArrayList<Books>();
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_book);
        libraryDB= LibraryDB.getInstance(this);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        btitle=(TextView)findViewById(R.id.btitle);
        btitle.setText("历史借阅");
        libraryDB.getPastBooks(pref.getInt("userId",0),booksList);
        //将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);

    }
}
