/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: FindPasswordActivity
 *
 *  class properties:
 *  findPassword: EditText
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  addSendEmailListener():void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;


public class FindPasswordActivity extends AppCompatActivity {

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private EditText findPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_findpasswordactivity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        addSendEmailListener();
        findPassword = (EditText) findViewById(R.id.edit_find_password);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSendEmailListener(){
        final Button button = (Button) findViewById(R.id.send_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!new CustomExceptionHandler().checkRegisterName(findPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("account",findPassword.getText().toString());
                DefaultSocketClient client = new DefaultSocketClient(QueryType.FIND_PASSWORD, jsonObject);
                client.start();
                Intent intent = new Intent(FindPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Please check your email",Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                //noinspection ConstantConditions
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }
        });
    }
}