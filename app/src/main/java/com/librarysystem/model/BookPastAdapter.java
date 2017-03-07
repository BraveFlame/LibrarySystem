

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
     *图书适配器，用于显示过去借阅的图书信息
     */

    public class BookPastAdapter extends ArrayAdapter<Books> {
        private int resourceId;
        public BookPastAdapter(Context context, int textViewResourceId, List<Books> objects){
            super(context,textViewResourceId,objects);
            resourceId=textViewResourceId;

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Books books=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            TextView bookName=(TextView) view.findViewById(R.id.past_book_name);
            TextView bookAuthor=(TextView) view.findViewById(R.id.past_book_author);
            TextView bookId=(TextView) view.findViewById(R.id.past_book_id);
            TextView bookBorrowDate=(TextView)view.findViewById(R.id.book_borrow_date);
            TextView bookBackDate=(TextView)view.findViewById(R.id.book_back_date);
            bookName.setText(books.getBookName());
            bookAuthor.setText("作者："+books.getBookAuthor());
            bookId.setText("编号："+String.valueOf(books.getBookId()));
            bookBorrowDate.setText("借阅日期："+books.getLentTime().toString());
            bookBackDate.setText("归还日期："+books.getBackTime().toString());
            return view;
        }

    }

