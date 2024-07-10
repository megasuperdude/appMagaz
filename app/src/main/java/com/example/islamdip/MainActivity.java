package com.example.islamdip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---Устанавливаем верхни бар и название на нем ---START
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Main");
        setSupportActionBar(toolbar);

        SearchView srv = toolbar.findViewById(R.id.search_bar);
        //srv.setQueryHint("test hint");
        //---Устанавливаем верхни бар и название на нем ---END

        srv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                return false;
            }
        });



        bottomBar = findViewById(R.id.bottom_bar);

        //установить дефолтный фрагмент
        replace(new mainFragment());

        //Start-------------------------------
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getTitle().toString()) {

                    case "main":
                        toolbar.setTitle("Main");
                        toolbar.findViewById(R.id.search_bar).setVisibility(View.VISIBLE);
                        replace(new mainFragment());
                        break;

                    case "cart":

                        toolbar.setTitle("Cart");
                        toolbar.findViewById(R.id.search_bar).setVisibility(View.INVISIBLE);
                        replace(new cartFragment());

                        break;

                    case "mutually":
                        toolbar.setTitle("Mutually");
                        toolbar.findViewById(R.id.search_bar).setVisibility(View.INVISIBLE);
                        replace(new mutuallyFragment());
                        break;

                    case "profile":
                        toolbar.setTitle("Profile");
                        toolbar.findViewById(R.id.search_bar).setVisibility(View.INVISIBLE);
                        replace(new fragment_profile());
                        break;
                }


                return true;
            }
        });
        //End-------------------------------

    }

    //Start-------------------------------
    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.framelayout, fragment);

        transaction.setReorderingAllowed(true);
        transaction.commitAllowingStateLoss();
    }
    //End-------------------------------
}