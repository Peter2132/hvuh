package com.example.newproject;

import static com.example.newproject.MainActivity.WINS_O_KEY;
import static com.example.newproject.MainActivity.WINS_X_KEY;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatActivity extends AppCompatActivity {

    SharedPreferences statsPreferences;
    TextView winsXTextView, winsOTextView, drawsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        winsXTextView = findViewById(R.id.winsXTextView);
        winsOTextView = findViewById(R.id.winsOTextView);
        drawsTextView = findViewById(R.id.drawsTextView);

        statsPreferences = getSharedPreferences("STATS", MODE_PRIVATE);

        int winsX = statsPreferences.getInt(MainActivity.WINS_X_KEY, 0);
        int winsO = statsPreferences.getInt(MainActivity.WINS_O_KEY, 0);

        int draws = statsPreferences.getInt("draws", 0);

        winsXTextView.setText("Победы X: " + winsX);
        winsOTextView.setText("Победы O: " + winsO);
        drawsTextView.setText("Ничьи: " + draws);
    }

}
