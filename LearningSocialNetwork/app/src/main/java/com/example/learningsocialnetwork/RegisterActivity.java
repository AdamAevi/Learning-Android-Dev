package com.example.learningsocialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userConfirmPassword, progressBarMessage;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userEmail = (EditText) findViewById(R.id.register_email);
        userPassword = (EditText) findViewById(R.id.register_password);
        userConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        createAccountButton = (Button) findViewById(R.id.register_create_account_button);
        progressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    // On startup, check if a user is logged in.
    // If so - go to Main Activity

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendUserToMainActivity();
        }
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_LONG).show();
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Password Mismatch!", Toast.LENGTH_LONG).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(RegisterActivity.this, "Connecting the dots", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            sendToSetupActivity();
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this, "Moving to Account Setup!", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Error occurred:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void sendUserToMainActivity() {
        Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
    }


    private void sendToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}