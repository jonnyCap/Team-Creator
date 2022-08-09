package com.myApplication.teamCreator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomSelectPlayerAdapter extends RecyclerView.Adapter<CustomSelectPlayerAdapter.ViewHolderSelectPlayer> {


    private ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Player> selectedPlayers = new ArrayList<>();

    @NonNull
    @Override
    public CustomSelectPlayerAdapter.ViewHolderSelectPlayer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_player_list_item, parent,false);
        return new ViewHolderSelectPlayer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSelectPlayerAdapter.ViewHolderSelectPlayer holder, int position) {
        holder.selectedPlayerText.setText(players.get(position).getName());
        holder.selectedPlayerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isShown()){
                    if(buttonView.isChecked()){
                        selectedPlayers.add(players.get(position));
                    }else{
                        selectedPlayers.remove(players.get(position));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolderSelectPlayer extends RecyclerView.ViewHolder{

        private final CheckBox selectedPlayerCheckbox;
        private final TextView selectedPlayerText;
        public ViewHolderSelectPlayer(@NonNull View itemView) {
            super(itemView);
            selectedPlayerCheckbox = itemView.findViewById(R.id.selectedPlayerCheckbox);
            selectedPlayerText = itemView.findViewById(R.id.selectedPlayerText);
        }
    }
    public void setPlayers(ArrayList<Player> player){
        this.players = player;
        notifyDataSetChanged();
    }
    public ArrayList<Player> getSelectedPlayers(){
        return this.selectedPlayers;
    }
    public ArrayList<Player> getAllPlayers(){
        return this.players;
    }
    public int getSelectedPlayersAmount(){
        return this.selectedPlayers.size();
    }
}
