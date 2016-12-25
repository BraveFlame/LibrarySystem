package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2016/12/21.
 */
/*
用于增加书库的书
*/
public class ChangeBooks extends Activity {
    private EditText bName,bId,bAuthor,bDes;
    private Button bsave;
    LibraryDB libraryDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changebooks);
        libraryDB=LibraryDB.getInstance(this);
        final Books books=new Books();
        bName=(EditText)findViewById(R.id.bName);
        bId=(EditText)findViewById(R.id.bId) ;
        bAuthor=(EditText)findViewById(R.id.bAuthor);
        bDes=(EditText)findViewById(R.id.bDesc);
        bsave=(Button)findViewById(R.id.bsave);

        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                books.setBookId(Integer.valueOf(bId.getText().toString()));
                books.setBookName(bName.getText().toString());
                books.setBookAuthor(bAuthor.getText().toString());
                books.setUserDescription(bDes.getText().toString());
                books.setIsLent("可借");

                //如果原来没有则添加成功
                if(libraryDB.saveBookMeassage(books)) {
                    Toast.makeText(ChangeBooks.this, "successful", Toast.LENGTH_SHORT);
                   finish();
                }
            }
        });

    }
}
