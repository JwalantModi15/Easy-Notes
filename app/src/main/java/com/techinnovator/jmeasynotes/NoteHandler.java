package com.techinnovator.jmeasynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteHandler extends DatabaseHelper{
    public NoteHandler(Context context) {
        super(context);
    }

    public boolean create(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("description", note.getDesc());
        contentValues.put("date", note.getDate());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccess = db.insert("Note", null, contentValues)>0;
        db.close();
        return isSuccess;
    }

    public ArrayList<Note> readNotes(){
        ArrayList<Note> arr = new ArrayList ();
        String sqlQuery = "Select * from Note order by id ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if(cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String desc = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                Note note = new Note(title, desc, date);
                note.setId(id);
                arr.add(note);
            }
            while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return arr;
    }
    public Note readSingleNote(int id){
        String sqlQuery = "Select * from Note where id = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);
        Note note = null;
        if(cursor.moveToFirst()){
            int note_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String desc = cursor.getString(cursor.getColumnIndex("description"));
            String date = cursor.getString(cursor.getColumnIndex("date"));

            note = new Note(title, desc, date);
            note.setId(note_id);
        }
        cursor.close();
        db.close();
        return note;
    }

    public boolean update(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("description", note.getDesc());
        contentValues.put("date", note.getDate());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessFull = db.update("Note", contentValues, "id='"+note.getId()+"'",null)>0;
        db.close();
        return isSuccessFull;
    }

    public boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessFull = db.delete("Note","id='"+id+"'",null)>0;
        db.close();
        return isSuccessFull;
    }
}
