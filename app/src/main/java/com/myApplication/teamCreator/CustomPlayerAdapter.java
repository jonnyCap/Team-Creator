package com.myApplication.teamCreator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class CustomPlayerAdapter extends RecyclerView.Adapter<CustomPlayerAdapter.ViewHolderPlayer> {

    private ArrayList<Player> players = new ArrayList<>();
    private final Context context;
    private DBHelper dbHelper;

    public CustomPlayerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPlayer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //initiate Instances of other classes
        dbHelper = new DBHelper(context);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_item, parent, false);
        return new ViewHolderPlayer(view);
    }

    @Override
    //Hier sollte anscheinend nicht der OnclickListener verwendet werden, muss geändert werden!
    public void onBindViewHolder(@NonNull CustomPlayerAdapter.ViewHolderPlayer holder, int position) {
        holder.playerText.setText(players.get(position).getName());
        int playerDefaultStrengthString = players.get(position).getDefaultStrength();
        holder.playerDefaultStrength.setText(Integer.toString(playerDefaultStrengthString));
        holder.parentPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                String currentPlayer = players.get(position).getName();
                if(currentPlayer.length() > 0) {
                    if(currentPlayer.length() > 10){
                        char point = '.';
                        String currentPlayerShort = currentPlayer.substring(0, 8) + point;
                        editor.putString("username", currentPlayerShort);
                    }else{
                        editor.putString("username", currentPlayer);
                    }
                    editor.putString("fullUsername", currentPlayer);
                    editor.apply();
                }
                context.startActivity(new Intent(context, PlayerInspectActivity.class));
            }
        });
        holder.deletePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDatabase(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolderPlayer extends RecyclerView.ViewHolder{

        private final TextView playerText;
        private final TextView playerDefaultStrength;
        private final CardView parentPlayer;
        private final ImageButton deletePlayerButton;

        public ViewHolderPlayer(@NonNull View itemView) {
            super(itemView);
            playerText = itemView.findViewById(R.id.playerText);
            playerDefaultStrength = itemView.findViewById(R.id.playerDefaultStrength);
            parentPlayer = itemView.findViewById(R.id.parentPlayer);
            deletePlayerButton = itemView.findViewById(R.id.deletePlayerButton);
        }
    }
    //notifyDataSetChanged does not work yet like in CusomGamesAdapter
    public void setPlayers(ArrayList<Player> player){
        this.players = player;
        notifyDataSetChanged();
    }
    private void deleteFromDatabase(int position){
        String playerToDelete = players.get(position).getName();
        this.players = dbHelper.DeleteAndFetchAllPlayerData("Players", "name", playerToDelete);
        notifyDataSetChanged();
    }
}