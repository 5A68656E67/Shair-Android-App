/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: InviteFriendsDialog
 *
 *  class properties:
 *  share:Button
 *  inviteFriends:Button
 *  cancel:TextView
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState):Dialog
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethan.shairversion1application.R;

/**

 */
public class InviteFriendsDialog extends DialogFragment {
    private Button share;
    @SuppressWarnings("FieldCanBeLocal")
    private Button inviteFriends;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView cancel;

    public InviteFriendsDialog(){
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_invitefriendsdialog, null);
        builder.setView(dialogView);


        inviteFriends = (Button) dialogView.findViewById(R.id.invite_friends);
        inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseContactActivity.class);
                startActivity(intent);
            }
        });

        cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        share = (Button) dialogView.findViewById(R.id.share_link);
        share.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link", share.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity().getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Come and join in our Shair Community: " + share.getText());
                startActivity(Intent.createChooser(shareIntent, "Share your link"));
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
        //TODO:
    }
}
