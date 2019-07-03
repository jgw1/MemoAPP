package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MemoRegister extends AppCompatActivity {
    EditText MEMO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_register);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Intent in = new Intent(MemoRegister.this, MainActivity.class);
                startActivity(in);
            }
        });

        findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Intent in = new Intent(MemoRegister.this, MainActivity.class);
                startActivity(in);
            }
        });
    }
    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
        Intent in=new Intent(MemoRegister.this,MainActivity.class);
        startActivity(in);
        finish();
    }

    private void registerMemo() {
        MEMO = findViewById(R.id.memo);
        String Memo = MEMO.getText().toString();

    }






}
