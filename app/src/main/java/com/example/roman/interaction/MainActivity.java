package com.example.roman.interaction;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView species;
    TextInputLayout til;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button lookup = findViewById(R.id.lookup);
        Button explore = findViewById(R.id.explore);
        lookup.setOnClickListener(new OnLookupClickListener());
        explore.setOnClickListener(new OnExploreListener());

        species = findViewById(R.id.species);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
        databaseAccess.open();
        String[] taxa = databaseAccess.getAllTaxa();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taxa);
        species.setAdapter(adapter);


    }

    class OnExploreListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lookup:
                    Intent intent = new Intent(MainActivity.this, SpeciesSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.explore:
                    Intent intent1 = new Intent(MainActivity.this, SearchMaschup.class);
                    startActivity(intent1);
                    //TODO
            }
        }
    }

    class OnLookupClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (species.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "No species specified.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("taxa", species.getText().toString());
            startActivity(intent);
        }
    }
}