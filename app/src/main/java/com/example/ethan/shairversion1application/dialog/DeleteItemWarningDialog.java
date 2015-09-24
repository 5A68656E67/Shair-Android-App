/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: DeleteItemWarningDialog
 *
 *  class properties:
 *  id: int
 *  buildAccount: BuildAccount
 *  name: String
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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;

public class DeleteItemWarningDialog extends DialogFragment {
    private int id;
    private BuildAccount buildAccount;
    private String name;
    public DeleteItemWarningDialog(){}
    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_deleteitemwarningdialog, null);
        builder.setView(dialogView);
        buildAccount = new BuildAccount();
        id = getArguments().getInt("id");
        name = getArguments().getString("name");

        Button cancel = (Button) dialogView.findViewById(R.id.cancel_delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Button confirm = (Button) dialogView.findViewById(R.id.confirm_delete);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //noinspection AccessStaticViaInstance
                buildAccount.getAccount().getUser().removePostedItem(id);
                JsonObject deleteItemJson = new JsonObject();
                deleteItemJson.addProperty("id",id);
                DefaultSocketClient client = new DefaultSocketClient(QueryType.DELETE_ITEM, deleteItemJson);
                client.start();
                Toast.makeText(getActivity(), name + " has been deleted",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return builder.create();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(750, 240);
        window.setGravity(Gravity.CENTER);
    }
}
