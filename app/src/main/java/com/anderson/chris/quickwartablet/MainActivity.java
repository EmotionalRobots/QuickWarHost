package com.anderson.chris.quickwartablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    Button btnHostGame;
    Button btnHowToPlay;
    private String uniqueId;
    int minimum = 1000;
    int maximum = 8999;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerListeners();
    }

    public void hostGameClick() {
        generateUniqueId();

        Intent intent = new Intent(this, GamePlay.class);

        Bundle extras = new Bundle();

        extras.putString(EXTRA_GAME_ID, uniqueId);

        intent.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(intent);
    }

    public void generateUniqueId() {
        uniqueId = minimum + (int) (Math.random() * maximum) + "";
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
