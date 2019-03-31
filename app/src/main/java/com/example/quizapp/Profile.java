package com.example.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity implements  View.OnClickListener{

    private static final int CHOOSE_IMAGE = 101;

    TextView textView,userem,textusername,textphno;
    ImageView imageView,h,lg,e1,e2;
    EditText editText,editmobile;
    Button add,chngpass,dltacc,myp;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;

    FirebaseAuth mAuth;
    DatabaseReference mdb;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);

        editText = (EditText) findViewById(R.id.editTextDisplayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        h = (ImageView) findViewById(R.id.imageViewhome);
        lg = (ImageView) findViewById(R.id.imageViewlogout);
        add = (Button)findViewById(R.id.addpost);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textViewVerified);
        userem = (TextView)findViewById(R.id.useremail);
        e1=(ImageView)findViewById(R.id.editusername);
        e2 = (ImageView)findViewById(R.id.editphone);
        textusername=(TextView)findViewById(R.id.changedname);
        textphno = (TextView)findViewById(R.id.phno);
        editmobile = (EditText)findViewById(R.id.editphno);
        chngpass = (Button)findViewById(R.id.changepass);
        dltacc = (Button)findViewById(R.id.deleteaccount);
        myp = (Button)findViewById(R.id.myblogbtn);

        imageView.setOnClickListener(this);
        h.setOnClickListener(this);
        lg.setOnClickListener(this);
        add.setOnClickListener(this);
        e1.setOnClickListener(this);
        e2.setOnClickListener(this);
        chngpass.setOnClickListener(this);
        dltacc.setOnClickListener(this);
        myp.setOnClickListener(this);
        loadUserInformation();
        findViewById(R.id.buttonSave).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.buttonSave)){
            saveUserInformation();
        }
        if(v==imageView){
            showImageChooser();
        }
        if(v==h){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        if(v==myp){
            finish();
            startActivity(new Intent(getApplicationContext(),Myposts.class));
        }
        if(v==lg){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        if(v==e1){
            textusername.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
        }
        if(v==e2){
            textphno.setVisibility(View.GONE);
            editmobile.setVisibility(View.VISIBLE);
        }
        if(v==add){
            finish();
            startActivity(new Intent(getApplicationContext(),NewBlog.class));
        }
        if(v==dltacc){
            FirebaseUser user = mAuth.getCurrentUser();
            progressDialog.setMessage("Deleting Account....");
            progressDialog.show();
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Account is deleted :(",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),Signupactivity.class));
                        }
                    });
        }
        if(v==chngpass){
            finish();
            startActivity(new Intent(getApplicationContext(),ChangePassword.class));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        final String u_id = mAuth.getCurrentUser().getUid();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }
           DatabaseReference  muserdata = mdb.child(u_id).child("User name");
            muserdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String name = String.valueOf(dataSnapshot.getValue());
                        textusername.setText(name);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference mphndata = mdb.child(u_id).child("Mobile No");
            mphndata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String num = String.valueOf(dataSnapshot.getValue());
                        textphno.setText(num);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            String email = mAuth.getCurrentUser().getEmail();
            userem.setText(email);
            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Profile.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }


    private void saveUserInformation() {
        progressDialog.setMessage("Saving information...");
        progressDialog.show();
        String displayName = editText.getText().toString();
        String mobilenum = editmobile.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }
        if (mobilenum.isEmpty()) {
            editmobile.setError("Mobile number required");
            editmobile.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        final String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference mdata = mdb.child(user_id);
        mdata.child("Mobile No").setValue(mobilenum);

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final String user_name = mAuth.getCurrentUser().getUid();
        final FirebaseUser u = mAuth.getCurrentUser();
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" +user_name+ ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             progressBar.setVisibility(View.GONE);
                             profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                             String u_name = u.getDisplayName();
                             DatabaseReference mpf = mdb.child(user_name);
                             mpf.child("User name").setValue(u_name);
                             mpf.child("imageurl").setValue(profileImageUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadUserInformation();
                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
}
