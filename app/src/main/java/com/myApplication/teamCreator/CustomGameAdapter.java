package com.myApplication.teamCreator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomGameAdapter extends RecyclerView.Adapter<CustomGameAdapter.ViewHolder> {

    private ArrayList<Game> games = new ArrayList<>();
    private final Context context;
    private DBHelper dbHelper;

    public CustomGameAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //initiate Instances of other classes
        dbHelper = new DBHelper(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomGameAdapter.ViewHolder holder, int position) {
        holder.gameText.setText(games.get(position).getName());
        holder.deleteGameButton.setOnClickListener(v -> deleteFromDatabase(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView gameText;
        private final ImageButton deleteGameButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameText = itemView.findViewById(R.id.gameText);
            deleteGameButton = itemView.findViewById(R.id.deleteGameButton);
        }
    }
    public void setGames(ArrayList<Game> games){
        this.games = games;
        notifyDataSetChanged();
    }
    private void deleteFromDatabase(int position){
        String gameToDelete = games.get(position).getName();
        this.games = dbHelper.deleteAndFetchAllData("Games", "game", gameToDelete);
        notifyDataSetChanged();
    }
}
