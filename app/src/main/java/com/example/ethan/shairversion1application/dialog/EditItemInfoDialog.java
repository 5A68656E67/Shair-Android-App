/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: EditItemInfoDialog
 *
 *  class properties:
 *  dialogView: View
 *  save: TextView
 *  itemName: EditText
 *  itemDescription: EditText
 *  itemNewDegree: SeekBar
 *  itemPrice: EditText
 *  itemDuration: EditText
 *  itemDiscuss: Switch
 *  itemSecurityDeposit: EditText
 *  itemDeadline: DatePicker
 *  cancel: TextView
 *  percentageTV: TextView
 *  itemID: int
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState): Dialog
 *  onResume():void
 *  initialViews(): void
 *  addSaveListener(): void
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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;

public class EditItemInfoDialog extends DialogFragment {
    private View dialogView;
    private TextView save;
    private EditText itemName;
    private EditText itemDescription;
    private SeekBar itemNewDegree;
    private EditText itemPrice;
    private EditText itemDuration;
    private Switch itemDiscuss;
    private EditText itemSecurityDeposit;
    private DatePicker itemDeadline;
    private TextView cancel;
    private TextView percentageTV;
    private int itemID;
    public EditItemInfoDialog(){}

    @SuppressLint("InflateParams")
    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_edititeminfodialog, null);
        builder.setView(dialogView);
        initialViews();
        itemName.setText(getArguments().getString("name"));
        itemDescription.setText(getArguments().getString("description"));
        itemNewDegree.setProgress(getArguments().getInt("new_degree"));
        itemPrice.setText(Double.toString(getArguments().getDouble("price")));
        itemDuration.setText(Integer.toString(getArguments().getInt("duration")));
        itemDiscuss.setChecked(getArguments().getBoolean("discuss"));
        itemSecurityDeposit.setText(Double.toString(getArguments().getDouble("security_deposit")));
        int oldDeadline = getArguments().getInt("deadline");
        int dayNum = oldDeadline % 100;
        int monthNum = (oldDeadline - dayNum) / 100 % 100 - 1;
        int yearNum = (oldDeadline - 100 * monthNum - dayNum) / 10000;
        itemDeadline.updateDate(yearNum,monthNum,dayNum);
        itemID = getArguments().getInt("item_id");



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getDialog().dismiss();
            }
        });
        addSaveListener();
        return builder.create();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        window.setLayout(750, 780);
        window.setGravity(Gravity.CENTER);
    }

    private void initialViews(){
        cancel = (TextView) dialogView.findViewById(R.id.cancel_window);
        save = (TextView) dialogView.findViewById(R.id.save_action);
        itemName = (EditText) dialogView.findViewById(R.id.stuff_name);
        itemDescription = (EditText) dialogView.findViewById(R.id.stuff_description);
        itemNewDegree = (SeekBar) dialogView.findViewById(R.id.seek_bar);
        itemPrice = (EditText) dialogView.findViewById(R.id.stuff_price);
        itemDuration = (EditText) dialogView.findViewById(R.id.stuff_duration);
        itemDiscuss = (Switch) dialogView.findViewById(R.id.item_discuss_on_off);
        itemSecurityDeposit = (EditText) dialogView.findViewById(R.id.stuff_security_deposit);
        itemDeadline = (DatePicker) dialogView.findViewById(R.id.stuff_datapicker);
        percentageTV = (TextView) dialogView.findViewById(R.id.percentage);
        itemNewDegree.setMax(100);
        itemNewDegree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentageTV.setText(progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void addSaveListener() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = itemName.getText().toString();
                String updatedDescription = itemDescription.getText().toString();
                int updatedNewDegree = itemNewDegree.getProgress();
                double updatedPrice = Double.parseDouble(itemPrice.getText().toString());
                int updatedDuration = Integer.parseInt(itemDuration.getText().toString());
                boolean updatedDiscuss = itemDiscuss.isChecked();
                double updatedSecurityDeposit = Double.parseDouble(itemSecurityDeposit.getText().toString());
                int updatedDeadline = itemDeadline.getYear() * 10000 + (itemDeadline.getMonth() + 1) * 100 + itemDeadline.getDayOfMonth();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",itemID);
                jsonObject.addProperty("name", updatedName);
                jsonObject.addProperty("description", updatedDescription);
                jsonObject.addProperty("new_degree", updatedNewDegree);
                jsonObject.addProperty("price", updatedPrice);
                jsonObject.addProperty("duration", updatedDuration);
                jsonObject.addProperty("discuss", updatedDiscuss);
                jsonObject.addProperty("security_deposit", updatedSecurityDeposit);
                jsonObject.addProperty("deadline", updatedDeadline);

                DefaultSocketClient client = new DefaultSocketClient(QueryType.UPDATE_ITEM, jsonObject);
                client.start();

                //noinspection AccessStaticViaInstance
                Item item = new BuildAccount().getAccount().getUser().getPostedItemById(itemID);
                item.setName(updatedName);
                item.setDescription(updatedDescription);
                item.setNewDegree(updatedNewDegree);
                item.setPrice(updatedPrice);
                item.setDuration(updatedDuration);
                item.setDiscuss(updatedDiscuss);
                item.setSecurityDeposit(updatedSecurityDeposit);
                item.setDeadLine(updatedDeadline);

                TextView nameTextView = (TextView) getActivity().findViewById(R.id.item_name);
                nameTextView.setText(updatedName);
                TextView priceRate = (TextView) getActivity().findViewById(R.id.price);
                priceRate.setText("$ " + item.getPrice() + "/" + item.getDuration() + " days");
                TextView textDiscuss = (TextView) getActivity().findViewById(R.id.discuss);
                if (item.isDiscuss()) {
                    textDiscuss.setText("The price can be discussed");
                } else {
                    textDiscuss.setText("The price cannot be discussed");
                }
                TextView newdgreeText = (TextView) getActivity().findViewById(R.id.new_degree);
                newdgreeText.setText("It is " + item.getNewDegree() + "% new");

                TextView securityText = (TextView) getActivity().findViewById(R.id.security_deposit);
                securityText.setText("$" + item.getSecurityDeposit());

                TextView deadlineText = (TextView) getActivity().findViewById(R.id.deadline);
                String time = Integer.toString(item.getDeadLine());
                deadlineText.setText(time.substring(4,6) + "/" + time.substring(6) + "/" + time.substring(0, 4));

                TextView descriptionText = (TextView) getActivity().findViewById(R.id.posted_item_description);
                descriptionText.setText(item.getDescription());

                Toast.makeText(getActivity(),"Item Info Updated",Toast.LENGTH_SHORT).show();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getDialog().dismiss();
            }
        });
    }
}