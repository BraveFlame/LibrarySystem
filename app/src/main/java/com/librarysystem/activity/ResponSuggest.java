package com.librarysystem.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.librarysystem.R;
import com.librarysystem.model.USerSuggest;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by g on 2017/4/21.
 */

public class ResponSuggest extends Activity {
    private TextView detail, id, name, time, resTime,resCont;
    private Button respon;
    private SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respon_suggestion);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final USerSuggest uSerSuggest = (USerSuggest) getIntent().getSerializableExtra("detail");
        detail = (TextView) findViewById(R.id.suggest_detail);
        id = (TextView) findViewById(R.id.sug_det_id);
        name = (TextView) findViewById(R.id.sug_det_name);
        time = (TextView) findViewById(R.id.sug_det_time);
        resTime = (TextView) findViewById(R.id.sug_det_restime);
        resCont=(TextView)findViewById(R.id.sug_det_cont);
        respon = (Button) findViewById(R.id.respon_suggest);
        detail.setText(uSerSuggest.getDesc());
        id.setText("账号："+uSerSuggest.getUserId().toString());
        name.setText("姓名："+uSerSuggest.getUserName());
        time.setText("反馈时间："+uSerSuggest.getSuggestTime());
        resTime.setText("回复时间："+uSerSuggest.getResponTime());
        resCont.setText("回复内容："+uSerSuggest.getContent());
        if(pref.getString("root","Root").equals("管理员")) {
            if (uSerSuggest.getIsAnswer().equals("否")) {
                respon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uSerSuggest.setIsAnswer("是");
                        uSerSuggest.setContent("已收到您的反馈，我们将会合理地采用您的建议，感谢！");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String time = sdf.format(new Date());
                        uSerSuggest.setResponTime(time);
                        DialogMessage.showDialog(ResponSuggest.this);
                        uSerSuggest.update(uSerSuggest.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                DialogMessage.closeDialog();
                                if (e == null) {
                                    ToastMessage.useToast(ResponSuggest.this, "回复成功！");
                                    respon.setEnabled(false);
                                    resTime.setText("回复时间："+time);
                                    resCont.setText("回复内容：已收到您的反馈，我们将会合理地采用您的建议，感谢！");
                                } else {
                                    ToastMessage.useToast(ResponSuggest.this, "网络异常！");
                                }
                            }
                        });
                    }
                });

            } else {
                respon.setEnabled(false);
            }
        }else {
            respon.setVisibility(View.GONE);
            if(uSerSuggest.getIsAnswer().equals("是"))
            uSerSuggest.setIsSee("是");
            uSerSuggest.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e!=null)
                        ToastMessage.useToast(ResponSuggest.this, "网络异常！");
                }
            });
        }

    }
}
