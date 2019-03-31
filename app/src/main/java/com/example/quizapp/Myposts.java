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

public class Myposts extends AppCompatActivity implements View.OnClickListener {
    ImageView hm,lg;
    //Button add;
    FirebaseAuth mAuth;

    private RecyclerView mbloglist;
    private DatabaseReference mdatabase;
    private  DatabaseReference mdatabaselikes;
    private DatabaseReference mdatabaseuser;
    private DatabaseReference mcomments;
    private boolean mlikes=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myposts);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("My_Posts").child(mAuth.getCurrentUser().getUid());
        mdatabaselikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");
        mcomments = FirebaseDatabase.getInstance().getReference().child("Comments");

        mbloglist = (RecyclerView)findViewById(R.id.blog_list);
        mbloglist.setHasFixedSize(true);
        mbloglist.setLayoutManager(new LinearLayoutManager(this));


        hm = (ImageView)findViewById(R.id.imageViewhome);
        lg = (ImageView)findViewById(R.id.imageViewlogout);
        //add = (Button)findViewById(R.id.addpost);
        hm.setOnClickListener(this);
        lg.setOnClickListener(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog,Timeline.BlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Blog, Timeline.BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                Timeline.BlogViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(final Timeline.BlogViewHolder viewHolder, Blog model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setlike(postkey);
                viewHolder.setpostTitle(model.getTitle());
                viewHolder.setpostDesc(model.getDescription());
                viewHolder.setusername(getApplicationContext(),model.getUser_id());
                viewHolder.setpostdate(model.getDate_posted());
                //viewHolder.setusertImage(getApplicationContext(),model.getUser_imageurl());
                viewHolder.setpostImage(getApplicationContext(),model.getImage_url());
                viewHolder.mview.findViewById(R.id.buttonread).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v==viewHolder.mview.findViewById(R.id.buttonread)){
                            Intent i = new Intent(getApplicationContext(),Readfull.class);
                            i.putExtra("activity",".Myposts");
                            i.putExtra("post_key",postkey);
                            startActivity(i);
                            finish();
                        }
                    }
                });
                viewHolder.mview.findViewById(R.id.buttonlikes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlikes=true;

                        mdatabaselikes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mlikes) {
                                    if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mdatabaselikes.child(postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mlikes = false;
                                    } else {
                                        mdatabaselikes.child(postkey).child(mAuth.getCurrentUser().getUid()).setValue("user liked");
                                        mlikes = false;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                viewHolder.mview.findViewById(R.id.buttoncomment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),Comment_page.class);
                        i.putExtra("post_key",postkey);
                        i.putExtra("activity",".Myposts");
                        startActivity(i);
                        finish();
                    }
                });
                DatabaseReference mcmntdb = mcomments.child(postkey);
                mcmntdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = (int)dataSnapshot.getChildrenCount();
                        viewHolder.mcmntbtn.setText(String.valueOf(count));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mbloglist.setAdapter(firebaseRecyclerAdapter);
        //firebaseRecyclerAdapter.startListening();

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        private Button rdf;
        private Button lks;
        private Button textlks;
        private  Button mcmntbtn;
        View mview;

        final DatabaseReference mlikedata;
        final DatabaseReference muserdb;
        final FirebaseAuth mauth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            rdf = mview.findViewById(R.id.buttonread);
            lks = mview.findViewById(R.id.buttonlikes);
            textlks = mview.findViewById(R.id.textlikes);
            mcmntbtn = mview.findViewById(R.id.buttoncomment);
            mlikedata = FirebaseDatabase.getInstance().getReference().child("Likes");
            muserdb= FirebaseDatabase.getInstance().getReference().child("Users");
            mauth = FirebaseAuth.getInstance();
        }
        public  void setpostTitle(String title){
            TextView mtitle = (TextView)mview.findViewById(R.id.post_title);
            mtitle.setText(title);
        }
        public void setlike(final String key){

            mlikedata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(key).hasChild(mauth.getCurrentUser().getUid())){
                        Resources res = mview.getContext().getResources();
                        Drawable d = res.getDrawable(R.drawable.likered);
                        lks.setBackground(d);
                        mlikedata.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int cnt = (int)dataSnapshot.getChildrenCount();
                                if(cnt==1){
                                    textlks.setText(String.valueOf(cnt)+" Like");
                                }
                                else{
                                    textlks.setText(String.valueOf(cnt)+" Likes");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        textlks.setTextColor(res.getColor(R.color.Red));
                    }
                    else{
                        Resources res = mview.getContext().getResources();
                        Drawable d = res.getDrawable(R.drawable.like);
                        lks.setBackground(d);
                        mlikedata.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int cnt = (int)dataSnapshot.getChildrenCount();
                                if(cnt==1){
                                    textlks.setText(String.valueOf(cnt)+" Like");
                                }
                                else{
                                    textlks.setText(String.valueOf(cnt)+" Likes");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        textlks.setTextColor(res.getColor(R.color.darkblack));

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public  void setpostDesc(String desc){
            TextView mdesc = (TextView)mview.findViewById(R.id.post_descp);
            String t[] = desc.split("\\.",10);
            Log.d("bfjbv", "setpostDesc: "+t[0]);
            mdesc.setText(t[0]);
        }
        public  void setusername(final Context ctx, String u_id){
            final TextView muser = (TextView)mview.findViewById(R.id.userid);
            final ImageView muserimage = (ImageView)mview.findViewById(R.id.userimage);
            DatabaseReference musername = muserdb.child(u_id).child("User name");
            musername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = String.valueOf(dataSnapshot.getValue());
                    muser.setText(name);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference murl= muserdb.child(u_id).child("imageurl");
            murl.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String url = String.valueOf(dataSnapshot.getValue());
                    Picasso.with(ctx).load(url).into(muserimage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public  void setpostdate(String date){
            TextView mdate = (TextView)mview.findViewById(R.id.dateid);
            mdate.setText(date);
        }
        public  void  setpostImage(Context ctx,String image){
            ImageView mimage = (ImageView)mview.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).into(mimage);
        }

    }



    @Override
    public void onClick(View v) {
        if(v==hm){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        if(v==lg){
            mAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
