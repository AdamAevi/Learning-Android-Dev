package com.example.learningsocialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText setupUserName, setupFullName, setupCountryName, setupWasteType;
    private Button saveInformationButton;
    private CircleImageView userProfileImage;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        progressBar = (ProgressBar) findViewById(R.id.setup_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        setupUserName = (EditText) findViewById(R.id.setup_username_entry);
        setupFullName = (EditText) findViewById(R.id.setup_fullname_entry);
        setupCountryName = (EditText) findViewById(R.id.setup_country_entry);
        setupWasteType = (EditText) findViewById(R.id.setup_waste_entry);

        saveInformationButton = (Button) findViewById(R.id.setup_save_info_button);
        userProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);

        saveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountSetupInformation();
            }
        });
    }

    private void saveAccountSetupInformation() {
        String userName = setupUserName.getText().toString();
        String fullName = setupFullName.getText().toString();
        String countryName = setupCountryName.getText().toString();
        String wasteType = setupWasteType.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Please enter a user name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(countryName)){
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(wasteType)){
            Toast.makeText(this, "Please enter a waste you're trying to reduce", Toast.LENGTH_SHORT).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(SetupActivity.this, "Connecting the dots", Toast.LENGTH_SHORT).show();

            HashMap userMap = new HashMap();
            userMap.put("userName",userName);
            userMap.put("fullName",fullName);
            userMap.put("countryName",countryName);
            userMap.put("wasteType",wasteType);
            userMap.put("status","I just started using The Learning Loop");
            userMap.put("gender","none");
            userMap.put("dateOfBirth","");
            userMap.put("userStatus","SuperUser");
            usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SetupActivity.this,"Saving your account details", Toast.LENGTH_LONG).show();
                        sendUserToMainActivity();
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error occurred:" + message, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}