package com.techinnovator.jmeasynotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDesc;
    Button save;
    Button cancel;
    LinearLayout btnHolder;
    int id;
    Intent intent;
    String noteTitle;
    String noteDesc;
    boolean isTapTwice, isDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit Note");
        setContentView(R.layout.activity_edit_note);

        intent = getIntent();
        etTitle = findViewById(R.id.etEditTitle);
        etDesc = findViewById(R.id.etEditDesc);
        save = findViewById(R.id.btnSave);
        cancel = findViewById(R.id.btnCancel);
        btnHolder = findViewById(R.id.btnHolder);

        etDesc.requestFocus();
        noteTitle = intent.getStringExtra("title");
        noteDesc = intent.getStringExtra("desc");
        String noteDate = intent.getStringExtra("date");
        id = intent.getIntExtra("id", 1);

        if(noteTitle.equals("Untitled")){
            etTitle.setText("");
        }
        else{
            etTitle.setText(intent.getStringExtra("title"));
        }
        etDesc.setText(intent.getStringExtra("desc"));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Date dateObj = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa, dd-MM-yyyy");
                String date = simpleDateFormat.format(dateObj);
                Note note;

                Note n = new NoteHandler(EditNoteActivity.this).readSingleNote(id);
                noteTitle = n.getTitle();
                noteDesc = n.getDesc();

                if(etTitle.getText().toString().equals("") && etDesc.getText().toString().equals("")){
                    new NoteHandler(EditNoteActivity.this).delete(id);
                }
                else if(!etTitle.getText().toString().equals(noteTitle) || !etDesc.getText().toString().equals(noteDesc)) {
                    note = new Note(etTitle.getText().toString(), etDesc.getText().toString(), date);
                    note.setId(intent.getIntExtra("id", 1));

                    if (new NoteHandler(EditNoteActivity.this).update(note)) {
                        Toast.makeText(EditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(EditNoteActivity.this, "Failed Updating", Toast.LENGTH_SHORT).show();
                    }
//                    }

                }
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                finish();
            }
        });

        etDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDisable){
                    if(isTapTwice){
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if(inputMethodManager!=null){
                            inputMethodManager.showSoftInput(etDesc.getRootView(), InputMethodManager.SHOW_IMPLICIT);
                            etTitle.setFocusable(true);
                            etTitle.setFocusableInTouchMode(true);
                            etTitle.requestFocus();
                            etTitle.setCursorVisible(true);

                            etDesc.setFocusable(true);
                            etDesc.setFocusableInTouchMode(true);
                            etDesc.requestFocus();
                            etDesc.setCursorVisible(true);
                            isTapTwice = false;
                            isDisable = false;
                        }
                    }
                    else{
                        isTapTwice = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isTapTwice = false;
                            }
                        }, 170);

                    }
                }

            }
        });

    }

    public void saveNote(){
        Date dateObj = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa, dd-MM-yyyy");
        String date = simpleDateFormat.format(dateObj);
        Note note;
        Note n = new NoteHandler(EditNoteActivity.this).readSingleNote(id);
        noteTitle = n.getTitle();
        noteDesc = n.getDesc();

        if(etTitle.getText().toString().equals("") && etDesc.getText().toString().equals("")){
            new NoteHandler(EditNoteActivity.this).delete(id);
        }
        else if(!etTitle.getText().toString().equals(noteTitle) || !etDesc.getText().toString().equals(noteDesc)){

            note = new Note(etTitle.getText().toString(), etDesc.getText().toString(), date);
            note.setId(intent.getIntExtra("id", 1));

            if (new NoteHandler(EditNoteActivity.this).update(note)) {
                Toast.makeText(EditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
            } else {
//            Toast.makeText(EditNoteActivity.this, "Unable to save note", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.delete){
            new AlertDialog.Builder(EditNoteActivity.this).setTitle("Delete note").setMessage("Delete this note?").setIcon(R.drawable.ic_delete_black)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new NoteHandler(EditNoteActivity.this).delete(id);
                            Toast.makeText(EditNoteActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            save.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setCancelable(false).show();
        }
        else if(item.getItemId()==R.id.copy){
            String copyStr = etDesc.getText().toString();
            if(!copyStr.equals("")){
                saveNote();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if(clipboardManager!=null){
                    ClipData clipData = ClipData.newPlainText("key", copyStr);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(this, "First, enter some text!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.share){
            String shareStr = etDesc.getText().toString();
            if(!shareStr.equals("")) {
                saveNote();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareStr);
                startActivity(Intent.createChooser(intent, "Share"));
            }
            else{
                Toast.makeText(this, "First, enter some text!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.read){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager!=null){
                inputMethodManager.hideSoftInputFromWindow(etDesc.getApplicationWindowToken(), 0);
                etTitle.clearFocus();
                etDesc.clearFocus();
                etTitle.setCursorVisible(false);
                etTitle.setFocusable(false);
                etDesc.setCursorVisible(false);
                etDesc.setFocusable(false);
                etDesc.setClickable(true);
                isDisable = true;
                Toast.makeText(EditNoteActivity.this, "Tap twice to edit", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void returnToBackScreen(){
        saveNote();
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void onBackPressed() {
        returnToBackScreen();
    }
}