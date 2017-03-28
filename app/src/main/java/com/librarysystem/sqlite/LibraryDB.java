package com.librarysystem.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;

import java.util.List;

/**
 * Created by g on 2016/12/2.
 * 用于数据库各种操作
 */

public class LibraryDB {
    /**
     * 数据库名
     */
    public static final String Library_DB = "library_db";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static LibraryDB libraryDB;
    private SQLiteDatabase db;


    /**
     * 构造方法私有化
     */
    private LibraryDB(Context context) {
        MySqliteManager dbHelper = new MySqliteManager(context, Library_DB, null, VERSION);
        db = dbHelper.getWritableDatabase();

    }

    /**
     * 获取LibraryDB实例
     */
    public synchronized static LibraryDB getInstance(Context context) {
        if (libraryDB == null) {
            libraryDB = new LibraryDB(context);
        }
        return libraryDB;
    }


    /**
     * 将PersonalMessage存入数据库
     */
    public void savePersonalMeassage(List<PersonMessage> ps) {
        /**
         * 清空用户
         *
         * @return
         */
        db.execSQL("delete from PersonalMessages");
            for (PersonMessage personMessage : ps) {
                ContentValues values = new ContentValues();
                values.put("user_id", personMessage.getUserId());
                values.put("user_password", personMessage.getUserPassword());
                values.put("user_object",personMessage.getObjectId());
                values.put("user_name", personMessage.getUserName());
                values.put("user_sex", personMessage.getUserSex());
                values.put("user_profession", personMessage.getUserProfession());
                values.put("user_description", personMessage.getUserDescription());
                values.put("user_tel", personMessage.getUserTel());
                values.put("now_borrow", personMessage.getNowBorrow());
                values.put("user_level", personMessage.getUserLevel());
                values.put("user_pastbooks", personMessage.getPastBooks());
                values.put("user_wpastbooks", personMessage.getWpastBooks());
                values.put("rootmanager", personMessage.getIsRootManager());
                db.insert("PersonalMessages", null, values);
            }
    }


