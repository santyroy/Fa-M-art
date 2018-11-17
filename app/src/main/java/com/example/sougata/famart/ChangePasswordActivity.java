package com.example.sougata.famart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPassword;
    private Button newPasswordButton;

    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = findViewById(R.id.changepassword_et);
        newPasswordButton = findViewById(R.id.changepassword_btn);

        //getting the current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        newPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.length() > 5) {
                    firebaseUser.updatePassword(newPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Updated Password", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
