package com.example.roman.interaction;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class AreaSearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button button = findViewById(R.id.search);
        button.setOnClickListener(new OnGoClickListener());

    }


    class OnGoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // Create new intent
            Intent intent = new Intent(AreaSearchActivity.this, ActionResultsActivity.class);

            //Get bounds of current screen. Northeastern corner and southwestern corner.
            LatLngBounds lngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            LatLng northEast = lngBounds.northeast;
            LatLng southWest = lngBounds.southwest;

            // Get coordinate strings from corners
            String north = String.valueOf(northEast.latitude);
            String east = String.valueOf(northEast.longitude);
            String south = String.valueOf(southWest.latitude);
            String west = String.valueOf(southWest.longitude);

            String[] array = {west, south, east, north};

            // Concatenate string to form easy extra in format: west, south, east, north
            String coords = west + "," + south + "," + east + "," + north;
            Log.d("COORDS", "onClick: " + coords);

            intent.putExtra("coords", coords);


            startActivity(intent);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.d("AREA", "onCameraMove:" + mMap.getProjection().getVisibleRegion().latLngBounds);
            }
        });
    }


}
