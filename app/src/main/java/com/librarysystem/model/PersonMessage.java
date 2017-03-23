package com.librarysystem.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by g on 2016/11/30.
 * 用户实体类
 */

public class PersonMessage extends BmobObject{
    private Integer userId;
    private String userName;
    private String userSex;
    private String userProfession;
    private String userDescription;
    private String userPassword;
    private Integer userLevel;
    private Integer pastBooks;
    private Integer wpastBooks;
    private String userTel;
    private String isRootManager;
    private Integer nowBorrow;


    public Integer getNowBorrow() {
        return nowBorrow;
    }

    public void setNowBorrow(Integer nowBorrow) {
        this.nowBorrow = nowBorrow;
    }



    public String getIsRootManager() {
        return isRootManager;
    }

    public void setIsRootManager(String isRootManager) {
        this.isRootManager = isRootManager;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getPastBooks() {
        return pastBooks;
    }

    public void setPastBooks(Integer pastBooks) {
        this.pastBooks = pastBooks;
    }

    public Integer getWpastBooks() {
        return wpastBooks;
    }

    public void setWpastBooks(Integer wpastBooks) {
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
