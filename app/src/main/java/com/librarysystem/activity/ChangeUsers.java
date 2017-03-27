package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.model.Books;
import com.librarysystem.model.PastBooks;
import com.librarysystem.model.PersonMessage;
import com.librarysystem.model.PresentBooks;
import com.librarysystem.model.Rule;
import com.librarysystem.others.DialogMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/2/27.
 * 用于注销用户
 */

public class ChangeUsers extends Activity {

    private TextView accountId, accountName, accountSex, accountpro, accounthobby, accounttel,
            accountlevel, accountpast, accountwpast, accountProperty,accountprebook;
    private Button deleteUser;
    private PersonMessage personMessage;
    private Toast mToast;
    private List<BmobObject> bookslist = new ArrayList<>();
    private List<BmobObject> deprelist = new ArrayList<>();
    private List<BmobObject> upprelist = new ArrayList<>();
    private Rule rule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_users);
        personMessage = (PersonMessage) getIntent().getSerializableExtra("user_id");
        rule = (Rule) getIntent().getSerializableExtra("rule");
        init();
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showDialog(ChangeUsers.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeUsers.this);
                dialog.setTitle("注销").setMessage("是否删除用户？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 用户注销时需同时将其借阅的书籍归还（若有人预约其书籍，需要及时转接预约者），以及将其预约的书籍取消预约
                         * 然后删除其过去借阅，最后删除个人信息
                         */
                        deleteUser.setEnabled(false);
                        deleteUser();
                        /**
                         * 删除该用户的过去借阅
                         */
                        deletePastBooks();
                        /**
                         * 改掉该用户的预约
                         */
                        changSub();
                        /**
                         * 更新书库
                         */
                        updateBooksShop();

                        /**
                         *更新当前借阅
                         */
                        updatePresentBS();
                    }
                })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }


        });
    }
    public void updatePresentBS() {
        BmobQuery<PresentBooks> presentBooksBmobQuery = new BmobQuery<PresentBooks>();
        presentBooksBmobQuery.addWhereEqualTo("userId", personMessage.getUserId());
        presentBooksBmobQuery.findObjects(new FindListener<PresentBooks>() {
            @Override
            public void done(List<PresentBooks> list, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null && list.size() > 0) {
                    BmobBatch batch = new BmobBatch();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setObjectId(list.get(i).getObjectId());
                        if (list.get(i).getIsSubscribe().equals("无")) {
                            deprelist.add(list.get(i));
                        } else {
                            String ss = list.get(i).getIsSubscribe().substring(0, list.get(i).getIsSubscribe().length() - 2);
                            final int userId = Integer.valueOf(ss);
                            list.get(i).setUserId(userId);
                            list.get(i).setIsSubscribe("无");
                            list.get(i).setIsContinue("无");
                            Date date1 = new Date();
                            Date date2 = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            final String borrowdate = sdf.format(date1);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date2);
                            calendar.add(calendar.DATE, rule.getFirstDay());//把日期往后增加60天整数往后推,负数往前移动
                            date2 = calendar.getTime();   //这个时间就是日期往后推60天的结果
                            final String backdate = sdf.format(date2);
                            list.get(i).setLentTime(borrowdate);
                            list.get(i).setBackTime(backdate);
                            upprelist.add(list.get(i));
                        }
                    }
                    if (deprelist.size() > 0)
                        batch.deleteBatch(deprelist);
                    if (upprelist.size() > 0)
                        batch.updateBatch(upprelist);
                    batch.doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {

                            } else {
                                DialogMessage.closeDialog();
                            }
                        }
                    });
                } else {

                }

            }
        });

    }

    public void deleteUser() {
        personMessage.delete(personMessage.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {
                    DialogMessage.closeDialog();
                }
            }
        });


    }


    public void deletePastBooks() {
        BmobQuery<PastBooks> pastBooksBmobQuery = new BmobQuery<PastBooks>();
        pastBooksBmobQuery.setLimit(100);
        pastBooksBmobQuery.addWhereEqualTo("userId", personMessage.getUserId());
        pastBooksBmobQuery.findObjects(new FindListener<PastBooks>() {
            @Override
            public void done(List<PastBooks> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
                    for (int i = 0; i < list.size(); i++) {
                        bmobObjectList.add(list.get(i));
                    }
                    new BmobBatch().deleteBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {
                            } else {
                                useToast("删除过去失败");
                                DialogMessage.closeDialog();
                            }
                        }
                    });
                } else {
                    //useToast("获取过去异常");
                    DialogMessage.closeDialog();
                }
            }
        });
    }


    public void changSub() {
        /**
         *将预约改掉
         */
        BmobQuery<Books> booksBmobQuery = new BmobQuery<Books>();
        booksBmobQuery.addWhereEqualTo("isSubscribe", "" + personMessage.getUserId() + "预约");
        booksBmobQuery.findObjects(new FindListener<Books>() {
            @Override
            public void done(List<Books> list, BmobException e) {
                if (e == null) {
                    List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIsSubscribe("无");
                        bmobObjectList.add(list.get(i));
                    }
                    new BmobBatch().updateBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {

                            } else {
                                useToast("改预约失败");
                                DialogMessage.closeDialog();
                            }
                        }
                    });
                } else {
                    useToast("获取当前预约失败");
                    DialogMessage.closeDialog();
                }
            }
        });
    }

    private void updateBooksShop() {
        BmobQuery<Books> booksBmobQuery = new BmobQuery<Books>();
        booksBmobQuery.addWhereEqualTo("isLent", "" + personMessage.getUserId() + "借出");
        booksBmobQuery.findObjects(new FindListener<Books>() {
            @Override
            public void done(List<Books> list, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setObjectId(list.get(i).getObjectId());
                        if (list.get(i).getIsSubscribe().equals("无")) {
                            list.get(i).setBackTime("");
                            list.get(i).setLentTime("");
                            list.get(i).setIsLent("可借");
                            list.get(i).setIsContinue("无");
                            bookslist.add(list.get(i));
                        } else {
                            String ss = list.get(i).getIsSubscribe().substring(0, list.get(i).getIsSubscribe().length() - 2);
                            final int userId = Integer.valueOf(ss);
                            list.get(i).setIsLent(userId + "借出");
                            list.get(i).setIsContinue("无");
                            list.get(i).setIsSubscribe("无");
                            Date date1 = new Date();
                            Date date2 = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            final String borrowdate = sdf.format(date1);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date2);
                            calendar.add(calendar.DATE, rule.getFirstDay());//把日期往后增加60天整数往后推,负数往前移动
                            date2 = calendar.getTime();   //这个时间就是日期往后推60天的结果
                            final String backdate = sdf.format(date2);
                            list.get(i).setLentTime(borrowdate);
                            list.get(i).setBackTime(backdate);
                            bookslist.add(list.get(i));

                        }
                    }
                    new BmobBatch().updateBatch(bookslist).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {

                            } else {
                                DialogMessage.closeDialog();
                            }
                        }
                    });


                }else {

                }
            }
        });


    }

    public void init() {
        accountName = (TextView) findViewById(R.id.userchname);
        accountSex = (TextView) findViewById(R.id.userchsex);
        accountId = (TextView) findViewById(R.id.userchId);
        accountpro = (TextView) findViewById(R.id.userchprofession);
        accounthobby = (TextView) findViewById(R.id.userchhobby);
        accounttel = (TextView) findViewById(R.id.userchtel);
        accountlevel = (TextView) findViewById(R.id.userchlevel);
        accountpast = (TextView) findViewById(R.id.userchlate);
        accountwpast = (TextView) findViewById(R.id.userchwpastbook);
        deleteUser = (Button) findViewById(R.id.user_delete);
        accountProperty = (TextView) findViewById(R.id.user_chproperty);
        accountprebook=(TextView)findViewById(R.id.userchprebook);

        accountName.setText("姓名：" + personMessage.getUserName().toString());
        accountSex.setText("性别：" + personMessage.getUserSex().toString());
        accountId.setText("账户：" + String.valueOf(personMessage.getUserId()));
        accounthobby.setText("书籍爱好：" + personMessage.getUserDescription().toString());
        accountpro.setText("专业：" + personMessage.getUserProfession().toString());
        accountlevel.setText("借阅等级：" + personMessage.getUserLevel().toString());
        accountpast.setText("逾期书本：" + personMessage.getPastBooks().toString());
        accountwpast.setText("即将到期：" + personMessage.getWpastBooks().toString());
        accounttel.setText("联系方式：" + personMessage.getUserTel().toString());
        accountprebook.setText("当前借阅："+personMessage.getNowBorrow().toString());
        accountProperty.setText("属性：" + personMessage.getIsRootManager().toString());


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

