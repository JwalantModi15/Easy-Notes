package com.techinnovator.jmeasynotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int Version = 2;
    private static final String Name = "NoteDatabase";

    public DatabaseHelper(Context context) {
        super(context, Name, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "Create Table Note (id Integer Primary Key AutoIncrement, title Text, description Text, date Text)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "Drop Table If Exists Note";
        db.execSQL(sqlQuery);
        onCreate(db);
    }
}
