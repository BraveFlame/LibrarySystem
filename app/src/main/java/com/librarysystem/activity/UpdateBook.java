package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.sqlite.LibraryDB;

/**
 * Created by g on 2017/2/21.
 */

public class UpdateBook extends Activity implements View.OnClickListener {
    private EditText update_name, update_author, update_id, update_message, update_version, update_status, update_date;
    private Button balter, bdelete, bupdate;
    private LibraryDB libraryDB;
    private SharedPreferences pref;
    private Books book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_book);
        libraryDB = LibraryDB.getInstance(this);
        book = (Books) getIntent().getParcelableExtra("bookmessage");
        balter = (Button) findViewById(R.id.balter);
        bdelete = (Button) findViewById(R.id.bdelete);
        bupdate = (Button) findViewById(R.id.bupdate);
        balter.setOnClickListener(this);
        bdelete.setOnClickListener(this);
        bupdate.setOnClickListener(this);
        update_id = (EditText) findViewById(R.id.update_id);
        update_name = (EditText) findViewById(R.id.update_name);
        update_author = (EditText) findViewById(R.id.update_author);
        update_version = (EditText) findViewById(R.id.update_version);
        update_message = (EditText) findViewById(R.id.update_message);
        update_status = (EditText) findViewById(R.id.update_status);
        update_date = (EditText) findViewById(R.id.update_date);

        update_id.setText(String.valueOf(book.getBookId()));
        update_id.setEnabled(false);
        update_name.setText(book.getBookName());
        update_name.setEnabled(false);
        update_author.setText(book.getBookAuthor());
        update_author.setEnabled(false);

        update_message.setText(book.getUserDescription());
        update_status.setText(book.getIsLent());
        update_date.setText(book.getBackTime());

        update_message.setEnabled(false);
        update_status.setEnabled(false);
        update_date.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        int id = book.getBookId();
        switch (v.getId()) {

            case R.id.bdelete:
                if (book.getIsLent().equals("借出")) {
                    Toast.makeText(UpdateBook.this, "书已借出！", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateBook.this);
                    dialog.setTitle("删除图书！").setMessage("是否确定删除？").setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (libraryDB.deleteBooks(book)) {

                                Toast.makeText(UpdateBook.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.bupdate:
                if (book.getIsLent().equals("借出")) {
                    Toast.makeText(UpdateBook.this, "借出无法更新！", Toast.LENGTH_SHORT).show();
                } else {
                    book.setBookId(Integer.valueOf(update_id.getText().toString()));
                    book.setBookAuthor(update_author.getText().toString());
                    book.setBookName(update_name.getText().toString());
                    book.setUserDescription(update_message.getText().toString());

                    libraryDB.alterBooks(book, id);
                    update_id.setEnabled(false);
                    update_name.setEnabled(false);
                    update_author.setEnabled(false);
                    update_message.setEnabled(false);
                    Toast.makeText(UpdateBook.this, "更新成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.balter:
                if (book.getIsLent().equals("借出")) {
                    Toast.makeText(UpdateBook.this, "借出无法更改！", Toast.LENGTH_SHORT).show();
                } else {
                    update_id.setEnabled(true);
                    update_name.setEnabled(true);
                    update_author.setEnabled(true);
                    update_message.setEnabled(true);
                    break;}
                    default:
                        break;

        }
    }
}
