package com.example.lcthack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Chat extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottm_navigation);
        bottomNavigationView.setSelectedItemId(R.id.chat);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                 if(item.getItemId() == R.id.inside_content){
                    startActivity(new Intent(getApplicationContext()
                            , InsideContent.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if(item.getItemId() == R.id.services){
                    startActivity(new Intent(getApplicationContext()
                            , CalendarActivity.class));
                     overridePendingTransition(0,0);
                } else if(item.getItemId() == R.id.chat){
                    return true;
                }
                return false;
            }
        });
    }
}