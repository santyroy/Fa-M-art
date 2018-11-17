package com.example.sougata.famart;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText updateName;
    private EditText updateEmail;
    private EditText updateAge;
    private Button updateButton;
    private ImageView updateProfilePicture;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        updateAge = findViewById(R.id.update_age_et);
        updateButton = findViewById(R.id.update_profile_btn);
        updateEmail = findViewById(R.id.update_email_et);
        updateName = findViewById(R.id.update_name_et);
        updateProfilePicture = findViewById(R.id.update_profile_picture_iv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Create Database reference
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            //Below function is called when the activity starts.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get the class which would retrieve the data (eg: User)
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    updateName.setText(String.format(user.getName()));
                    updateAge.setText(String.format(user.getAge()));
                    updateEmail.setText(String.format(user.getEmail()));
                    progressDialog.dismiss();
                }
            }

            //Below function is called if the event listener is cancelled.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfileActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateName.getText().toString();
                String email = updateEmail.getText().toString();
                String age = updateAge.getText().toString();

                User user = new User(name, email, age);
                databaseReference.setValue(user);
                Toast.makeText(UpdateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
