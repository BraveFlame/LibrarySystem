package com.librarysystem.model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.librarysystem.R;

import java.util.List;

/**
 * Created by g on 2017/4/21.
 */

public class SuggestionAdapter extends ArrayAdapter<USerSuggest> {
    private int resourceId;
    public SuggestionAdapter(Context context, int textViewResourceId, List<USerSuggest> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        USerSuggest uSerSuggest=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView userId = (TextView) view.findViewById(R.id.suggest_id_item);
        TextView userName = (TextView) view.findViewById(R.id.suggest_name_item);
        TextView userDesc = (TextView) view.findViewById(R.id.suggest_desc_item);
        TextView isAnswer=(TextView)view.findViewById(R.id.suggest_answer_item);
        userId.setText("账号："+uSerSuggest.getUserId().toString());
        userName.setText("姓名："+uSerSuggest.getUserName());
        userDesc.setText("详细信息："+uSerSuggest.getDesc());
        isAnswer.setText("是否答复："+uSerSuggest.getIsAnswer());
        if(uSerSuggest.getIsSee().equals("否")||uSerSuggest.getIsAnswer().equals("否"))
        view.setBackgroundColor(Color.parseColor("#f5dd96"));
        return view;
    }
}
