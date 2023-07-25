package com.kee.vlmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class intro extends AppCompatActivity {
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(intro.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}