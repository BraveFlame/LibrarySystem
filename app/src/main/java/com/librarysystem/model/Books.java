package com.librarysystem.model;

import java.sql.Date;

/**
 * Created by g on 2016/11/30.
 */

public class Books {
private int bookId;

    private String bookName;
    private String bookAuthor;
    private boolean isLent;
    private String userDescription;
    private Date LentTime;
    private Date backTime;
    private int mount,rest;

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

    public boolean isLent() {
        return isLent;
    }

    public void setLent(boolean lent) {
        isLent = lent;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Date getLentTime() {
        return LentTime;
    }

    public void setLentTime(Date lentTime) {
        LentTime = lentTime;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }
}
