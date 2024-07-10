package com.example.islamdip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loginpage extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";

    private DatabaseReference labDB;

    private String login_chek = "";
    private String login_password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        Intent intent = new Intent(this, MainActivity.class);

        labDB = FirebaseDatabase.getInstance().getReference("User");

        EditText login = findViewById(R.id.textName);
        EditText password = findViewById(R.id.textPassword);
        Button btnSend = findViewById(R.id.buttonSend);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = login.getText().toString();
                String pass = password.getText().toString();

                labDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds: snapshot.getChildren()){
                            User user = ds.getValue(User.class);
                            if (name.equals(user.login)) {
                                login_chek = user.login;
                                login_password = user.password;
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                if(login_chek.isEmpty()){
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                    login_chek = "";
                    return;
                }
                if (!pass.equals(login_password)) {
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Done!!!", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", login_chek);
                editor.apply();

                startActivity(intent);

            }
        });

    }
}