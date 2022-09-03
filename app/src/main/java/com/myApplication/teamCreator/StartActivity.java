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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private Spinner selectGameSpinner, selectTeamBalancingSpinner;
    private EditText amountOfTeamsEdtTxt;
    private CheckBox selectAllCheckBox;
    private CustomSelectPlayerAdapter adapter;
    private final String[] balancingMethods = new String[4];
    ArrayList<Game> games;
    ArrayList<Player> players;

    private static ArrayList<Player> selectedPlayers = new ArrayList<>();
    private static String finalGame;
    private static int teamsAmount;
    private static String finalBalancingMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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

        DBHelper dbHelper = new DBHelper(this);
        RecyclerView selectPlayerRecyclerview = findViewById(R.id.selectPlayerRecyclerview);

        //final Button
        ImageButton finalCreateTeamsButton = findViewById(R.id.finalCreateTeamsButton);
        finalCreateTeamsButton.setOnClickListener(v -> {
            selectedPlayers = adapter.getSelectedPlayers();
            if(games.size() == 0){
                Toast.makeText(StartActivity.this, "You must create a Game first!", Toast.LENGTH_SHORT).show();
            }else if(players.size() == 0){
                Toast.makeText(StartActivity.this, "You must add Players first!", Toast.LENGTH_SHORT).show();
            }else {
                finalGame = selectGameSpinner.getSelectedItem().toString();
                //Muss noch mit Try und catch versehen werden
                if (amountOfTeamsEdtTxt.getText().toString().trim().equals("")) {
                    Toast.makeText(StartActivity.this, "Enter Number of Teams!", Toast.LENGTH_SHORT).show();
                } else {
                    teamsAmount = Integer.parseInt(amountOfTeamsEdtTxt.getText().toString());
                    finalBalancingMethod = selectTeamBalancingSpinner.getSelectedItem().toString();
                    if (teamsAmount > adapter.getSelectedPlayersAmount() && adapter.getSelectedPlayersAmount() != 0 && !selectAllCheckBox.isChecked()) {
                        Toast.makeText(StartActivity.this, "Cannot create more Teams than Players!", Toast.LENGTH_SHORT).show();
                    } else if (teamsAmount == 0) {
                        Toast.makeText(StartActivity.this, "Cannot create 0 Teams!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (adapter.getSelectedPlayersAmount() == 0 || selectAllCheckBox.isChecked()) {
                            selectedPlayers = adapter.getAllPlayers();
                        }
                        startActivity(new Intent(StartActivity.this, BackgroundActivity.class));
                    }
                }
            }
        });

        //Checkbox for Select All
        selectAllCheckBox = findViewById(R.id.selectAllPlayersCheckBox);
        //spinner for Game
        selectGameSpinner = findViewById(R.id.selectGameSpinner);
        games = dbHelper.fetchAllData();
        ArrayList<String> gamesString = arrayToString(games);
        ArrayAdapter<String> gameSpinnerAdapter= new ArrayAdapter<>(this, R.layout.spinner_item, gamesString);
        gameSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        selectGameSpinner.setAdapter(gameSpinnerAdapter);

        //Spinner
        selectTeamBalancingSpinner = findViewById(R.id.selectTeamBalancingSpinner);
        this.balancingMethods[0] = "balanced";
        this.balancingMethods[1] = "strongest with weakest";
        this.balancingMethods[2] = "strongest with strongest";
        this.balancingMethods[3] = "random";

        ArrayAdapter<String> teamBalancingAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, balancingMethods);
        teamBalancingAdapter.setDropDownViewResource(R.layout.spinner_item);
        selectTeamBalancingSpinner.setAdapter(teamBalancingAdapter);

        //Edittext
        amountOfTeamsEdtTxt = findViewById(R.id.numberOfTeamsEdtTxt);

        //Recyclerview
        players = dbHelper.fetchAllPlayerData();
        adapter = new CustomSelectPlayerAdapter();
        adapter.setPlayers(players);
        selectPlayerRecyclerview.setAdapter(adapter);
        selectPlayerRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }
    private ArrayList<String> arrayToString(ArrayList<Game> games){
        ArrayList<String> newArray = new ArrayList<>();
        for(int i = 0; i < games.size(); i++){
            newArray.add(games.get(i).getName());
        }
        return  newArray;
    }
    public static ArrayList<Player> getSelectedPlayers(){
        return selectedPlayers;
    }
    public static String getFinalGame() {
        return finalGame;
    }
    public static int getTeamsAmount() {
        return teamsAmount;
    }
    public static String getFinalBalancingMethod() {
        return finalBalancingMethod;
    }
}