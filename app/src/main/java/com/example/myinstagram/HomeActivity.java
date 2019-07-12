package com.example.myinstagram;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myinstagram.fragments.ComposeFragment;
import com.example.myinstagram.fragments.HomeFragment;
import com.example.myinstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final FragmentManager fragmentManager = getSupportFragmentManager();


        Toolbar mytoolbar = findViewById(R.id.toolbar);

        
        mytoolbar.setLogo(R.drawable.nav_logo_whiteout);
//        setSupportActionBar(mytoolbar);
//        getSupportActionBar().setTitle("Hello");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // define your fragments here
        final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new ComposeFragment();
        final Fragment fragment3 = new ProfileFragment(ParseUser.getCurrentUser());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:

                        fragment = fragment1;
//                        finish();
                        break;
                    case R.id.action_compose:
                        fragment = fragment2;

                        break;
                    case R.id.action_profile:
                    default:
//                        showProgressBar();
                        fragment = fragment3;


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
