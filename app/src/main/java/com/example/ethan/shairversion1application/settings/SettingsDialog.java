/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: SettingsDialog
 *
 *  class properties:
 *  notifications:TextView
 *  autologin:TextView
 *  switchNotifications:Switch
 *  switchAutoLogin:Switch
 *  temp: Account
 *  sqlDataBase: SQLDataBase
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState):Dialog
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.database.SQLDataBase;
import com.example.ethan.shairversion1application.entities.Account;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsDialog extends DialogFragment {
    private TextView notifications;
    private TextView autologin;
    @SuppressWarnings("FieldCanBeLocal")
    private Switch switchNotifications;
    @SuppressWarnings("FieldCanBeLocal")
    private Switch switchAutoLogin;
    private Account temp;
    private SQLDataBase sqlDataBase;
    private TextView cancel;

    public SettingsDialog(){}

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        sqlDataBase = new SQLDataBase(getActivity());

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.settings_settingsdialog, null);
        builder.setView(dialogView);

        notifications = (TextView) dialogView.findViewById(R.id.notifications);
        autologin = (TextView) dialogView.findViewById(R.id.auto_login);

        switchNotifications = (Switch) dialogView.findViewById(R.id.notifications_on_off);
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifications.setTextColor(Color.DKGRAY);

                } else {
                    notifications.setTextColor(Color.GRAY);

                }
            }
        });

        switchAutoLogin = (Switch) dialogView.findViewById(R.id.auto_log_in_on_off);
        if (sqlDataBase.getAutoLoginFlag().get(0) == 1) {
            switchAutoLogin.setChecked(true);
            autologin.setTextColor(Color.DKGRAY);
        } else {
            switchAutoLogin.setChecked(false);
            autologin.setTextColor(Color.GRAY);
        }
        switchAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    autologin.setTextColor(Color.DKGRAY);
                    temp = sqlDataBase.getAcount().get(0);
                    sqlDataBase.deleteAllAccount();
                    sqlDataBase.addItem(temp.getAccountName(), temp.getAccountPassword(), 1);
                }else{
                    autologin.setTextColor(Color.GRAY);
                    temp = sqlDataBase.getAcount().get(0);
                    sqlDataBase.deleteAllAccount();
                    sqlDataBase.addItem(temp.getAccountName(), temp.getAccountPassword(), 0);
                }
            }
        });
        cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return builder.create();
    }
}
