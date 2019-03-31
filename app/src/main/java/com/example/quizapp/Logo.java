package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Logo extends AppCompatActivity {
    ImageView imglg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        imglg = (ImageView)findViewById(R.id.imagelogo);
        Animation manim = AnimationUtils.loadAnimation(this,R.anim.logoanim);
        imglg.startAnimation(manim);
        final Intent i = new Intent(getApplicationContext(),MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
