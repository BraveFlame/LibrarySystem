package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        libraryDB=LibraryDB.getInstance(this);
        libraryDB.getPresentBooks(booksList);
        //将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_search_book);
        bookList.setAdapter(adapter);
    }
}
