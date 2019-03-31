package com.example.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{
    Button bc;
    EditText ec,en,ecn;
    ImageView hm,lgt;
    FirebaseAuth mauth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_change_password);
        bc = (Button)findViewById(R.id.buttonchange);
        en=(EditText)findViewById(R.id.newpass);
        ecn=(EditText)findViewById(R.id.confirmnew);
        hm = (ImageView)findViewById(R.id.imageViewhome);
        lgt = (ImageView)findViewById(R.id.imageViewlogout);

        bc.setOnClickListener(this);
        hm.setOnClickListener(this);
        lgt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==hm){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        if(v==lgt){
            mauth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        if(v==bc){
            progressDialog.setMessage("Changing Password...:)");
            progressDialog.show();
            FirebaseUser user = mauth.getCurrentUser();
            String newpass = en.getText().toString();
            String confirmpass = ecn.getText().toString();
            if(newpass.equals(confirmpass)){
                user.updatePassword(newpass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Password changed..Login again",Toast.LENGTH_SHORT).show();
                                    mauth.getInstance().signOut();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Error in changing Password..:(",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(getApplicationContext(),"Passwords doesn't match",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
