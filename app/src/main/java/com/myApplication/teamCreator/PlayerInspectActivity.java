package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerInspectActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private final ArrayList<String> newPlayerStrengthOptions= new ArrayList<>();
    private Spinner newStrengthOptionSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_player);
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

        TextView inspectPlayerTextView = findViewById(R.id.inspectPlayerName);
        RecyclerView recyclerViewPlayerStrength = findViewById(R.id.recyclerViewPlayerStrength);
        //Header
        SharedPreferences pref = getApplication().getSharedPreferences("MyPref", 0);
        String playerName = pref.getString("username", "");
        inspectPlayerTextView.setText(playerName);
        //RecyclerView
        ArrayList<String> playerStrengthArray = dbHelper.fetchAllDataString();
        playerStrengthArray.add(0, "Default Strength");
        CustomPlayerStrengthAdapter adapter = new CustomPlayerStrengthAdapter(this);
        adapter.setGameAndStrength(playerStrengthArray);
        recyclerViewPlayerStrength.setAdapter(adapter);
        recyclerViewPlayerStrength.setLayoutManager(new LinearLayoutManager(this));
        //Spinner
        setOptionsToArray();
        newStrengthOptionSpinner = findViewById(R.id.newStrengthOptionsSpinner);
        ArrayAdapter<String> gameSpinnerAdapter;
        gameSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_white, this.newPlayerStrengthOptions);
        gameSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        newStrengthOptionSpinner.setAdapter(gameSpinnerAdapter);
        //Back Button
        ImageButton backButton = findViewById(R.id.closeBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPlayerStrengthAdapter.setSelectedGame("");
                finish();
            }
        });
        //Update Strength on Specific Games
        ImageButton submitSpecificStrength = findViewById(R.id.submitNewSpecificStrength);
        submitSpecificStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStrengthString = newStrengthOptionSpinner.getSelectedItem().toString();
                int newStrength = Integer.parseInt(newStrengthString);
                String playerName = pref.getString("fullUsername", "");
                if(CustomPlayerStrengthAdapter.getSelectedGame().equals("")){
                    Toast.makeText(PlayerInspectActivity.this, "Choose a game!", Toast.LENGTH_SHORT).show();
                }else {
                    dbHelper.updateColumn(CustomPlayerStrengthAdapter.getSelectedGame(), newStrength, playerName);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void setOptionsToArray(){
        for(int i = 0; i < 11; i++) {
            String variable = String.valueOf(i);
            this.newPlayerStrengthOptions.add(variable);
        }
    }

}