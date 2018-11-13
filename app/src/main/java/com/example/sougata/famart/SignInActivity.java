package com.example.sougata.famart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView attempts;
    private TextView register;
    private Button login;
    private ProgressDialog progressDialog;
    private int countAttempts = 5;

    //Add Firebase authentication service
    private FirebaseAuth firebaseAuth;

    //get the String value for attempts
    private String attemptString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.name_et);
        password = findViewById(R.id.password_et);
        attempts = findViewById(R.id.attempts_tv);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_tv);
        attemptString = SignInActivity.this.getString(R.string.no_of_incorrect_attempts);
        attempts.setText(String.format(Locale.getDefault(), "%s%d", attemptString, countAttempts));

        //Instantiating Firebase auth component
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);

        //Getting the current user from Firebase
        //Check with the database if a user is already logged into the app
        //if the user has logged in we must take the user to next activity without
        //asking for logging again
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //destroys current activity
            finish();
            //starting the next activity
            startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = email.getText().toString();
                String p = password.getText().toString();
                validate(e, p);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i("MY LOG : ", "Back button pressed");
        finish();
    }

    public void validate(String email, String password) {

        //Check if the field are not empty/blank
        if (signinCheck(email, password)) {
            //start progress dialog
            progressDialog.setMessage("Verifying...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        //Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));

                        //Implement check for email verification
                        emailVerificationCheck();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        countAttempts--;
                        attempts.setText(String.format(Locale.getDefault(), "%s%d", attemptString, countAttempts));
                        if (countAttempts == 0) {
                            login.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    private boolean signinCheck(String uemail, String upass) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (uemail.isEmpty() || upass.isEmpty()) {
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
        return true;
    }

    private void emailVerificationCheck() {
        //FirebaseAuth.getInstance() is used to get the existing user's instance
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag) {
            finish();
            startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));
        } else {
            Toast.makeText(SignInActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
