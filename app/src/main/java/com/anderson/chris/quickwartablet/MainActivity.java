package com.anderson.chris.quickwartablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    Button btnHostGame;
    Button btnHowToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerListeners();
    }

    public void hostGameClick() {

        Intent intent = new Intent(this, Lobby.class);

        startActivity(intent);
        //finish();
    }

    public void howToPlayClick() {

        Intent intent = new Intent(this, HowToPlay.class);

        startActivity(intent);
    }

    public void registerListeners() {
        btnHostGame = (Button) findViewById(R.id.btnHostGame);
        btnHowToPlay = (Button) findViewById(R.id.btnHowToPlay);

        btnHostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostGameClick();
            }
        });

        btnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howToPlayClick();

            }
        });
    }


}
