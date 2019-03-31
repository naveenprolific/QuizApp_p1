package com.example.quizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.ui.phone.CompletableProgressDialog;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewBlog extends AppCompatActivity implements View.OnClickListener {
    Button sbp;
    ImageView hm,lgt,ip;
    EditText pt;
    MultiAutoCompleteTextView desc;

    private static final int CHOOSE_IMAGE = 102;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;

    FirebaseAuth mAuth;
    DatabaseReference mdb,muserdb;
    private ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blog);
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference().child("Posts");
        muserdb = FirebaseDatabase.getInstance().getReference().child("Users");
        mprogress = new ProgressDialog(this);

        sbp = (Button)findViewById(R.id.postblog);
        hm = (ImageView)findViewById(R.id.imageViewhome);
        lgt = (ImageView)findViewById(R.id.imageViewlogout);
        ip = (ImageView)findViewById(R.id.imageViewnewb);
        pt = (EditText)findViewById(R.id.editTitle);
        desc = (MultiAutoCompleteTextView)findViewById(R.id.editdesc);

        hm.setOnClickListener(this);
        lgt.setOnClickListener(this);
        ip.setOnClickListener(this);
        sbp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==sbp){
            savePostInformation();
        }
        if(v==ip){
            showImageChooser();
        }
        if(v==hm){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        if(v==lgt){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                ip.setImageBitmap(bitmap);
                //uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePostInformation() {
        mprogress.setMessage("Posting...");
        mprogress.show();
        final String title = pt.getText().toString();
        final String decp = desc.getText().toString();
        if (title.isEmpty()) {
            pt.setError("Title required");
            pt.requestFocus();
            return;
        }
        if (decp.isEmpty()) {
            desc.setError("Description required");
            desc.requestFocus();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        final String format = simpleDateFormat.format(new Date());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm aa  dd-MM-yyyy");
        final String format1 = simpleDateFormat1.format(new Date());
        final FirebaseUser user = mAuth.getCurrentUser();
        final String u_id = mAuth.getCurrentUser().getUid();
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("postpics/" +format+ ".jpg");

        if (uriProfileImage != null && user !=null ) {
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                            final DatabaseReference mpinf = mdb.push();
                            mpinf.child("image_url").setValue(profileImageUrl);
                            mpinf.child("Title").setValue(title);
                            mpinf.child("Description").setValue(decp);
                            mpinf.child("User_id").setValue(u_id);
                            mpinf.child("Date_posted").setValue(format1);
                            DatabaseReference musername = muserdb.child(u_id).child("User name");
                            musername.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = String.valueOf(dataSnapshot.getValue());
                                    mpinf.child("User_name").setValue(name);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            DatabaseReference muserimage = muserdb.child(u_id).child("imageurl");
                            muserimage.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String imageurluser = String.valueOf(dataSnapshot.getValue());
                                    mpinf.child("User_imageurl").setValue(imageurluser);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mprogress.dismiss();
                            Toast.makeText(getApplicationContext(),"POST ADDED",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(NewBlog.this,Timeline.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
