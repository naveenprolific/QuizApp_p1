package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Lhist extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mrecycle;
    FirebaseAuth mauth;
    private DatabaseReference mdatabase;
    TextView tpc;
    ImageView hm,lg;
    private  Query mquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhist);

        mauth = FirebaseAuth.getInstance();

        mrecycle = (RecyclerView)findViewById(R.id.lhistid);
        mrecycle.setHasFixedSize(true);
        mrecycle.setLayoutManager(new LinearLayoutManager(this));

        hm = (ImageView)findViewById(R.id.imageViewhome);
        lg = (ImageView)findViewById(R.id.imageViewlogout);
        tpc = (TextView)findViewById(R.id.topic);
        hm.setOnClickListener(this);
        lg.setOnClickListener(this);


        Intent i = getIntent();
        String a = i.getStringExtra("activity");
        tpc.setText(a+" Leaderboard");

        //String user_id = mauth.getCurrentUser().getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(a);
        mquery = mdatabase.orderByChild("Order");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Rank,Rankholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Rank, Rankholder>(
                Rank.class,
                R.layout.rank_row,
                Rankholder.class,
                mquery
        ) {
            @Override
            protected void populateViewHolder(Rankholder viewHolder, Rank model, int position) {
                viewHolder.setRank(String.valueOf(position+1));
                viewHolder.setUsername(model.getUsername());
                viewHolder.setUserscore(model.getScore());
            }
        };
        mrecycle.setAdapter(firebaseRecyclerAdapter);
    }

    public static class Rankholder extends RecyclerView.ViewHolder{
        View mview;
        public Rankholder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setRank(String rank){
            TextView mrank = (TextView)mview.findViewById(R.id.rankid);
            mrank.setText(rank);
        }
        public void setUsername(String username){
            TextView musername = (TextView)mview.findViewById(R.id.usernameid);
            musername.setText(username);
        }
        public void setUserscore(int score){
            TextView mscore = (TextView)mview.findViewById(R.id.scoreid);
            mscore.setText(String.valueOf(score));
        }
    }

    @Override
    public void onClick(View v) {
        if(v==hm){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        if(v==lg){
            mauth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
