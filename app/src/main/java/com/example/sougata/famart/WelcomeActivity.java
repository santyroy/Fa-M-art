package com.example.sougata.famart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    //Initializing the views
    private Button logout;

    //Create Firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Instantiating Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Instantiating the views
        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutMethod();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_menu:
                logoutMethod();
                break;

            case R.id.settings_menu:
                //TODO: Add settings option
                break;

            case R.id.refresh_menu:
                //TODO: Add refresh option
                break;

            case R.id.profile_menu:
                startActivity(new Intent(WelcomeActivity.this, ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutMethod() {
        String email = firebaseAuth.getCurrentUser().getEmail();
        Log.i("EMAIL : ", email);
        if (email != null) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));
            Toast.makeText(WelcomeActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
        }
    }
}
