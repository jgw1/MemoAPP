package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AppLock extends AppCompatActivity {
    EditText pwedit;
    String abs;
    String password = "asdfgasldgasdg";
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
                    Intent in = new Intent(AppLock.this,MainActivity.class);
                    in.putExtra("Password",pwedit.getText().toString());
                    startActivity(in);
                }
            });
        }

        if (firstviewshow == 1) {
            setContentView(R.layout.activity_main);
            Intent in = new Intent(AppLock.this,MainActivity.class);
            startActivity(in);
        }
    }

    private void savePassword() {
        String password = "abcdefg12354545d";
        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        try {
            abs = encrypt(pwedit.getText().toString(), password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString("Password", abs);
        editor.putInt("First", 1);
        editor.commit();
    }
    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
    private String encrypt(String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }
}

