package com.anderson.chris.quickwartablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.Random;

public class Lobby extends AppCompatActivity {

    TextView txtUniqueId;
    TextView txtPlayersConnected;
    Button btnStartGame;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";

    private String uniqueId;
    int minimum = 1000;
    int maximum = 8999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnStartGame = (Button) findViewById(R.id.btnStartGame);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginGame();
            }
        });

        generateUniqueId();

//        while(playersConnected != 2){
//
//        }
    }

    public void generateUniqueId() {
        uniqueId = minimum + (int) (Math.random() * maximum) + "";
        txtUniqueId = (TextView) findViewById(R.id.uniqueGameId);
        txtUniqueId.setText(uniqueId);
    }

    public void beginGame() {


        Intent intent = new Intent(this, GamePlay.class);

        Bundle extras = new Bundle();

        extras.putString(EXTRA_GAME_ID, uniqueId);

        intent.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(intent);
    }
}
