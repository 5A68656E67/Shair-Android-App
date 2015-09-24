/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: EditProfileDialog
 *
 *  class properties:
 *  cancel:TextView
 *  phoneNumberTV:TextView
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState):Dialog
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.ethan.shairversion1application.R;

public class HelpDialog extends DialogFragment {
    @SuppressWarnings("FieldCanBeLocal")
    private TextView cancel;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView phoneNumberTV;

    public HelpDialog(){
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.settings_helpdialog, null);
        builder.setView(dialogView);

        cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        phoneNumberTV = (TextView) dialogView.findViewById(R.id.phone_number);
        final String phone = phoneNumberTV.getText().toString();
        phoneNumberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                getDialog().dismiss();
            }
        });
        return  builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(750, 700);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }

}
