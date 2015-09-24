/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ChooseStartDateDialog
 *
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState): Dialog
 *  onResume():void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;

public class ChooseStartDateDialog extends DialogFragment {
    public ChooseStartDateDialog(){}
    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_choosestartdate, null);
        builder.setView(dialogView);
        final int deadline = getArguments().getInt("deadline");
        final int itemID = getArguments().getInt("item_id");
        final int userID = getArguments().getInt("user_id");
        final String ddlStr = Integer.toString(deadline);

        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel_choose_start_date_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.choose_start_date_picker);
        Button confirmDate = (Button) dialogView.findViewById(R.id.confirm_start_date);
        confirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int startdate = dp.getYear() * 10000 + (dp.getMonth() + 1) * 100 + dp.getDayOfMonth();
                final String stdStr = Integer.toString(startdate);
                if(startdate >= deadline){
                    Toast.makeText(getActivity(),"start date should be earlier than deadline",Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // alert dialog settings
                builder.setTitle("Please Confirm");
                builder.setMessage("Start date: " + stdStr.substring(4, 6) + "/" + stdStr.substring(6) + "/" + stdStr.substring(0, 4)
                        + "\nDeadline: " + ddlStr.substring(4, 6) + "/" + ddlStr.substring(6) + "/" + ddlStr.substring(0, 4));
                // set the "delete image" button, if the use click it, it will delete the image from the database
                // then it will also be deleted from the gallery

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // default socket client
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("item_id",itemID);
                        jsonObject.addProperty("needer_id",userID);
                        jsonObject.addProperty("startdate",startdate);
                        DefaultSocketClient client = new DefaultSocketClient(QueryType.UPDATE_SHARE, jsonObject);
                        client.start();

                        getDialog().dismiss();
                        Button itemPageButton = (Button) getActivity().findViewById(R.id.i_want_it);
                        itemPageButton.setText("You've got this");
                        itemPageButton.setBackgroundColor(getResources().getColor(R.color.grey));
                        itemPageButton.setClickable(false);
                    }
                });
                // set the "set as a wallpaper" button, if the user click it, it will set the chosen image as device's wallpaper
                builder.setNegativeButton("Cancel", null);
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                    }
                });
                dialog.show();
            }
        });



        return builder.create();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        window.setLayout(750, 760);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }
}