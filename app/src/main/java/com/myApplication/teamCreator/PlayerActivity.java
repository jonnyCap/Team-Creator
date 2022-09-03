package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<Player> players;
    private CustomPlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
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
        RecyclerView recyclerViewPlayer = findViewById(R.id.recyclerViewPlayer);

        ImageButton addPlayerButton = findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(v -> startActivity(new Intent(PlayerActivity.this, AddPlayerActivity.class)));

            this.players = dbHelper.fetchAllPlayerData();
            adapter = new CustomPlayerAdapter(this);
            adapter.setPlayers(this.players);
            recyclerViewPlayer.setAdapter(adapter);
            recyclerViewPlayer.setLayoutManager(new LinearLayoutManager(this));
    }
    protected void onResume() {
        super.onResume();
        this.players = dbHelper.fetchAllPlayerData();
        adapter.setPlayers(this.players);
    }
}