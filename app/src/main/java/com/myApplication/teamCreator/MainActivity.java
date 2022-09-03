package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button playerButton;
    Button gamesButton;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check current Config
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        } else {
            Objects.requireNonNull(getSupportActionBar()).show();
        }
        //Page Content
        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        //Player Button
        playerButton = findViewById(R.id.playerButton);
        playerButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlayerActivity.class)));
        //Games Button
        gamesButton = findViewById(R.id.gamesButton);
        gamesButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GamesActivity.class)));
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StartActivity.class)));
    }
}