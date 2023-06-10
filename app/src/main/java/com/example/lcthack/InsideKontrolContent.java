package com.example.lcthack;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InsideKontrolContent extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;


    TextView date, kno, type, time, theme, email, id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_kontrol_content);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        date = findViewById(R.id.textView31);
        kno = findViewById(R.id.textView32);
        type = findViewById(R.id.textView33);
        time = findViewById(R.id.textView34);
        theme = findViewById(R.id.textView35);


        final DatabaseReference UserRef;
        UserRef = FirebaseDatabase.getInstance().getReference();

        UserRef.child("Consulting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemControlBanner> items = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User2 user2 = userSnapshot.getValue(User2.class);
                    String control = userSnapshot.getKey(); // получаем ID текущего пользователя
                    DatabaseReference userDataRef = UserRef.child("Consulting").child(control);
                    System.out.println(control);
                    userDataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot controlSnapshot) {
                            // обработка данных, полученных из базы данных
                            System.out.println(controlSnapshot.getValue());

                                ItemControlBanner itemControlBanner = controlSnapshot.getValue(ItemControlBanner.class);
                                items.add(itemControlBanner);


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // обработка ошибок
                        }
                    });

                }
                AdapterForControlList adapter = new AdapterForControlList(items);
                recyclerView.setAdapter(adapter);

        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            });


        Button sign_out_kontrol;
        sign_out_kontrol = (Button)findViewById(R.id.sign_out_kontrol);

        sign_out_kontrol.setOnClickListener(view -> signOut());

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(InsideKontrolContent.this, Login.class));
        }
    }



    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(InsideKontrolContent.this, Signinoptions.class);
        startActivity(intent);
    }
}