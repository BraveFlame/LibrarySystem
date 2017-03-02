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
import com.librarysystem.model.PersonMessage;
import com.librarysystem.sqlite.LibraryDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_book);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        libraryDB=LibraryDB.getInstance(this);
        libraryDB.getPresentBooks(pref.getInt("userId",0),booksList);
        if (booksList.size()==0){
            useToast("当前没有借阅的书籍！");
        }
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

        impart();
      //  将搜索结果显示出来
        BookAdapter adapter=new BookAdapter(this,R.layout.book_item,booksList);
        bookList=(ListView)findViewById(R.id.list_present_book);
        bookList.setAdapter(adapter);
        final Intent bookIntent=new Intent(this,BackBook.class);
                /*
                点击查看每本书的信息
                 */
//        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view.setBackgroundColor(Color.parseColor("#FF4081"));
//                Books book=booksList.get(position);
//                bookIntent.putExtra("bookmessage",book);
//                startActivity(bookIntent);
//
//            }
//        });
    }
/*
过期时，还完可以预约
 */
    private void impart() {
        SharedPreferences pref;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        int  k = 0;
        PersonMessage personMessage = new PersonMessage();
        Books book = new Books();
        List<Books> books = new ArrayList<Books>();

        libraryDB.getPersonalMeassage(personMessage, pref.getInt("userId", 200000));
        libraryDB.getPresentBooks(pref.getInt("userId", 200000), books);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        for (int i = 0; i < books.size(); i++) {

            try {
                date1 = sdf.parse(books.get(i).getBackTime());

                Date nowDate = new Date();
                Date date2=sdf.parse(sdf.format(nowDate));
                long distance = date1.getTime() - date2.getTime();
                long days = distance / (1000 * 60 * 60 * 24);

               if (days <1) {
                    k++;
                }

            } catch (Exception e) {

            }
            personMessage.setPastBooks("" + k);
            libraryDB.alterPersonalMessage(personMessage, personMessage.getUserId());        }
    }

    public void useToast(String text){
        if(mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelToast();
    }
}
