package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
    private List<Books> booksList=new ArrayList<Books>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_book);
        libraryDB= LibraryDB.getInstance(this);
        libraryDB.getPastBooks(booksList);
        //将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
//        final Intent bookIntent=new Intent(this,BackBook.class);
//                /*
//                点击查看每本书的信息
//                 */
//        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Books book=booksList.get(position);
//                bookIntent.putExtra("bookmessage",book);
//                startActivity(bookIntent);
//
//
//            }
//        });
    }
}
