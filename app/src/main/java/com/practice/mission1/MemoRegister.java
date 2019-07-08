package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.mission1.db.DatabaseAccess;

public class MemoRegister extends AppCompatActivity {
    private EditText editMemo;
    private MEMO memo;
    private Button save;
    private DatabaseAccess databaseAccess;
    private CheckBox secret_checkBox;
    int secret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_register);
        this.databaseAccess = DatabaseAccess.getInstance(this);
        this.save = (Button) findViewById(R.id.Save);
        this.editMemo = (EditText) findViewById(R.id.editMemo);
        this.secret_checkBox = (CheckBox) findViewById(R.id.secret_checkBox);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (MEMO) bundle.get("MEMO");
            if(memo != null) {
                this.editMemo.setText(memo.getText());
                this.secret_checkBox.setChecked(false);
            }
        }

        secret_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    secret = 1;
                }
                else
                    secret = 0;
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Intent in = new Intent(MemoRegister.this, MainActivity.class);
                startActivity(in);
            }
        });

        this.save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                databaseAccess.open();
                MEMO temp = new MEMO();
                if(memo == null) {
                    temp.setText(editMemo.getText().toString());
                    temp.setSecret(secret);
                    databaseAccess.save(temp);
                } else {
                    // Update the memo
                    memo.setText(editMemo.getText().toString());
                    temp.setSecret(secret);
                    databaseAccess.update(memo);
                }
                databaseAccess.close();
                Toast.makeText(getApplicationContext(),"저장이 되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
                goToMain();
            }
        });
        findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });
    }
    @Override
    public void onBackPressed(){
        goToMain();
        finish();
    }

    public void goToMain(){
        setContentView(R.layout.activity_main);
        Intent in = new Intent(MemoRegister.this, MainActivity.class);
        startActivity(in);
        finish();
    }
}