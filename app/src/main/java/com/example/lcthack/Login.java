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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    public EditText passwordInput;
    public EditText EmailInput;
    public String mail;
    public String pass;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 100;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordInput = findViewById(R.id.secondName);
        passwordInput.setTransformationMethod(new PasswordTransformationMethod());
        EmailInput = findViewById(R.id.sername);
        //CheckBox button_password;
        TextView create_profile_btn;
        create_profile_btn=findViewById(R.id.textView4);
        //button_password= findViewById(R.id.checkBox2);
        TextView sign_in;
        sign_in = findViewById(R.id.enterOC2);

        try{
            Intent i = getIntent();
            mail = i.getStringExtra("mail");
            pass = i.getStringExtra("password");
        }catch (Exception e){}
        if(!(TextUtils.isEmpty(mail))){
            EmailInput.setText(mail);
            passwordInput.setText(pass);
        }





//        button_password.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        passwordInput.setTransformationMethod(null);
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        passwordInput.setTransformationMethod(new PasswordTransformationMethod());
//
//                }
//                return true;
//            }
//        });

        create_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
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

    public void onCheckBoxClicked (View view) {
        CheckBox checkBox = (CheckBox) view;
        if(checkBox.isChecked()){
            passwordInput.setTransformationMethod(null);
        } else {
            passwordInput.setTransformationMethod(new PasswordTransformationMethod());
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent contentIntent = new Intent(Login.this, InsideContent.class);
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
                            Intent i = new Intent(Login.this,InsideContent.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Wrong Email or password",
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