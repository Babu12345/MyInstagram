package com.example.myinstagram;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myinstagram.fragments.ComposeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
//        final Fragment fragment1 = new ComposeFragment();
        final Fragment fragment2 = new ComposeFragment();
//        final Fragment fragment3 = new ThirdFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = fragment2;
//                        finish();
                        break;
                    case R.id.action_compose:
                        fragment = fragment2;

                        break;
                    case R.id.action_profile:
                    default:
                        fragment = fragment2;

                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();




                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}
