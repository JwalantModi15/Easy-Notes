package com.techinnovator.jmeasynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ImageView imgLogo;
    TextView txtAddNote;
    LocalDate date;
    ActionMode actionMode;
    View v;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Notes");
        imageButton = findViewById(R.id.imgAdd);
        imgLogo = findViewById(R.id.imgLogo);
        txtAddNote = findViewById(R.id.txtAddNote);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), 10);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                noteAdapter.notifyDataSetChanged();
                if(notes.size()>0){
                    imgLogo.setVisibility(View.GONE);
                    txtAddNote.setVisibility(View.GONE);
                }
                else if(notes.size()==0){
                    imgLogo.setVisibility(View.VISIBLE);
                    txtAddNote.setVisibility(View.VISIBLE);
                }
                Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadNotes();
    }
    public ArrayList<Note> readNotes(){
        ArrayList<Note> arr = new NoteHandler(this).readNotes();
        return arr;
    }
    public void loadNotes(){
        notes = readNotes();
        if(notes.size()>0){
            imgLogo.setVisibility(View.GONE);
            txtAddNote.setVisibility(View.GONE);
        }
        else if(notes.size()==0){
            imgLogo.setVisibility(View.VISIBLE);
            txtAddNote.setVisibility(View.VISIBLE);
        }
        noteAdapter = new NoteAdapter(this, notes, new NoteAdapter.ItemClicked(){

            @Override
            public void onClick(int pos, View view) {
                editNote(notes.get(pos).getId(), view);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClickForDel(int pos, View view) {
                if(actionMode!=null){
                    return;
                }
                actionMode = startSupportActionMode(callback);
                v = view;
                position = pos;
                v.setBackgroundResource(0);
                view.setBackgroundResource(R.drawable.card_background_main);
                view.setElevation(9);
            }
        });
        recyclerView.setAdapter(noteAdapter);
    }
    public ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.notes_options, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if(itemId==R.id.deleteNote){
                new AlertDialog.Builder(MainActivity.this).setTitle("Delete note").setMessage("Delete this note?").setIcon(R.drawable.ic_delete_black)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new NoteHandler(MainActivity.this).delete(notes.get(position).getId());
                        notes.remove(position);
                        noteAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        actionMode.finish();
                        if(notes.size()>0){
                            imgLogo.setVisibility(View.GONE);
                            txtAddNote.setVisibility(View.GONE);
                        }
                        else if(notes.size()==0){
                            imgLogo.setVisibility(View.VISIBLE);
                            txtAddNote.setVisibility(View.VISIBLE);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        actionMode.finish();
                    }
                }).setCancelable(false).show();

            }
            else if(itemId == R.id.copyNote){
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if(clipboardManager!=null){
                    ClipData clipData = ClipData.newPlainText("main_key", notes.get(position).getDesc());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
            }
            else if(itemId == R.id.shareNote){
                String shareStr = notes.get(position).getDesc();
                if(!shareStr.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, shareStr);
                    startActivityForResult(Intent.createChooser(intent, "Share"), 50);
                }
            }
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            v.setBackgroundResource(R.drawable.card_background);
            v.setElevation(9);
        }
    };
    public void editNote(int noteId, View view){
        Note note = new NoteHandler(this).readSingleNote(noteId);

        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("desc", note.getDesc());
        intent.putExtra("id", note.getId());
        intent.putExtra("date", note.getDate());

//        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId==R.id.giveRate){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.techinnovator.jmeasynotes"));
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://privacypolicyeasynotesapp.blogspot.com/2021/11/privacy-policy-easy-notes-notes-and.html"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            loadNotes();
            if(actionMode!=null){
                actionMode.finish();
                actionMode = null;
                v.setBackgroundResource(R.drawable.card_background);
                v.setElevation(9);
            }
        }
        else if(requestCode==10){
            loadNotes();
        }
        else if(requestCode==50){
            actionMode.finish();
        }
    }
}