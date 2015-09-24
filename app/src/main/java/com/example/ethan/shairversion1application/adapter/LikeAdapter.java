/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: LikeAdapter
 *
 *  class methods:
 *  getItemId(int pos): long
 *  getView(int position, View convertView, ViewGroup parent): View
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Notification;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class LikeAdapter extends ArrayAdapter<Notification> {
    public LikeAdapter(Context context, ArrayList<Notification> users) {
        super(context, 0, users);
    }

    public long getItemId(int pos) { return pos; }

    public static class ViewHolder {
        TextView tvinfo;
        ImageView ivitemImg;
        RoundedImageView ivneederImg;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Notification notification = getItem(position);
        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvinfo = (TextView)  convertView.findViewById(R.id.notification_info);
            viewHolder.ivitemImg = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.ivneederImg = (RoundedImageView) convertView.findViewById(R.id.needer_img);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvinfo.setText(notification.getNeederName() + " likes your item " + notification.getItemName());
        Ion.with(viewHolder.ivitemImg).load(notification.getItemImg());
        Ion.with(viewHolder.ivneederImg).load(notification.getNeederImg());





        // Return the completed view to render on screen
        return convertView;
    }
}
