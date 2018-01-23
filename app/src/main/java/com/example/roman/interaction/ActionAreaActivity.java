package com.example.roman.interaction;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActionAreaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText source;
    private EditText target;
    private Spinner interaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_area);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button button = findViewById(R.id.search);

        source = findViewById(R.id.source);
        target = findViewById(R.id.target);
        interaction = findViewById(R.id.interaction);

        button.setOnClickListener(new OnSearchClickListener());

        // instantiate list of possible interactions
        final ArrayList<String> interactionList = new ArrayList<>();

        // getting suppoorted interaction types from API
        String url = "https://api.globalbioticinteractions.org/interactionTypes";

        // Open a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ActionAreaActivity.this);

        // Create a stringRequest for getting possible interactions. TODO: maybe put this in seperate function.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            for (int i = 0; i < jsonObject.length(); i++) {
                                // put every interaction in list and convert camelCase to readable text
                                interactionList.add(jsonObject.names().getString(i).replaceAll(
                                        String.format("%s|%s|%s",
                                                "(?<=[A-Z])(?=[A-Z][a-z])",
                                                "(?<=[^A-Z])(?=[A-Z])",
                                                "(?<=[A-Za-z])(?=[^A-Za-z])"
                                        ),
                                        " "
                                ));
                            }
                            // Instantiate new adapter
                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<String>(ActionAreaActivity.this, android.R.layout.simple_list_item_1, interactionList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Set adapter for spinner
                            interaction.setAdapter(adapter);
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

    class OnSearchClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String interactionValue = interaction.getSelectedItem().toString().replaceAll(" ", "");
            String targetText = target.getText().toString();
            String sourceText = source.getText().toString();

            // Use getMapCoords function to get the coordinates of the map
            String coords = MapUtils.getMapCoords(mMap);


            // Create new intent with extras and start the new Activity
            Intent intent = new Intent(ActionAreaActivity.this, ActionResultsActivity.class);
            intent  .putExtra("coords", coords)
                    .putExtra("interaction", interactionValue)
                    .putExtra("target", targetText)
                    .putExtra("source", sourceText);
            startActivity(intent);
        }
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
