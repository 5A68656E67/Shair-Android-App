/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: GridAdapterBorrow
 *
 *  class methods:
 *  getItemId(int pos): long
 *  getView(int position, View convertView, ViewGroup parent): View
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Item;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class GridAdapterBorrow extends ArrayAdapter<Item> {
    public GridAdapterBorrow(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }

    public long getItemId(int pos) { return pos; }

    public static class ViewHolder {
        TextView tvName;
        ImageView ivImage;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView)  convertView.findViewById(R.id.item_grid_name);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.item_grid_back);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvName.setText(item.getName());
        if (!item.getImageArrayList().isEmpty()) {
            Ion.with(viewHolder.ivImage).load(item.getImageArrayList().get(0));
            Log.e("add image: ", item.getImageArrayList().get(0));
        }



        // Return the completed view to render on screen
        return convertView;
    }
}
