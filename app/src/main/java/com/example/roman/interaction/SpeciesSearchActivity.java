package com.example.roman.interaction;

import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpeciesSearchActivity extends AppCompatActivity {

    DatabaseAccess databaseAccess;
    EditText editText;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_search);
        editText = findViewById(R.id.latin);
        textview = findViewById(R.id.common);
        Button button = findViewById(R.id.search);
        button.setOnClickListener(new OnClickListener());
    }

    class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
}
