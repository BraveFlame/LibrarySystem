package com.librarysystem.model;
import cn.bmob.v3.BmobObject;

/**
 * Created by g on 2017/3/21.
 */

public class Rule extends BmobObject {
    private Integer maxBooks;
    private Integer firstDay;
    private Integer secondDay;
    public Integer getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(Integer maxBooks) {
        this.maxBooks = maxBooks;
    }

    public Integer getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Integer firstDay) {
        this.firstDay = firstDay;
    }

    public Integer getSecondDay() {
        return secondDay;
    }

    public void setSecondDay(Integer secondDay) {
        this.secondDay = secondDay;
    }




}
