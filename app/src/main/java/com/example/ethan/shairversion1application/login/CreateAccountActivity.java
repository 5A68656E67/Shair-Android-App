/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: CreateAccountActivity
 *
 *  class properties:
 *  account:EditText
 *  password:EditText
 *  firstName:EditText
 *  lastName:EditText
 *  button:Button
 *  customExceptionHandler:CustomExceptionHandler
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  initializeViews():void
 *  addSignUpListener():void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Account;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.koushikdutta.ion.Ion;
import java.io.ObjectInputStream;


public class CreateAccountActivity extends AppCompatActivity {
    private EditText accountET;
    private EditText passwordET;
    private EditText firstNameET;
    private EditText lastNameET;
    private Button button;
    private ImageView imageView;
    private CustomExceptionHandler customExceptionHandler;


    private UserSignupTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff3c3c")));
        setContentView(R.layout.login_createaccountactivity);
        addSignUpListener();
        initializeViews();
        customExceptionHandler = new CustomExceptionHandler();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeViews(){
        this.accountET = (EditText) findViewById(R.id.register_account_name);
        this.passwordET = (EditText) findViewById(R.id.register_password);
        this.firstNameET = (EditText) findViewById(R.id.register_first_name);
        this.lastNameET = (EditText) findViewById(R.id.register_last_name);
        this.button = (Button) findViewById(R.id.button_signup);
        this.imageView = (ImageView) findViewById(R.id.gif_image);
    }

    public void addSignUpListener(){
        button = (Button) findViewById(R.id.button_signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                String accountName = accountET.getText().toString();
                String accountPassword = passwordET.getText().toString();
                String userName = firstNameET.getText().toString() + " " + lastNameET.getText().toString();
                Account account = new Account();
                if (!customExceptionHandler.checkRegisterName(accountName)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(CreateAccountActivity.this);
                    ad.setTitle("Sign Up Error");
                    ad.setMessage("Invalid email. Please use the correct email address as account name");
                    ad.setPositiveButton("OK", null);
                    ad.setCancelable(false);
                    final AlertDialog dialog = ad.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                            }
                    });
                    dialog.show();
                    return;
                }
                if (!customExceptionHandler.checkRegisterPassword(accountPassword)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(CreateAccountActivity.this);
                    ad.setTitle("Invalid password format");
                    ad.setMessage("For your account safety, your password length must between 6 and 20, and it must contain at least one lowercase letter, one uppercase letter, one special character, one digit, and whitespace is not allowed. Thanks.");
                    ad.setPositiveButton("OK", null);
                    ad.setCancelable(false);
                    final AlertDialog dialog = ad.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                        }
                    });
                    dialog.show();
                    return;
                }
                if (!customExceptionHandler.checkProfileName(firstNameET.getText().toString())) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(CreateAccountActivity.this);
                    ad.setTitle("Sign Up Error");
                    ad.setMessage("Invalid email. Please enter your name.");
                    ad.setPositiveButton("OK", null);
                    ad.setCancelable(false);
                    final AlertDialog dialog = ad.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                        }
                    });
                    dialog.show();
                    return;
                }
                if (!customExceptionHandler.checkProfileName(lastNameET.getText().toString())) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(CreateAccountActivity.this);
                    ad.setTitle("Sign Up Error");
                    ad.setMessage("Invalid email. Please enter your name.");
                    ad.setPositiveButton("OK", null);
                    ad.setCancelable(false);
                    final AlertDialog dialog = ad.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.crimson));
                        }
                    });
                    dialog.show();
                    return;
                }
                account.setAccountName(accountName);
                account.setAccountPassword(accountPassword);
                account.getUser().setName(userName);
                mAuthTask = new UserSignupTask(account);
                mAuthTask.execute((Void) null);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final Account account;
        private int i = -1;

        UserSignupTask(Account account) {
            this.account = account;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REGISTER_ACCOUNT,account.toJson());
                client.start();
                ObjectInputStream ois = client.getInputStream();
                i = (Integer) ois.readObject();
                client.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
            }
            //noinspection RedundantIfStatement
            if(i == 1){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            accountET.setVisibility(View.GONE);
            passwordET.setVisibility(View.GONE);
            firstNameET.setVisibility(View.GONE);
            lastNameET.setVisibility(View.GONE);
            button.setVisibility(View.GONE);

            imageView.setVisibility(View.VISIBLE);
            Ion.with(imageView).load("https://s3.amazonaws.com/startupshair/itemimg/fire_dragon.gif");
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                finish();
                Intent intent = new Intent(CreateAccountActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Sign up success!",Toast.LENGTH_SHORT).show();

            }
            else {
                accountET.setVisibility(View.VISIBLE);
                passwordET.setVisibility(View.VISIBLE);
                firstNameET.setVisibility(View.VISIBLE);
                lastNameET.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                accountET.requestFocus();
                passwordET.setText("");
                lastNameET.setText("");
                firstNameET.setText("");
                imageView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Account name has already be taken",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}


