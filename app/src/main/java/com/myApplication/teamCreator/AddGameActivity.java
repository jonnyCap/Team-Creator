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
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
        //Page Content
        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        dbHelper = new DBHelper(this);
        adapter = new CustomGameAdapter( this);

        gameName = findViewById(R.id.gameName);
        finalAddGameButton = findViewById(R.id.finalAddGameButton);
        finalAddGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameNameString = gameName.getText().toString();
                char startChar = gameNameString.charAt(0);
                if(gameNameString.trim().equals("")){
                    Toast.makeText(AddGameActivity.this, "No game entered yet!", Toast.LENGTH_SHORT).show();
                }
                if(Character.isDigit(startChar) ) {
                    Toast.makeText(AddGameActivity.this, "Game must start with a Letter!", Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.setGames(dbHelper.addAndFetchAllData("Games", "game", gameNameString));
                    finish();
                }
            }
        });
    }
}