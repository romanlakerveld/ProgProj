package com.example.roman.interaction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Initialize buttons
        Button Species      = (Button) findViewById(R.id.Species);
        Button Interaction  = (Button) findViewById(R.id.Interaction);
        Button Area         = (Button) findViewById(R.id.Map);
        Button AreaAction   = (Button) findViewById(R.id.ActionArea);

        // Initialize click navigation handling
        Species.setOnClickListener(new OnNavigationClickListener());
        Interaction.setOnClickListener(new OnNavigationClickListener());
        Area.setOnClickListener(new OnNavigationClickListener());
        AreaAction.setOnClickListener(new OnNavigationClickListener());

    }

    class OnNavigationClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent;

            // Check which button has been pressed and navigate to the corresponding activity
            switch (view.getId()) {
                case R.id.Species:
                    intent = new Intent(OverviewActivity.this, SpeciesSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.Interaction:
                    intent = new Intent(OverviewActivity.this, ActionSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.Map:
                    intent = new Intent(OverviewActivity.this, AreaSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ActionArea:
                    intent = new Intent(OverviewActivity.this, ActionAreaActivity.class);
                    startActivity(intent);
            }

        }
    }

}
