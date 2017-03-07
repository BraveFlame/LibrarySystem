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
 * 增加书库，以及列出书库的书以便修改
 */

public class ChangeBooks extends Activity implements View.OnClickListener {
    private EditText bName, bId, bAuthor, bDes, queryName, bPress, bVersion;
    private Button bsave, bquery;
    private String bookName;
    private ListView bookList;
    private List<Books> booksList = new ArrayList<Books>();
    private LibraryDB libraryDB;
    private boolean isQuery;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_books);
        libraryDB = LibraryDB.getInstance(this);
        init();
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
                    books.setBookName(bName.getText().toString());
                    books.setBookAuthor(bAuthor.getText().toString());
                    books.setUserDescription(bDes.getText().toString());
                    books.setPress(bPress.getText().toString());
                    books.setVersion(bVersion.getText().toString());
                    books.setIsSubscribe("无");
                    books.setIsContinue("无");
                    books.setBackTime("");
                    books.setIsLent("可借");
                    /**
                     * 如果书库原来没有此编号的书，则添加成功
                     */
                    if (libraryDB.saveBookMeassage(books)) {
                        useToast("successful");
                        finish();
                    } else {
                        useToast("已存在！");
                    }
                } catch (Exception e) {
                    useToast("编号格式错误！");
                }
                break;

/**
 *查询书库的书
 */
            case R.id.bquery:

                try {
                    isQuery = true;
                    bookName = queryName.getText().toString();
                    libraryDB.getBookMeassage(bookName, booksList);
                    if (booksList.size() == 0) {
                        useToast("没有符合搜索要求的图书！");
                    }
                    /**
                     * 将搜索结果显示出来
                     */
                    BookAdapter adapter = new BookAdapter(ChangeBooks.this, R.layout.book_item, booksList);
                    bookList = (ListView) findViewById(R.id.list_query_book);
                    bookList.setAdapter(adapter);
                    final Intent bookIntent = new Intent(this, UpdateBook.class);
                    /**
                     *点击查看每本书的信息
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
                    useToast("编号格式错误！");
                }
                break;

            default:
                break;
        }
    }

    /**
     * 重写onRestart（）以便有删除书时及时刷新界面
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isQuery) {
            try {

                bookName = queryName.getText().toString();
                libraryDB.getBookMeassage(bookName, booksList);
                BookAdapter adapter = new BookAdapter(ChangeBooks.this, R.layout.book_item, booksList);
                bookList = (ListView) findViewById(R.id.list_query_book);
                bookList.setAdapter(adapter);
                final Intent bookIntent = new Intent(this, UpdateBook.class);
                bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Books book = booksList.get(position);
                        bookIntent.putExtra("bookmessage", book);
                        startActivity(bookIntent);
                    }
                });
            } catch (Exception e) {
                useToast("编号格式错误！");
            }
        }
    }

    public void init() {
        bName = (EditText) findViewById(R.id.bName);
        bId = (EditText) findViewById(R.id.bId);
        bAuthor = (EditText) findViewById(R.id.bAuthor);
        bDes = (EditText) findViewById(R.id.bDesc);
        queryName = (EditText) findViewById((R.id.query_book));
        bPress = (EditText) findViewById(R.id.bPress);
        bVersion = (EditText) findViewById(R.id.bVersion);
        bsave = (Button) findViewById(R.id.bsave);
        bquery = (Button) findViewById(R.id.bquery);
    }

    public void useToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(ChangeBooks.this, text, Toast.LENGTH_SHORT);
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