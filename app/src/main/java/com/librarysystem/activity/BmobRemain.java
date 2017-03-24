package com.librarysystem.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.librarysystem.R;

/**
 * Created by g on 2017/3/24.
 */

public class BmobRemain extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmob_remain);
        textView=(TextView)findViewById(R.id.bmob_remain);
        textView.setText(getIntent().getStringExtra("remain"));
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(9);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
