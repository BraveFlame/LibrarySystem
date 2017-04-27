package com.librarysystem.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.librarysystem.R;
import com.librarysystem.model.SuggestionAdapter;
import com.librarysystem.model.USerSuggest;
import com.librarysystem.others.DialogMessage;
import com.librarysystem.others.ToastMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by g on 2017/4/21.
 */

public class ShowSuggestion extends Activity {
    private ListView suggest_list;
    private EditText suggest_text;
    private Button suggest_button;
    private SharedPreferences pref;
    private List<USerSuggest> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_suggestion);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        suggest_button = (Button) findViewById(R.id.suggest_button);
        suggest_text = (EditText) findViewById(R.id.suggest_text);
        suggest_list = (ListView) findViewById(R.id.suggest_list);

        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(19);

        searchSuggestion();

        suggest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = suggest_text.getText().toString();
                USerSuggest uSerSuggest = new USerSuggest();
                uSerSuggest.setUserId(pref.getInt("userId", 0));
                uSerSuggest.setUserName(pref.getString("userName", "N"));
                uSerSuggest.setDesc(text);
                uSerSuggest.setIsAnswer("否");
                uSerSuggest.setIsSee("否");
                uSerSuggest.setContent("无");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                uSerSuggest.setSuggestTime(time);
                uSerSuggest.setResponTime("");
                DialogMessage.showDialog(ShowSuggestion.this);
                uSerSuggest.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        DialogMessage.closeDialog();
                        if (e == null) {
                            ToastMessage.useToast(ShowSuggestion.this, "感谢您提出宝贵建议！");
                            suggest_text.setText("");
                            searchSuggestion();
                        } else {
                            ToastMessage.useToast(ShowSuggestion.this, "网络异常！");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchSuggestion();
    }

    public void searchSuggestion() {
        BmobQuery<USerSuggest> bmobQuery = new BmobQuery<>();
        if (!pref.getString("root", "Root").equals("管理员")) {
            bmobQuery.addWhereEqualTo("userId", pref.getInt("userId", 0));
        }
        DialogMessage.showDialog(ShowSuggestion.this);
        bmobQuery.findObjects(new FindListener<USerSuggest>() {
            @Override
            public void done(List<USerSuggest> list, BmobException e) {
                DialogMessage.closeDialog();
                if (e == null) {
                    if (list.size() <= 0) {
                        ToastMessage.useToast(ShowSuggestion.this, "当前没有建议！");
                    } else {
                        suggestList = list;
                    }
                    SuggestionAdapter suggestionAdapter = new SuggestionAdapter(ShowSuggestion.this, R.layout
                            .suggest_item, suggestList);
                    suggest_list.setAdapter(suggestionAdapter);
                } else {
                    ToastMessage.useToast(ShowSuggestion.this, "网络错误！！");
                }
            }
        });

        final Intent suggestIntent = new Intent(this, ResponSuggest.class);
        suggest_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                USerSuggest uSerSuggest = suggestList.get(position);
                suggestIntent.putExtra("detail", uSerSuggest);
                startActivity(suggestIntent);
            }
        });
    }

}
