package com.example.newproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {
    public static final String WINS_X_KEY = "wins_x";
    public static final String WINS_O_KEY = "wins_o";



    SharedPreferences themeSettings;
    SharedPreferences.Editor editorSettings;
    SharedPreferences statsPreferences;
    SharedPreferences.Editor statsEditor;
    ImageButton imageTheme;
    Button[][] buttons = new Button[3][3];
    boolean playerXTurn = true;
    int roundCount = 0;
    TextView statusTextView;
    Button statsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeSettings = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        statsPreferences = getSharedPreferences("STATS", MODE_PRIVATE);
        statsEditor = statsPreferences.edit();


        if (!themeSettings.contains("MODE_NIGHT_ON")) {
            editorSettings = themeSettings.edit();
            editorSettings.putBoolean("MODE_NIGHT_ON", false);
            editorSettings.apply();
        }

        setCurrentTheme();
        setContentView(R.layout.activity_main);


        buttons[0][0] = findViewById(R.id.button_00);
        buttons[0][1] = findViewById(R.id.button_01);
        buttons[0][2] = findViewById(R.id.button_02);
        buttons[1][0] = findViewById(R.id.button_10);
        buttons[1][1] = findViewById(R.id.button_11);
        buttons[1][2] = findViewById(R.id.button_12);
        buttons[2][0] = findViewById(R.id.button_20);
        buttons[2][1] = findViewById(R.id.button_21);
        buttons[2][2] = findViewById(R.id.button_22);
        statsButton = findViewById(R.id.statsButton);

        imageTheme = findViewById(R.id.imageBtn);
        updateImageButton();


        imageTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (themeSettings.getBoolean("MODE_NIGHT_ON", false)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editorSettings = themeSettings.edit();
                    editorSettings.putBoolean("MODE_NIGHT_ON", false);
                    editorSettings.apply();
                    Toast.makeText(MainActivity.this, "Темная тема отключена", Toast.LENGTH_SHORT).show();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editorSettings = themeSettings.edit();
                    editorSettings.putBoolean("MODE_NIGHT_ON", true);
                    editorSettings.apply();
                    Toast.makeText(MainActivity.this, "Темная тема включена", Toast.LENGTH_SHORT).show();
                }
                updateImageButton();
                recreate(); // Перезагрузка активности
            }
        });

        // Настроим логику игры
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick(finalI, finalJ);
                    }
                });
            }
        }

        // Переход на экран статистики
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StatActivity.class);
                startActivity(intent);
            }
        });
    }


    private void onButtonClick(int i, int j) {
        if (!buttons[i][j].getText().toString().equals("")) {
            return;
        }

        if (playerXTurn) {
            buttons[i][j].setText("X");
        } else {
            buttons[i][j].setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (playerXTurn) {
                playerWins("X");
            } else {
                playerWins("O");
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            playerXTurn = !playerXTurn;
        }
    }


    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }

            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void playerWins(String player) {
        Toast.makeText(this, "Игрок " + player + " победил!", Toast.LENGTH_SHORT).show();
        updateStats(player);
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Ничья!", Toast.LENGTH_SHORT).show();
        statsEditor.putInt("draws", statsPreferences.getInt("draws", 0) + 1);
        statsEditor.apply();
        resetBoard();
    }


    private void resetBoard() {
        roundCount = 0;
        playerXTurn = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void updateImageButton() {
        if (themeSettings.getBoolean("MODE_NIGHT_ON", false)) {
            imageTheme.setImageResource(R.drawable.moon);
        } else {
            imageTheme.setImageResource(R.drawable.sunday);
        }
    }

    private void setCurrentTheme() {
        if (themeSettings.getBoolean("MODE_NIGHT_ON", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    private void updateStats(String winner) {
        if (winner.equals("X")) {
            statsEditor.putInt(WINS_X_KEY, statsPreferences.getInt(WINS_X_KEY, 0) + 1);
        } else if (winner.equals("O")) {
            statsEditor.putInt(WINS_O_KEY, statsPreferences.getInt(WINS_O_KEY, 0) + 1);
        }
        statsEditor.apply();
    }

}
