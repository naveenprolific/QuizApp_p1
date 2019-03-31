package com.example.quizapp;

import android.content.Intent;
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

public class Comment_page extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mcmntdb;
    private RecyclerView mcommentlist;
    String a;
    String key ;
    ImageView bck,send;
    MultiAutoCompleteTextView cmnt;
    FirebaseAuth mAuth;
    private DatabaseReference mdatabaseuser;
    private DatabaseReference mcomments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);

        Intent i = getIntent();
        key = i.getStringExtra("post_key");
        a = i.getStringExtra("activity");

        mAuth = FirebaseAuth.getInstance();
        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");
        mcomments = FirebaseDatabase.getInstance().getReference().child("Comments");
        mcmntdb = FirebaseDatabase.getInstance().getReference().child("Comments").child(key);

        mcommentlist = (RecyclerView)findViewById(R.id.recycle2);
        mcommentlist.setHasFixedSize(true);
        mcommentlist.setLayoutManager(new LinearLayoutManager(this));


        bck = (ImageView)findViewById(R.id.imageViewback);
        send = (ImageView)findViewById(R.id.buttonsend);
        cmnt = (MultiAutoCompleteTextView)findViewById(R.id.editcomment);
        bck.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Comment,commentholder> commentadapter = new FirebaseRecyclerAdapter<Comment, commentholder>(
                Comment.class,
                R.layout.comments,
                commentholder.class,
                mcmntdb
        ) {
            @Override
            protected void populateViewHolder(final commentholder viewHolder, Comment model, int position) {
                viewHolder.setuserc(model.getUserc());
                viewHolder.setdatec(model.getDatec());
                viewHolder.setcommentc(model.getComment());
            }
        };
        mcommentlist.setAdapter(commentadapter);
    }

    public static class commentholder extends RecyclerView.ViewHolder{
        View cview;

        public commentholder(View itemView) {
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
                    String comment = cmnt.getText().toString();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm aa  dd-MM-yyyy");
                    final String format1 = simpleDateFormat1.format(new Date());
                    final DatabaseReference mpostc = mcomments.child(key).push();
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
                            cmnt.getText().clear();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
