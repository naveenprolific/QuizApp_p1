package com.example.quizapp;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.sql.Time;

public class Home extends AppCompatActivity implements View.OnClickListener {
    Button bplay,bset,blead,bform;
    ImageView iacc,iset,itime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bplay = (Button)findViewById(R.id.buttonPlay);
        bset = (Button)findViewById(R.id.buttonsettings);
        blead = (Button)findViewById(R.id.buttonLeaderboard);
        bform = (Button)findViewById(R.id.buttonforum);
        iacc = (ImageView)findViewById(R.id.imageViewaccount);
        //iset = (ImageView)findViewById(R.id.imageViewsettings);
        itime = (ImageView)findViewById(R.id.timeline);
        bplay.setOnClickListener(this);
        bset.setOnClickListener(this);
        blead.setOnClickListener(this);
        bform.setOnClickListener(this);
        iacc.setOnClickListener(this);
        //iset.setOnClickListener(this);
        itime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==iacc){
            finish();
            Intent i = new Intent(getApplicationContext(),Profile.class);
            startActivity(i);
        }
        if(v==bplay){
            finish();
            Intent i1 = new Intent(getApplicationContext(),Categories.class);
            startActivity(i1);
        }
        if(v==itime){
            finish();
            startActivity(new Intent(getApplicationContext(),Timeline.class));
        }
        if(v==blead){
            finish();
            startActivity(new Intent(getApplicationContext(),LBCategories.class));
        }
        if(v==bset){
            finish();
            startActivity(new Intent(getApplicationContext(), Setting.class));
        }
        if(v==bform){
            finish();
            startActivity(new Intent(getApplicationContext(), Forum.class));
        }
    }
}
