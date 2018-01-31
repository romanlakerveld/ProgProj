package com.example.roman.interaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wefika.horizontalpicker.HorizontalPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMashup extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public AutoCompleteTextView target;
    public AutoCompleteTextView source;
    HorizontalPicker horizontalPicker;
    CheckBox mapToggle;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_maschup);
        setTitle("Explore the database");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);

        // Initialize views and set click listeners
        target          = findViewById(R.id.target);
        source          = findViewById(R.id.source);
        mapToggle       = findViewById(R.id.mapToggle);
        Button search   = findViewById(R.id.search);
        horizontalPicker= findViewById(R.id.picker);
        mapToggle.setOnClickListener(new OnClickHandler());
        search.setOnClickListener(new OnClickHandler());

        new GetTaxaFromDatabase().execute();

        // getting supported interaction types from API
        String url = "https://api.globalbioticinteractions.org/interactionTypes";

        // Open a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a stringRequest for getting possible interactions.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String[] interactions = new String[jsonObject.length()];
                            for (int i = 0; i < jsonObject.length(); i++) {
                                interactions[i] = jsonObject.names().getString(i).replaceAll(
                                        String.format("%s|%s|%s",
                                                "(?<=[A-Z])(?=[A-Z][a-z])",
                                                "(?<=[^A-Z])(?=[A-Z])",
                                                "(?<=[A-Za-z])(?=[^A-Za-z])"
                                        ),
                                        " "
                                );

                            }
                            horizontalPicker.setValues(interactions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    class GetTaxaFromDatabase extends AsyncTask<String[], Integer, String[]> {

        @Override
        protected String[] doInBackground(String[]... strings) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SearchMashup.this);
            databaseAccess.open();
            String[] taxa = databaseAccess.getAllTaxa();
            databaseAccess.close();
            return  taxa;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchMashup.this, android.R.layout.simple_list_item_1, s);

            source.setAdapter(adapter);
            target.setAdapter(adapter);
        }
    }

    class OnClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mapToggle:
                    if (mapToggle.isChecked()) {
                        mapFragment.getView().setVisibility(View.VISIBLE);
                        hideSoftKeyboard(SearchMashup.this, view);
                    }
                    else {
                        mapFragment.getView().setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.search:
                    String interaction = horizontalPicker.getValues()[horizontalPicker.getSelectedItem()].toString().replaceAll(" ", "");
                    String targetText = target.getText().toString();
                    String sourceText = source.getText().toString();

                    if (targetText.equals("") && sourceText.equals("") && !mapToggle.isChecked()) {
                        Toast.makeText(SearchMashup.this, "Please enter at least a source or a target. Or try using the map to search.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(SearchMashup.this, ActionResultsActivity.class);
                    intent  .putExtra("interaction", interaction)
                            .putExtra("target", targetText)
                            .putExtra("source", sourceText);
                    if (mapToggle.isChecked()) {
                        String coords = MapUtils.getMapCoords(mMap);
                        intent.putExtra("coords", coords);
                    }
                    startActivity(intent);
            }

        }
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
    }
}
