package com.myApplication.teamCreator;

import androidx.annotation.NonNull;
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
import java.util.Arrays;
import java.util.Objects;

public class CreateTeamsActivity extends AppCompatActivity {

    private final DBHelper dbHelper = new DBHelper(this);

    private ArrayList<Player> selectedPlayers = new ArrayList<>();
    private Player[] sortedPlayers;
    private int playerAmount;
    private final ArrayList<Team> finishedTeamsList = new ArrayList<>();
    private Team[] finishedTeams;
    private String selectedGame;
    private int teamsAmount;
    private int finishedTeamsCounter;
    private String finalBalancingMethod;
    private double entireStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teams);

        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        selectedPlayers = StartActivity.getSelectedPlayers();
        selectedGame = StartActivity.getFinalGame();
        teamsAmount = StartActivity.getTeamsAmount();
        finalBalancingMethod = StartActivity.getFinalBalancingMethod();

        this.playerAmount = this.selectedPlayers.size();
        this.sortedPlayers = new Player[playerAmount];
        this.finishedTeams = new Team[this.teamsAmount];
        this.finishedTeamsCounter = 0;

        RecyclerView finishedTeamsRecyclerView = findViewById(R.id.finishedTeamsRecyclerView);
        CustomFinishedTeamsAdapter adapter = new CustomFinishedTeamsAdapter(this);

        //------------------------------Hier startet die Teamerstellung------------------------
        doCalculations();
        //----------------------------Hier der RecyclerView-------------------------------------
        ImageButton redoTeamsWNS = findViewById(R.id.redoTeamsWNS);
        redoTeamsWNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateTeamsActivity.this, StartActivity.class));
            }
        });
        ImageButton redoTeamsWOS = findViewById(R.id.redoTeamsWOS);
        redoTeamsWOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(CreateTeamsActivity.this, BackgroundActivity.class));
            }
        });
        //RecyclerView
        adapter.setFinishedTeams(this.finishedTeamsList);
        finishedTeamsRecyclerView.setAdapter(adapter);
        finishedTeamsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setSpecificStrengthToAllPlayer(){
        for(int i = 0; i < this.playerAmount; i++){
            setSpecificStrength(this.sortedPlayers[i], this.selectedGame);
        }
    }

    private void setSpecificStrength(Player player, String game){
        int specificStrength = dbHelper.returnSpecificStrength(game, player.getName());
        player.setSpecificStrength(specificStrength);
    }

    private void arrayListToArray(ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = new Player(players.get(i).getName(), players.get(i).getDefaultStrength());
            player.setSpecificStrength(players.get(i).getSpecificStrength());
            this.sortedPlayers[i] = player;
        }
    }
    private void arrayToArrayList(Team[] teams){
        this.finishedTeamsList.addAll(Arrays.asList(teams));
    }
    private void sortPlayers(){
        boolean playerSorted = false;
        while(!playerSorted){
            playerSorted = true;
            for(int i = 0; i < this.playerAmount - 1; i++) {
                if (this.sortedPlayers[i].getSpecificStrength() < this.sortedPlayers[i + 1].getSpecificStrength()) {
                    Player copie = this.sortedPlayers[i];
                    this.sortedPlayers[i] = this.sortedPlayers[i + 1];
                    this.sortedPlayers[i + 1] = copie;
                    playerSorted = false;
                }
                else if(this.sortedPlayers[i].getSpecificStrength() == this.sortedPlayers[i + 1].getSpecificStrength()){
                    int x = getRandomNumber(0, 10);
                    int y = getRandomNumber(0, 10);
                    if(x < y){
                        Player copie = this.sortedPlayers[i];
                        this.sortedPlayers[i] = this.sortedPlayers[i + 1];
                        this.sortedPlayers[i + 1] = copie;
                        playerSorted = false;
                    }
                }
            }
        }
    }
    private void setTeamSize(){
        if(checkForEvenTeams()){
            int teamSize = this.playerAmount/this.teamsAmount;
            for(int i = 0; i < this.teamsAmount; i++) {
                this.finishedTeams[i] = new Team(teamSize);
            }
        }else{
            //Local variables
            int rest = this.playerAmount % this.teamsAmount;
            int evenPlayerAmount = this.playerAmount - rest;
            int evenTeamSize = evenPlayerAmount/this.teamsAmount;
            for(int i = 0; i < this.teamsAmount; i++){
                if(rest > 0){
                    int teamsSize = evenTeamSize + 1;
                    this.finishedTeams[i] = new Team(teamsSize);
                    rest--;
                }else{
                    this.finishedTeams[i] = new Team(evenTeamSize);
                }
            }
        }
    }
    private boolean checkForEvenTeams(){
        return this.playerAmount % this.teamsAmount == 0;
    }
    private void balanceTeams(double averageStrength, double deviationValue, ArrayList<Team> teams){

        boolean teamsSorted;
        this.finishedTeamsCounter = 0;

        ArrayList<Team> newTeams = new ArrayList<>();
        for(int i = 0; i < teams.size(); i++){
            Team team = new Team(teams.get(i).getTeamMembers());
            newTeams.add(team);
        }

        teamsSorted = false;
        while(!teamsSorted) {
            teamsSorted = true;
            for (int i = 0; i < newTeams.size(); i++) {
                double strengthDif = newTeams.get(i).getAverageTeamStrength() - averageStrength;
                if (Math.abs(strengthDif) > deviationValue) {
                    int firstTeamIndex = getRandomNumber(0, newTeams.size() - 1);
                    int secondTeamIndex = getRandomNumber(0, newTeams.size() -1);
                    int firstMemberIndex = getRandomNumber(0, newTeams.get(firstTeamIndex).getTeamMembersAmount() -1);
                    int secondMemberIndex = getRandomNumber(0, newTeams.get(secondTeamIndex).getTeamMembersAmount() - 1);
                    Player copie = newTeams.get(firstTeamIndex).getTeamMember(firstMemberIndex);
                    newTeams.get(firstTeamIndex).setTeamMember(newTeams.get(secondTeamIndex).getTeamMember(secondMemberIndex), firstMemberIndex);
                    newTeams.get(secondTeamIndex).setTeamMember(copie, secondMemberIndex);
                    teamsSorted = false;
                    deviationValue += 0.0025;

                }else {
                    Team team = new Team(newTeams.get(i).getTeamMembers());
                    this.finishedTeams[this.finishedTeamsCounter] = team; //Es ist fertig und wird von der Array liste nicht mehr bearbeitet
                    this.finishedTeamsCounter++;
                    newTeams.remove(i);
                    if(newTeams.size() > 0) {
                        //Set new average and increase deviation value
                        averageStrength = getAverageStrength(newTeams);
                        deviationValue = 0.05;
                        teamsSorted = false;
                    }
                }
            }
        }
    }
    private void setTeams(@NonNull String finalBalancingMethod){

        switch(finalBalancingMethod){
            case "balanced":
                double averageStrength = getAverageStrength();
                double deviationValue = 0.05;
                setTeams("random");

                ArrayList<Team> teams;
                teams = new ArrayList<>(Arrays.asList(this.finishedTeams).subList(0, this.teamsAmount));
                balanceTeams(averageStrength, deviationValue, teams);

                break;
            case "strongest with weakest":
                int playerIndexFront = 0;
                int playerIndexBack = this.playerAmount -1;
                boolean even;
                int x = getRandomNumber(0,1);
                even = x == 0;
                for(int i = 0; i < this.teamsAmount; i++) {
                    for(int j = 0; j < this.finishedTeams[i].getTeamMembersAmount(); j++) {
                        if(even){
                            this.finishedTeams[i].setTeamMember(sortedPlayers[playerIndexFront], j);
                            playerIndexFront++;
                            even = false;
                        }else {
                            this.finishedTeams[i].setTeamMember(sortedPlayers[playerIndexBack], j);
                            playerIndexBack--;
                            even = true;
                        }
                    }
                }
                break;

            case "strongest with strongest":
                int playerIndex = 0;
                for(int i = 0; i < this.teamsAmount; i++) {
                    for(int j = 0; j < this.finishedTeams[i].getTeamMembersAmount(); j++) {
                        this.finishedTeams[i].setTeamMember(sortedPlayers[playerIndex], j);
                        playerIndex++;
                    }
                }
                break;
            case "random":
                int randomFactor = getRandomNumber(0,2);
                switch(randomFactor) {
                    case 0:
                        setTeams("balanced");
                        break;
                    case 1:
                        setTeams("strongest with weakest");
                        break;
                    case 2:
                        setTeams("strongest with strongest");
                        break;
                    }
                    int repetition = getRandomNumber(500, 1500);
                    for(int i = 0; i < repetition; i++){
                        int firstTeamIndex = getRandomNumber(0, this.teamsAmount -1);
                        int secondTeamIndex = getRandomNumber(0, this.teamsAmount -1);
                        int firstMemberIndex = getRandomNumber(0, this.finishedTeams[firstTeamIndex].getTeamMembersAmount() - 1);
                        int secondMemberIndex = getRandomNumber(0, this.finishedTeams[secondTeamIndex].getTeamMembersAmount() - 1);

                        Player copie = this.finishedTeams[firstTeamIndex].getTeamMember(firstMemberIndex);
                        this.finishedTeams[firstTeamIndex].setTeamMember( this.finishedTeams[secondTeamIndex].getTeamMember(secondMemberIndex), firstMemberIndex);
                        this.finishedTeams[secondTeamIndex].setTeamMember(copie, secondMemberIndex);
                    }
                break;

            default:
                break;
        }
    }
    private double getAverageStrength(){
        this.entireStrength = 0d;
        for(int i = 0; i < this.playerAmount; i++){
            this.entireStrength += this.sortedPlayers[i].getSpecificStrength();
        }
        return this.entireStrength/ this.playerAmount;
    }

    private double getAverageStrength(ArrayList<Team> team){
        int playerAmountLocal = 0;
        this.entireStrength = 0d;
        for(int i = 0; i < team.size(); i++){
            playerAmountLocal += team.get(i).getTeamMembersAmount();
        }
        for(int i = 0; i < team.size(); i++){
            for(int j = 0; j < team.get(i).getTeamMembersAmount(); j++){
                this.entireStrength += team.get(i).getTeamMember(j).getSpecificStrength();
            }
        }
        return this.entireStrength/playerAmountLocal;
    }
    private int getRandomNumber(int newMin, int newMax){
        int range = newMax - newMin + 1;
        return (int)(Math.random() * range) + newMin;
    }
    private void doCalculations(){
        arrayListToArray(this.selectedPlayers);
        setSpecificStrengthToAllPlayer();
        sortPlayers();
        setTeamSize();
        setTeams(this.finalBalancingMethod);
        arrayToArrayList(this.finishedTeams);
    }

}