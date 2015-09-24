/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: MainActivity
 *
 *  class properties:
 *  accountName:EditText
 *  accountPassword:EditText
 *  buttonsLayout: LinearLayout
 *  imageView: ImageView
 *  motionBack: ImageView
 *  shairLogo: ImageView
 *  linearLayout: LinearLayout
 *  mAuthTask: UserLoginTask
 *  customExceptionHandler: CustomExceptionHandler
 *  key: String
 *  value: String
 *  sqlDataBase: SQLDataBase
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  addCreateAccountListener():void
 *  addLoginListener():void
 *  addFindPasswordListener():void
 *  onEditorAction(TextView v, int actionId, KeyEvent event): boolean
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.database.SQLDataBase;
import com.example.ethan.shairversion1application.entities.Account;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.example.ethan.shairversion1application.ui.UIManageActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;
import java.io.ObjectInputStream;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    private EditText accountName;
    private EditText accountPassword;
    private LinearLayout buttonsLayout;
    private ImageView imageView;
    private ImageView motionBack;
    private ImageView shairLogo;
    private LinearLayout linearLayout;
    private UserLoginTask mAuthTask = null;
    private CustomExceptionHandler customExceptionHandler;
    private String key;
    private String value;
    private SQLDataBase sqlDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        sqlDataBase = new SQLDataBase(getApplicationContext());

        motionBack = (ImageView) findViewById(R.id.motion_background);
        motionBack.setBackgroundResource(R.drawable.gif);

        AnimationDrawable pro = (AnimationDrawable)motionBack.getBackground();
        pro.start();


        shairLogo = (ImageView) findViewById(R.id.shair_logo);
        linearLayout = (LinearLayout) findViewById(R.id.login_content);
        linearLayout.setVisibility(View.GONE);

        Animation animTranslate  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
        animTranslate.setStartOffset(2000);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade);
                linearLayout.startAnimation(animFade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        shairLogo.startAnimation(animTranslate);
        accountName = (EditText) findViewById(R.id.login_account);
        accountPassword = (EditText) findViewById(R.id.login_password);
        buttonsLayout = (LinearLayout) findViewById(R.id.login_buttons_layout);
        imageView = (ImageView) findViewById(R.id.login_gif_image);
        addCreateAccountListener();
        addLoginListener();
        addFindPasswordListener();
        customExceptionHandler = new CustomExceptionHandler();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (sqlDataBase.getAutoLoginFlag().get(0) == 1) {
            Account tempAccount = sqlDataBase.getAcount().get(0);
            System.out.println("account name from database: " + tempAccount.getAccountName());
            System.out.println("account password from database: " + tempAccount.getAccountPassword());
            mAuthTask = new UserLoginTask(tempAccount.getAccountName(),tempAccount.getAccountPassword());
            mAuthTask.execute((Void) null);
        } else {
            if (!sqlDataBase.getAcount().isEmpty()) {
                Account tempAccount = sqlDataBase.getAcount().get(0);
                accountName.setText(tempAccount.getAccountName());
            }

        }

        accountName.setOnEditorActionListener(this);
        accountPassword.setOnEditorActionListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void addCreateAccountListener() {
        final TextView textView = (TextView) findViewById(R.id.text_create);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addLoginListener() {
        final Button button = (Button) findViewById(R.id.button_login_default);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (!isConnected) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View toastView = inflater.inflate(R.layout.offline_toast,
                            (ViewGroup) findViewById(R.id.toast_offline));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(toastView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    key = accountName.getText().toString();
                    value = accountPassword.getText().toString();
                    if (!customExceptionHandler.checkRegisterName(key)) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                        ad.setTitle("Log In Error");
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
                    if (!customExceptionHandler.checkRegisterPassword(value)) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                        ad.setTitle("Invalid password format");
                        // ad.setMessage("For your account safety, your password length must between 6 and 20, and it must contain at least one lowercase letter, one uppercase letter, one special character, one digit, and whitespace is not allowed. Thanks.");
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

                    mAuthTask = new UserLoginTask(key,value);
                    mAuthTask.execute((Void) null);
                }



            }
        });
    }


    public void addFindPasswordListener() {
        final TextView textView = (TextView) findViewById(R.id.text_forget);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_GO == actionId) {

            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View toastView = inflater.inflate(R.layout.offline_toast,
                        (ViewGroup) findViewById(R.id.toast_offline));
                Toast toast = new Toast(getApplicationContext());
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                key = accountName.getText().toString();
                value = accountPassword.getText().toString();
                if (!customExceptionHandler.checkRegisterName(key)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                    ad.setTitle("Log In Error");
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
                    return false;
                }
                if (!customExceptionHandler.checkRegisterPassword(value)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                    ad.setTitle("Invalid password format");
                    // ad.setMessage("For your account safety, your password length must between 6 and 20, and it must contain at least one lowercase letter, one uppercase letter, one special character, one digit, and whitespace is not allowed. Thanks.");
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
                    return false;
                }

                mAuthTask = new UserLoginTask(key,value);
                mAuthTask.execute((Void) null);
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                //noinspection ConstantConditions
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                return  true;
            }

        }


        return false;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String taskkey;
        private final String taskvalue;
        private String jsonStr;
        private int i = -1;

        UserLoginTask(String key, String value) {
            this.taskkey = key;
            this.taskvalue = value;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                JsonObject keyvaluePair = new JsonObject();
                keyvaluePair.addProperty("key",taskkey);
                keyvaluePair.addProperty("value", taskvalue);
                DefaultSocketClient client = new DefaultSocketClient(QueryType.LOGIN_ACCOUNT,keyvaluePair);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                i = (Integer) ois.readObject();
                if(i == 1){
                    jsonStr = (String) ois.readObject();
                    new BuildAccount().build(new JsonParser().parse(jsonStr).getAsJsonObject());
                }
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
            buttonsLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Ion.with(imageView).load("https://s3.amazonaws.com/startupshair/itemimg/fire_dragon.gif");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                if (sqlDataBase.getAutoLoginFlag().get(0) == 0) {
                    sqlDataBase.deleteAllAccount();
                    sqlDataBase.addItem(taskkey, taskvalue, 0);
                }

                Intent intent = new Intent(MainActivity.this, UIManageActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Welcome back", Toast.LENGTH_SHORT).show();
            }
            else {
                buttonsLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Log In Error");
                ad.setMessage("Invalid email or password. Please try again.");
                ad.setPositiveButton("OK", null);
                ad.setCancelable(false);
                ad.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }


}

