package com.example.lcthack;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InsideContent extends AppCompatActivity {

    public int i = 0;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_content);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottm_navigation);
        bottomNavigationView.setSelectedItemId(R.id.inside_content);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.inside_content){
                    return true;
                } else if(item.getItemId() == R.id.services){
                    startActivity(new Intent(getApplicationContext()
                            , CalendarActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if(item.getItemId() == R.id.chat){
                    startActivity(new Intent(getApplicationContext()
                            , Chat.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });



        Button sign_out;
        sign_out = (Button)findViewById(R.id.sign_out);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(InsideContent.this, Login.class));
        }
    }



    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(InsideContent.this, Signinoptions.class);
        startActivity(intent);
    }
}