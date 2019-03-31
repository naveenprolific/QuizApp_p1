package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class LBCategories extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4,b5;
    ImageView hm,lgt;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lbcategories);

        mauth = FirebaseAuth.getInstance();

        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        b5 = (Button)findViewById(R.id.button5);
        hm = (ImageView)findViewById(R.id.imageViewhome);
        lgt = (ImageView)findViewById(R.id.imageViewlogout);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        hm.setOnClickListener(this);
        lgt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==b1) {
            finish();
            Intent i2 = new Intent(getApplicationContext(), Math.class);
            startActivity(i2);
        }
        if(v==b2){
            finish();
            Intent i3 = new Intent(getApplicationContext(),Lhist.class);
            i3.putExtra("activity","History");
            startActivity(i3);
        }
        if(v==b3){
            finish();
            Intent i4 = new Intent(getApplicationContext(),Lhist.class);
            i4.putExtra("activity","Math");
            startActivity(i4);
        }
        if(v==b4){
            finish();
            Intent i5 = new Intent(getApplicationContext(),Lhist.class);
            i5.putExtra("activity","Sports");
            startActivity(i5);
        }
        if(v==b5){
            finish();
            Intent i6 = new Intent(getApplicationContext(),Lhist.class);
            i6.putExtra("activity","GK");
            startActivity(i6);
        }
        if(v==hm){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        if(v==lgt){
            mauth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}
