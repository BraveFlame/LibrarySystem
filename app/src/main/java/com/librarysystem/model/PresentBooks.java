package com.librarysystem.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by g on 2017/3/21.
 */

public class PresentBooks extends BmobObject {
    private Integer bookId;
    private String bookName;
    private String bookAuthor;
    private String press;
    private String version;
    private String userDescription;

    private String isLent;
    private String LentTime;
    private String backTime;

    private String isSubscribe;
    private String isContinue;


    private Integer userId;
    private String userName;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


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


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }




    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
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
}
