package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Math extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4;
    ImageView h,lg;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        mAuth=FirebaseAuth.getInstance();
        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        h=(ImageView)findViewById(R.id.imageViewhome);
        lg=(ImageView)findViewById(R.id.imageViewlogout);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        h.setOnClickListener(this);
        lg.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == lg) {
            mAuth.getInstance().signOut();
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        if (v == h) {
            finish();
            Intent i1 = new Intent(getApplicationContext(), Home.class);
            startActivity(i1);
        }
        if (v == b1) {
            finish();
            Intent i2 = new Intent(getApplicationContext(), Lhist.class);
            i2.putExtra("activity", "OS");
            startActivity(i2);
        }
        if (v == b2) {
            finish();
            Intent i3 = new Intent(getApplicationContext(), Lhist.class);
            i3.putExtra("activity", "Hardware");
            startActivity(i3);
        }
        if (v == b3) {
            finish();
            Intent i4 = new Intent(getApplicationContext(), Lhist.class);
            i4.putExtra("activity", "DBMS");
            startActivity(i4);
        }
        if (v == b4) {
            finish();
            Intent i5 = new Intent(getApplicationContext(), Lhist.class);
            i5.putExtra("activity", "Networks");
            startActivity(i5);
        }
    }
}
