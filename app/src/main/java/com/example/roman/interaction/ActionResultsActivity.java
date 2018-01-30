package com.example.roman.interaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_results);

        // Instantiate listView
        final ListView listView = findViewById(R.id.actionList);
        final TextView textView = findViewById(R.id.resultText);

        // Get intent and extract extras
        Intent intent = getIntent();
        String interactionValue = intent.getStringExtra("interaction");
        final String target = intent.getStringExtra("target");
        final String source = intent.getStringExtra("source");
        String coordinates = intent.getStringExtra("coords");

        // Check if interaction isn't null, else set it to a basic value
        if (interactionValue == null) {
            interactionValue = "interactsWith";
        }

        // Instantiate ArrayList for interactions
        interactions = new ArrayList<>();

        // Build the url for API-request
        String url = "https://api.globalbioticinteractions.org/interaction?";

        // Check if extras were null, if so dont add them to the API request.
        if (target != null){
            if (!target.equals("")) {
                url += "targetTaxon=" + target.replaceAll("\\(.*?\\)","").trim() + "&";
            }
        }
        if (source != null){
            if (!source.equals("")) {
                url += "sourceTaxon=" + source.replaceAll("\\(.*?\\)","").trim() + "&";
            }
        }
        if (coordinates != null) {
            url += "bbox=" + coordinates + "&";
        }

        // interactionType is never null so can simply be added
        url += "interactionType=" + interactionValue;

        // replace spaces with "%20"
        url = url.replaceAll(" ", "%20");
        Log.d("URL", "onCreate: " + url);

        // New requestqueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create new requestQueue for getting the interactions.
        final String finalInteractionValue = interactionValue;
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray array = null;
                        try {
                            // extract array with interactions from the response
                            array = new JSONObject(response).getJSONArray("data");

                            if (array.length() == 0) {
                                textView.setText("Unfortunately, no results for this search were found.");
                            }
                            else {
                                Resources resources = getResources();
                                String string = resources.getString(R.string.results_info, source, finalInteractionValue, target);
                                textView.setText(string);
                            }

                            // for every interaction in array
                            for (int i = 0; i < array.length() && i < 30; i++) {
                                // get a single interaction
                                JSONArray jsonArray = array.getJSONArray(i);

                                // create new interaction class and set values
                                Interaction interaction = new Interaction();
                                interaction.setSource(jsonArray.getString(1));
                                interaction.setTarget(jsonArray.getString(7));
                                interaction.setInteraction(jsonArray.getString(5));

                                // add interaction to the list of interactions
                                interactions.add(interaction);
                            }

                            // create new adapter with the list and connect to the listview
                            InteractionAdapter adapter = new InteractionAdapter(ActionResultsActivity.this, android.R.layout.simple_list_item_1, interactions);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new OnItemClicked());
                            listView.setOnItemLongClickListener(new OnItemLongClicked());
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

    public class Interaction {
        private String source;
        private String target;
        private String interaction;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ActionResultsActivity.this);
            databaseAccess.open();
            String common = databaseAccess.getCommon(source);
            databaseAccess.close();

            this.source = source + common;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ActionResultsActivity.this);
            databaseAccess.open();
            String common = databaseAccess.getCommon(target);
            databaseAccess.close();

            this.target = target + common;
        }

        public String getInteraction() {
            return interaction;
        }

        public void setInteraction(String interaction) {
            this.interaction = interaction;
        }

    }

    class ViewHolder {
        TextView mysource;
        TextView myinteraction;
        TextView mytarget;
    }

    public class InteractionAdapter extends ArrayAdapter<Interaction> {
        private int layoutResourceId;
        public List<Interaction> data;

        public InteractionAdapter(Context context, int resource, List<Interaction> objects) {
            super(context, resource, objects);
            layoutResourceId = resource;
            data = objects;
        }



        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            final ViewHolder holder;

            // TODO: comments
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.interaction_item, null);

                holder = new ViewHolder();

                holder.mysource = (TextView) convertView.findViewById(R.id.source);
                holder.mytarget = (TextView) convertView.findViewById(R.id.target);
                holder.myinteraction = (TextView) convertView.findViewById(R.id.interaction);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mysource.setText(data.get(position).getSource());
            holder.mytarget.setText(data.get(position).getTarget());
            holder.myinteraction.setText(data.get(position).getInteraction());
            return convertView;
        }




    }

    class OnItemClicked implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String source = interactions.get(i).getSource();

            Intent intent = new Intent(ActionResultsActivity.this, ResultsActivity.class);
            intent.putExtra("taxa", source);
            startActivity(intent);
        }
    }

    class OnItemLongClicked implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            String target = interactions.get(i).getTarget();

            Intent intent = new Intent(ActionResultsActivity.this, ResultsActivity.class);
            intent.putExtra("taxa", target);
            startActivity(intent);
            return true;
        }
    }


}
