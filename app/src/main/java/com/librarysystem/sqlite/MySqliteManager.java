package com.librarysystem.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by g on 2016/11/21.
 */
public class MySqliteManager extends SQLiteOpenHelper {
    /*
    PersonalMessage建表语句
     */
    private static final String CREATE_PERSONALMES="create table PersonalMessages(user_id integer primary" +
            "key ,user_password text,user_name text,user_sex text,user_profession text,user_description text)";
    /*
    account建表语句
     */
   // private static final String CREATE_ACCOUNT="user_id integer primary key,user_password text";
    /*
   repertory建表语句
     */
    private static final String CREATE_REPERTORY="create table BookRepertory(book_id integer primary" +
            "key ,book_name text,user_name text,user_sex text,user_profession text,user_description text)";
    /*
    percent建表语句
     */
    private static final String CREATE_PERCENT="";

    /*
    passed建表语句
     */
    private static final String CREATE_PASSED="";

    public MySqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSONALMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
