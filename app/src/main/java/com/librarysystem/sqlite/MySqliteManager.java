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
            "key ,user_password text,user_name text,user_sex text,user_profession text,user_description text," +
            "user_tel text,user_level text,user_pastbooks text,user_wpastbooks text)";
    /*
    account建表语句
     */
   // private static final String CREATE_ACCOUNT="user_id integer primary key,user_password text";
    /*
   repertory建表语句
     */
    private static final String CREATE_REPERTORY="create table BookRepertory(book_id integer primary" +
            "key ,book_name text,book_author text,book_version text,book_press,book_status text,back_time text,book_description text)";
    /*
    present建表语句
     */
    private static final String CREATE_PRESENT="create table PresentBooks(book_id integer primary" +
            "key ,reader_id integer ,book_name text,book_author text,book_version text,book_press,book_description text,borrow_start text,back_time text)";

    /*
    passed建表语句
     */
    private static final String CREATE_PAST="create table PastBooks(book_id integer primary" +
            "key ,reader_id integer ,book_name text,book_author text,book_version text,book_press,book_description text,borrow_start text,back_time text)";

private static final String CREATE_BOOKS="insert into BookRepertory(book_id,book_name,book_author," +
        "book_version,book_press,book_status,book_description)values(?,?,?,?,?,?,?)";





    public MySqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSONALMES);
        db.execSQL(CREATE_REPERTORY);
        db.execSQL(CREATE_PRESENT);
        db.execSQL(CREATE_PAST);
        db.execSQL(CREATE_BOOKS,new String[]{"1001","西游记","吴承恩","第三版","清华大学出版社","可借","明清四大名著之一的神话小说"});
        db.execSQL(CREATE_BOOKS,new String[]{"1002","西游记","吴承恩","第2版","北京大学出版社","可借","明清四大名著之一的神话小说"});
        db.execSQL(CREATE_BOOKS,new String[]{"1004","三国演义","罗贯中","第1版","清华大学出版社","可借","明清四大名著之一，魏蜀吴三国鼎立"});
        db.execSQL(CREATE_BOOKS,new String[]{"1005","聊斋志异","蒲松龄","第5版","清华大学出版社","可借","志怪狐鬼，古代短篇小说巅峰之作"});
        db.execSQL(CREATE_BOOKS,new String[]{"1005","java基础教程","老蒋","第7版","北京大学出版社","可借","java初学者入门首先"});
        db.execSQL(CREATE_BOOKS,new String[]{"1006","赵孟頫行书教程","罗培源","第1版","长江出版社","可借","中国书法培训教程"});


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
