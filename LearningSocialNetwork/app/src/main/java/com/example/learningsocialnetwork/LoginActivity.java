package com.example.learningsocialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail, userPassword;
    private TextView needNewAccountLink;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        needNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_create_account_button);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
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

    private void userLogin() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, "Connecting the dots", Toast.LENGTH_SHORT).show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Logging you in", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity();
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error occurred:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToMainActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}