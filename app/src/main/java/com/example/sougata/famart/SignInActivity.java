package com.example.sougata.famart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private TextView attempts;
    private TextView register;
    private Button login;
    private int countAttempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        name = findViewById(R.id.name_et);
        password = findViewById(R.id.password_et);
        attempts = findViewById(R.id.attempts_tv);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_tv);
        final String s = SignInActivity.this.getString(R.string.no_of_incorrect_attempts);
        attempts.setText(s + countAttempts);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(name.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
                    String n = name.getText().toString();
                    String p = password.getText().toString();
                    if (n.equals("admin") && p.equals("1234")) {
                        Intent intent = new Intent(SignInActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        if (countAttempts >= 0) {
                            countAttempts--;
                            attempts.setText(s + countAttempts);
                            if (countAttempts == 0) {
                                login.setEnabled(false);
                            }
                        }
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                }
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
        Log.i("MY LOG : ","Back button pressed");
        finish();
    }
}
