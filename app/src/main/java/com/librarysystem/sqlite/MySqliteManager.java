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
            "key ,user_name text,user_sex text,user_profession text,user_description text)";
    /*
    account建表语句
     */
    private static final String CREATE_ACCOUNT="";
    /*
   repertory建表语句
     */
    private static final String CREATE_REPERTORY="";
    /*
    percent建表语句
     */
    private static final String CREATE_PERCENT="";

    /*
    passed见表语句
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
