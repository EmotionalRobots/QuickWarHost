package com.anderson.chris.quickwartablet;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WinnerActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_WINNER = "EXTRA_GAME_WINNER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        Bundle extrasReceived = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        TextView txtWinner = (TextView) findViewById(R.id.txtWinnerName);
        final TextView txtTimer = (TextView) findViewById(R.id.txtFinalCountdown);
        txtWinner.setText(extrasReceived.getString(EXTRA_GAME_WINNER));

        new CountDownTimer(10000, 100) {

            public void onTick(long millisUntilFinished) {
                txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
            }

            public void onFinish() {
                finish();
            }
        }.start();

    }

}
