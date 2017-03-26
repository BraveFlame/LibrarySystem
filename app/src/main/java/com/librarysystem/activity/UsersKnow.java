package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.librarysystem.R;

/**
 * Created by g on 2017/3/26.
 */

public class UsersKnow extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_know);
        ImageView imageView=(ImageView)findViewById(R.id.must_know_bg);
        imageView.setAlpha(0.65f);
    }
}
