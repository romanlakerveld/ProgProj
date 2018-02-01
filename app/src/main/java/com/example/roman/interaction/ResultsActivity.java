package com.example.roman.interaction;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    ArrayList<String> convertedURl;
    RequestQueue requestQueue;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // initiate views
        ListView urlList  = findViewById(R.id.urlList);
        TextView textView = findViewById(R.id.resulttext);

        // get source extra from intent
        String species = getIntent().getStringExtra("taxa");

        requestQueue = Volley.newRequestQueue(ResultsActivity.this);

        String[] ids = getIDsOfSpecies(species);

        // if ids is empty, notify the user and return to previous activity
        if (ids.length == 0) {
            Toast.makeText(ResultsActivity.this, "No data on this species was found",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        // if ids is not empty, display info on results
        else {
            String info = getResources().getString(R.string.url_result, ids.length, species);
            textView.setText(info);
        }

        // create arrayList to fill with urls and set adapter
        convertedURl = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, convertedURl);
        urlList.setAdapter(adapter);
        urlList.setOnItemClickListener(new OnUrlClicked());

        convertIDsToURLs(ids);
    }

    /**
     * Converts a species String to relevant IDs
     * @param species species to be converted
     * @return array of IDs
     */
    public String[] getIDsOfSpecies(String species) {
        // get ids from database
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ResultsActivity.this);
        databaseAccess.open();
        String[] ids = databaseAccess.getAllUrls(species);
        databaseAccess.close();

        return ids;
    }

    /**
     * Converts IDs to URLs and updates the listview when a new URL has been added
     * @param ids ids to be converted
     */
    public void convertIDsToURLs(String[] ids) {
        // loop over ids
        for (int i = 0; i < ids.length; i++) {
            // string for api request
            String url = "https://api.globalbioticinteractions.org/findExternalUrlForExternalId/" + ids[i];
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        // add url to data
                        convertedURl.add(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // notify that a new url has been added
                    adapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest);
        }
    }


    /**
     * Handles URL clicks
     */
    class OnUrlClicked implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // launch browser with the URL
            Uri uriUrl = Uri.parse(adapterView.getItemAtPosition(i).toString());
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }

}
