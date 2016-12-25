package com.librarysystem.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.librarysystem.R;

import java.util.List;

/**
 * Created by g on 2016/12/21.
 */
/*
图书适配器，用于显示页面的图书信息

 */
public class BookAdapter extends ArrayAdapter<Books> {
    private int resourceId;
    public BookAdapter(Context context, int textViewResourceId, List<Books>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Books books=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView bookName=(TextView) view.findViewById(R.id.book_name);
        TextView bookAuthor=(TextView) view.findViewById(R.id.book_author);
        TextView bookId=(TextView) view.findViewById(R.id.book_id);
        //TextView bookDate=(TextView)view.findViewById(R.id.book_date);
        bookName.setText(books.getBookName());
        bookAuthor.setText(books.getBookAuthor());
        bookId.setText(String.valueOf(books.getBookId()));
       // bookDate.setText(books.getLentTime().toString());


        return view;
    }

}