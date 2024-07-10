package com.example.islamdip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Regpage extends AppCompatActivity {
    private DatabaseReference labDB;
    private String login_chek = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regpage);

        labDB = FirebaseDatabase.getInstance().getReference("User");

        Intent login_intent = new Intent(this, Loginpage.class);
        Intent main_intent = new Intent(this, MainActivity.class);

        EditText login = findViewById(R.id.textName);
        EditText password = findViewById(R.id.textPassword);
        EditText repeatPassword = findViewById(R.id.textRepeatPassword);
        TextView goLoginPage = findViewById(R.id.textViewLoginPage);

        Button btnSend = findViewById(R.id.buttonSend);

        /* ловим нажатие кнопки назад --- START
        из страницы регистрации на главную страницу */
        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(main_intent);
            }
        });
        //ловим нажатие кнопки назад --- END

        //ловим нажатие кнопки вход --- START
        goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login_intent);
            }
        });
        //ловим нажатие кнопки вход --- END




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = login.getText().toString();
                String pass = password.getText().toString();
                String rPass = repeatPassword.getText().toString();

                labDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds: snapshot.getChildren()){
                            User user = ds.getValue(User.class);
                            if (name.equals(user.login)) {
                                login_chek = user.login;
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                if(name.equals(login_chek)){
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please repeat password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(rPass)) {
                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User(name, pass);
                labDB.push().setValue(newUser);

            }
        });



    }
}