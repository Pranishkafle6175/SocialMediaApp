package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagram.Fragments.HomeFragment;
import com.example.instagram.Fragments.NotificationFragment;
import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragmentinstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case (R.id.nav_home):
                        // getting the instance of the fragment class inside the fragment type variable
                        fragmentinstance = new HomeFragment();
                        break;

                    case (R.id.nav_search):
                        fragmentinstance = new SearchFragment();
                        break;

                    case (R.id.nav_add):
                        fragmentinstance = null;
                        startActivity(new Intent(MainActivity.this , PostActivity.class));
                        break;

                    case (R.id.nav_heart):
                        fragmentinstance = new NotificationFragment();
                        break;

                    case(R.id.nav_profile):
                        fragmentinstance = new ProfileFragment();
                        break;
                }

                if(fragmentinstance != null){

                    /*
                    * In some cases, you may see getSupportFragmentManager() being called without a reference to a FragmentActivity instance.
                    * This can happen when you're calling the method from within a class that extends FragmentActivity or another class
                    * that has access to a FragmentManager instance, such as a class that extends Fragment.

                        For example, if you have a class that extends FragmentActivity and you want to add a fragment to the activity,
                         you can call getSupportFragmentManager() directly within the class:
                    * */
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,fragmentinstance).commit();
                }

                return true;
            }
        });

        //By defeault before the user presses the navigation view our home fragment should appear
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
}