package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity implements View.OnClickListener {
    TextView t,t2,t3;
    Button trb,hm;
    private String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        t=(TextView)findViewById(R.id.text);
        t2=(TextView)findViewById(R.id.text5);
        //t3=(TextView)findViewById(R.id.text6);
        trb = (Button)findViewById(R.id.trbid);
        hm = (Button)findViewById(R.id.homeid);

        Intent i = getIntent();
        String q=i.getStringExtra("total");
        String c=i.getStringExtra("correct");
        String w=i.getStringExtra("wrong");
        String a = i.getStringExtra("activity");

        t.setText(a);
        t2.setText("Score : "+c);
        t2.setTextColor(getResources().getColor(R.color.Green));
        //t3.setText(w);
        trb.setOnClickListener(this);
        hm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==trb){
            Intent intent = new Intent(getApplicationContext(),Categories.class);
            //intent.setClassName(getApplicationContext(),"com.example.quizapp"+activity);
            startActivity(intent);
            finish();
        }
        if(v==hm){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
    }
}
