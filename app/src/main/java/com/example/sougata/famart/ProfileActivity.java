package com.example.sougata.famart;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private ImageView profilePic;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileAge;
    private Button profileUpdateButton;

    //Get Firebase instances
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_picture_iv);
        profileName = findViewById(R.id.profile_name_tv);
        profileEmail = findViewById(R.id.profile_email_tv);
        profileAge = findViewById(R.id.profile_age_tv);
        profileUpdateButton = findViewById(R.id.profile_update_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Create Database reference
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            //Below function is called when the activity starts.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get the class which would retrieve the data (eg: User)
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    profileName.setText(String.format("Name : %s", user.getName()));
                    profileAge.setText(String.format("Age : %s", user.getAge()));
                    profileEmail.setText(String.format("Email : %s", user.getEmail()));
                    progressDialog.dismiss();
                }
            }

            //Below function is called if the event listener is cancelled.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
