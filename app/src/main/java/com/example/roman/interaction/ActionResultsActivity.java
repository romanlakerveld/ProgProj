package com.example.roman.interaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wefika.horizontalpicker.HorizontalPicker;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActionResultsActivity extends AppCompatActivity {
    ArrayList<Interaction> interactions;
    ListView listView;
    TextView infoView;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_results);
        setTitle("Interaction results");

        // initiate views
        listView = findViewById(R.id.actionList);
        infoView = findViewById(R.id.resultText);

        // initiate arrayList for interactions
        interactions = new ArrayList<>();

        // get a new requestQueue
        requestQueue = Volley.newRequestQueue(this);

        // call necessary functions
        String url = HandleParametersFromIntent(getIntent());
        GetInteractionsFromURL(url);
    }

    /**
     * Takes an intent and extracts the extras, next it build an url using those extras and returns
     * the url. Also changes the textview to display information on the search parameters.
     * @param intent intent from previous activity
     * @return url to be used for calling the API
     */
    public String HandleParametersFromIntent (Intent intent) {
        // get string extras from intent
        String interaction = intent.getStringExtra("interaction");
        String target = intent.getStringExtra("target");
        String source = intent.getStringExtra("source");
        String coordinates = intent.getStringExtra("coords");

        // build the url for API-request
        String url = "https://api.globalbioticinteractions.org/interaction?";

        // build url (if extras are not empty, add them to the parameters)
        if (!target.equals("")) {
            url += "targetTaxon=" + target.replaceAll("\\(.*?\\)","").trim() + "&";
        }
        if (!source.equals("")) {
            url += "sourceTaxon=" + source.replaceAll("\\(.*?\\)","").trim() + "&";
        }
        if (coordinates != null) {
            url += "bbox=" + coordinates + "&";
        }
        url += "interactionType=" + interaction;
        url = url.replaceAll(" ", "%20");

        // TODO: remove log
        Log.d("URL", "HandleParametersFromIntent: " + url);

        // change empty fields to "anything" for the sake of displaying search parameters
        if (source.equals("")) {
            source = "anything";
        }
        if (target.equals("")) {
            target = "anything";
        }

        // use appropriate string resource depending on whether coordinates is or isn't null
        String info;
        if (coordinates == null) {
            info = getResources().getString(R.string.results_info, source, interaction, target);
        }
        else {
            info = getResources().getString(R.string.results_info_map, source, interaction, target);
        }

        // set text of textview with html
        infoView.setText(Html.fromHtml(info));

        return url;
    }

    /**
     * Calls the API for interactions with the given url and subsequently puts them in the listview.
     * @param url url to be called by API
     */
    public void GetInteractionsFromURL(String url) {

        // Create new requestQueue for getting the interactions.
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // extract array with interactions from the response
                            JSONArray array = new JSONObject(response).getJSONArray("data");

                            // if array is empty, display a message to the user
                            if (array.length() == 0) infoView.setText(R.string.no_results_info);

                            // loop over interactions in array
                            for (int i = 0; i < array.length() && i < 30; i++) {
                                // get a single interaction
                                JSONArray jsonArray = array.getJSONArray(i);

                                // create new interaction class and set values
                                Interaction interaction = new Interaction(ActionResultsActivity.this);
                                interaction.setSource(jsonArray.getString(1));
                                interaction.setInteraction(jsonArray.getString(5));
                                interaction.setTarget(jsonArray.getString(7));

                                // add interaction to the list of interactions
                                interactions.add(interaction);
                            }

                            // create new adapter with the list and connect to the listview
                            InteractionAdapter adapter = new InteractionAdapter(ActionResultsActivity.this, android.R.layout.simple_list_item_1, interactions);
                            listView.setAdapter(adapter);
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
}
