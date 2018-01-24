package com.example.roman.interaction;

import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpeciesSearchActivity extends AppCompatActivity {

    DatabaseAccess databaseAccess;
    AutoCompleteTextView editText;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_search);
        editText = findViewById(R.id.latin);
        textview = findViewById(R.id.common);
        Button button = findViewById(R.id.search);
        button.setOnClickListener(new OnClickListener());

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SpeciesSearchActivity.this);
        databaseAccess.open();
        String[] taxa = databaseAccess.getAllTaxa();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taxa);
        editText.setAdapter(adapter);
    }

    class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
}
