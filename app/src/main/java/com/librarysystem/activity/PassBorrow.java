package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.BookPastAdapter;
import com.librarysystem.model.PastBooks;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by g on 2016/12/25.
 * 过去借阅的书籍
 */

public class PassBorrow extends Activity {

    private ListView bookList;
    private TextView title;
    private List<PastBooks> booksList = new ArrayList<PastBooks>();
    private SharedPreferences pref;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_book);
        title = (TextView) findViewById(R.id.title);
        title.setText("历史借阅");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        bookList = (ListView) findViewById(R.id.list_present_book);

        BmobQuery<PastBooks> pb = new BmobQuery<>();
        pb.addWhereEqualTo("userId", pref.getInt("userId", 0));
        pb.findObjects(new FindListener<PastBooks>() {
            @Override
            public void done(List<PastBooks> list, BmobException e) {
                if (e == null) {
                    booksList = list;
                    //将搜索结果显示出来
                    BookPastAdapter adapter = new BookPastAdapter(PassBorrow.this, R.layout.past_book_item, booksList);
                    bookList.setAdapter(adapter);
                    if (booksList.size() == 0) {
                        useToast("暂无历史借阅！");
                    }
                } else {
                    useToast("获取异常！");
                }
            }
        });



    }

    public void useToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
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
