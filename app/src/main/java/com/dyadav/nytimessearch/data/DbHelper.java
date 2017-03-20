package com.dyadav.nytimessearch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NYTIMESSEARCH_DB";
    public static final String TABLE_NAME = "bookmark";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_HEADLINE = "headline";
    public static final String COLUMN_SNIPPET = "snippet";
    public static final String COLUMN_PUBDATE = "pubdate";
    public static final String COLUMN_IMAGE = "image";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TODO_TABLE  = "create table "
                + TABLE_NAME + "('_id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_URL + " TEXT NOT NULL, "
                + COLUMN_HEADLINE + " TEXT NOT NULL, "
                + COLUMN_SNIPPET + " TEXT NOT NULL, "
                + COLUMN_PUBDATE + " TEXT NOT NULL, "
                + COLUMN_IMAGE + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
