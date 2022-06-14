package com.techinnovator.jmeasynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText etAddTitle, etAddDesc;
    Button btnAdd, btnCancel;
    LinearLayout btnHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setTitle("Add Note");
        etAddTitle = findViewById(R.id.etAddTitle);
        etAddDesc = findViewById(R.id.etAddDesc);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnHolder = findViewById(R.id.btnHolder);

        etAddDesc.requestFocus();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etAddTitle.getText().toString().equals("") || !etAddDesc.getText().toString().equals("")){
                    Date dateObj = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa, dd-MM-yyyy");
                    String date = simpleDateFormat.format(dateObj);
                    String title = etAddTitle.getText().toString();
                    Note note;

                    note = new Note(etAddTitle.getText().toString(), etAddDesc.getText().toString(), date);

                    boolean isInserted = new NoteHandler(AddNoteActivity.this).create(note);
                    if(isInserted){
                        Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                        returnToBackScreen();
                    }
                }
                else{
                    returnToBackScreen();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToBackScreen();
            }
        });
    }

    public void returnToBackScreen(){
            btnAdd.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!etAddTitle.getText().toString().equals("") || !etAddDesc.getText().toString().equals("")){
            Date dateObj = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa, dd-MM-yyyy");
            String date = simpleDateFormat.format(dateObj);
            String title = etAddTitle.getText().toString();
            Note note;
            if(title.equals("")){
                note = new Note("Untitled", etAddDesc.getText().toString(), date);
            }
            else{
                note = new Note(etAddTitle.getText().toString(), etAddDesc.getText().toString(), date);
            }
            boolean isInserted = new NoteHandler(AddNoteActivity.this).create(note);
            if(isInserted){
                Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                returnToBackScreen();
            }
        }
        else{
            returnToBackScreen();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!etAddTitle.getText().toString().equals("") || !etAddDesc.getText().toString().equals("")){
            Date dateObj = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa, dd-MM-yyyy");
            String date = simpleDateFormat.format(dateObj);
            String title = etAddTitle.getText().toString();
            Note note;
            if(title.equals("")){
                note = new Note("Untitled", etAddDesc.getText().toString(), date);
            }
            else{
                note = new Note(etAddTitle.getText().toString(), etAddDesc.getText().toString(), date);
            }
            boolean isInserted = new NoteHandler(AddNoteActivity.this).create(note);
            if(isInserted){
                Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                returnToBackScreen();
            }
        }
        else{
            returnToBackScreen();
        }
    }
}