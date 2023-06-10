package com.example.lcthack;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Signinoptions extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinoptions);

        LinearLayout kontrol_sign;
        kontrol_sign = findViewById(R.id.enter_control);
        LinearLayout business_sign;
        business_sign = findViewById(R.id.enter_buiseness);

        kontrol_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signinoptions.this, LoginKontrol.class);
                startActivity(intent);
            }
        });

        business_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signinoptions.this, Login.class);
                startActivity(intent);
            }
        });
    }
}