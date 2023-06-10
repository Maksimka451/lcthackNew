package com.example.lcthack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginKontrol extends AppCompatActivity {

    public EditText passwordInput;
    public EditText EmailInput;
    public String mail;
    public String pass;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 100;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_kontrol);

        passwordInput = findViewById(R.id.enter_password_oc);
        EmailInput = findViewById(R.id.login_oc);
        ImageButton button_password;
        button_password= findViewById(R.id.showPasswordEye);
        TextView sign_in;
        sign_in = findViewById(R.id.enterOC);


        button_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passwordInput.setTransformationMethod(null);

                        break;
                    case MotionEvent.ACTION_UP:
                        passwordInput.setTransformationMethod(new PasswordTransformationMethod());

                }
                return true;
            }
        });



        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = EmailInput.getText().toString();
                String password = passwordInput.getText().toString();
                signIn(Email,password);
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent contentIntent = new Intent(LoginKontrol.this, InsideKontrolContent.class);
            startActivity(contentIntent);
        }
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Database validate
                            final DatabaseReference Rootref;
                            Rootref = FirebaseDatabase.getInstance().getReference();
                            Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child("UsersKontrol").child(mAuth.getCurrentUser().getUid()).exists())
                                    {
                                        Toast.makeText(LoginKontrol.this, "Добро пожаловать, сотрудник КНО!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginKontrol.this,InsideKontrolContent.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginKontrol.this, "Wrong email or password!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginKontrol.this, "Wrong Email or password",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = EmailInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            EmailInput.setError("Required.");
            valid = false;
        } else {
            EmailInput.setError(null);
        }

        String password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Required.");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        return valid;
    }
}