    /**
     * 搜索符合条件的联系人
     */
    public List<PersonMessage> getUsers(String input, List<PersonMessage> usersList) {
        usersList.clear();
        try {
            int userId = Integer.valueOf(input);
            Cursor cursor = db.rawQuery("select * from PersonalMessages where user_id like '%" + userId + "%' order by user_id", null);
            if (cursor.moveToFirst()) {
                do {
                    PersonMessage personMessage = new PersonMessage();
                    personMessage.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                    personMessage.setUserPassword(cursor.getString(cursor.getColumnIndex("user_password")));
                    personMessage.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                    personMessage.setUserSex(cursor.getString(cursor.getColumnIndex("user_sex")));
                    personMessage.setUserProfession(cursor.getString(cursor.getColumnIndex("user_profession")));
                    personMessage.setUserDescription(cursor.getString(cursor.getColumnIndex("user_description")));
                    personMessage.setUserTel(cursor.getString(cursor.getColumnIndex("user_tel")));
                    personMessage.setObjectId(cursor.getString(cursor.getColumnIndex("user_object")));
                    personMessage.setNowBorrow(cursor.getInt(cursor.getColumnIndex("now_borrow")));
                    personMessage.setUserLevel(cursor.getInt(cursor.getColumnIndex("user_level")));
                    personMessage.setPastBooks(cursor.getInt(cursor.getColumnIndex("user_pastbooks")));
                    personMessage.setWpastBooks(cursor.getInt(cursor.getColumnIndex("user_wpastbooks")));
                    personMessage.setIsRootManager(cursor.getString(cursor.getColumnIndex("rootmanager")));


                    usersList.add(personMessage);
                } while (cursor.moveToNext());
            }
            if (usersList.size() == 0) {
                int i = Integer.valueOf("sdsdf");
            }
            cursor.close();
            return usersList;
        } catch (Exception e) {
            try {
                usersList.clear();
                Cursor cursor = db.rawQuery("select * from PersonalMessages where user_name like '%" + input + "%' COLLATE NOCASE order by user_id", null);
                if (cursor.moveToFirst()) {
                    do {
                        PersonMessage personMessage = new PersonMessage();

                        personMessage.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                        personMessage.setObjectId(cursor.getString(cursor.getColumnIndex("user_object")));
                        personMessage.setUserPassword(cursor.getString(cursor.getColumnIndex("user_password")));
                        personMessage.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                        personMessage.setUserSex(cursor.getString(cursor.getColumnIndex("user_sex")));
                        personMessage.setUserProfession(cursor.getString(cursor.getColumnIndex("user_profession")));
                        personMessage.setUserDescription(cursor.getString(cursor.getColumnIndex("user_description")));
                        personMessage.setUserTel(cursor.getString(cursor.getColumnIndex("user_tel")));

                        personMessage.setUserLevel(cursor.getInt(cursor.getColumnIndex("user_level")));
                        personMessage.setPastBooks(cursor.getInt(cursor.getColumnIndex("user_pastbooks")));
                        personMessage.setWpastBooks(cursor.getInt(cursor.getColumnIndex("user_wpastbooks")));
                        personMessage.setNowBorrow(cursor.getInt(cursor.getColumnIndex("now_borrow")));
                        personMessage.setIsRootManager(cursor.getString(cursor.getColumnIndex("rootmanager")));


                        usersList.add(personMessage);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return usersList;
            } catch (Exception a) {
                return usersList;
            }

        }
    }


    /**
     * 用户搜索从数据库读取书本的信息
     */
    public List<Books> getBookMeassage(String bookName, List<Books> booksList) {
        booksList.clear();
        Cursor cursor = db.rawQuery("select * from BookRepertory where book_name like '%" + bookName + "%'  COLLATE NOCASE order by book_id", null);
        if (cursor.moveToFirst()) {
            do {
                Books book = new Books();
                book.setBookId((cursor.getInt(cursor.getColumnIndex("book_id"))));
                book.setBookName(cursor.getString(cursor.getColumnIndex("book_name")));
                book.setBookAuthor(cursor.getString(cursor.getColumnIndex("book_author")));
                book.setVersion(cursor.getString(cursor.getColumnIndex("book_version")));
                book.setPress(cursor.getString(cursor.getColumnIndex("book_press")));
                book.setUserDescription(cursor.getString(cursor.getColumnIndex("book_description")));
                book.setIsLent(cursor.getString(cursor.getColumnIndex("book_status")));
                book.setBackTime(cursor.getString(cursor.getColumnIndex("back_time")));
                book.setLentTime(cursor.getString(cursor.getColumnIndex("lent_time")));
                book.setIsContinue(cursor.getString(cursor.getColumnIndex("book_continue")));
                book.setIsSubscribe(cursor.getString(cursor.getColumnIndex("book_subscribe")));
                book.setObjectId(cursor.getString(cursor.getColumnIndex("book_object")));
                booksList.add(book);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return booksList;
    }


    /**
     * 管理员保存图书
     */
    public void saveBookMeassage(List<Books> book) {
        /**
         * 管理员清空图书
         */
        db.execSQL("delete from BookRepertory");
            for (Books books : book) {
                ContentValues values = new ContentValues();
                values.put("book_id", books.getBookId());
                values.put("book_name", books.getBookName());
                values.put("book_author", books.getBookAuthor());
                values.put("book_version", books.getVersion());
                values.put("book_press", books.getPress());
                values.put("book_description", books.getUserDescription());

                values.put("book_object", books.getObjectId());
                values.put("book_subscribe", books.getIsSubscribe());
                values.put("book_continue", books.getIsContinue());
                values.put("book_status", books.getIsLent());
                values.put("lent_time", books.getLentTime());
                values.put("back_time", books.getBackTime());
                db.insert("BookRepertory", null, values);
            }

    }


}
