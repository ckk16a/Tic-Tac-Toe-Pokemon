package com.example.tictactoe;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private boolean hasWon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("") || hasWon) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
            v.setBackgroundResource(R.drawable.kyogre);
        } else {
            ((Button) v).setText("O");
            v.setBackgroundResource(R.drawable.groudon);
        }
        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
            hasWon = true;
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
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
        }
        for (int i = 0; i < 3; i++) {
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

    private void player1Wins() {
        MediaPlayer KyogreWin = MediaPlayer.create(this, R.raw.kyogrewin);
        player1Points++;
        Toast.makeText(this, "Kyogre wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        KyogreWin.start();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void player2Wins() {
        MediaPlayer GroudonWin = MediaPlayer.create(this, R.raw.groudonwin);
        player2Points++;
        Toast.makeText(this, "Groudon wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        GroudonWin.start();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void draw() {
        MediaPlayer Draw = MediaPlayer.create(this, R.raw.draw);
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        Draw.start();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void updatePointsText(){
        textViewPlayer1.setText("Kyogre: " + player1Points);
        textViewPlayer2.setText("Groudon: " + player2Points);
    }

    private void resetBoard(){
        Button buttonReset = findViewById(R.id.button_reset);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText("");
                buttons[i][j].setBackground(buttonReset.getBackground());
            }
        }
        roundCount = 0;
        player1Turn = true;
        hasWon = false;
    }

    private void resetGame(){
        if(!hasWon) {
            player1Points = 0;
            player2Points = 0;
            updatePointsText();
            resetBoard();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
        outState.putBoolean("hasWon", hasWon);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        hasWon = savedInstanceState.getBoolean("hasWon");

        restoreMyImages();

        if(hasWon)
            resetBoard();
    }

    void restoreMyImages(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){

                Button thisButton = buttons[i][j];

                if(thisButton.getText() == "X"){
                    thisButton.setBackgroundResource(R.drawable.kyogre);
                }
                else if(thisButton.getText() == "O"){
                    thisButton.setBackgroundResource(R.drawable.groudon);
                }
            }
        }
    }
}