package com.example.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailId;
    EditText pwd;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = emailId.getText().toString();
                String user_password = pwd.getText().toString();
                if(user_password.length() < 6)
                    Toast.makeText(SignUp.this, "Password must be 6 characters long", Toast.LENGTH_SHORT).show();
                if(user_email.isEmpty()){
                    emailId.setError("Please enter Email");
                    emailId.requestFocus();
                }
                else if(user_password.isEmpty()){
                    pwd.setError("Please enter Password");
                    pwd.requestFocus();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, HomeActivity.class));
                            }else
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
}
