package com.example.sougata.famart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView signin;
    private TextView age;
    private ImageView profilePiture;

    //Instance variables
    private String uname;
    private String uemail;
    private String upass;
    private String uage;

    //Adding Firebase instances
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupUIViews();

        //Setting up Firebase
        firebaseAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //Upload data to database
                    String user_email = uemail;
                    String user_password = upass;
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            task.getResult();
                            if (task.isSuccessful()) {
                                //Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegisterActivity.this, SignInActivity.class));

                                //Send verify email to the user
                                sendEmailVerification();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.i("MY LOG : ", "Back button pressed");
        finish();
    }

    private void setupUIViews() {
        username = findViewById(R.id.username_et);
        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        register = findViewById(R.id.register_btn);
        signin = findViewById(R.id.signin_tv);
        age = findViewById(R.id.age_et);
        profilePiture = findViewById(R.id.profile_picture_iv);
    }

    private boolean validate() {
        uname = username.getText().toString().trim();
        uemail = email.getText().toString().trim();
        upass = password.getText().toString().trim();
        uage = age.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (uname.isEmpty() || uemail.isEmpty() || upass.isEmpty() || uage.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!uemail.matches(emailPattern)) {
            Toast.makeText(this, "Email should valid format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (upass.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(uage) < 18) {
            Toast.makeText(this, "Age cannot be less than 18", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendEmailVerification() {
        //Trying to get the new instance of FirebaseAuth object as the user is not yet created
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //add data to database
                        uploadUser();
                        Toast.makeText(RegisterActivity.this, "Registration Successful. Verification email sent.", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to sent verification email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadUser() {
        //Creating an instance of FirebaseDatabase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //Creating a database reference for the current user using its UUID
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        //Passing the user as a parameter to database reference
        databaseReference.setValue(new User(uname, uemail, uage));


    }
}
