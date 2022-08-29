package com.myApplication.teamCreator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomPlayerStrengthAdapter extends RecyclerView.Adapter<CustomPlayerStrengthAdapter.ViewHolderPlayerStrength> {

    private ArrayList<String> gameAndStrength = new ArrayList<>();
    private final Context context;
    private DBHelper dbHelper;
    private static String selectedGame;
    private int row_index = -1;

    public CustomPlayerStrengthAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderPlayerStrength onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelper = new DBHelper(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_strength_list_item, parent, false);
        return new ViewHolderPlayerStrength(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlayerStrength holder, int position) {
        holder.finalPlayerStrengthText.setText(gameAndStrength.get(position));
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        String player = pref.getString("fullUsername", "");
        String specificStrengthString = String.valueOf(dbHelper.returnSpecificStrength(gameAndStrength.get(position),player));
        holder.specificStrength.setText(specificStrengthString);
        holder.parentPlayerStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedGame(gameAndStrength.get(position));
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index == position){
            holder.inspectPlayerlayout.setBackgroundColor(Color.parseColor("#9CD8FF"));
        }else{
            holder.inspectPlayerlayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return gameAndStrength.size();
    }

    public class ViewHolderPlayerStrength extends RecyclerView.ViewHolder{
        private final TextView finalPlayerStrengthText;
        private final TextView specificStrength;
        private final CardView parentPlayerStrength;
        private final RelativeLayout inspectPlayerlayout;
        public ViewHolderPlayerStrength(@NonNull View itemView) {
            super(itemView);
            finalPlayerStrengthText = itemView.findViewById(R.id.finalPlayerStrengthText);
            parentPlayerStrength = itemView.findViewById(R.id.parentPlayerStrength);
            specificStrength = itemView.findViewById(R.id.specificStrength);
            inspectPlayerlayout = itemView.findViewById(R.id.inspectPlayerLayout);
        }
    }

    public void setGameAndStrength(ArrayList<String> gameAndStrength) {
        this.gameAndStrength = gameAndStrength;
        notifyDataSetChanged();
    }
    public static String getSelectedGame() {
        return selectedGame;
    }

    public static void setSelectedGame(String thisSelectedGame) {
        selectedGame = thisSelectedGame;
    }

}
