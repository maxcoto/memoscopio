package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = findViewById(R.id.playButton);
        Button rankingButton = findViewById(R.id.rankingButton);
        Button sensorsButton = findViewById(R.id.sensorsButton);

        playButton.setOnClickListener(playHandler);
        rankingButton.setOnClickListener(rankingHandler);
        sensorsButton.setOnClickListener(sensorsHandler);
    }

    private final View.OnClickListener playHandler = (_v) -> {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        startActivity(intent);
    };

    private final View.OnClickListener rankingHandler = (_v) -> {
        Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
        startActivity(intent);
    };

    private final View.OnClickListener sensorsHandler = (_v) -> {
        Intent intent = new Intent(MenuActivity.this, SensorsActivity.class);
        startActivity(intent);
    };
}