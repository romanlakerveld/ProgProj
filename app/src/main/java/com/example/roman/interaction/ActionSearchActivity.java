package com.example.roman.interaction;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActionSearchActivity extends AppCompatActivity {
    public Spinner spinner;
    public TextView target;
    public TextView source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_search);

        // Initialize views
        target = findViewById(R.id.target);
        source = findViewById(R.id.source);
        spinner = findViewById(R.id.spinner);

        // initialize button and set listener
        Button button = findViewById(R.id.ActionSearch);
        button.setOnClickListener(new OnSearchClickListener());

        // instantiate list of possible interactions
        final ArrayList<String> interactionList = new ArrayList<>();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ActionSearchActivity.this);
        databaseAccess.open();
        String[] taxa = databaseAccess.getAllTaxa();
        databaseAccess.close();

        // getting suppoorted interaction types from API
        String url = "https://api.globalbioticinteractions.org/interactionTypes";

        // Open a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ActionSearchActivity.this);

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
                                    new ArrayAdapter<String>(ActionSearchActivity.this, android.R.layout.simple_list_item_1, interactionList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Set adapter for spinner
                            spinner.setAdapter(adapter);
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
            // extract strings from sources
            String interaction = spinner.getSelectedItem().toString().replaceAll(" ", "");
            String targetText = target.getText().toString();
            String sourceText = source.getText().toString();

            // Create new intent with extras and start the new Activity
            Intent intent = new Intent(ActionSearchActivity.this, ActionResultsActivity.class);
            intent  .putExtra("interaction", interaction)
                    .putExtra("target", targetText)
                    .putExtra("source", sourceText);
            startActivity(intent);
        }
    }
}