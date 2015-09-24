/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: MapsActivity
 *
 *  class properties:
 *  mMap:GoogleMap
 *  locationManager:LocationManager
 *  locationProvider:String
 *  markerOptions:MarkerOptions
 *  itemArrayList:ArrayList<Item>
 *  markerArrayList:ArrayList<Marker>
 *
 *  class methods:
 *  setUpMapIfNeeded():void
 *  setUpMap():void
 *  onMarkerClick(Marker marker):boolean
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.ui.ExploreFragment;
import com.example.ethan.shairversion1application.ui.ItemActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "unused"})
public class MapsActivity extends FragmentActivity implements ExploreFragment.OnFragmentInteractionListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private String locationProvider;
    private static MarkerOptions markerOptions;
    private ArrayList<Item> itemArrayList;
    private ArrayList<Marker> markerArrayList;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        markerArrayList = new ArrayList<>();
        //noinspection unchecked
        itemArrayList = (ArrayList<Item>) getIntent().getExtras().getSerializable("list");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Get Current Location
        Location myLocation = null;
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            Location mmyLocation = locationManager.getLastKnownLocation(provider);
            if (mmyLocation  != null) {
                myLocation = mmyLocation;
                locationProvider = provider;
            }

        }

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        assert myLocation != null;
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location


        for (Item item : itemArrayList) {
            markerOptions = new MarkerOptions().position(new LatLng(item.getLatitude(), item.getLongitude())).title(item.getName()).snippet(item.getDescription());
            Marker marker = mMap.addMarker(markerOptions);
            Log.e("marker name: ", marker.getTitle());
            markerArrayList.add(marker);
        }



        LatLng myCoordinates = new LatLng(latitude, longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
        mMap.animateCamera(yourLocation);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public void onFragmentInteraction(ArrayList<Item> arrayOfUsers) {
        //itemArrayList  = arrayOfUsers;
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public boolean onMarkerClick(Marker marker) {
        for (Item item : itemArrayList) {
            final Item newItem = item;
            if (item.getName().equals(marker.getTitle()))
                if (item.getDescription().equals(marker.getSnippet())) {
                    AlertDialog alertDialog = null;
                    alertDialog = new AlertDialog.Builder(this)
                            .setIcon(R.drawable.item_info)
                            .setTitle("Item details")
                            .setMessage("Do you want to check the details of the item?\n\n" + item.getName() + "\n" + item.getDescription())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("item", newItem);
                                    Intent intent = new Intent(MapsActivity.this, ItemActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    //noinspection RedundantCast
                                    ((AlertDialog)dialog).dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
        }
        return false;
    }
}
