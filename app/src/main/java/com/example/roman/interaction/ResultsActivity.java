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

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ResultsActivity.this);
        databaseAccess.open();
        String source = intent.getStringExtra("taxa");
        String[] ids = databaseAccess.getAllUrls(source);
        final ArrayList<String> convertedURl = new ArrayList<>();
        databaseAccess.close();
        final RequestQueue requestQueue = Volley.newRequestQueue(ResultsActivity.this);

        ListView urlList = findViewById(R.id.urlList);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, convertedURl);

        urlList.setAdapter(adapter);
        urlList.setOnItemClickListener(new OnUrlClicked());

        if (ids.length == 0) {
            Toast.makeText(ResultsActivity.this, "No data on this species was found",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        for (int i = 0; i < ids.length; i++) {
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
                        convertedURl.add(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    class OnUrlClicked implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String url = "https://api.globalbioticinteractions.org/findExternalUrlForExternalId/" + adapterView.getItemAtPosition(i).toString();


            Uri uriUrl = Uri.parse(adapterView.getItemAtPosition(i).toString());
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }

}
