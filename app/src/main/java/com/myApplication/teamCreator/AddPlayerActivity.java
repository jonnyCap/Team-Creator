package com.myApplication.teamCreator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Objects;

public class AddPlayerActivity extends AppCompatActivity {

    ImageButton finalAddPlayerButton;
    EditText playerName;
    Spinner playerDefaultStrengthSpinner;
    CheckBox adjustIndividualStrenghCeckBox;
    private CustomPlayerAdapter adapter;
    private DBHelper dbHelper;
    private final ArrayList<String> newPlayerStrengthOptions= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        //Check current Config
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            //Close Button
            ImageButton closeBtn = findViewById(R.id.closeBtn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            getSupportActionBar().show();
        }
        //Page Content
        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        dbHelper = new DBHelper(this);
        adapter = new CustomPlayerAdapter(this);
        //Spinner
        playerDefaultStrengthSpinner = findViewById(R.id.playerDefaultStrengthSpinner);
        setOptionsToArray();
        ArrayAdapter<String> gameSpinnerAdapter= new ArrayAdapter<>(this, R.layout.spinner_item, this.newPlayerStrengthOptions);
        gameSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        playerDefaultStrengthSpinner.setAdapter(gameSpinnerAdapter);
        //Shared Preference
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        //Elements
        adjustIndividualStrenghCeckBox = findViewById(R.id.adjustIndividualStrengthCheckBox);
        playerName = findViewById(R.id.playerName);

        finalAddPlayerButton = findViewById(R.id.finalAddPlayerButton);
        finalAddPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerNameString = playerName.getText().toString();
                String playerDefaultStrengthString = playerDefaultStrengthSpinner.getSelectedItem().toString();
                int playerDefaultStrengthInt;
                if(playerNameString.trim().equals("")){
                    Toast.makeText(AddPlayerActivity.this, "No name entered yet!", Toast.LENGTH_SHORT).show();
                }else if(nameExists(playerNameString)){
                    Toast.makeText(AddPlayerActivity.this, "A Player with this name already exists!", Toast.LENGTH_SHORT).show();
                }else {
                    playerDefaultStrengthInt = Integer.parseInt(playerDefaultStrengthString);
                    adapter.setPlayers(dbHelper.AddAndFetchAllPlayerData("Players", "name", "defaultStrength", playerNameString, playerDefaultStrengthInt));
                    if(adjustIndividualStrenghCeckBox.isChecked()){
                        editor.putString("fullUsername", playerNameString);
                        if(playerNameString.length() > 10){
                            char point = '.';
                            String currentPlayerShort = playerNameString.substring(0, 8) + point;
                            editor.putString("username", currentPlayerShort);
                        }else{
                            editor.putString("username", playerNameString);
                        }
                        editor.apply();
                        startActivity(new Intent(AddPlayerActivity.this, PlayerInspectActivity.class));
                    }
                    else{
                        finish();
                    }
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
    private boolean nameExists(String playerName){
        ArrayList<String> playerNames = dbHelper.getPlayerNames();
        for (String name: playerNames) {
            if(playerName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
