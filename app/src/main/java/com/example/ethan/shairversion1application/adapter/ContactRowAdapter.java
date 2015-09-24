/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ContactRowAdapter
 *
 *  class methods:
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
import android.widget.CheckedTextView;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Contacts;

import java.util.ArrayList;

public class ContactRowAdapter extends ArrayAdapter<Contacts> {
    public ContactRowAdapter(Context context, ArrayList<Contacts> contacts){
        super(context,0,contacts);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Contacts uc = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_row, parent, false);
        }
        CheckedTextView ctv = (CheckedTextView) convertView.findViewById(R.id.single_contact);
        ctv.setText(uc.getName());
        return convertView;
    }
}
