package com.practice.mission1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.practice.mission1.db.DatabaseAccess;

import java.security.MessageDigest;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseAccess databaseAccess;
    private List<MEMO> memos;
    private MEMO memo;
    private MemoAdapter adapter;
    private DownLoadFileTask task;
    //private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GoogleAnalyticsApplication application = (GoogleAnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();

        setContentView(R.layout.activity_main);
        this.databaseAccess = DatabaseAccess.getInstance(this);
        this.listView=(ListView) findViewById(R.id.listview);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object itemObject = adapterView.getAdapter().getItem(position);
                final MEMO memo = (MEMO)itemObject;
                final CheckBox itemCheckBox = (CheckBox)view.findViewById(R.id.checkBox);
                adapter.notifyDataSetChanged();
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
                                int size = memos.size();
                                for (int i=0;i<size;i++){
                                    MEMO memo = memos.get(i);

                                    if(memo.isChecked())
                                    {

                                        databaseAccess.open();
                                        databaseAccess.delete(memo);
                                        databaseAccess.close();

                                        memos.remove(i);
                                        i--;
                                        size=memos.size();
                                    }
                                }
                                ArrayAdapter<MEMO> adapter = (ArrayAdapter<MEMO>) listView.getAdapter();
                                adapter.notifyDataSetChanged();
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
        findViewById(R.id.kakaotalk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = memos.size();
                for (int i=0;i<size;i++){
                    MEMO memo = memos.get(i);
                    if(memo.isChecked()){
                        try {
                            final KakaoLink kakaoLink = (KakaoLink) KakaoLink.getKakaoLink(MainActivity.this);
                            final KakaoTalkLinkMessageBuilder kakaobuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                            kakaobuilder.addText("카카오링크 테스트");
                            kakaobuilder.addText(memo.getShortText());
                            kakaobuilder.addAppButton("앱 실행");
                            kakaoLink.sendMessage(kakaobuilder,MainActivity.this);

                        } catch (KakaoParameterException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        //mTracker.setScreenName(String.valueOf(MainActivity.this));
        databaseAccess.open();
        SharedPreferences secret = getSharedPreferences("b", MODE_PRIVATE);
        String  val = secret.getString("SecretMode", null);

        if(val.equals("1")){
            this.memos = databaseAccess.getAllMemos();
        }
        else{
            this.memos = databaseAccess.getOnlyGeneralMemos();
        }
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, memos,listView);
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
        Button sw = (Button) findViewById(R.id.Secret);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("비밀번호를 입력하세요");
                builder.setView(edittext);
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
                                String val = preference.getString("Password", null);
                                val = val.replaceAll("\n","");
                                val = val.trim();
                                if (val != null)
                                    try {
                                        String locker = edittext.getText().toString();
                                        locker = encrypt(locker, "abcdefg12354545d");
                                        locker = locker.replaceAll("\n","");
                                        locker = locker.trim();
                                        if(locker.equals(val)){
                                            SharedPreferences secret = getSharedPreferences("b", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = secret.edit();
                                            editor.putString("SecretMode", "1");
                                            editor.commit();
                                            onResume();
                                        }
                                        else{
                                        }
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
        });
    };
    void Save_show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("선택한 파일들을 저장하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int size = memos.size();
                        for (int i=0;i<size;i++){
                            final MEMO memo = memos.get(i);
                            if(memo.isChecked()){
                                task = new DownLoadFileTask(MainActivity.this);
                                task.execute(memo.getDate1(),memo.getShortText());
                            }
                        }

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
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
        public Context context;
        private List mList;
        private ListView mListView;

        public MemoAdapter(Context context,List<MEMO> list,ListView listview) {
            super(context, 0, list);
            this.context = context;
            this.mList = list;
            this.mListView = listview;

        }

        private class ViewHolder{
            public ImageView btnEdit;
            public TextView txtMemo;
            public TextView txtDate;
            public CheckBox checkBox;
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
                holder.btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);
                holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

                convertView.setTag(holder);

            }
            holder = (ViewHolder) convertView.getTag();

            final MEMO memo = memos.get(position);


            memo.setFullDisplayed(false);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            memo.setChecked(true);
                    }
                        else{
                            memo.setChecked(false);
                        }
                }
            });

            holder.txtMemo.setText(memo.getShortText());
            holder.txtDate.setText(memo.getDate());
            holder.checkBox.setChecked(memo.isChecked());
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
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