package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;

import com.librarysystem.R;

/**
 * Created by g on 2017/2/21.
 */

public class ManagerRoot extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_root);
    }
}
