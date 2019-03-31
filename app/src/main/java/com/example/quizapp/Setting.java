package com.example.quizapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.SharedMemory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.quizapp.BroadcastReceiver.AlarmReceiver;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Setting extends AppCompatActivity {//implements CompoundButton.OnCheckedChangeListener
    ImageView bck,lgt;
    public static Switch s1,s2;
    Button save;
    public  static  boolean bl=false;
    public static boolean switchonoff=false;
    public static boolean switchonoff1=false;
    MediaPlayer mediaPlayer;
    FirebaseAuth mauth;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    public static final String SWITCH2 = "switch2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mauth = FirebaseAuth.getInstance();
         s1 = (Switch)findViewById(R.id.switch1);
         s2 = (Switch)findViewById(R.id.switch2);
         bck = (ImageView)findViewById(R.id.imageViewback);
         lgt = (ImageView)findViewById(R.id.imageViewlogout);
         save = (Button)findViewById(R.id.buttonsave1);
         //s1.setOnCheckedChangeListener(this);
         //s2.setOnCheckedChangeListener(this);
         bck.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
                 startActivity(new Intent(getApplicationContext(),Home.class));
             }
         });
        lgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });
        loaddata();
        updateviews();
    }
    public void  savedata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1,s1.isChecked());
        editor.putBoolean(SWITCH2,s2.isChecked());
        editor.apply();
        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
    }

    public void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        switchonoff = sharedPreferences.getBoolean(SWITCH1,false);
        switchonoff1 = sharedPreferences.getBoolean(SWITCH2,false);
    }
    public void updateviews(){
        s1.setChecked(switchonoff);
        s2.setChecked(switchonoff1);
        if(s2.isChecked()){
            registeralarm();
        }
        else{

        }
    }

    private  void  registeralarm(){
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(Setting.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }
    /*

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(s1.isChecked()){
            //mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.drift);
            //mediaPlayer.start();
            bl = true;
            s1.setChecked(true);

        }
        else{
            //mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.drift);
           // mediaPlayer.stop();
            //mediaPlayer.release();
            //mediaPlayer=null;
            bl = false;
            s1.setChecked(false);
        }
        if(s2.isChecked()){
            registeralarm();
            s2.setChecked(true);
        }
        else{
            s2.setChecked(false);
        }
    }
    */
    public  static boolean getMys(){
       return switchonoff;
    }
}
