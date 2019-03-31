package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity implements View.OnClickListener {
    Button rst;
    EditText useremail;
    TextView lgn;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mauth = FirebaseAuth.getInstance();

        rst =(Button)findViewById(R.id.buttonreset);
        lgn =(TextView)findViewById(R.id.textViewLogin);
        useremail=(EditText)findViewById(R.id.resetEmail);
        rst.setOnClickListener(this);
        lgn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String resetemail = useremail.getText().toString().trim();
        if(v==lgn){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        if(v==rst){
            if (resetemail.isEmpty()) {
                useremail.setError("Email is required");
                useremail.requestFocus();
                return;
            }
            else{
                mauth.sendPasswordResetEmail(resetemail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Email sent for Password reste :)",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Email sending failed :( ,Try again",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        }
    }
}
