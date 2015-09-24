/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: AboutUsDialog
 *
 *  class properties:
 *  cancel:TextView
 *  soundsGood:Button
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState): Dialog
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ethan.shairversion1application.R;

/**
 * A simple {@link Fragment} subclass.
 * Activ
 */
public class AboutUsDialog extends DialogFragment {
    @SuppressWarnings("FieldCanBeLocal")
    private TextView cancel;
    @SuppressWarnings("FieldCanBeLocal")
    private Button soundsGood;

    public AboutUsDialog(){}

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.settings_aboutusdialog, null);
        builder.setView(dialogView);

        cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                }
            });
        soundsGood = (Button) dialogView.findViewById(R.id.sounds_good);
        soundsGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                }
            });
        return builder.create();
        }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(750, 700);
        window.setGravity(Gravity.CENTER);
    }

}
