package com.myApplication.teamCreator;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private CustomPlayerAdapter adapter;
    private DBHelper dbHelper;
    private final ArrayList<String> newPlayerStrengthOptions= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

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
                }else {
                    playerDefaultStrengthInt = Integer.parseInt(playerDefaultStrengthString);
                    adapter.setPlayers(dbHelper.AddAndFetchAllPlayerData("Players", "name", "defaultStrength", playerNameString, playerDefaultStrengthInt));
                    finish();
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
