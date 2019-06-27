package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private static int time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        new Handler().postDelayed(new Runnable() {
            @Override
            public  void run() {
                Intent in=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(in);

                finish();

            }
        },time);
    }
}