package com.librarysystem.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.librarysystem.R;

import java.util.List;

/**
 * Created by g on 2017/2/27.
 * 用户适配器
 */

public class UserAdapter extends ArrayAdapter<PersonMessage> {
private int resourceId;

    public UserAdapter(Context context, int textViewResourceId, List<PersonMessage> objects) {
        super(context,  textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PersonMessage personMessage=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        /**
         * 过期图书的用户标识为粉红色
         */
        if(personMessage.getPastBooks()!=0)
            view.setBackgroundResource(R.drawable.selector);
        TextView userName=(TextView) view.findViewById(R.id.user_item_name);
        TextView userId=(TextView) view.findViewById(R.id.user_item_id);
        TextView userProprety=(TextView) view.findViewById(R.id.user_item_property);

        userId.setText(""+String.valueOf(personMessage.getUserId()));
        userName.setText(personMessage.getUserName());
        userProprety.setText(personMessage.getIsRootManager());
        return view;
    }

}
