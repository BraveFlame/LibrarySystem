package com.librarysystem.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by g on 2017/4/21.
 */

public class USerSuggest extends BmobObject {
    private Integer userId;
    private String userName;
    private String Desc;
    private String isAnswer;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getIsSee() {
        return isSee;
    }

    public void setIsSee(String isSee) {
        this.isSee = isSee;
    }

    private String isSee;
    public String getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(String suggestTime) {
        this.suggestTime = suggestTime;
    }

    public String getResponTime() {
        return responTime;
    }

    public void setResponTime(String responTime) {
        this.responTime = responTime;
    }

    private String suggestTime;
    private String responTime;

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

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }


}
