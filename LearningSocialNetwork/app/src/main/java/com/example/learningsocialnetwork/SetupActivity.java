package com.example.learningsocialnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText userName, fullName, countryName, wasteType;
    private Button saveInformationButton;
    private CircleImageView userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        userName = (EditText) findViewById(R.id.setup_username_entry);
        fullName = (EditText) findViewById(R.id.setup_fullname_entry);
        countryName = (EditText) findViewById(R.id.setup_country_entry);
        wasteType = (EditText) findViewById(R.id.setup_waste_entry);

        saveInformationButton = (Button) findViewById(R.id.setup_save_info_button);
        userProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);

    }
}