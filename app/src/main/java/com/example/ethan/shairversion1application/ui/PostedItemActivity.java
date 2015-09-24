/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: PostedItemActivity
 *
 *  class properties:
 *  item: Item
 *  mDemoSlider: SliderLayout
 *  jsonStr: String
 *  requestUserInfoTask: RequestUserInfoTask
 *  buildAccount: BuildAccount
 *  linearLayout: LinearLayout
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.dialog.DeleteItemWarningDialog;
import com.example.ethan.shairversion1application.dialog.EditItemInfoDialog;
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

public class PostedItemActivity  extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener {
    private Item item;
    @SuppressWarnings("FieldCanBeLocal")
    private SliderLayout mDemoSlider;
    private String jsonStr = null;
    @SuppressWarnings("FieldCanBeLocal")
    private RequestUserInfoTask requestUserInfoTask;
    private BuildAccount buildAccount;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_posteditemactivity);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff3c3c")));

        item = (Item) getIntent().getExtras().getSerializable("item");
        this.setTitle(item.getName());
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("sharer_id", item.getSharerID());
        jsonObject.addProperty("item_id", item.getId());
        //noinspection AccessStaticViaInstance
        jsonObject.addProperty("user_id", new BuildAccount().getAccount().getUser().getId());
        requestUserInfoTask = new RequestUserInfoTask(jsonObject);
        requestUserInfoTask.execute();

        JsonParser parser = new JsonParser();
        //noinspection StatementWithEmptyBody
        while (jsonStr == null) {

        }

        JsonObject jsonObject1 = parser.parse(jsonStr).getAsJsonObject();

        User user = new User();
        user.setName(jsonObject1.get("name").getAsString());
        user.setEmail(jsonObject1.get("email").isJsonNull() ? null : jsonObject1.get("email").getAsString());
        user.setPhone(jsonObject1.get("phone").isJsonNull() ? null : jsonObject1.get("phone").getAsString());
        user.setImgPath(jsonObject1.get("profile_image").isJsonNull() ? null : jsonObject1.get("profile_image").getAsString());


        linearLayout = (LinearLayout) findViewById(R.id.share_on_item);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I've shared an item on Shair: " + item.getName() + ". Come and have a look!");
                startActivity(Intent.createChooser(shareIntent, "Share your item to..."));
            }
        });


        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        //noinspection MismatchedQueryAndUpdateOfCollection
        HashMap<String, String> url_maps = new HashMap<>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (String imgpath : item.getImageArrayList()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(item.getName())
                    .image(imgpath)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", imgpath);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);


//

        TextView nameTextView = (TextView) findViewById(R.id.item_name);
        nameTextView.setText(item.getName());

        LinearLayout editItem = (LinearLayout) findViewById(R.id.edit_action);
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //noinspection AccessStaticViaInstance
                Item updatedItem = buildAccount.getAccount().getUser().getPostedItemById(item.getId());
                FragmentManager fm = getSupportFragmentManager();
                EditItemInfoDialog dialog = new EditItemInfoDialog();
                // when press the send message button, pop up a window for uses entering phone number
                dialog.show(fm, "edit_item_info_dialog");
                Bundle args = new Bundle();
                args.putString("name", updatedItem.getName());
                args.putString("description", updatedItem.getDescription());
                args.putInt("new_degree", updatedItem.getNewDegree());
                args.putDouble("price", updatedItem.getPrice());
                args.putInt("duration", updatedItem.getDuration());
                args.putBoolean("discuss", updatedItem.isDiscuss());
                args.putDouble("security_deposit", updatedItem.getSecurityDeposit());
                args.putInt("deadline", updatedItem.getDeadLine());
                args.putInt("item_id", updatedItem.getId());
                dialog.setArguments(args);
            }
        });

        LinearLayout deleteItem = (LinearLayout) findViewById(R.id.delete_item_action);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DeleteItemWarningDialog dialog = new DeleteItemWarningDialog();
                // when press the send message button, pop up a window for uses entering phone number
                dialog.show(fm, "edit_item_info_dialog");
                Bundle args = new Bundle();
                args.putInt("id", item.getId());
                args.putString("name", item.getName());
                dialog.setArguments(args);
            }
        });


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
        deadlineText.setText(time.substring(4, 6) + "/" + time.substring(6) + "/" + time.substring(0, 4));

        TextView descriptionText = (TextView) findViewById(R.id.posted_item_description);
        descriptionText.setText(item.getDescription());
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
        buildAccount = new BuildAccount();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
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

        RequestUserInfoTask(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REQUEST_USER_ON_ITEM, jsonObject);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                jsonStr = (String) ois.readObject();
                client.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //noinspection RedundantIfStatement
            if (jsonStr != null) {
                return true;
            } else {
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