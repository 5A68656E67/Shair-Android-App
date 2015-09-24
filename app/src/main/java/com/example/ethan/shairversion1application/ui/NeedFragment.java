/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: NeedFragment
 *
 *  class properties:
 *  customExceptionHandler: CustomExceptionHandler
 *  view: View
 *  jsonStr: String
 *  itemAdapter: ItemAdapter
 *  imageView: ImageView
 *  empty: TextView
 *  sqlDataBase: SQLDataBase
 *  My_auto_Cmplt_Tv: AutoCompleteTextView
 *  listView: ListView
 *  buildAccount: BuildAccount
 *  needTask: NeedTask
 *  flag: boolean
 *  data: String
 *  searchIcon: ImageView
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState):View
 *  onEditorAction(TextView v, int actionId, KeyEvent event):boolean
 *  getLocation(): LatLng
 *  onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount): void
 *  calculateDistance(double latitute, double longitude, LatLng latLng): double
 *  roundOff(double x, int position): double
 *  addSearchIconListener(): void
 *  onActivityResult(int requestCode, int resultCode, Intent data): void
 *  startVoiceRecognitionActivity(): void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.ItemAdapter;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.database.SQLDataBase;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.exception.CustomExceptionHandler;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NeedFragment extends Fragment implements TextView.OnEditorActionListener,AbsListView.OnScrollListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CustomExceptionHandler customExceptionHandler;
    private View view;
    private String jsonStr;
    private ItemAdapter itemAdapter;
    private ImageView imageView;
    private TextView empty;
    private OnFragmentInteractionListener mListener;
    private SQLDataBase sqlDataBase;
    private AutoCompleteTextView My_auto_Cmplt_Tv;
    private ListView listView;
    private BuildAccount buildAccount;
    private NeedTask needTask;
    private boolean flag = false;
    private String data;
    private ImageView searchIcon;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NeedFragment.
     */
    @SuppressWarnings("unused")
    public static NeedFragment newInstance(String param1, String param2) {
        NeedFragment fragment = new NeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        buildAccount = new BuildAccount();
        view = inflater.inflate(R.layout.ui_needfragment, container, false);
        sqlDataBase = new SQLDataBase(getActivity());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        listView = (ListView) view.findViewById(R.id.search_list);
        imageView = (ImageView) view.findViewById(R.id.loading);
        searchIcon = (ImageView) view.findViewById(R.id.micro_search);
        empty = (TextView) view.findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        Ion.with(imageView).load("https://s3.amazonaws.com/startupshair/itemimg/loading.gif");
        imageView.setVisibility(View.GONE);
        ArrayAdapter<String> My_arr_adapter= new ArrayAdapter<>(getActivity(),R.layout.dropdown_layout,sqlDataBase.getHistory());

        My_auto_Cmplt_Tv = (AutoCompleteTextView) view.findViewById(R.id.autotv);
        My_auto_Cmplt_Tv.setThreshold(2);
        My_auto_Cmplt_Tv.setOnEditorActionListener(this);
        My_auto_Cmplt_Tv.setAdapter(My_arr_adapter);
        My_auto_Cmplt_Tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getActivity(), (CharSequence) arg0.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
            }
        });
        addSearchIconListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Item item = (Item) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("type", 2);
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    @SuppressWarnings("unused")
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_SEARCH == actionId) {
            data = v.getText().toString();
            if (customExceptionHandler.checkSearchInformation(data)) {
                //add search actions
                if (!sqlDataBase.checkHistory(data)) {
                    sqlDataBase.addHistory(data);
                    ArrayAdapter<String> My_arr_adapter= new ArrayAdapter<>(getActivity(),R.layout.dropdown_layout,sqlDataBase.getHistory());
                    My_auto_Cmplt_Tv.setAdapter(My_arr_adapter);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("keyword", data);
                jsonObject.addProperty("start", 0);

                //noinspection AccessStaticViaInstance
                jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
                needTask = new NeedTask(jsonObject);
                needTask.execute();
                JsonParser parser = new JsonParser();
                //noinspection StatementWithEmptyBody
                while (jsonStr == null) {

                }
                System.out.println(jsonStr);
                JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
                jsonStr = null;
                int len = jsonArray.size();
                ItemAdapter.searchCount = len;
                //noinspection AccessStaticViaInstance
                buildAccount.getSearchArraylist().clear();
                for (int i = 0; i < len; i++) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    Item item = new Item();
                    item.setId(jsonObject1.get("id").getAsInt());
                    item.setName(jsonObject1.get("name").getAsString());
                    item.setDescription(jsonObject1.get("description").isJsonNull() ? null : jsonObject1.get("description").getAsString());
                    item.setNewDegree(jsonObject1.get("new_degree").getAsInt());
                    item.setPrice(jsonObject1.get("price").getAsDouble());
                    item.setDuration(jsonObject1.get("duration").getAsInt());
                    item.setDiscuss(jsonObject1.get("discuss").getAsBoolean());
                    item.setSecurityDeposit(jsonObject1.get("security_deposit").getAsDouble());
                    item.setDeadLine(jsonObject1.get("deadline").getAsInt());
                    item.setLongitude(jsonObject1.get("longitude").getAsDouble());
                    item.setLatitude(jsonObject1.get("latitude").getAsDouble());
                    item.setSharerID(jsonObject1.get("sharer_id").getAsInt());
                    item.setDistance(calculateDistance(item.getLatitude(), item.getLongitude(), getLocation()));

                    ArrayList<String> images = item.getImageArrayList();
                    JsonArray imagesJsonArray = jsonObject1.get("images").getAsJsonArray();
                    for (int j = 0; j < imagesJsonArray.size(); j++) {
                        images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                        Log.e("image:    ", imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                    }
                    item.setImageArrayList(images);
                    //noinspection AccessStaticViaInstance
                    buildAccount.getSearchArraylist().add(item);


                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                //noinspection AccessStaticViaInstance
                if (buildAccount.getSearchArraylist().isEmpty()) {
                    listView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    //noinspection AccessStaticViaInstance
                    itemAdapter = new ItemAdapter(getActivity(), buildAccount.getSearchArraylist());
                    listView.setAdapter(itemAdapter);


                    if (ItemAdapter.searchCount >= 10 ) {
                        listView.setOnScrollListener(this);
                    } else {
                        listView.setOnScrollListener(null);
                    }
                }
                return true;
            } else {
                Toast toast=Toast.makeText(getActivity(),"Please input your search item name", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.show();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = /* maybe add a padding */
                firstVisibleItem + visibleItemCount >= totalItemCount;
        if (loadMore) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!flag) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("keyword", data);
                        jsonObject.addProperty("start", ItemAdapter.searchCount);
                        //noinspection AccessStaticViaInstance
                        jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
                        needTask = new NeedTask(jsonObject);
                        needTask.execute();

                        JsonParser parser = new JsonParser();
                        imageView.setVisibility(View.VISIBLE);
                        //noinspection StatementWithEmptyBody
                        while (jsonStr == null) {

                        }
                        imageView.setVisibility(View.GONE);
                        JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
                        jsonStr = null;
                        ArrayList<Item> newArraylist = new ArrayList<>();
                        int len = jsonArray.size();
                        if (len == 0) {
                            flag = true;
                            listView.setOnScrollListener(null);
                        } else {
                            flag = false;
                        }
                        ItemAdapter.searchCount += len;
                        Log.e("item length: ", Integer.toString(ItemAdapter.count));
                        for (int i = 0; i < len; i++) {
                            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                            Item item = new Item();
                            item.setId(jsonObject1.get("id").getAsInt());
                            item.setName(jsonObject1.get("name").getAsString());
                            item.setDescription(jsonObject1.get("description").isJsonNull() ? null : jsonObject1.get("description").getAsString());
                            item.setNewDegree(jsonObject1.get("new_degree").getAsInt());
                            item.setPrice(jsonObject1.get("price").getAsDouble());
                            item.setDuration(jsonObject1.get("duration").getAsInt());
                            item.setDiscuss(jsonObject1.get("discuss").getAsBoolean());
                            item.setSecurityDeposit(jsonObject1.get("security_deposit").getAsDouble());
                            item.setDeadLine(jsonObject1.get("deadline").getAsInt());
                            item.setLongitude(jsonObject1.get("longitude").getAsDouble());
                            item.setLatitude(jsonObject1.get("latitude").getAsDouble());
                            item.setSharerID(jsonObject1.get("sharer_id").getAsInt());

                            ArrayList<String> images = item.getImageArrayList();
                            JsonArray imagesJsonArray = jsonObject1.get("images").getAsJsonArray();
                            for (int j = 0; j < imagesJsonArray.size(); j++) {
                                images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                                Log.e("image:    ", imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                            }
                            item.setImageArrayList(images);
                            newArraylist.add(item);
                        }
                        //noinspection AccessStaticViaInstance
                        buildAccount.getSearchArraylist().addAll(newArraylist);

                    }

                }
            });
            itemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        @SuppressWarnings("UnnecessaryInterfaceModifier")
        public void onFragmentInteraction(Uri uri);
    }





    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Get Current Location
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            Location myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation  != null) {

                double latitude = myLocation.getLatitude();

                // Get longitude of the current location
                double longitude = myLocation.getLongitude();

                // Create a LatLng object for the current location
                //noinspection UnnecessaryLocalVariable
                LatLng latLng = new LatLng(latitude, longitude);
                return latLng;
            }

        }
        return null;
    }


    public double calculateDistance(double latitute, double longitude, LatLng latLng) {
        Location locationA = new Location("point A");
        locationA.setLatitude(latitute);
        locationA.setLongitude(longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latLng.latitude);
        locationB.setLongitude(latLng.longitude);
        return roundOff(locationA.distanceTo(locationB) * 0.0006, 2);
    }

    static double roundOff(double x, int position)
    {
        double a = x;
        double temp = Math.pow(10.0, position);
        a *= temp;
        a = Math.round(a);
        //noinspection RedundantCast
        return (a / (double)temp);
    }


    public void addSearchIconListener() {
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();

            }
        });
    }



    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
        startActivityForResult(intent, 1);
        System.out.println("start voice recognition");
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("if waimian");
        //noinspection AccessStaticViaInstance
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK)
        {
            System.out.println("if limian");
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String keyword = matches.get(0);
            My_auto_Cmplt_Tv.setText(keyword);


            if (customExceptionHandler.checkSearchInformation(keyword)) {
                //add search actions
                if (!sqlDataBase.checkHistory(keyword)) {
                    sqlDataBase.addHistory(keyword);
                    ArrayAdapter<String> My_arr_adapter= new ArrayAdapter<>(getActivity(),R.layout.dropdown_layout,sqlDataBase.getHistory());
                    My_auto_Cmplt_Tv.setAdapter(My_arr_adapter);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("keyword", keyword);
                jsonObject.addProperty("start", 0);
                //noinspection AccessStaticViaInstance
                jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
                needTask = new NeedTask(jsonObject);
                needTask.execute();
                JsonParser parser = new JsonParser();
                //noinspection StatementWithEmptyBody
                while (jsonStr == null) {

                }
                System.out.println(jsonStr);
                JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
                jsonStr = null;
                int len = jsonArray.size();
                ItemAdapter.searchCount = len;
                //noinspection AccessStaticViaInstance
                buildAccount.getSearchArraylist().clear();
                for (int i = 0; i < len; i++) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    Item item = new Item();
                    item.setId(jsonObject1.get("id").getAsInt());
                    item.setName(jsonObject1.get("name").getAsString());
                    item.setDescription(jsonObject1.get("description").isJsonNull() ? null : jsonObject1.get("description").getAsString());
                    item.setNewDegree(jsonObject1.get("new_degree").getAsInt());
                    item.setPrice(jsonObject1.get("price").getAsDouble());
                    item.setDuration(jsonObject1.get("duration").getAsInt());
                    item.setDiscuss(jsonObject1.get("discuss").getAsBoolean());
                    item.setSecurityDeposit(jsonObject1.get("security_deposit").getAsDouble());
                    item.setDeadLine(jsonObject1.get("deadline").getAsInt());
                    item.setLongitude(jsonObject1.get("longitude").getAsDouble());
                    item.setLatitude(jsonObject1.get("latitude").getAsDouble());
                    item.setSharerID(jsonObject1.get("sharer_id").getAsInt());
                    item.setDistance(calculateDistance(item.getLatitude(), item.getLongitude(), getLocation()));

                    ArrayList<String> images = item.getImageArrayList();
                    JsonArray imagesJsonArray = jsonObject1.get("images").getAsJsonArray();
                    for (int j = 0; j < imagesJsonArray.size(); j++) {
                        images.add(imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                        Log.e("image:    ", imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
                    }
                    item.setImageArrayList(images);
                    //noinspection AccessStaticViaInstance
                    buildAccount.getSearchArraylist().add(item);


                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                //noinspection AccessStaticViaInstance
                if (buildAccount.getSearchArraylist().isEmpty()) {
                    listView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    //noinspection AccessStaticViaInstance
                    itemAdapter = new ItemAdapter(getActivity(), buildAccount.getSearchArraylist());
                    listView.setAdapter(itemAdapter);


                    if (ItemAdapter.searchCount >= 10 ) {
                        listView.setOnScrollListener(this);
                    } else {
                        listView.setOnScrollListener(null);
                    }
                }
                return;
            } else {
                Toast toast=Toast.makeText(getActivity(),"Please input your search item name", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.show();
                return;
            }




        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class NeedTask extends AsyncTask<Void, Void, Boolean> {
        private JsonObject jsonObject;

        NeedTask(JsonObject jsonObject ){
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REQUEST_SEARCH, jsonObject);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                jsonStr = (String) ois.readObject();
                System.out.println("SEARCH json string" + jsonStr);
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
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            listView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }

}
