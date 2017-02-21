package com.librarysystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/12/21.
 */
/*
用于增加书库的书
*/
public class ChangeBooks extends Activity implements View.OnClickListener {
    private EditText bName, bId, bAuthor, bDes, queryName;
    private Button bsave, bquery;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList = new ArrayList<Books>();
    private LibraryDB libraryDB;
    private boolean isQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changebooks);
        libraryDB = LibraryDB.getInstance(this);

        bName = (EditText) findViewById(R.id.bName);
        bId = (EditText) findViewById(R.id.bId);
        bAuthor = (EditText) findViewById(R.id.bAuthor);
        bDes = (EditText) findViewById(R.id.bDesc);
        queryName = (EditText) findViewById((R.id.query_book));
        bsave = (Button) findViewById(R.id.bsave);
        bquery = (Button) findViewById(R.id.bquery);

        bsave.setOnClickListener(this);
        bquery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Books books = new Books();
        switch (v.getId()) {
            case R.id.bsave:

                try {
                    books.setBookId(Integer.valueOf(bId.getText().toString()));
                } catch (Exception e) {
                    Toast.makeText(ChangeBooks.this, "编号格式错误！", Toast.LENGTH_SHORT).show();
                }
                books.setBookName(bName.getText().toString());
                books.setBookAuthor(bAuthor.getText().toString());
                books.setUserDescription(bDes.getText().toString());
                books.setIsLent("可借");

                //如果原来没有则添加成功
                if (libraryDB.saveBookMeassage(books)) {
                    Toast.makeText(ChangeBooks.this, "successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangeBooks.this, "已存在！", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.bquery:

                try {
                    isQuery = true;
                    bookName = queryName.getText().toString();
                    libraryDB.getBookMeassage(bookName, booksList);
                    //将搜索结果显示出来
                    BookAdapter adapter = new BookAdapter(ChangeBooks.this, R.layout.book_item, booksList);
                    bookList = (ListView) findViewById(R.id.list_query_book);
                    bookList.setAdapter(adapter);
                    final Intent bookIntent = new Intent(this, UpdateBook.class);
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
                } catch (Exception e) {
                    Toast.makeText(ChangeBooks.this, "编号格式错误！", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isQuery) {
            try {

                bookName = queryName.getText().toString();
                libraryDB.getBookMeassage(bookName, booksList);
                //将搜索结果显示出来
                BookAdapter adapter = new BookAdapter(ChangeBooks.this, R.layout.book_item, booksList);
                bookList = (ListView) findViewById(R.id.list_query_book);
                bookList.setAdapter(adapter);
                final Intent bookIntent = new Intent(this, UpdateBook.class);
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
            } catch (Exception e) {
                Toast.makeText(ChangeBooks.this, "编号格式错误！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}