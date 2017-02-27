package com.librarysystem.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.librarysystem.model.Books;
import com.librarysystem.model.PersonMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by g on 2016/12/2.
 */

public class LibraryDB {
    /*
    数据库名
     */
    public static final String Library_DB = "library_db";
    /*
    数据库版本
     */
    public static final int VERSION = 1;
    private static LibraryDB libraryDB;
    private SQLiteDatabase db;

    /*
    构造方法私有化
     */
    private LibraryDB(Context context) {
        MySqliteManager dbHelper = new MySqliteManager(context, Library_DB, null, VERSION);
        db = dbHelper.getWritableDatabase();

    }

    /*
    获取LibraryDB实例
     */
    public synchronized static LibraryDB getInstance(Context context) {
        if (libraryDB == null) {
            libraryDB = new LibraryDB(context);
        }
        return libraryDB;
    }

    /*
    将PersonalMessage存入数据库
     */
    public boolean savePersonalMeassage(PersonMessage personMessage) {
        int id = -1;
        Cursor cursor = db.rawQuery("select * from PersonalMessages where user_id=" + personMessage.getUserId(), null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("user_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (id == personMessage.getUserId())
            return false;
        else {
            if (personMessage != null) {
                ContentValues values = new ContentValues();
                values.put("user_id", personMessage.getUserId());
                values.put("user_password", personMessage.getUserPassword());
                values.put("user_name", personMessage.getUserName());
                values.put("user_sex", personMessage.getUserSex());
                values.put("user_profession", personMessage.getUserProfession());
                values.put("user_description", personMessage.getUserDescription());
                values.put("user_tel", personMessage.getUserTel());
                values.put("user_level", personMessage.getUserLevel());
                values.put("user_pastbooks", personMessage.getPastBooks());
                values.put("user_wpastbooks", personMessage.getWpastBooks());
                values.put("rootmanager", personMessage.getIsRootManager());
                db.insert("PersonalMessages", null, values);

            }
            return true;
        }
    }

    /*
    从数据库修改个人资料
     */
    public void alterPersonalMessage(PersonMessage personMessage, int userId) {
        ContentValues values = new ContentValues();
        values.put("user_name", personMessage.getUserName().toString());
        values.put("user_password", personMessage.getUserPassword().toString());
        values.put("user_sex", personMessage.getUserSex().toString());
        values.put("user_profession", personMessage.getUserProfession().toString());
        values.put("user_description", personMessage.getUserDescription().toString());
        values.put("user_pastbooks", personMessage.getPastBooks().toString());
        values.put("user_wpastbooks", personMessage.getWpastBooks().toString());
        values.put("rootmanager", personMessage.getIsRootManager().toString());
        values.put("user_level", personMessage.getUserLevel().toString());
        db.update("PersonalMessages", values, "user_id=?", new String[]{"" + userId});
    }

    /*
    搜索符合条件的联系人
     */
    public List<PersonMessage> getUsers(String input, List<PersonMessage> usersList) {
        usersList.clear();
        try {
            int userId=Integer.valueOf(input);
            Cursor cursor = db.rawQuery("select * from PersonalMessages where user_id!=12345 and user_id like '%" + userId + "%'" , null);

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
                    personMessage.setUserLevel(cursor.getString(cursor.getColumnIndex("user_level")));
                    personMessage.setPastBooks(cursor.getString(cursor.getColumnIndex("user_pastbooks")));
                    personMessage.setWpastBooks(cursor.getString(cursor.getColumnIndex("user_wpastbooks")));
                    personMessage.setIsRootManager(cursor.getString(cursor.getColumnIndex("rootmanager")));
                    usersList.add(personMessage);

                } while (cursor.moveToNext());
            }
            cursor.close();
            return usersList;
        } catch (Exception e) {
           try {
               usersList.clear();
               Cursor cursor = db.rawQuery("select * from PersonalMessages where user_id!=12345 and user_name like '%" + input + "%' COLLATE NOCASE", null);

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
                       personMessage.setUserLevel(cursor.getString(cursor.getColumnIndex("user_level")));
                       personMessage.setPastBooks(cursor.getString(cursor.getColumnIndex("user_pastbooks")));
                       personMessage.setWpastBooks(cursor.getString(cursor.getColumnIndex("user_wpastbooks")));
                       personMessage.setIsRootManager(cursor.getString(cursor.getColumnIndex("rootmanager")));
                       usersList.add(personMessage);

                   } while (cursor.moveToNext());
               }
               cursor.close();
           }catch (Exception a){


           }
            return usersList;
        }

    }

    /*
    删除用户
     */
    public boolean deleteUsers(int id) {
        try {
            db.execSQL("delete from PersonalMessages where user_id=?", new String[]{"" + id});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    从数据库读取个人PersonalMeassage
     */
    public void getPersonalMeassage(PersonMessage personMessage, int userId) {

        if (personMessage != null) {

            Cursor cursor = db.rawQuery("select * from PersonalMessages where user_id=" + userId, null);

            if (cursor.moveToFirst()) {
                do {
                    personMessage.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                    personMessage.setUserPassword(cursor.getString(cursor.getColumnIndex("user_password")));
                    personMessage.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                    personMessage.setUserSex(cursor.getString(cursor.getColumnIndex("user_sex")));
                    personMessage.setUserProfession(cursor.getString(cursor.getColumnIndex("user_profession")));
                    personMessage.setUserDescription(cursor.getString(cursor.getColumnIndex("user_description")));
                    personMessage.setUserTel(cursor.getString(cursor.getColumnIndex("user_tel")));
                    personMessage.setUserLevel(cursor.getString(cursor.getColumnIndex("user_level")));
                    personMessage.setPastBooks(cursor.getString(cursor.getColumnIndex("user_pastbooks")));
                    personMessage.setWpastBooks(cursor.getString(cursor.getColumnIndex("user_wpastbooks")));
                    personMessage.setIsRootManager(cursor.getString(cursor.getColumnIndex("rootmanager")));


                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

    /*
      用户搜索从数据库读取书本的信息
      */
    public List<Books> getBookMeassage(String bookName, List<Books> booksList) {
        booksList.clear();
        Cursor cursor = db.rawQuery("select * from BookRepertory where book_name like '%" + bookName + "%'  COLLATE NOCASE", null);
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
                booksList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return booksList;
    }

    /*
    当前借阅
     */
    public List<Books> getPresentBooks(int readerId, List<Books> booksList) {
        booksList.clear();
        Cursor cursor = db.rawQuery("select * from PresentBooks where reader_id=" + readerId, null);
        if (cursor.moveToFirst()) {
            do {
                Books book = new Books();
                book.setBookId((cursor.getInt(cursor.getColumnIndex("book_id"))));
                book.setBookName(cursor.getString(cursor.getColumnIndex("book_name")));
                book.setBookAuthor(cursor.getString(cursor.getColumnIndex("book_author")));
                book.setUserDescription(cursor.getString(cursor.getColumnIndex("book_description")));
                book.setLentTime(cursor.getString(cursor.getColumnIndex("borrow_start")));
                book.setBackTime(cursor.getString(cursor.getColumnIndex("back_time")));
                booksList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return booksList;
    }

    /*
    过去借阅
     */
    public List<Books> getPastBooks(int readerId, List<Books> booksList) {
        booksList.clear();
        Cursor cursor = db.rawQuery("select * from PastBooks where reader_id=" + readerId, null);
        if (cursor.moveToFirst()) {
            do {
                Books book = new Books();
                book.setBookId((cursor.getInt(cursor.getColumnIndex("book_id"))));
                book.setBookName(cursor.getString(cursor.getColumnIndex("book_name")));
                book.setBookAuthor(cursor.getString(cursor.getColumnIndex("book_author")));
                book.setUserDescription(cursor.getString(cursor.getColumnIndex("book_description")));
                book.setLentTime(cursor.getString(cursor.getColumnIndex("borrow_start")));
                book.setBackTime(cursor.getString(cursor.getColumnIndex("back_time")));
                booksList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return booksList;
    }

    /*
    管理员保存图书
     */
    public boolean saveBookMeassage(Books books) {
        int id = -1;
        Cursor cursor = db.rawQuery("select * from BookRepertory where book_id=" + books.getBookId(), null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("book_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //如果原来存在则不保存
        if (id == books.getBookId())
            return false;
        else {
            if (books != null) {
                ContentValues values = new ContentValues();
                values.put("book_id", books.getBookId());
                values.put("book_name", books.getBookName());
                values.put("book_author", books.getBookAuthor());
                values.put("book_description", books.getUserDescription());
                values.put("book_status", "可借");
                db.insert("BookRepertory", null, values);

            }
            return true;
        }
    }
/*
管理员删除图书
 */

    public boolean deleteBooks(int bookId) {
        try {
            db.execSQL("delete from BookRepertory where book_id=?", new String[]{"" + bookId});
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    /*
    管理员修改图书
     */

    public void alterBooks(Books book, int bookId) {
        ContentValues values = new ContentValues();
        values.put("book_id", Integer.valueOf(book.getBookId()));
        values.put("book_name", book.getBookName().toString());
        values.put("book_author", book.getBookAuthor().toString());
        values.put("book_version", book.getVersion().toString());
        values.put("book_press", book.getPress().toString());
        values.put("book_description", book.getUserDescription().toString());
        db.update("BookRepertory", values, "book_id=?", new String[]{"" + bookId});
    }


    /*
    借书
     */
    public boolean borrowBook(int readerId, Books book) {
        if (book != null) {
            try {
                PersonMessage personMessage = new PersonMessage();
                libraryDB.getPersonalMeassage(personMessage, readerId);
                int i = Integer.valueOf(personMessage.getPastBooks());
                if (i > 0) {

                    return false;
                }
            } catch (Exception e) {

            }
            ContentValues values = new ContentValues();
            values.put("book_id", book.getBookId());
            values.put("reader_id", readerId);
            values.put("book_name", book.getBookName());
            values.put("book_author", book.getBookAuthor());
            values.put("book_description", book.getUserDescription());

            Date date1 = new Date();
            Date date2 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String borrowdate = sdf.format(date1);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date2);
            calendar.add(calendar.DATE, 1);//把日期往后增加60天整数往后推,负数往前移动
            date2 = calendar.getTime();   //这个时间就是日期往后推60天的结果
            String backdate = sdf.format(date2);
            values.put("borrow_start", borrowdate);
            values.put("back_time", backdate);
            db.insert("PresentBooks", null, values);
            ContentValues value = new ContentValues();

            value.put("book_status", "借出");
            value.put("back_time", backdate);
            db.update("BookRepertory", value, "book_id=?", new String[]{"" + book.getBookId()});
            book.setBackTime(backdate);
            return true;
        } else return false;
    }

    /*
还书
 */
    public boolean backBook(int readerId, Books book) {
        if (book != null) {
            db.execSQL("delete from PresentBooks where book_id=?", new String[]{"" + book.getBookId()});
            ContentValues values = new ContentValues();
            values.put("book_id", book.getBookId());
            values.put("reader_id", readerId);
            values.put("book_name", book.getBookName());
            values.put("book_author", book.getBookAuthor());
            values.put("book_description", book.getUserDescription());
            values.put("borrow_start", book.getLentTime());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String backdate = sdf.format(date);
            values.put("back_time", backdate);
            db.insert("PastBooks", null, values);
            ContentValues value = new ContentValues();
            value.put("book_status", "可借");
            value.put("back_time", " ");
            db.update("BookRepertory", value, "book_id=?", new String[]{"" + book.getBookId()});
            return true;
        } else return false;
    }
}
