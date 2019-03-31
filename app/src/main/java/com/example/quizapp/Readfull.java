package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Readfull extends AppCompatActivity implements View.OnClickListener {
    ImageView bck,lg,uim,pim;
    TextView unm,dt,pt,pd;
    Button blb,tlb,bcb;
    FirebaseAuth mAuth;
    String a;

    private RecyclerView mbloglist;
    private DatabaseReference mdatabase;
    private DatabaseReference mdatabaselikes;
    private DatabaseReference mdatabaseuser;
    private DatabaseReference mcomments;
    private boolean mlikes=false;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readfull);

        mAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        key = i.getStringExtra("post_key");
        Log.d("hell", "onCreate: "+key);
        a = i.getStringExtra("activity");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(key);
        mdatabaselikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");
        mcomments = FirebaseDatabase.getInstance().getReference().child("Comments");


        bck = (ImageView)findViewById(R.id.imageViewback);
        lg = (ImageView)findViewById(R.id.imageViewlogout);
        uim = (ImageView)findViewById(R.id.usrimg);
        pim = (ImageView)findViewById(R.id.postimg);
        unm = (TextView)findViewById(R.id.usrnmid);
        dt = (TextView)findViewById(R.id.dateid);
        pt = (TextView)findViewById(R.id.ptitle);
        pd = (TextView)findViewById(R.id.pdescp);
        blb = (Button)findViewById(R.id.btnlikes);
        tlb = (Button)findViewById(R.id.txtlikes);
        bcb = (Button)findViewById(R.id.buttoncomment1);

        bck.setOnClickListener(this);
        lg.setOnClickListener(this);
        blb.setOnClickListener(this);
        bcb.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mdate = mdatabase.child("Date_posted");
        mdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = String.valueOf(dataSnapshot.getValue());
                dt.setText(date);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference mdesc = mdatabase.child("Description");
        mdesc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String desc = String.valueOf(dataSnapshot.getValue());
                pd.setText(desc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference mtitle = mdatabase.child("Title");
        mtitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = String.valueOf(dataSnapshot.getValue());
                pt.setText(title);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference muid = mdatabase.child("User_id");
        muid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String u_id = String.valueOf(dataSnapshot.getValue());
                DatabaseReference muserd = FirebaseDatabase.getInstance().getReference().child("Users").child(u_id);
                DatabaseReference musername = muserd.child("User name");
                musername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     String name = String.valueOf(dataSnapshot.getValue());
                     unm.setText(name);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               DatabaseReference muserimage = muserd.child("imageurl");
               muserimage.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       String image = String.valueOf(dataSnapshot.getValue());
                       Picasso.with(getApplicationContext()).load(image).into(uim);
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference mpim = mdatabase.child("image_url");
        mpim.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pimurl = String.valueOf(dataSnapshot.getValue());
                Picasso.with(getApplicationContext()).load(pimurl).into(pim);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mdatabaselikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())){
                    Drawable d = getResources().getDrawable(R.drawable.likered);
                    blb.setBackground(d);
                    mdatabaselikes.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int cnt = (int)dataSnapshot.getChildrenCount();
                            if(cnt==1){
                                tlb.setText(String.valueOf(cnt)+" Like");
                            }
                            else{
                                tlb.setText(String.valueOf(cnt)+" Likes");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    tlb.setTextColor(getResources().getColor(R.color.Red));
                }
                else{
                    Drawable d = getResources().getDrawable(R.drawable.like);
                    blb.setBackground(d);
                    mdatabaselikes.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int cnt = (int)dataSnapshot.getChildrenCount();
                            if(cnt==1){
                                tlb.setText(String.valueOf(cnt)+" Like");
                            }
                            else{
                                tlb.setText(String.valueOf(cnt)+" Likes");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    tlb.setTextColor(getResources().getColor(R.color.darkblack));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference mcmntdb = mcomments.child(key);
        mcmntdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = (int)dataSnapshot.getChildrenCount();
                bcb.setText(String.valueOf(count));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        if(v==blb){
            mlikes = true;
            mdatabaselikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(mlikes) {
                        if (dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())) {
                            mdatabaselikes.child(key).child(mAuth.getCurrentUser().getUid()).removeValue();
                            mlikes = false;
                        } else {
                            mdatabaselikes.child(key).child(mAuth.getCurrentUser().getUid()).setValue("user liked");
                            mlikes = false;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(v==bcb){
            Intent i = new Intent(getApplicationContext(),Comment_page.class);
            i.putExtra("post_key",key);
            i.putExtra("activity",".Timeline");
            startActivity(i);
            finish();
        }
        if(v==bck){
            finish();
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(),"com.example.quizapp"+a);
            startActivity(intent);
        }
        if(v==lg){
            mAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
