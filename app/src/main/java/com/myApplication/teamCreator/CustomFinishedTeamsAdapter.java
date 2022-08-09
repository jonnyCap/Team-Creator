package com.myApplication.teamCreator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomFinishedTeamsAdapter extends RecyclerView.Adapter<CustomFinishedTeamsAdapter.ViewHolderFinishedTeams> {

    private ArrayList<Team> finishedTeams = new ArrayList<>();
    private static Team team;

    private final Context context;


    public CustomFinishedTeamsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderFinishedTeams onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finished_teams_list_item, parent,false);
        return new ViewHolderFinishedTeams(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFinishedTeams holder, int position) {
        String teamsName = "Team " + (position + 1);
        holder.finishedTeamsText.setText(teamsName);
        String memberAmount = String.valueOf(this.finishedTeams.get(position).getTeamMembersAmount());
        String averageStrength = String.valueOf(this.finishedTeams.get(position).getRoundedAverageTeamStrength());
        holder.finalTeamMemberAmount.setText(memberAmount);
        holder.finalTeamAverageStrength.setText(averageStrength);

        holder.parentFinishedTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("team", teamsName);
                editor.apply();
                team = new Team(finishedTeams.get(position).getTeamMembers());
                context.startActivity(new Intent(context, ShowTeamMembersActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return finishedTeams.size();
    }

    public class ViewHolderFinishedTeams extends RecyclerView.ViewHolder{
        private final TextView finishedTeamsText;
        private final TextView finalTeamMemberAmount;
        private final TextView finalTeamAverageStrength;
        private final CardView parentFinishedTeam;
        public ViewHolderFinishedTeams(@NonNull View itemView) {
            super(itemView);
            finishedTeamsText = itemView.findViewById(R.id.finishedTeamHeader);
            finalTeamMemberAmount = itemView.findViewById(R.id.finalTeamMemberAmount);
            finalTeamAverageStrength = itemView.findViewById(R.id.finalTeamAverageStrength);
            parentFinishedTeam = itemView.findViewById(R.id.parentFinishedTeam);
        }
    }
    public void setFinishedTeams(ArrayList<Team> team){
        this.finishedTeams = team;
    }
    public static Team getTeam() {
        return team;
    }
}
