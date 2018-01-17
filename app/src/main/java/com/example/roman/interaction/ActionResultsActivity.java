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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActionResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_results);



        final ListView listView = findViewById(R.id.actionList);

        Intent intent = getIntent();
        String interaction = intent.getStringExtra("interaction");
        String target = intent.getStringExtra("target");
        String source = intent.getStringExtra("source");
        String coords = intent.getStringExtra("coords");

        if (interaction == null) {
            interaction = "interactsWith";
        }

        final ArrayList<Interaction> interactions = new ArrayList<>();

        String url = "https://api.globalbioticinteractions.org/interaction?bbox=" + coords;
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority("api.globalbioticinteractions.org")
//                .appendPath("interaction")
//                .appendQueryParameter("interactionType", interaction)
//                .appendQueryParameter("targetTaxon", target)
//                .appendQueryParameter("sourceTaxon", source);
//        String url = builder.build().toString();
        url = url.replaceAll(" ", "%20");
        Log.d("URL", "onCreate: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray array = null;
                        try {
                            array = new JSONObject(response).getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONArray jsonArray = array.getJSONArray(i);
                                Interaction interaction1 = new Interaction();
                                interaction1.setSource(jsonArray.getString(1));
                                interaction1.setTarget(jsonArray.getString(7));
                                interaction1.setinteraction(jsonArray.getString(5));
                                interactions.add(interaction1);
                            }
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

    public class Interaction {
        private String source;
        private String target;
        private String interaction;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getinteraction() {
            return interaction;
        }

        public void setinteraction(String interaction) {
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
        private List<Interaction> data;

        public InteractionAdapter(Context context, int resource, List<Interaction> objects) {
            super(context, resource, objects);
            layoutResourceId = resource;
            data = objects;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

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
            holder.myinteraction.setText(data.get(position).getinteraction());
            return convertView;
        }
    }


}
