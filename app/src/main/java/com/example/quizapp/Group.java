package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Group extends AppCompatActivity implements View.OnClickListener {
    String a,g;
    TextView grpname;
    ImageView bck,send;
    MultiAutoCompleteTextView echat;

    FirebaseAuth mAuth;
    private DatabaseReference mdatabaseuser;
    private DatabaseReference mchat;
    private DatabaseReference mchatgrp;
    private RecyclerView mchatlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent i = getIntent();
        g = i.getStringExtra("grpname");
        a = i.getStringExtra("activity");

        mAuth = FirebaseAuth.getInstance();
        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");
        mchat = FirebaseDatabase.getInstance().getReference().child("Groups");
        mchatgrp = FirebaseDatabase.getInstance().getReference().child("Groups").child(g);

        mchatlist = (RecyclerView)findViewById(R.id.recycle3);
        mchatlist.setHasFixedSize(true);
        mchatlist.setLayoutManager(new LinearLayoutManager(this));

        grpname = (TextView)findViewById(R.id.grpid);
        bck = (ImageView)findViewById(R.id.imageViewback);
        send = (ImageView)findViewById(R.id.buttonsendc);
        echat = (MultiAutoCompleteTextView)findViewById(R.id.editchat);
        grpname.setText(g+"  Group");

        bck.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Comment,Chatholder> chatadapter = new FirebaseRecyclerAdapter<Comment, Chatholder>(
                Comment.class,
                R.layout.chat,
                Chatholder.class,
                mchatgrp
        ) {
            @Override
            protected void populateViewHolder(Chatholder viewHolder, Comment model, int position) {
                String user_id = mAuth.getCurrentUser().getUid();
                if(user_id.equals(model.getU_id())){
                    viewHolder.cview.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                    viewHolder.cview.findViewById(R.id.layout).setVisibility(View.GONE);
                    viewHolder.setcommentc1(model.getComment());
                    viewHolder.setdatec1(model.getDatec());
                    viewHolder.setuserc1(model.getUserc());
                }
                else{
                    viewHolder.cview.findViewById(R.id.layout1).setVisibility(View.GONE);
                    viewHolder.cview.findViewById(R.id.layout).setVisibility(View.VISIBLE);
                    viewHolder.setuserc(model.getUserc());
                    viewHolder.setdatec(model.getDatec());
                    viewHolder.setcommentc(model.getComment());
                }
            }
        };
        mchatlist.setAdapter(chatadapter);
    }

    public static class Chatholder extends RecyclerView.ViewHolder{

        View cview;
        public Chatholder(View itemView) {
            super(itemView);
            cview = itemView;
        }
        public void  setuserc(String name){
            TextView cname = (TextView)cview.findViewById(R.id.cuser);
            cname.setText(name);
        }

        public void setdatec(String date){
            TextView cdt = (TextView)cview.findViewById(R.id.cdate);
            cdt.setText(date);
        }
        public void setcommentc(String cmnt){
            TextView cmntc = (TextView)cview.findViewById(R.id.comment);
            cmntc.setText(cmnt);
        }
        public void  setuserc1(String name){
            TextView cname = (TextView)cview.findViewById(R.id.cuser1);
            cname.setText(name);
        }

        public void setdatec1(String date){
            TextView cdt = (TextView)cview.findViewById(R.id.cdate1);
            cdt.setText(date);
        }
        public void setcommentc1(String cmnt){
            TextView cmntc = (TextView)cview.findViewById(R.id.comment1);
            cmntc.setText(cmnt);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==bck){
            finish();
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(),"com.example.quizapp"+a);
            startActivity(intent);
        }
        if(v==send){
            String comment = echat.getText().toString();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm aa  dd-MM-yyyy");
            final String format1 = simpleDateFormat1.format(new Date());
            final DatabaseReference mpostc = mchat.child(g).push();
            String use_id = mAuth.getCurrentUser().getUid();
            mpostc.child("u_id").setValue(use_id);
            mpostc.child("comment").setValue(comment);
            mpostc.child("datec").setValue(format1);
            DatabaseReference usern = mdatabaseuser.child(mAuth.getCurrentUser().getUid()).child("User name");
            usern.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = String.valueOf(dataSnapshot.getValue());
                    mpostc.child("userc").setValue(name);
                    echat.getText().clear();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
