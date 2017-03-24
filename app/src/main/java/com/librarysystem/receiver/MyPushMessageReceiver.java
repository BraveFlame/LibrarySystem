package com.librarysystem.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.librarysystem.R;
import com.librarysystem.activity.BmobRemain;

import org.json.JSONObject;

import cn.bmob.push.PushConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by g on 2017/3/24.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("msg"));
                String remain=jsonObject.getString("alert");
                Intent intents=new Intent(context, BmobRemain.class);
                intents.putExtra("remain",remain);
                PendingIntent pi=PendingIntent.getActivity(context,0,intents,PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder notification = new Notification.Builder(context).setTicker("收到推送").setContentTitle("推送")
                        .setContentText("客户端收到推送内容："+remain)
                        .setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.bmob)
                        .setContentIntent(pi);
                notification.setDefaults(Notification.DEFAULT_ALL);
                manager.notify(9, notification.build());
            }catch (Exception e){
                Toast.makeText(context,intent.getStringExtra("msg"),Toast.LENGTH_LONG).show();
            }
        }
    }

}