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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText emailId;
    EditText pwd;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.login);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = emailId.getText().toString();
                String user_password = pwd.getText().toString();

                if(user_email.isEmpty()){
                    emailId.setError("Please enter Email");
                    emailId.requestFocus();
                }
                else if(user_password.isEmpty()){
                    pwd.setError("Please enter Password");
                    pwd.requestFocus();
                }
                else {
                mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Sucessfully Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn.this, HomeActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(SignIn.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }
        });
    }

    //@Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
           startActivity(new Intent(SignIn.this, HomeActivity.class));
            finish();
        }else
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
    }
}
