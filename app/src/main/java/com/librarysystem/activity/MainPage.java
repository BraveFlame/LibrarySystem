package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.librarysystem.R;
import com.librarysystem.model.BookAdapter;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2016/10/16.
 */

public class MainPage extends Activity implements View.OnClickListener{
    private Button selectButton,searchButton;
    private final int LISTSELECT=1;
    private EditText inputSearchBook;
    private String bookName;
    private ListView bookList;
    private List<Books>booksList=new ArrayList<Books>();
    private LibraryDB libraryDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        libraryDB=LibraryDB.getInstance(this);
        selectButton=(Button)findViewById(R.id.select_button);
        searchButton=(Button)findViewById(R.id.search_book);
        inputSearchBook=(EditText)findViewById(R.id.book_search_name);
        selectButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(LISTSELECT);
            }
        };
        selectButton.setOnClickListener(listener);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;

        switch (id){
            case LISTSELECT:
                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("选择");
                final  Intent intent4=new Intent(this,Login.class);
                final  Intent intent0=new Intent(this,PersonalSet.class);
                final Intent intent5=new Intent(this,ChangeBooks.class);
                final Intent intent1=new Intent(this,PresentBorrow.class);
                DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                String data="personSet";
                                intent0.putExtra("activity",data);
                                startActivity(intent0);
                                break;
                            case 1:
                                startActivity(intent1);
                                break;
                            case 4:
                                startActivity(intent4);
                                break;
                            case 5:
                                startActivity(intent5);
                                break;
                            default:
                                break;
                        }
                    }
                };
                builder.setItems(R.array.arrays,listener);
                dialog=builder.create();
                break;
        }

        return dialog;
    }
    //用户搜索图书
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_book:
                bookName=inputSearchBook.getText().toString();
                libraryDB.getBookMeassage(bookName,booksList);
                //将搜索结果显示出来
                BookAdapter adapter=new BookAdapter(MainPage.this,R.layout.book_item,booksList);
                bookList=(ListView)findViewById(R.id.list_search_book);
                bookList.setAdapter(adapter);
                final Intent bookIntent=new Intent(this,DetailedBook.class);
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
                break;
            default:break;
        }
    }



}
