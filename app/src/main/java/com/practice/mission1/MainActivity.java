package com.practice.mission1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.practice.mission1.db.DatabaseAccess;
import com.practice.mission1.db.DatabaseOpenHelper;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseAccess databaseAccess;
    private List<MEMO> memos;
    private MEMO memo;
    private MemoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.databaseAccess = DatabaseAccess.getInstance(this);
        this.listView=(ListView) findViewById(R.id.listview);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                MEMO memo = memos.get(position);
                TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
                CheckBox checkBox = (CheckBox)(listView.getChildAt(position)).findViewById(R.id.checkBox);

                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);

                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });




        findViewById(R.id.WebPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"));
                startActivity(browser);
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_memo_register);
                Intent in = new Intent(MainActivity.this, MemoRegister.class);
                startActivity(in);
            }
        });
        findViewById(R.id.Secret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secret_show();
            }
        });
        findViewById(R.id.Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("선택한 파일들을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            onResume();
                            }
                        });
                builder.show();
            }
        });
        findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_show();
            }
        });

    }
    @Override
    protected void onResume(){
        super.onResume();
        databaseAccess.open();
        this.memos = databaseAccess.getAllMemos();
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, memos);
        adapter.notifyDataSetChanged();
        this.listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
        Intent in=new Intent(this,MainActivity.class);
        startActivity(in);
        finish();
    }

    void secret_show() {
        final EditText edittext = new EditText(this);
        Switch sw = (Switch) findViewById(R.id.Secret);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("비밀번호를 입력하세요");
                    builder.setView(edittext);
                    builder.setPositiveButton("입력",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
                                    String val = preference.getString("Password", null);
                                    String locker = null;
                                    if (val != null)

                                        try {
                                            locker = edittext.getText().toString();
                                            locker = encrypt(locker, "abcdefg12354545d");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                }
                            });
                    builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }
            }
        });
    }

    void Save_show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("선택한 파일들을 저장하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //선택한 파일들을 삭제
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
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

    public class MemoAdapter extends ArrayAdapter<MEMO> {

        public MemoAdapter(Context context, List<MEMO> objects) {
            super(context, 0, objects);
        }
        private class ViewHolder{
            CheckBox checkBox;
        }
        @Override
        public com.practice.mission1.MEMO getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.memo_list_view, null);

                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        CheckBox cb = (CheckBox) v;
                        Toast.makeText(getApplicationContext(),"CLicked on CheckBox: "+cb.getText() + "is" + cb.isChecked(),Toast.LENGTH_SHORT).show();
                        cb.setSelected(cb.isChecked());

                    }
                });
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);



            final MEMO memo = memos.get(position);

            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_memo_register);
                    Intent intent = new Intent(MainActivity.this, MemoRegister.class);
                    intent.putExtra("MEMO", memo);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}