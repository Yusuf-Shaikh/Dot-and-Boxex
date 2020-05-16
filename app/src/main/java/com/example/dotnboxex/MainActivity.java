package com.example.dotnboxex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1500;
    ImageView imageview;
    TextView textview1,textview2;
    Animation top,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageview = (ImageView) findViewById(R.id.doteximage);
        textview1 = (TextView) findViewById(R.id.textViewbottom1);
        textview2 = (TextView) findViewById(R.id.textViewbottom2);

        top = AnimationUtils.loadAnimation(this,R.anim.top);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom);

        imageview.setAnimation(top);
        textview2.setAnimation(bottom);
        textview1.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,home.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
