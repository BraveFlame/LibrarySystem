package com.librarysystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.librarysystem.R;

/**
 * Created by g on 2016/10/16.
 */

public class MainPage extends Activity implements View.OnClickListener{
    private Button selectButton;
    private final int LISTSELECT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page);
        selectButton=(Button)findViewById(R.id.select_button);
        selectButton.setOnClickListener(this);
        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(LISTSELECT);
            }
        };
        selectButton.setOnClickListener(listener);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;

        switch (id){
            case LISTSELECT:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("选择");
                final  Intent intent4=new Intent(this,Login.class);
                final  Intent intent0=new Intent(this,PersonalSet.class);
                DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                String data="personSet";
                                intent0.putExtra("activity",data);
                                startActivity(intent0);
                                break;
                            case 4:
                                startActivity(intent4);
                                break;
                            default:
                                break;
                        }
                    }
                };
                builder.setItems(R.array.arrays,listener);
                dialog=builder.create();
                break;
        }

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 1:
                break;
            default:break;
        }
    }

}
