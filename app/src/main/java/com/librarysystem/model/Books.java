package com.librarysystem.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by g on 2016/11/30.
 */

public class Books implements Parcelable {
    private int bookId;

    private String bookName;
    private String bookAuthor;
    private String isLent;
    private String userDescription;
    private String LentTime;
    private String backTime;
    private String version;
    private String isSubscribe;
    private String isContinue;



    public String getIsContinue() {
        return isContinue;
    }

    public void setIsContinue(String isContinue) {
        this.isContinue = isContinue;
    }


    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }



    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    private String press;
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }




    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getIsLent() {
        return isLent;
    }

    public void setIsLent(String lent) {
        isLent = lent;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getLentTime() {
        return LentTime;
    }

    public void setLentTime(String lentTime) {
        LentTime = lentTime;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
        dest.writeString(bookAuthor);
        dest.writeString(version);
        dest.writeString(press);
        dest.writeString(userDescription);
        dest.writeString(isLent);
        dest.writeString(LentTime);
        dest.writeString(backTime);
        dest.writeString(isContinue);
        dest.writeString(isSubscribe);

    }

    public static final Parcelable.Creator<Books> CREATOR = new Parcelable.Creator<Books>() {
        @Override
        public Books createFromParcel(Parcel source) {
            Books book = new Books();
            book.bookId = source.readInt();
            book.bookName = source.readString();
            book.bookAuthor = source.readString();
            book.version=source.readString();
            book.press=source.readString();
            book.userDescription=source.readString();
            book.isLent=source.readString();
            book.LentTime=source.readString();
            book.backTime=source.readString();
            book.isContinue=source.readString();
            book.isSubscribe=source.readString();
            return book;
        }

        @Override
        public Books[] newArray(int size) {
            return new Books[size];
        }
    };


}
