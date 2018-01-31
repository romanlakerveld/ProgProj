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

/**
 * Activity that combines different search options (searching with or without map).
 */
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
        // initially set map invisible
        mapFragment.getView().setVisibility(View.INVISIBLE);

        // Initialize views and set click listeners
        target          = findViewById(R.id.target);
        source          = findViewById(R.id.source);
        mapToggle       = findViewById(R.id.mapToggle);
        Button search   = findViewById(R.id.search);
        horizontalPicker= findViewById(R.id.picker);
        mapToggle.setOnClickListener(new OnClickHandler());
        search.setOnClickListener(new OnClickHandler());

        // asyncTask for getting all taxa and putting them in the auto-completes
        new GetTaxaFromDatabase().execute();

        // Uses stringrequest to get all supported interactions.
        GetInteractionTypes();
    }

    /**
     * Uses StringRequest to get all supported interactions from the GloBI API and puts them into
     * the horizontal picker widget.
     */
    public void GetInteractionTypes() {
        String url = "https://api.globalbioticinteractions.org/interactionTypes";

        // Open a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a stringRequest for getting possible interactions.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // make a new jsonObject from the response
                            JSONObject jsonObject = new JSONObject(response);

                            // initialize array of strings to put interaction types into
                            String[] interactions = new String[jsonObject.length()];

                            for (int i = 0; i < jsonObject.length(); i++) {
                                // add every interaction, putting a space in front of uppercase
                                interactions[i] = jsonObject.names().getString(i).replaceAll(
                                        String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
                            }
                            // set interaction types as values for horizontal picker
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

    /**
     * AsyncTask for getting all available taxa from the database.
     */
    class GetTaxaFromDatabase extends AsyncTask<String[], Integer, String[]> {

        /**
         * Opens a databaseAccess connecting and calls getAllTaxa to get the available taxa and
         * puts it into an array of strings.
         * @return String taxa contains all possible taxa.
         */
        @Override
        protected String[] doInBackground(String[]... strings) {
            // get all taxa from database
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SearchMashup.this);
            databaseAccess.open();
            String[] taxa = databaseAccess.getAllTaxa();
            return  taxa;
        }

        /**
         * Set adapters for the autocomplete texts using the array of string provided.
         * @param s contains the array of string from doInBackground
         */
        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SearchMashup.this);
            databaseAccess.close();
            // create adapter with all taxa and connect it to the autocomplete texts
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchMashup.this, android.R.layout.simple_list_item_1, s);
            source.setAdapter(adapter);
            target.setAdapter(adapter);
        }
    }

    /**
     * Handles button clicks
     */
    class OnClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mapToggle:
                    if (mapToggle.isChecked()) {
                        // if map is turned on, make it visible and remove current keyboard
                        mapFragment.getView().setVisibility(View.VISIBLE);
                        hideSoftKeyboard(SearchMashup.this, view);
                    }
                    else {
                        // if map is turned off, hide map
                        mapFragment.getView().setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.search:
                    // get all the values from the input fields
                    String interaction = horizontalPicker.getValues()[horizontalPicker.getSelectedItem()].toString().replaceAll(" ", "");
                    String targetText = target.getText().toString();
                    String sourceText = source.getText().toString();

                    // if not enough parameters were given raise a toast and return
                    if (targetText.equals("") && sourceText.equals("") && !mapToggle.isChecked()) {
                        Toast.makeText(SearchMashup.this, "Please enter at least a source or a target. Or try using the map to search.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // navigate to action results and put the parameters as extras
                    Intent intent = new Intent(SearchMashup.this, ActionResultsActivity.class);
                    intent  .putExtra("interaction", interaction)
                            .putExtra("target", targetText)
                            .putExtra("source", sourceText);

                    // if map is hidden, dont provide coordinates as extra
                    if (mapToggle.isChecked()) {
                        String coords = MapUtils.getMapCoords(mMap);
                        intent.putExtra("coords", coords);
                    }
                    startActivity(intent);
            }

        }
    }

    /**
     * Hides the keyboard when called
     */
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        // hides keyboard
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
    }
}
