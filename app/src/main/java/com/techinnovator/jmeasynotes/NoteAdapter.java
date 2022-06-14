package com.techinnovator.jmeasynotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    @NonNull
    Context context;
    ArrayList<Note> notes;
    ViewGroup parent;
    ItemClicked itemClicked;

    public NoteAdapter(@NonNull Context context, ArrayList<Note> note, ItemClicked itemClicked) {
        this.context = context;
        this.notes = note;
        this.itemClicked = itemClicked;
    }

    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_header, parent, false);
        this.parent = parent;
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        String title = note.getTitle();
        if(title.equals("")){
            holder.txtTitle.setText("Untitled");
        }
        else{
            holder.txtTitle.setText(notes.get(position).getTitle());
        }

        holder.txtDesc.setText(notes.get(position).getDesc());
        holder.txtDate.setText(notes.get(position).getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if(holder.txtDesc.isSingleLine()){
                        holder.txtDesc.setSingleLine(false);
                        System.out.println("max");
                    }
                    else{
                        holder.txtDesc.setSingleLine(true);
                        System.out.println("1");
                    }
                    if(holder.txtTitle.isSingleLine()){
                        holder.txtTitle.setSingleLine(false);
                        System.out.println("max");
                    }
                    else{
                        holder.txtTitle.setSingleLine(true);
                        System.out.println("1");
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
                else{
                    if(holder.txtDesc.getMaxLines()==1){
                        holder.txtDesc.setSingleLine(false);
                        System.out.println("max");
                    }
                    else{
                        holder.txtDesc.setSingleLine(true);
                        System.out.println("1");
                    }
                    if(holder.txtTitle.getMaxLines()==1){
                        holder.txtTitle.setSingleLine(false);
                        System.out.println("max");
                    }
                    else{
                        holder.txtTitle.setSingleLine(true);
                        System.out.println("1");
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtDesc;
        TextView txtDate;
        ImageView imgEdit;
        CardView cardView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            txtDate = itemView.findViewById(R.id.txtDateTime);
            cardView = itemView.findViewById(R.id.cardView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.Q)
//                @Override
//                public void onClick(View v) {

//                    if(Build.VERSION.SDK_INT<28){
//                        if(txtDesc.getMaxLines()==1){
//                            txtDesc.setMaxLines(Integer.MAX_VALUE);
//                            System.out.println("max desc <");
//                        }
//                        else{
//                            txtDesc.setMaxLines(1);
//                            System.out.println("1 desc <");
//                        }
//                        if(txtTitle.getMaxLines()==1){
//                            txtTitle.setMaxLines(Integer.MAX_VALUE);
//                            System.out.println("max title <");
//                        }
//                        else{
//                            txtTitle.setMaxLines(1);
//                            System.out.println("1 title <");
//                        }
//                    }

//                    else{

//                    }


//                }
//            });
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onClick(getAdapterPosition(), itemView);
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    itemClicked.onClick(getAdapterPosition(), itemView);
                    itemClicked.onClickForDel(getAdapterPosition(), v);
                    return true;
                }
            });
        }
    }
    interface ItemClicked{
        void onClick(int pos, View view);
        void onClickForDel(int pos, View view);
    }
}
