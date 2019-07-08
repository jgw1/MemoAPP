package com.practice.mission1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.practice.mission1.MEMO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseOpenHelper openHelper;
    private static volatile DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void save(MEMO memo) {
        ContentValues values = new ContentValues();
        values.put("date", memo.getTime());
        values.put("memo", memo.getText());
        values.put("secret",memo.getSecret());
        database.insert(DatabaseOpenHelper.TABLE, null, values);
    }

    public void update(MEMO memo) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        values.put("secret",memo.getSecret());
        String date = Long.toString(memo.getTime());
        database.update(DatabaseOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(MEMO memo) {
        String date = Long.toString(memo.getTime());
        database.delete(DatabaseOpenHelper.TABLE, "date = ?", new String[]{date});
    }

    public List getAllMemos() {
        List memos = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY date DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(0);
            String text = cursor.getString(1);
            int secret = cursor.getInt(2);

            memos.add(new MEMO(time, text,secret));
            cursor.moveToNext();
        }
        cursor.close();
        return memos;
    }
    public List getOnlyGeneralMemos() {
        List memos = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY date DESC", null);
        cursor.moveToFirst();
        String text;
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(0);
            int secret = cursor.getInt(2);
            if (secret == 1)
                text = "";
            else text = cursor.getString(1);
            memos.add(new MEMO(time, text,secret));
            cursor.moveToNext();
        }
        cursor.close();
        return memos;
    }
}