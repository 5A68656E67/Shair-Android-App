/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ItemAdapter
 *
 *  class properties:
 *  count: int
 *  searchCount: int
 *
 *  class methods:
 *  getItemId(int pos): long
 *  convertDateFormat(int dateInNumber): String
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }

    public static int count; /* starting amount */
    public static int searchCount;

    public long getItemId(int pos) { return pos; }

    public static class ViewHolder {
        TextView tvName;
        TextView tvdistance;
        TextView tvprice;
        TextView tvduration;
        TextView tvdiscuss;
        TextView tvnewDegree;
        ImageView ivImageID;
        TextView tvdeadLine;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView)  convertView.findViewById(R.id.item_name);
            viewHolder.tvdistance = (TextView) convertView.findViewById(R.id.item_distance);
            viewHolder.tvprice = (TextView) convertView.findViewById(R.id.item_price);
            viewHolder.tvduration = (TextView) convertView.findViewById(R.id.item_duration);
            viewHolder.tvdiscuss = (TextView) convertView.findViewById(R.id.item_discuss);
            viewHolder.tvnewDegree = (TextView) convertView.findViewById(R.id.item_new_degree);
            viewHolder.ivImageID = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tvdeadLine = (TextView) convertView.findViewById(R.id.item_deadline);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvdistance.setText(Double.toString(item.getDistance()));
        viewHolder.tvprice.setText(Double.toString(item.getPrice()));
        viewHolder.tvduration.setText(Integer.toString(item.getDuration()));
        if (item.isDiscuss()) {
            viewHolder.tvdiscuss.setText("Discussed");
        } else {
            viewHolder.tvdiscuss.setText("");
        }
        viewHolder.tvnewDegree.setText(Integer.toString(item.getNewDegree()));
       // String time = Integer.toString(item.getDeadLine());
        viewHolder.tvdeadLine.setText(convertDateFormat(item.getDeadLine()));
        //viewHolder.ivImageID.setImageResource(item.getImgID());
        if (!item.getImageArrayList().isEmpty()) {
            Ion.with(viewHolder.ivImageID).load(item.getImageArrayList().get(0));
            Log.e("add image: ", item.getImageArrayList().get(0));
        } else {
            Ion.with(viewHolder.ivImageID).load("https://s3.amazonaws.com/startupshair/itemimg/no-image-thumb.png");
        }



        // Return the completed view to render on screen
        return convertView;
    }


    private String convertDateFormat(int dateInNumber){
        int dayNum = dateInNumber % 100;
        int monthNum = (dateInNumber - dayNum) / 100 % 100;
        String monthStr = null;
        switch(monthNum){
            case 1 : monthStr = "Jan";break;
            case 2 : monthStr = "Feb";break;
            case 3 : monthStr = "Mar";break;
            case 4 : monthStr = "Apr";break;
            case 5 : monthStr = "May";break;
            case 6 : monthStr = "Jun";break;
            case 7 : monthStr = "Jul";break;
            case 8 : monthStr = "Aug";break;
            case 9 : monthStr = "Sept";break;
            case 10 : monthStr = "Oct";break;
            case 11 : monthStr = "Nov";break;
            case 12 : monthStr = "Dec";break;
        }
        Calendar cal = Calendar.getInstance();
        String input = Integer.toString(dateInNumber);
        String format = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
        Date date = null;
        try {
            date = df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String weekStr = null;
        switch(week){
            case 0: weekStr = "Sunday";break;
            case 1: weekStr = "Monday";break;
            case 2: weekStr = "Tuesday";break;
            case 3: weekStr = "Wednesday";break;
            case 4: weekStr = "Thursday";break;
            case 5: weekStr = "Friday";break;
            case 6: weekStr = "Saturday";break;
        }
        return weekStr + ", " + monthStr + " " + dayNum;
    }


}
