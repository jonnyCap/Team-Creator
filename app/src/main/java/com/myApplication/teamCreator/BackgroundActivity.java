package com.myApplication.teamCreator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundActivity extends AppCompatActivity {
    private ProgressBar pB;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        } else {
            Objects.requireNonNull(getSupportActionBar()).show();
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("TC");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0F9FFF")));

        pB = findViewById(R.id.progressBar);
        addProgress();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            finish();
            startActivity(new Intent(BackgroundActivity.this, CreateTeamsActivity.class));
        }, 1000);

    }
    private void addProgress(){

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pB.setProgress(counter);
                if(counter == 100){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 100, 6);
    }
}