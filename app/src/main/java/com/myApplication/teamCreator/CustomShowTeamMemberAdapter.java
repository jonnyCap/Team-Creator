package com.myApplication.teamCreator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomShowTeamMemberAdapter extends RecyclerView.Adapter<CustomShowTeamMemberAdapter.ViewHolderTeamMembers> {
    Team team = new Team();
    @NonNull
    @Override
    public ViewHolderTeamMembers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_member_list_item, parent,false);
        return new ViewHolderTeamMembers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTeamMembers holder, int position) {
        holder.finalTeamMember.setText(team.getTeamMember(position).getName());
        String specificStrength = String.valueOf(team.getTeamMember(position).getSpecificStrength());
        holder.finalTeamMemberStrength.setText(specificStrength);
    }

    @Override
    public int getItemCount() {
        return team.getTeamMembersAmount();
    }

    public class ViewHolderTeamMembers extends RecyclerView.ViewHolder {
        private final TextView finalTeamMember;
        private final TextView finalTeamMemberStrength;
        public ViewHolderTeamMembers(@NonNull View itemView) {
            super(itemView);
            finalTeamMember = itemView.findViewById(R.id.finalTeamMember);
            finalTeamMemberStrength = itemView.findViewById(R.id.finalTeamMemberStrength);
        }
    }
    public void setTeam(Team team){
        this.team = team;
    }
}
