package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/2/21.
 * 管理员删除，修改书籍
 */

public class UpdateBook extends Activity implements View.OnClickListener {
    private EditText update_name, update_author, update_id, update_message, update_press, update_version, update_status, update_date;
    private Button balter, bdelete, bupdate;
    private Books book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_book);
        book = (Books) getIntent().getSerializableExtra("bookmessage");
        init();
        balter.setOnClickListener(this);
        bdelete.setOnClickListener(this);
        bupdate.setOnClickListener(this);


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

    private void init() {
        update_id = (EditText) findViewById(R.id.update_id);
        update_name = (EditText) findViewById(R.id.update_name);
        update_author = (EditText) findViewById(R.id.update_author);
        update_version = (EditText) findViewById(R.id.update_version);
        update_message = (EditText) findViewById(R.id.update_message);
        update_status = (EditText) findViewById(R.id.update_status);
        update_date = (EditText) findViewById(R.id.update_date);
        update_press = (EditText) findViewById(R.id.update_press);
        balter = (Button) findViewById(R.id.balter);
        bdelete = (Button) findViewById(R.id.bdelete);
        bupdate = (Button) findViewById(R.id.bupdate);
    }

    @Override
    public void onClick(View v) {
        int id = book.getBookId();
        switch (v.getId()) {
            case R.id.bdelete:
                if (!book.getIsLent().equals("可借")) {
                    ToastMessage.useToast(UpdateBook.this, "书已借出,无法删除！");
                } else {
                    DialogMessage.showDialog(UpdateBook.this);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateBook.this);
                    dialog.setTitle("删除图书！").setMessage("是否确定删除？").setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            book.delete(book.getObjectId(), new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    DialogMessage.closeDialog();
                                    if (e == null) {
                                        ToastMessage.useToast(UpdateBook.this, "删除成功");
                                        finish();
                                    } else {
                                        ToastMessage.useToast(UpdateBook.this, "删除异常");
                                    }
                                }
                            });
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
                if (!book.getIsLent().equals("可借")) {
                    ToastMessage.useToast(UpdateBook.this, "借出无法更新！");
                } else {
                    try {
                        book.setBookId(Integer.valueOf(update_id.getText().toString()));
                        if (!update_author.getText().toString().equals("") & !update_name.getText().toString().equals("")) {
                            book.setBookAuthor(update_author.getText().toString());
                            book.setBookName(update_name.getText().toString());
                            book.setUserDescription(update_message.getText().toString());
                            book.setVersion(update_version.getText().toString());
                            book.setPress(update_press.getText().toString());
                            DialogMessage.showDialog(UpdateBook.this);
                            book.update(book.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    DialogMessage.closeDialog();
                                    if (e == null) {
                                        ToastMessage.useToast(UpdateBook.this, "更新成功");
                                        update_id.setEnabled(false);
                                        update_name.setEnabled(false);
                                        update_author.setEnabled(false);
                                        update_message.setEnabled(false);
                                        update_version.setEnabled(false);
                                        update_press.setEnabled(false);

                                    } else {
                                        ToastMessage.useToast(UpdateBook.this, "更新异常");
                                    }
                                }
                            });

                        } else {
                            ToastMessage.useToast(UpdateBook.this, "请完善信息！");
                        }
                    } catch (Exception e) {
                        ToastMessage.useToast(UpdateBook.this, "编号格式错误！");
                    }
                }
                break;
            case R.id.balter:
                if (!book.getIsLent().equals("可借")) {
                    ToastMessage.useToast(UpdateBook.this, "借出无法更改！");
                } else {
                    update_id.setEnabled(true);
                    update_name.setEnabled(true);
                    update_author.setEnabled(true);
                    update_message.setEnabled(true);
                    update_press.setEnabled(true);
                    update_version.setEnabled(true);
                    break;
                }
            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastMessage.cancelToast();
    }
}
