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
    private EditText update_name, update_author, update_id, update_message,update_press, update_version, update_status, update_date;
    private Button balter, bdelete, bupdate;
    private LibraryDB libraryDB;
    private SharedPreferences pref;
    private Books book;
    private Toast mToast;

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
        update_press=(EditText)findViewById(R.id.update_press);

        update_id.setText(String.valueOf(book.getBookId()));
        update_id.setEnabled(false);
        update_name.setText(book.getBookName());
        update_name.setEnabled(false);
        update_author.setText(book.getBookAuthor());
        update_author.setEnabled(false);

        update_message.setText(book.getUserDescription());
        update_status.setText(book.getIsLent());
        update_date.setText(book.getBackTime());
        update_press.setText(book.getPress());
        update_version.setText(book.getVersion());

        update_message.setEnabled(false);
        update_status.setEnabled(false);
        update_date.setEnabled(false);
        update_version.setEnabled(false);
        update_press.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        int id = book.getBookId();
        switch (v.getId()) {

            case R.id.bdelete:
                if (book.getIsLent().equals("借出")) {
                    useToast("书已借出,无法删除！");
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateBook.this);
                    dialog.setTitle("删除图书！").setMessage("是否确定删除？").setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (libraryDB.deleteBooks(book.getBookId())) {

                                useToast("删除成功！");
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
                   useToast("借出无法更新！");
                } else {
                    try {
                        book.setBookId(Integer.valueOf(update_id.getText().toString()));
                        if(!update_author.getText().toString().equals("")&!update_name.getText().toString().equals("")) {
                            book.setBookAuthor(update_author.getText().toString());
                            book.setBookName(update_name.getText().toString());
                            book.setUserDescription(update_message.getText().toString());
                            book.setVersion(update_version.getText().toString());
                            book.setPress(update_press.getText().toString());

                            libraryDB.alterBooks(book, id);
                            update_id.setEnabled(false);
                            update_name.setEnabled(false);
                            update_author.setEnabled(false);
                            update_message.setEnabled(false);
                            update_version.setEnabled(false);
                            update_press.setEnabled(false);
                            useToast("更新成功！");
                        }else  {
                            useToast( "请完善信息！");
                        }
                    }catch (Exception e){
                        useToast("编号格式错误！");
                    }
                }
                break;
            case R.id.balter:
                if (book.getIsLent().equals("借出")) {
                  useToast( "借出无法更改！");
                } else {

                    update_id.setEnabled(true);
                    update_name.setEnabled(true);
                    update_author.setEnabled(true);
                    update_message.setEnabled(true);
                    update_press.setEnabled(true);
                    update_version.setEnabled(true);
                    break;}
                    default:
                        break;

        }
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
