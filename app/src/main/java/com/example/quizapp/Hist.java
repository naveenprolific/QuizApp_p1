package com.example.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.Model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Hist extends AppCompatActivity {
    Button op1,op2,op3,op4;
    TextView ques,timer;
    DatabaseReference reference,mdatabase;
    FirebaseAuth mauth;
    ProgressBar progressBar;
    CountDownTimer mcountdown;
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;
    int total=0;
    int correct=0;
    int wrong=0;
    final static long INTERVAL =1000;
    final static long TIMEOUT =11000;
    String activity;
    public static boolean ison =  Setting.getMys();

    int progresvalue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);
        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Leaderboard");

        Intent i = getIntent();
        activity = i.getStringExtra("activity");

        progressDialog = new ProgressDialog(this);

        op1=(Button)findViewById(R.id.buttonop1);
        op2=(Button)findViewById(R.id.buttonop2);
        op3=(Button)findViewById(R.id.buttonop3);
        op4=(Button)findViewById(R.id.buttonop4);
        ques=(TextView)findViewById(R.id.questionstext);
        timer=(TextView)findViewById(R.id.timertext);
        progressBar=(ProgressBar)findViewById(R.id.progtimer);
        mediaPlayer = MediaPlayer.create(Hist.this,R.raw.drift);
        mediaPlayer.setLooping(true);
        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ison){
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
       // mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.drift);
        if(ison){
            mediaPlayer.pause();
        }
        else{

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.drift);
        if(mediaPlayer!=null){
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            finally {
                mediaPlayer=null;
            }
        }
    }
    private void updateQuestion(){
        //mcountdown.start();
        final String u_id=mauth.getCurrentUser().getUid();
        final DatabaseReference mscore = mdatabase.child(activity).child(u_id);
        final DatabaseReference muser  =  FirebaseDatabase.getInstance().getReference().child("Users").child(u_id).child("User name");
        muser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = String.valueOf(dataSnapshot.getValue());
                mscore.child("username").setValue(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        total++;
        if(total>50){
            finish();
            Intent i1=new Intent(getApplicationContext(),Result.class);
            i1.putExtra("total",String.valueOf(total-1));
            i1.putExtra("correct",String.valueOf(correct));
            i1.putExtra("wrong",String.valueOf(wrong));
            i1.putExtra("activity",activity);
            DatabaseReference newref = mscore.child("Score");
            newref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String s = String.valueOf(dataSnapshot.getValue());
                        int sc = Integer.valueOf(s);
                        if(sc<correct){
                            mscore.child("Score").setValue(correct);
                            mscore.child("Order").setValue((-1)*correct);
                        }
                    }
                    else{
                        mscore.child("Score").setValue(correct);
                        mscore.child("Order").setValue((-1)*correct);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            startActivity(i1);
        }
        else{
            reference = FirebaseDatabase.getInstance().getReference().child(activity).child(String.valueOf(total));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Question question=dataSnapshot.getValue(Question.class);

                    ques.setText(question.getQuestions());
                    op1.setText(question.getOption1());
                    op2.setText(question.getOption2());
                    op3.setText(question.getOption3());
                    op4.setText(question.getOption4());
                    progressBar.setProgress(0);
                    progresvalue=0;
                    mcountdown.start();
                    op1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mcountdown.cancel();
                            if(op1.getText().toString().equals(question.getAnswer())){
                                op1.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        op1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1000);
                            }
                            else{
                                wrong++;
                                op1.setBackgroundColor(Color.RED);

                                if(op2.getText().toString().equals(question.getAnswer())){
                                    op2.setBackgroundColor(Color.GREEN);
                                }
                                else if(op3.getText().toString().equals(question.getAnswer())){
                                    op3.setBackgroundColor(Color.GREEN);
                                }
                                else if(op4.getText().toString().equals(question.getAnswer())){
                                    op4.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        op1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        //updateQuestion();
                                    }
                                },1000);
                                finish();
                                Intent i2=new Intent(getApplicationContext(),Result.class);
                                i2.putExtra("total",String.valueOf(total-1));
                                i2.putExtra("correct",String.valueOf(correct));
                                i2.putExtra("wrong",String.valueOf(wrong));
                                i2.putExtra("activity",activity);
                                DatabaseReference newref = mscore.child("Score");
                                newref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String s = String.valueOf(dataSnapshot.getValue());
                                            int sc = Integer.valueOf(s);
                                            Log.d("hello", "onDataChange: "+String.valueOf(sc));
                                            if(sc<correct){
                                                mscore.child("Score").setValue(correct);
                                                mscore.child("Order").setValue((-1)*correct);
                                            }
                                        }
                                        else{
                                            mscore.child("Score").setValue(correct);
                                            mscore.child("Order").setValue((-1)*correct);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                startActivity(i2);
                            }
                        }
                    });


                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mcountdown.cancel();
                            if(op2.getText().toString().equals(question.getAnswer())){
                                op2.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        op2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1000);
                            }
                            else{
                                wrong++;
                                op2.setBackgroundColor(Color.RED);

                                if(op1.getText().toString().equals(question.getAnswer())){
                                    op1.setBackgroundColor(Color.GREEN);
                                }
                                else if(op3.getText().toString().equals(question.getAnswer())){
                                    op3.setBackgroundColor(Color.GREEN);
                                }
                                else if(op4.getText().toString().equals(question.getAnswer())){
                                    op4.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        op1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        //updateQuestion();
                                    }
                                },1000);
                                finish();
                                Intent i3=new Intent(getApplicationContext(),Result.class);
                                i3.putExtra("total",String.valueOf(total-1));
                                i3.putExtra("correct",String.valueOf(correct));
                                i3.putExtra("wrong",String.valueOf(wrong));
                                i3.putExtra("activity",activity);
                                DatabaseReference newref = mscore.child("Score");
                                newref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String s = String.valueOf(dataSnapshot.getValue());
                                            int sc = Integer.valueOf(s);
                                            Log.d("hello", "onDataChange: "+String.valueOf(sc));
                                            if(sc<correct){
                                                mscore.child("Score").setValue(correct);
                                                mscore.child("Order").setValue((-1)*correct);
                                            }
                                        }
                                        else{
                                            mscore.child("Score").setValue(correct);
                                            mscore.child("Order").setValue((-1)*correct);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                startActivity(i3);
                            }
                        }
                    });

                    op3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mcountdown.cancel();
                            if(op3.getText().toString().equals(question.getAnswer())){
                                op3.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        op3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1000);
                            }
                            else{
                                wrong++;
                                op3.setBackgroundColor(Color.RED);

                                if(op2.getText().toString().equals(question.getAnswer())){
                                    op2.setBackgroundColor(Color.GREEN);
                                }
                                else if(op1.getText().toString().equals(question.getAnswer())){
                                    op1.setBackgroundColor(Color.GREEN);
                                }
                                else if(op4.getText().toString().equals(question.getAnswer())){
                                    op4.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        op1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        //updateQuestion();
                                    }
                                },1000);
                                finish();
                                Intent i4=new Intent(getApplicationContext(),Result.class);
                                i4.putExtra("total",String.valueOf(total-1));
                                i4.putExtra("correct",String.valueOf(correct));
                                i4.putExtra("wrong",String.valueOf(wrong));
                                i4.putExtra("activity",activity);
                                DatabaseReference newref = mscore.child("Score");
                                newref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String s = String.valueOf(dataSnapshot.getValue());
                                            int sc = Integer.valueOf(s);
                                            Log.d("hello", "onDataChange: "+String.valueOf(sc));
                                            if(sc<correct){
                                                mscore.child("Score").setValue(correct);
                                                mscore.child("Order").setValue((-1)*correct);
                                            }
                                        }
                                        else{
                                            mscore.child("Score").setValue(correct);
                                            mscore.child("Order").setValue((-1)*correct);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                startActivity(i4);

                            }
                        }
                    });

                    op4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mcountdown.cancel();
                            if(op4.getText().toString().equals(question.getAnswer())){
                                op4.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        op4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1000);
                            }
                            else{
                                wrong++;
                                op4.setBackgroundColor(Color.RED);

                                if(op2.getText().toString().equals(question.getAnswer())){
                                    op2.setBackgroundColor(Color.GREEN);
                                }
                                else if(op3.getText().toString().equals(question.getAnswer())){
                                    op3.setBackgroundColor(Color.GREEN);
                                }
                                else if(op1.getText().toString().equals(question.getAnswer())){
                                    op1.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        op1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        op4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        //updateQuestion();
                                    }
                                },1000);
                                finish();
                                Intent i5=new Intent(getApplicationContext(),Result.class);
                                i5.putExtra("total",String.valueOf(total-1));
                                i5.putExtra("correct",String.valueOf(correct));
                                i5.putExtra("wrong",String.valueOf(wrong));
                                i5.putExtra("activity",activity);
                                DatabaseReference newref = mscore.child("Score");
                                newref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String s = String.valueOf(dataSnapshot.getValue());
                                            int sc = Integer.valueOf(s);
                                            Log.d("hello", "onDataChange: "+String.valueOf(sc));
                                            if(sc<correct){
                                                mscore.child("Score").setValue(correct);
                                                mscore.child("Order").setValue((-1)*correct);
                                            }
                                        }
                                        else{
                                            mscore.child("Score").setValue(correct);
                                            mscore.child("Order").setValue((-1)*correct);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                startActivity(i5);
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mcountdown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progresvalue);
                progresvalue+=1;
                int seconds = (int)(millisUntilFinished/1000)%60;
                timer.setText(String.format("%02d",seconds));
            }
            @Override
            public void onFinish() {
                mcountdown.cancel();
                updateQuestion();
            }
        };
    }

}
