package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button playButton;
    private Button rankingButton;
    private Button sensorsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        playButton = findViewById(R.id.playButton);
        rankingButton = findViewById(R.id.rankingButton);
        sensorsButton = findViewById(R.id.sensorsButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, SensorsActivity.class);
                startActivity(intent);
            }
        });

    }
}