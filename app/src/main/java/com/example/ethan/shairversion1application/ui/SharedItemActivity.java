/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: SharedItemActivity
 *
 *  class properties:
 *  item: Item
 *  mDemoSlider: SliderLayout
 *  jsonStr: String
 *  requestUserInfoTask: RequestUserInfoTask
 *  type: int
 *  shareAction: LinearLayout
 *  callAction: LinearLayout
 *  emailAction: LinearLayout
 *  needer: User
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  getLocationInfo(LatLng latLng): String
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.entities.User;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class SharedItemActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener{
    private Item item;
    private SliderLayout mDemoSlider;
    private String jsonStr = null;
    private RequestUserInfoTask requestUserInfoTask;
    private int type;
    private LinearLayout shareAction;
    private LinearLayout callAction;
    private LinearLayout emailAction;
    private User needer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_shareditemactivity);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = (Item) getIntent().getExtras().getSerializable("item");
        this.setTitle(item.getName());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff3c3c")));
        type = getIntent().getExtras().getInt("type");
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("needer_id",item.getNeederID());
        requestUserInfoTask = new RequestUserInfoTask(jsonObject);
        requestUserInfoTask.execute();

        JsonParser parser = new JsonParser();
        //noinspection StatementWithEmptyBody
        while (jsonStr == null) {

        }

        JsonObject jsonObject1 = parser.parse(jsonStr).getAsJsonObject();
        needer = new User();
        needer.setName(jsonObject1.get("name").getAsString());
        needer.setEmail(jsonObject1.get("email").isJsonNull() ? null : jsonObject1.get("email").getAsString());
        needer.setPhone(jsonObject1.get("phone").isJsonNull() ? null : jsonObject1.get("phone").getAsString());




        shareAction = (LinearLayout) findViewById(R.id.share_on_item);
        shareAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I've shared " + item.getName() + " with " + needer.getName() + " on Shair. Check it out!");
                startActivity(Intent.createChooser(shareIntent, "Share this transaction to..."));
            }
        });

        emailAction = (LinearLayout) findViewById(R.id.email_action);
        emailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needer.getEmail() == null || needer.getEmail().length() == 0){
                    Toast.makeText(getApplicationContext(),"Borrower's email unavailable",Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{needer.getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, item.getName());
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        callAction = (LinearLayout) findViewById(R.id.call_action);
        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needer.getPhone() == null || needer.getPhone().length() == 0){
                    Toast.makeText(getApplicationContext(),"Borrower's number unavailable",Toast.LENGTH_SHORT).show();
                }else{
                    Uri number = Uri.parse("tel:" + needer.getPhone());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                }
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        //noinspection MismatchedQueryAndUpdateOfCollection
        HashMap<String,String> url_maps = new HashMap<>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for(String imgpath : item.getImageArrayList()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(item.getName())
                    .image(imgpath)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",imgpath);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);


        TextView nameTextView = (TextView) findViewById(R.id.item_name);
        nameTextView.setText(item.getName());

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(getLocationInfo(new LatLng(item.getLatitude(), item.getLongitude())));

        TextView priceRate = (TextView) findViewById(R.id.price);
        priceRate.setText("$ " + item.getPrice() + "/" + item.getDuration() + " days");

        TextView textDiscuss = (TextView) findViewById(R.id.discuss);
        if (item.isDiscuss()) {
            textDiscuss.setText("The price can be discussed");
        } else {
            textDiscuss.setText("The price cannot be discussed");
        }

        TextView newdgreeText = (TextView) findViewById(R.id.new_degree);
        newdgreeText.setText("It is " + item.getNewDegree() + "% new");

        TextView securityText = (TextView) findViewById(R.id.security_deposit);
        securityText.setText("$" + item.getSecurityDeposit());

        TextView deadlineText = (TextView) findViewById(R.id.deadline);
        String time = Integer.toString(item.getDeadLine());
        deadlineText.setText(time.substring(4,6) + "/" + time.substring(6) + "/" + time.substring(0, 4));

        TextView descriptionText = (TextView) findViewById(R.id.description);
        descriptionText.setText(item.getDescription());

        TextView borrowerNameTV = (TextView) findViewById(R.id.borrower_name);
        borrowerNameTV.setText(needer.getName());

        TextView borrowerEmailTV = (TextView) findViewById(R.id.borrower_email);
        if (needer.getEmail() == null || needer.getEmail().length() == 0) {
            borrowerEmailTV.setText("Not Available");
        } else {
            borrowerEmailTV.setText(needer.getEmail());
        }

        TextView borrowerPhoneTV = (TextView) findViewById(R.id.borrower_phone);
        if (needer.getPhone() == null || needer.getPhone().length() == 0) {
            borrowerPhoneTV.setText("Not Available");
        } else  {
            borrowerPhoneTV.setText(needer.getPhone());
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            if (type == 1) {

                Intent intent = new Intent(SharedItemActivity.this, UIManageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(ArrayList<Item> arrayOfUsers) {

    }

    public String getLocationInfo(LatLng latLng) {
        String message = null;
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String postalCode = addresses.get(0).getPostalCode();

            message = address + ", " + city + ", " + postalCode;

        }
        return message;

    }


    public class RequestUserInfoTask extends AsyncTask<Void, Void, Boolean> {

        private JsonObject jsonObject;

        RequestUserInfoTask(JsonObject jsonObject ){
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REQUEST_NEEDER_ON_ITEM, jsonObject);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                jsonStr = (String) ois.readObject();
                client.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
            }
            //noinspection RedundantIfStatement
            if(jsonStr != null){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }
    }


}