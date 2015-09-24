/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ItemActivity
 *
 *  class properties:
 *  item: Item
 *  mDemoSlider: SliderLayout
 *  jsonStr: String
 *  requestUserInfoTask: RequestUserInfoTask
 *  buildAccount: BuildAccount
 *  type: int
 *  linearLayout: LinearLayout
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  onOptionsItemSelected(MenuItem item): boolean
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.dialog.ChooseStartDateDialog;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.entities.User;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ItemActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener{
    private Item item;
    @SuppressWarnings("FieldCanBeLocal")
    private SliderLayout mDemoSlider;
    private String jsonStr = null;
    @SuppressWarnings("FieldCanBeLocal")
    private RequestUserInfoTask requestUserInfoTask;
    private BuildAccount buildAccount;
    private int type;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_itemactivity);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffff3c3c")));

        item = (Item) getIntent().getExtras().getSerializable("item");
        this.setTitle(item.getName());
        type = getIntent().getExtras().getInt("type");
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("sharer_id", item.getSharerID());
        jsonObject.addProperty("item_id",item.getId());
        //noinspection AccessStaticViaInstance
        jsonObject.addProperty("user_id",new BuildAccount().getAccount().getUser().getId());
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

        int like = jsonObject1.get("like_item").getAsInt();
        int neederID = jsonObject1.get("needer_id").getAsInt();



        linearLayout = (LinearLayout) findViewById(R.id.share_on_item);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I've found an interesting item on Shair: " + item.getName() + ". Check it out!");
                startActivity(Intent.createChooser(shareIntent, "Share this item to..."));
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


        RoundedImageView profileImg = (RoundedImageView) findViewById(R.id.item_page_profile_image);
        if (user.getImgPath() != null) {
            System.out.println("profile image path:    " + user.getImgPath());
            Ion.with(profileImg).load(user.getImgPath());
        } else {
            Ion.with(profileImg).load("https://s3.amazonaws.com/startupshair/itemimg/profile_image_default.png");
        }
//

        TextView nameTextView = (TextView) findViewById(R.id.item_name);
        nameTextView.setText(item.getName());

        Button buyButton = (Button) findViewById(R.id.i_want_it);
        buyButton.setText("I want it!");
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ChooseStartDateDialog dialog = new ChooseStartDateDialog();
                // when press the send message button, pop up a window for uses entering phone number
                dialog.show(fm, "choose_start_date");
                Bundle args = new Bundle();
                args.putInt("deadline", item.getDeadLine());
                args.putInt("item_id",item.getId());
                //noinspection AccessStaticViaInstance
                args.putInt("user_id",new BuildAccount().getAccount().getUser().getId());
                dialog.setArguments(args);
            }
        });

        final ImageView likeImag = (ImageView) findViewById(R.id.heart_icon);

        final TextView likeText = (TextView) findViewById(R.id.like_icon);

        if(like == 1){
            likeImag.setImageResource(R.drawable.liked);
            likeText.setText("Unlike");
        }else{
            likeImag.setImageResource(R.drawable.unliked);
            likeText.setText("Like");
        }
//noinspection AccessStaticViaInstance
        if(neederID == buildAccount.getAccount().getUser().getId()){
            buyButton.setText("You've got this");
            buyButton.setBackgroundColor(getResources().getColor(R.color.grey));
            buyButton.setClickable(false);
        }

        likeImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeText.getText().toString().equals("Like")) {
                    likeText.setText("Unlike");
                    likeImag.setImageResource(R.drawable.liked);
                } else {
                    likeText.setText("Like");
                    likeImag.setImageResource(R.drawable.unliked);
                }
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
        deadlineText.setText(time.substring(4,6) + "/" + time.substring(6) + "/" + time.substring(0, 4));

        TextView descriptionText = (TextView) findViewById(R.id.description);
        descriptionText.setText(item.getDescription());

        TextView leaserNameText = (TextView) findViewById(R.id.leaser_name);
        leaserNameText.setText(user.getName());

        TextView emailText = (TextView) findViewById(R.id.leaer_email);
        if (user.getEmail() == null) {
            emailText.setText("Not Available");
        } else {
            emailText.setText(user.getEmail());
        }


        TextView phoneText = (TextView) findViewById(R.id.leaser_phone);
        if (user.getPhone() == null) {
            phoneText.setText("Not Available");
        } else  {
            phoneText.setText(user.getPhone());
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
        Item temp = this.item;
        buildAccount = new BuildAccount();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            TextView likeText = (TextView) findViewById(R.id.like_icon);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item_id", temp.getId());
            if (likeText.getText().toString().equals("Like")) {
                jsonObject.addProperty("type", 0);
            } else {
                jsonObject.addProperty("type", 1);
            }
            //noinspection AccessStaticViaInstance
            jsonObject.addProperty("liker_id", buildAccount.getAccount().getUser().getId());
            DefaultSocketClient client = new DefaultSocketClient(QueryType.UPDATE_LIKE, jsonObject);
            client.start();

            this.finish();
            if (type == 1) {

                Intent intent = new Intent(ItemActivity.this, UIManageActivity.class);
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
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REQUEST_USER_ON_ITEM, jsonObject);
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