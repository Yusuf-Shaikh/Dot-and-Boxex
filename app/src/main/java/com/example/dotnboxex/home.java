package com.example.dotnboxex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {
    private Button hstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hstart = (Button) findViewById(R.id.mainstart);
        hstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlevelactivity();
            }
        });
    }
    public void openlevelactivity() {
        Intent intent = new Intent(home.this,levelselector.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    }
}
