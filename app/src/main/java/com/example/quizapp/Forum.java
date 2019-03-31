package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Forum extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4;
    ImageView bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        b1= (Button)findViewById(R.id.histbutton);
        b2= (Button)findViewById(R.id.csebutton);
        b3= (Button)findViewById(R.id.mathbutton);
        b4= (Button)findViewById(R.id.sportbutton);
        bck = (ImageView)findViewById(R.id.imageViewback);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        bck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==bck){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }

        if(v==b1){
            Intent i1= new Intent(getApplicationContext(),Group.class);
            i1.putExtra("grpname","History");
            i1.putExtra("activity",".Forum");
            startActivity(i1);
            finish();
        }
        if(v==b2){
            Intent i2= new Intent(getApplicationContext(),Group.class);
            i2.putExtra("grpname","CSE");
            i2.putExtra("activity",".Forum");
            startActivity(i2);
            finish();
        }
        if(v==b3){
            Intent i3= new Intent(getApplicationContext(),Group.class);
            i3.putExtra("grpname","Math");
            i3.putExtra("activity",".Forum");
            startActivity(i3);
            finish();
        }
        if(v==b4){
            Intent i4= new Intent(getApplicationContext(),Group.class);
            i4.putExtra("grpname","Sport");
            i4.putExtra("activity",".Forum");
            startActivity(i4);
            finish();
        }
    }
}