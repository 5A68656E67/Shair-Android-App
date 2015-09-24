/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ExploreFragment
 *
 *  class properties:
 *  arrayOfUsers:ArrayList<Item>
 *  adapter:ItemAdapter
 *  latLng:LatLng
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState):View
 *  onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount):void
 *  addExploreMapsListener(View view):void
 *  getLocation():LatLng
 *  calculateDistance(double latitute, double longitude, LatLng latLng):double
 *  roundOff(double x, int position):double
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.ItemAdapter;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.map.MapsActivity;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements AbsListView.OnScrollListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ItemAdapter adapter;
    private LatLng latLng;
    private String jsonStr;
    private ExploreTask exploreTask;
    private BuildAccount buildAccount;
    private boolean flag = false;
    private ListView listView;
    private Spinner spinner;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    @SuppressWarnings("unused")
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        buildAccount = new BuildAccount();
        latLng = getLocation();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("latitude", latLng.latitude);
        jsonObject.addProperty("longitude", latLng.longitude);
        //noinspection AccessStaticViaInstance
        jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
        jsonObject.addProperty("start", 0);
        exploreTask = new ExploreTask(jsonObject);
        exploreTask.execute();

        JsonParser parser = new JsonParser();
        //noinspection StatementWithEmptyBody
        while (jsonStr == null) {

        }
        Log.e("json string", jsonStr);
        JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
        jsonStr = null;
        int len = jsonArray.size();
        ItemAdapter.count = len;
        Log.e("item length: ", Integer.toString(ItemAdapter.count));
        //noinspection AccessStaticViaInstance
        buildAccount.getExploreArraylist().clear();
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
                Log.e("image:    " , imagesJsonArray.get(j).getAsJsonObject().get("path").getAsString());
            }
            item.setImageArrayList(images);
            //noinspection AccessStaticViaInstance
            buildAccount.getExploreArraylist().add(item);
            //noinspection AccessStaticViaInstance
            System.out.println("The current number of list: " + buildAccount.getExploreArraylist().size());


        }

        View view = inflater.inflate(R.layout.ui_explorefragment, container, false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        listView = (ListView) view.findViewById(R.id.explore_list);
        spinner = (Spinner) view.findViewById(R.id.spinner_filter);
        //noinspection AccessStaticViaInstance
        adapter = new ItemAdapter(getActivity(), buildAccount.getExploreArraylist());
        listView.setAdapter(adapter);
        if (ItemAdapter.count >= 10 ) {
            listView.setOnScrollListener(this);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Item item = (Item) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("type", 1);
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        addExploreMapsListener(view);
        addSpinnerListener();
        return view;
    }
    @SuppressWarnings("unused")
    public void onButtonPressed(ArrayList<Item> arrayOfUsers) {
        if (mListener != null) {
            mListener.onFragmentInteraction(arrayOfUsers);
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
                        jsonObject.addProperty("latitude", latLng.latitude);
                        jsonObject.addProperty("longitude", latLng.longitude);
                        //noinspection AccessStaticViaInstance
                        jsonObject.addProperty("user_id", buildAccount.getAccount().getUser().getId());
                        jsonObject.addProperty("start", ItemAdapter.count);
                        exploreTask = new ExploreTask(jsonObject);
                        exploreTask.execute();

                        JsonParser parser = new JsonParser();
                        //noinspection StatementWithEmptyBody
                        while (jsonStr == null) {

                        }
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
                        ItemAdapter.count += len;
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
                            item.setDistance(calculateDistance(item.getLatitude(), item.getLongitude(), getLocation()));

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
                        buildAccount.getExploreArraylist().addAll(newArraylist);
                        //noinspection AccessStaticViaInstance
                        System.out.println("The current number of list: " + buildAccount.getExploreArraylist().size());

                    }

                }
            });
            adapter.notifyDataSetChanged();
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
        public void onFragmentInteraction(ArrayList<Item> arrayOfUsers);
    }

    public void addExploreMapsListener(View view) {
        final Button button = (Button) view.findViewById(R.id.item_map);
        //noinspection AccessStaticViaInstance
        System.out.println("before send the list:" + buildAccount.getExploreArraylist().size());
        //noinspection AccessStaticViaInstance
        final ArrayList<Item> newArraylist = buildAccount.getExploreArraylist();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.onFragmentInteraction(arrayOfUsers);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", newArraylist);
                System.out.println("before send the list:"+newArraylist.size());
//                Log.i("size of array", Integer.toString(arrayOfUsers.size()));
//                Log.i("latitute", Double.toString(arrayOfUsers.get(arrayOfUsers.size() - 1).getLatitude()));
//                Log.i("name: " , arrayOfUsers.get(arrayOfUsers.size() - 1).getName());
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void addSpinnerListener() {
      //  if (!buildAccount.getExploreArraylist().isEmpty()) {
           // System.out.println("The first item name of a list:          " + buildAccount.getExploreArraylist().get(0).getName());
     //   }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("you selected:        " + parent.getItemIdAtPosition(position));
                BuildAccount newBuildAccount = new BuildAccount();
                //noinspection AccessStaticViaInstance
        //        System.out.println("The first item name of a list:          " + newBuildAccount.getExploreArraylist().get(0).getName());
                switch (Long.toString(parent.getItemIdAtPosition(position))) {
                    case "1":
                        //noinspection AccessStaticViaInstance
                        Collections.sort(newBuildAccount.getExploreArraylist(), new Comparator<Item>() {
                            @Override
                            public int compare(Item item1, Item item2) {
                                Double price1 = item1.getPrice() / item1.getDuration();
                                Double price2 = item2.getPrice() / item2.getDuration();
                                if (price1 >= price2) {
                                    System.out.println("big");
                                    return -1;

                                } else {
                                    System.out.println("small");
                                    return 1;
                                }
                            }
                        });
                        System.out.println("you entered 0");
                        //noinspection AccessStaticViaInstance
                        if (!newBuildAccount.getExploreArraylist().isEmpty()) {
                            //noinspection AccessStaticViaInstance
                            System.out.println("The first item name of a list:          " + newBuildAccount.getExploreArraylist().get(0).getName());
                        }
                        //noinspection AccessStaticViaInstance
                        adapter = new ItemAdapter(getActivity(), newBuildAccount.getExploreArraylist());
                        listView.setAdapter(adapter);
                        break;
                    case "2":
                        //noinspection AccessStaticViaInstance
                        Collections.sort(newBuildAccount.getExploreArraylist(), new Comparator<Item>() {
                            @Override
                            public int compare(Item item1, Item item2) {
                                Double price1 = item1.getPrice() / item1.getDuration();
                                Double price2 = item2.getPrice() / item2.getDuration();
                                if (price1 >= price2) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        System.out.println("you entered 1");
                        //noinspection AccessStaticViaInstance
                        if (!newBuildAccount.getExploreArraylist().isEmpty()) {
                            //noinspection AccessStaticViaInstance
                            System.out.println("The first item name of a list:          " + newBuildAccount.getExploreArraylist().get(0).getName());
                        }
                     //   System.out.println("The first item name of a list:          " + buildAccount.getExploreArraylist().get(0).getName());
                        //noinspection AccessStaticViaInstance
                        adapter = new ItemAdapter(getActivity(), newBuildAccount.getExploreArraylist());
                        listView.setAdapter(adapter);
                        break;
                    case "3":
                        //noinspection AccessStaticViaInstance
                        Collections.sort(newBuildAccount.getExploreArraylist(), new Comparator<Item>() {
                            @Override
                            public int compare(Item item1, Item item2) {
                                Double distance1 = item1.getDistance();
                                Double distance2 = item2.getDistance();
                                if (distance1 >= distance2) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        //noinspection AccessStaticViaInstance
                        if (!newBuildAccount.getExploreArraylist().isEmpty()) {
                            //noinspection AccessStaticViaInstance
                            System.out.println("The first item name of a list:          " + newBuildAccount.getExploreArraylist().get(0).getName());
                        }
                        System.out.println("you entered 2");
                    //    System.out.println("The first item name of a list:          " + buildAccount.getExploreArraylist().get(0).getName());
                        //noinspection AccessStaticViaInstance
                        adapter = new ItemAdapter(getActivity(), newBuildAccount.getExploreArraylist());
                        listView.setAdapter(adapter);
                        break;
                    case "4":
                        //noinspection AccessStaticViaInstance
                        Collections.sort(newBuildAccount.getExploreArraylist(), new Comparator<Item>() {
                            @Override
                            public int compare(Item item1, Item item2) {
                                Double distance1 = item1.getDistance();
                                Double distance2 = item2.getDistance();
                                if (distance1 >= distance2) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        });
                        //noinspection AccessStaticViaInstance
                        if (!newBuildAccount.getExploreArraylist().isEmpty()) {
                            //noinspection AccessStaticViaInstance
                            System.out.println("The first item name of a list:          " + newBuildAccount.getExploreArraylist().get(0).getName());
                        }
                        System.out.println("you entered 3");
                    //    System.out.println("The first item name of a list:          " + buildAccount.getExploreArraylist().get(0).getName());
                        //noinspection AccessStaticViaInstance
                        adapter = new ItemAdapter(getActivity(), newBuildAccount.getExploreArraylist());
                        listView.setAdapter(adapter);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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







    public class ExploreTask extends AsyncTask<Void, Void, Boolean> {
        private JsonObject jsonObject;

        ExploreTask(JsonObject jsonObject ){
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                DefaultSocketClient client = new DefaultSocketClient(QueryType.REQUEST_NEARBY, jsonObject);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                jsonStr = (String) ois.readObject();
                System.out.println("hot fucking json string" + jsonStr);
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
