package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView instagramlogo =(ImageView)findViewById(R.id.instagramlogo);
        ImageView instagramimage = (ImageView) findViewById(R.id.instalinearlayout);
        Button loginbuttonstart = (Button) findViewById(R.id.loginbutton);
        Button registerbuttonstart = (Button) findViewById(R.id.registerbutton);
        LinearLayout linearLayoutstart=(LinearLayout) findViewById(R.id.startlinearlayout);

        instagramlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instagramlogo.setVisibility(View.INVISIBLE);
                linearLayoutstart.setVisibility(View.VISIBLE);
            }
        });

        registerbuttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });

        loginbuttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if the app has been launched before
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            // If it's the first launch, set the flag to false and redirect to the login screen
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply();
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        } else {
            // If it's not the first launch, check if the user is already logged in
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        }
    }
}
