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
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by g on 2016/12/21.
 * 增加书库，以及列出书库的书以便修改
 */

public class ChangeBooks extends Activity implements View.OnClickListener {
    private EditText bName, bId, bAuthor, bDes, queryName, bPress, bVersion;
    private Button bsave, bquery;
    private String bookName;
    private ListView bookList;
    private List<Books> repertoryBooks = new ArrayList<Books>();
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
                DialogMessage.showDialog(ChangeBooks.this);
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
                    if (!books.getBookName().equals("") && !books.getBookAuthor().equals("") && !books.getPress().equals("")
                            && !books.getVersion().equals("")) {
                        /**
                         Bmob
                         */
                        books.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {
                                    ToastMessage.useToast(ChangeBooks.this, "增加成功");
                                    bName.setText("");
                                    bId.setText("");
                                    bAuthor.setText("");
                                    bDes.setText("");
                                    bPress.setText("");
                                    bVersion.setText("");
                                } else {
                                    ToastMessage.useToast(ChangeBooks.this, "已存在或网络异常");
                                }
                            }
                        });
                    } else {
                        ToastMessage.useToast(ChangeBooks.this, "输入书名");
                    }
                } catch (Exception e) {
                    ToastMessage.useToast(ChangeBooks.this, "编号格式错误！");
                }
                break;

/**
 *查询书库的书
 */
            case R.id.bquery:
                isQuery = true;
                queryBooks();
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
            queryBooks();
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

    public void queryBooks() {
        try {
            DialogMessage.showDialog(ChangeBooks.this);
            bookName = queryName.getText().toString();
            bookList = (ListView) findViewById(R.id.list_query_book);
            BmobQuery<Books> pb = new BmobQuery<>();
            pb.findObjects(new FindListener<Books>() {
                @Override
                public void done(List<Books> list, BmobException e) {
                    DialogMessage.closeDialog();
                    if (e == null) {
                        if (list.size() == 0) {
                            ToastMessage.useToast(ChangeBooks.this, "没有该书籍！");
                        } else {
                            libraryDB.saveBookMeassage(list);
                            libraryDB.getBookMeassage(bookName, repertoryBooks);
                            if (repertoryBooks.size() == 0) {
                                ToastMessage.useToast(ChangeBooks.this, "没有该书籍！");
                            } else {
                                BookAdapter adapter = new BookAdapter(ChangeBooks.this, R.layout.book_item, repertoryBooks);
                                bookList.setAdapter(adapter);
                            }
                        }
                    } else {
                        ToastMessage.useToast(ChangeBooks.this, "获取失败！");
                    }
                }
            });
            final Intent bookIntent = new Intent(this, UpdateBook.class);
            /**
             *点击查看每本书的信息
             */
            bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Books book = repertoryBooks.get(position);
                    bookIntent.putExtra("bookmessage", book);
                    startActivity(bookIntent);
                }
            });
        } catch (Exception e) {
            ToastMessage.useToast(ChangeBooks.this, "编号格式错误！");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }
}