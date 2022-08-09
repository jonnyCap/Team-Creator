package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Objects;

public class GamesActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<Game> games;
    public CustomGameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        dbHelper = new DBHelper(this);
        RecyclerView recyclerViewGame = findViewById(R.id.recyclerViewGame);

        ImageButton addGameButton = findViewById(R.id.addGameButton);
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamesActivity.this, AddGameActivity.class));
            }
        });
            this.games = dbHelper.fetchAllData();
            adapter = new CustomGameAdapter(this);
            adapter.setGames(this.games);
            recyclerViewGame.setAdapter(adapter);
            recyclerViewGame.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.games = dbHelper.fetchAllData();
        adapter.setGames(this.games);
    }
}