package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.librarysystem.R;

/**
 * Created by g on 2016/10/16.
 */

public class MainPage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
    }
}
