package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Objects;

public class ShowTeamMembersActivity extends AppCompatActivity {

    Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_show_team_members);

        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#75C5C5")));

        this.team = CustomFinishedTeamsAdapter.getTeam();

        TextView teamMembersHeader = findViewById(R.id.teamMembersHeader);
        SharedPreferences pref = getApplication().getSharedPreferences("MyPref", 0);
        String teamMemberHeaderString = pref.getString("team", "");
        teamMembersHeader.setText(teamMemberHeaderString);
        ImageButton goBackToTeamsOverView = findViewById(R.id.goBackTeamsOverView);
        goBackToTeamsOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView averageTeamStrength = findViewById(R.id.averageTeamStrength);
        String averageStrength = String.valueOf(this.team.getRoundedAverageTeamStrength());
        averageTeamStrength.setText(averageStrength);

        RecyclerView teamMembersRecyclerView = findViewById(R.id.teamMembersRecyclerView);
        CustomShowTeamMemberAdapter adapter = new CustomShowTeamMemberAdapter();
        adapter.setTeam(this.team);
        teamMembersRecyclerView.setAdapter(adapter);
        teamMembersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}