package com.librarysystem.model;

/**
 * Created by g on 2016/11/30.
 * 用户实体类
 */

public class PersonMessage {
    private int userId;
    private String userName;
    private String userSex;
    private String userProfession;
    private String userDescription;
    private String userPassword;
    private String userLevel;
    private String pastBooks;
    private String wpastBooks;
    private String userTel;
    private String isRootManager;

    public int getNowBorrow() {
        return nowBorrow;
    }

    public void setNowBorrow(int nowBorrow) {
        this.nowBorrow = nowBorrow;
    }

    private int nowBorrow;

    public String getIsRootManager() {
        return isRootManager;
    }

    public void setIsRootManager(String isRootManager) {
        this.isRootManager = isRootManager;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getPastBooks() {
        return pastBooks;
    }

    public void setPastBooks(String pastBooks) {
        this.pastBooks = pastBooks;
    }

    public String getWpastBooks() {
        return wpastBooks;
    }

    public void setWpastBooks(String wpastBooks) {
        this.wpastBooks = wpastBooks;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public String getUserProfession() {
        return userProfession;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


}
