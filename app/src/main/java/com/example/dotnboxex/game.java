package com.example.dotnboxex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class game extends AppCompatActivity {
    private Button home ,undo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlevelactivity();
            }
        });

    }
    public void openlevelactivity() {
        Intent quizintent = new Intent(game.this,home.class);
        startActivity(quizintent);
    }

    @Override
    public void onBackPressed() {
    }

}
