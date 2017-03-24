package com.librarysystem.model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.librarysystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by g on 2016/12/21.
 * 图书适配器，用于显示页面的图书信息
 */




public class BookAdapter extends ArrayAdapter<Books> {
    private int resourceId;
    private Context contextThe;

    public BookAdapter(Context context, int textViewResourceId, List<Books> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        contextThe = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Books books = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = sdf.parse(books.getBackTime());
            Date nowDate = new Date();
            Date date2 = sdf.parse(sdf.format(nowDate));
            long distance = date1.getTime() - date2.getTime();
            long days = distance / (1000 * 60 * 60 * 24);
            /**
             * 根据书应该归还时间标识不同颜色
             */
            if (days < 1) {
                view.setBackgroundResource(R.drawable.selector);
            } else if (days >= 1 && days < 7) {
                view.setBackgroundColor(Color.parseColor("#efc95b"));
            } else {
                view.setBackgroundColor(Color.parseColor("#f2d47e"));
            }

        } catch (Exception e) {


        }

        TextView bookName = (TextView) view.findViewById(R.id.book_name);
        TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);
        TextView bookId = (TextView) view.findViewById(R.id.book_id);

        bookName.setText(books.getBookName());
        bookAuthor.setText("作者：" + books.getBookAuthor());
        bookId.setText("编号：" + String.valueOf(books.getBookId()));


        return view;
    }


}