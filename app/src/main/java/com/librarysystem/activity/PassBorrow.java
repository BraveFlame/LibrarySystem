package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.BookPastAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/12/25.
 */

public class PassBorrow extends Activity {
    private LibraryDB libraryDB;
    private ListView bookList;
    private TextView title;
    private List<Books> booksList=new ArrayList<Books>();
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_book);
        title=(TextView)findViewById(R.id.title) ;
        title.setText("历史借阅");
        libraryDB= LibraryDB.getInstance(this);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB.getPastBooks(pref.getInt("userId",0),booksList);
        //将搜索结果显示出来
        BookPastAdapter adapter=new BookPastAdapter(this,R.layout.past_book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);

    }
}
