package com.example.learningsocialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Instantiate Views to manipulate and read from
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private Toolbar mToolbar;

    // Instantiate Firebase Login / DB
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    // When this activity is created, start doing the following
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Instances of Firebase details
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Creating a Toolbar at the top of the screen
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        // Creating a Drawer that holds the options for navigation
        // Allows navigation to profile, messages, settings, logout, etc
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Creating a navigation view that populates the drawer created above
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        // A method on the navigation view that responds to specific button presses from the user
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });
    }

    // On startup, check if a user is logged in.
    // If not - go to Login Activity
    // Else - check if user exists in the DB with a method
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            sendUserToLoginActivity();
        }else{
            checkUserExistence();
        }
    }

    private void checkUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            // If we can't find the user in the DB - create their profile
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id)){
                    sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendUserToSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    // Method to choose action based on input of drawer / navigation view
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                Toast.makeText(this,"Viewing Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_home:
                Toast.makeText(this,"Going Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friends:
                Toast.makeText(this,"Opening Friends List", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find_friends:
                Toast.makeText(this,"Finding Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messages:
                Toast.makeText(this,"Opening Messages", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this,"Accessing Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
//                Toast.makeText(this,"Logging you out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }

    }
}