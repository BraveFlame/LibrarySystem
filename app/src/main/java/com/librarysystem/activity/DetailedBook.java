package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/12/25.
 */
/*
书的主要信息
 */
public class DetailedBook extends Activity {
    private TextView detailed_name,detailed_author,detailed_id,detailed_message,detailed_status;
    private Button detailed_button;
    private LibraryDB libraryDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedbook);
        final Books book=(Books)getIntent().getParcelableExtra("bookmessage");
        detailed_id=(TextView)findViewById(R.id.detailed_id);
        detailed_name=(TextView)findViewById(R.id.detailed_name);
        detailed_author=(TextView)findViewById(R.id.detailed_author);
        detailed_message=(TextView)findViewById(R.id.detailed_message);
        detailed_status=(TextView)findViewById(R.id.detailed_status);
        detailed_id.setText("编号："+book.getBookId());
        detailed_name.setText("书名："+book.getBookName());
        detailed_author.setText("作者："+book.getBookAuthor());
        detailed_message.setText("主要信息："+book.getUserDescription());
        detailed_status.setText("状态："+book.getIsLent());
        detailed_button=(Button)findViewById(R.id.detailed_button);
        libraryDB=LibraryDB.getInstance(this);
        if((book.getIsLent().equals("借出"))){
            detailed_button.setEnabled(false);
        }
        detailed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(book.getIsLent().equals("可借")){
                 book.setIsLent("借出");
               if( libraryDB.borrowBook(book));
                 {
                     detailed_status.setText("状态：借出");
                    detailed_button.setEnabled(false);

                 }


             }
            }
        });

    }
}
