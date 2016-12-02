package com.librarysystem.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.librarysystem.model.PersonMessage;

/**
 * Created by g on 2016/12/2.
 */

public class LibraryDB {
    /*
    数据库名
     */
    public static final String Library_DB="library_db";
    /*
    数据库版本
     */
    public static final int VERSION=1;
    private static LibraryDB libraryDB;
    private SQLiteDatabase db;
    /*
    构造方法私有化
     */
    private LibraryDB(Context context){
        MySqliteManager dbHelper=new MySqliteManager(context,Library_DB,null,VERSION);
        db=dbHelper.getWritableDatabase();

    }
    /*
    获取LibraryDB实例
     */
    public synchronized static LibraryDB getInstance(Context context){
        if(libraryDB==null) {
            libraryDB=new LibraryDB(context);
        }
        return libraryDB;
    }
    /*
    将PersonalMessage存入数据库
     */
    public void savePersonalMeassage(PersonMessage personMessage){
        if(personMessage!=null){
            ContentValues values=new ContentValues();
            values.put("user_id",personMessage.getUserId());
            values.put("user_name",personMessage.getUserName());
            values.put("user_sex",personMessage.getUserSex());
            values.put("user_profession",personMessage.getUserProfession());
            values.put("user_description",personMessage.getUserDescription());
            db.insert("PersonalMessages",null,values);
        }
    }
    /*
    从数据库修改个人资料
     */
    public void alterPersonalMessage(PersonMessage personMessage,int userId){
        ContentValues values=new ContentValues();
        values.put("user_name",personMessage.getUserName().toString());
        values.put("user_sex",personMessage.getUserSex().toString());
        values.put("user_profession",personMessage.getUserProfession().toString());
        values.put("user_description",personMessage.getUserDescription().toString());
        db.update("PersonalMessages",values,"user_id=?",new String[]{""+userId});
    }
    /*
    从数据库读取PersonalMeassage
     */
    public void getPersonalMeassage(PersonMessage personMessage,int userId){

        if(personMessage!=null){

            Cursor cursor=db.rawQuery("select * from PersonalMessages where user_id="+userId,null);

            if(cursor.moveToFirst()){
                do{
                    personMessage.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                    personMessage.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                    personMessage.setUserSex(cursor.getString(cursor.getColumnIndex("user_sex")));
                    personMessage.setUserProfession(cursor.getString(cursor.getColumnIndex("user_profession")));
                    personMessage.setUserDescription(cursor.getString(cursor.getColumnIndex("user_description")));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

    }


}
