package com.librarysystem.model;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g on 2017/3/2.
 * 管理活动，用以销毁
 */

public class ActivityCollector {
    public static List<Activity>activities=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void  removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        if(activities!=null)
        for (Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
