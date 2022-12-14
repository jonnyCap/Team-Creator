package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AddGameActivity extends AppCompatActivity {

    ImageButton finalAddGameButton;
    EditText gameName;
    DBHelper dbHelper;
    private CustomGameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        //Check current Config
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Objects.requireNonNull(getSupportActionBar()).hide();
            //Close Button
            ImageButton closeBtn = findViewById(R.id.closeBtn);
            closeBtn.setOnClickListener(v -> finish());
        } else {
            Objects.requireNonNull(getSupportActionBar()).show();
        }
        //Page Content
        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        dbHelper = new DBHelper(this);
        adapter = new CustomGameAdapter( this);

        gameName = findViewById(R.id.gameName);

        finalAddGameButton = findViewById(R.id.finalAddGameButton);
        finalAddGameButton.setOnClickListener(v -> {
            String gameNameString = gameName.getText().toString();
            char startChar = gameNameString.charAt(0);
            if(gameNameString.trim().equals("")){
                Toast.makeText(AddGameActivity.this, "No game entered yet!", Toast.LENGTH_SHORT).show();
            }
            else if(Character.isDigit(startChar) ) {
                Toast.makeText(AddGameActivity.this, "Game must start with a Letter!", Toast.LENGTH_SHORT).show();
            }else if(nameExists(gameNameString)){
                Toast.makeText(AddGameActivity.this, "A Game with this name already exists!", Toast.LENGTH_SHORT).show();
            }
            else {
                adapter.setGames(dbHelper.addAndFetchAllData("Games", "game", gameNameString));
                finish();
            }
        });
    }
    private boolean nameExists(String gameName){
        ArrayList<String> gameNames = dbHelper.getGameNames();
        for (String name: gameNames) {
            if(gameName.equals(name)){
                return true;
            }
        }
        return false;
    }
}