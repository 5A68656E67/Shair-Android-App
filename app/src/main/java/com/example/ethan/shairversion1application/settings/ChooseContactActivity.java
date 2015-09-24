/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ChooseContactActivity
 *
 *
 *  class methods:
 *  onCreateDialog(Bundle savedInstanceState): Dialog
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.ContactRowAdapter;
import com.example.ethan.shairversion1application.entities.Contacts;

import java.util.ArrayList;

public class ChooseContactActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_choosecontactactivity);

        //noinspection ConstantConditions
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Contacts> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        @SuppressLint("Recycle") Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    pCur.moveToNext();
                    String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    while (pCur.moveToNext()) {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                        Toast.makeText(getApplicationContext(), "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
//                    }
                    Contacts uc = new Contacts();
                    uc.setName(name);
                    uc.setPhone(phoneNumber);
                    contactList.add(uc);
                    pCur.close();
                }
            }
        }

        ListView contactLV = (ListView) findViewById(R.id.contact_list);
        contactLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ContactRowAdapter cra = new ContactRowAdapter(getApplicationContext(),contactList);
        contactLV.setAdapter(cra);
        final ArrayList<Contacts> selectedContact = new ArrayList<>();
        contactLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedContact.contains(contactList.get(position))){
                    selectedContact.remove(contactList.get(position));
                }else{
                    selectedContact.add(contactList.get(position));
                }
            }
        });

        Button sendInvites = (Button) findViewById(R.id.send_invites);
        sendInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "I am inviting you to join the Shair community!";
                SmsManager smsManager = SmsManager.getDefault();
                for(Contacts u : selectedContact){
                    String number = u.getPhone();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "Your invites has been sent to " + u.getName(), Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            // if it is back home click, finish this activity
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
