package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AppLock extends AppCompatActivity {
    EditText pwedit;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);

        int firstviewshow = preference.getInt("First", 0);
        if (firstviewshow == 0) {
            setContentView(R.layout.activity_app_lock);
            pwedit = findViewById(R.id.edit1);
            findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePassword();
                    setContentView(R.layout.activity_main);
                }
            });


        }

        if (firstviewshow == 1) {
            setContentView(R.layout.activity_main);

        }


    }

    private void savePassword() {
        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("Password", pwedit.getText().toString());
        editor.putInt("First", 1);
        editor.commit();
    }
}

