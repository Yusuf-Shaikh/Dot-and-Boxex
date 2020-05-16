package com.example.dotnboxex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class levelselector extends AppCompatActivity {
    private Button start;
    Vibrator vibrator;
    private EditText grid,players;
    boolean con1 = false, con2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselector);
        start = (Button) findViewById(R.id.startgame);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        grid = (EditText) findViewById(R.id.girdselector);
        players = (EditText) findViewById(R.id.players);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkplayers();
                checkgridsize();
                if(con1&&con2){
                    opengameactivity();
                }
            }
        });

    }
    public void checkplayers() {
        if (TextUtils.isEmpty(players.getText().toString())) {
            vibrator.vibrate(300);
            Toast.makeText(this, "Enter number of players", Toast.LENGTH_SHORT).show();
        } else {
            if ((Integer.parseInt(players.getText().toString()) > 1) && (Integer.parseInt(players.getText().toString()) < 4)) {
                saveplayers();
                con1 = true;
            } else {
                vibrator.vibrate(300);
                Toast.makeText(this, "select number of player between 2 and 3", Toast.LENGTH_SHORT).show();
        }
        }
    }
    public void checkgridsize() {
        if (TextUtils.isEmpty(grid.getText().toString())) {
            vibrator.vibrate(300);
            Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
        } else {
            if ((Integer.parseInt(grid.getText().toString()) > 1) && (Integer.parseInt(grid.getText().toString()) < 7)) {
                savegridsize();
                con2 = true;
            } else {
                vibrator.vibrate(300);
                Toast.makeText(this, "Enter a grid size between 2 and 6", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void savegridsize() {
        SharedPreferences sharedpreferences = getSharedPreferences("gridsize",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("gridsize",Integer.parseInt(grid.getText().toString()));
        editor.apply();
    }
    public void saveplayers() {
        SharedPreferences sharedpreferences = getSharedPreferences("players",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("players",Integer.parseInt(players.getText().toString()));
        editor.apply();
    }
    public void opengameactivity() {
        Intent gameintent = new Intent(levelselector.this,game.class);
        startActivity(gameintent);
    }
    @Override
    public void onBackPressed() {
    }
}
