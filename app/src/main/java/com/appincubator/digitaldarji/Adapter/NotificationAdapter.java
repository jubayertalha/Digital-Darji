package com.appincubator.digitaldarji.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.appincubator.digitaldarji.Model.Notifications;
import com.appincubator.digitaldarji.R;

public class NotificationAdapter extends ArrayAdapter<Notifications> {

    Activity context;
    ArrayList<Notifications> notifications;

    public NotificationAdapter(Activity context, ArrayList<Notifications> objects) {
        super(context, R.layout.item_notification, objects);
        this.context = context;
        this.notifications = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_notification,null,true);

        LinearLayout ll_notification = view.findViewById(R.id.ll_notification);
        ImageView iv_notification = view.findViewById(R.id.iv_notification);
        TextView tv_notification = view.findViewById(R.id.tv_notification);

        Notifications notification = notifications.get(position);

        if (notification.getType().equals("ORDER")){
            iv_notification.setImageResource(R.drawable.cash);
            ll_notification.setBackgroundColor(Color.parseColor("#2603F9F1"));
        }else if (notification.getType().equals("REQUEST")){
            iv_notification.setImageResource(R.drawable.card);
            ll_notification.setBackgroundColor(Color.parseColor("#261B13F9"));
        }else {
            iv_notification.setImageResource(R.drawable.gift_card);
            ll_notification.setBackgroundColor(Color.parseColor("#260FF51E"));
        }

        tv_notification.setText(notification.getMsg());

        return view;
    }
}
