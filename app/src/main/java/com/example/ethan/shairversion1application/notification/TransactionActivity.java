/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: TransactionActivity
 *
 *  class properties:
 *  transactions: ArrayList<Notification>
 *  transactionAdapter: TransactionAdapter
 *  listView: ListView
 *  display: ArrayList<Notification>
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.notification;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.TransactionAdapter;
import com.example.ethan.shairversion1application.entities.Notification;
import com.example.ethan.shairversion1application.ui.NotificationFragment;
import java.util.ArrayList;
import java.util.Collections;


public class TransactionActivity extends AppCompatActivity implements NotificationFragment.OnFragmentInteractionListener {
    public static ArrayList<Notification> transactions = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private ListView listView;
    @SuppressWarnings("FieldCanBeLocal")
    private TransactionAdapter transactionAdapter;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<Notification> display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_transactionactivity);
        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff3c3c")));
        this.setTitle("News");
        display = new ArrayList<>(transactions);
        Collections.reverse(display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.transaction_list);
        transactionAdapter = new TransactionAdapter(getApplication(), display);
        listView.setAdapter(transactionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
