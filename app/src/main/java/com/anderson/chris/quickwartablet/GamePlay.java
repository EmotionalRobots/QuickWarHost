package com.anderson.chris.quickwartablet;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GamePlay extends AppCompatActivity {
    int deckSize;
    int player1Score = 0;
    int player2Score = 0;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";
    private static final String EXTRA_GAME_WINNER = "EXTRA_GAME_WINNER";

    TextView player1Label, player2Label, txtPlayer1Score, txtPlayer2Score, txtTimer, txtGameID;
    ImageView imgPlayer1Card, imgPlayer2Card;
    Firebase mGameDatabase, mRootDatabase, mNumOfPlayers, mPlayer1CardsDatabase, mPlayer2CardsDatabase, player1ScoreRef, player2ScoreRef;
    CardData cardData;
    Card player1Card, player2Card;
    List<String> cardList;
    List<Card> cardObjectList;
    String gameID, player1Check, player2Check, player1Name, player2Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Bundle extrasReceived = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        //store content locally
        gameID = extrasReceived.getString(EXTRA_GAME_ID);
        Firebase.setAndroidContext(this);
        mRootDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/");
        mGameDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID);
        mNumOfPlayers = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/NumOfPlayers");
        mPlayer1CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player1");
        mPlayer2CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player2");
        player1ScoreRef = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/SCORE/PLAYER1");
        player2ScoreRef = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/SCORE/PLAYER2");


        registerListeners();
        uploadCards();
        mNumOfPlayers.setValue(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRootDatabase.child(gameID).removeValue();
    }

    public void uploadCards() {
        int i = 1;
        while (i <= cardObjectList.size() / 2) {

            Firebase mRefChild = mPlayer1CardsDatabase.child(i + "");
            mRefChild.setValue(cardObjectList.get(i - 1));
            i++;
        }
        int j = 1;
        while (j <= cardObjectList.size() / 2) {

            Firebase mRefChild = mPlayer2CardsDatabase.child(j + "");
            mRefChild.setValue(cardObjectList.get(i - 1));
            i++;
            j++;
        }
    }

    public void registerListeners() {
        imgPlayer1Card = (ImageView) findViewById(R.id.imgPlayer1Card);
        imgPlayer2Card = (ImageView) findViewById(R.id.imgPlayer2Card);
        player1Label = (TextView) findViewById(R.id.txtPlayer1Label);
        player2Label = (TextView) findViewById(R.id.txtPlayer2Label);
        txtTimer = (TextView) findViewById(R.id.txtxCountdown);
        txtPlayer1Score = (TextView) findViewById(R.id.txtPlayer1Score);
        txtPlayer2Score = (TextView) findViewById(R.id.txtPlayer2Score);
        txtGameID = (TextView) findViewById(R.id.txtShowGameID);
        cardData = new CardData();
        cardList = cardData.getList();
        cardObjectList = cardData.getCardObjectList();
        deckSize = cardList.size();

        txtGameID.setText(gameID);
        setPlayer1Name();
        setPlayer2Name();

        //CHANGE CARD IMG
        Firebase player1LastSentCardData = mPlayer1CardsDatabase.child("LastSentCard");
        player1LastSentCardData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    player1Card = dataSnapshot.getValue(Card.class);
                    int id = getResources().getIdentifier(player1Card.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer1Card.setImageResource(id);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Firebase player2LastSentCardData = mPlayer2CardsDatabase.child("LastSentCard");
        player2LastSentCardData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    player2Card = dataSnapshot.getValue(Card.class);
                    int id = getResources().getIdentifier(player2Card.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer2Card.setImageResource(id);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Check and maintain last sent cards
        Firebase checkIfPlayer1SentACard = player1ScoreRef.child("PLAY_CHECK");
        player1ScoreRef.child("PLAY_CHECK").setValue("FALSE");
        checkIfPlayer1SentACard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                player1Check = dataSnapshot.getValue(String.class);
                try {
                    if (player1Check.equals("FALSE")) {
                        Log.d("DO I GET HERE?", " HELLO WORLD, FALSE1");

                    } else {
                        //try and perform logic
                        Log.d("DO I GET HERE?", " HELLO WORLD, TRUE1");

                        performGameLogic();
                    }
                } catch (Exception e) {

                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //Check and maintain last sent cards
        Firebase checkIfPlayer2SentACard = player2ScoreRef.child("PLAY_CHECK");
        player2ScoreRef.child("PLAY_CHECK").setValue("FALSE");
        checkIfPlayer2SentACard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    player2Check = dataSnapshot.getValue(String.class);
                    if (player2Check.equals("FALSE")) {
                        Log.d("DO I GET HERE?", " HELLO WORLD, FALSE2");
                    } else {
                        //try and perform logic
                        Log.d("DO I GET HERE?", " HELLO WORLD, TRUE2");

                        performGameLogic();
                    }
                } catch (
                        Exception e)

                {

                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        setCardsToTransparent();
    }

    public void performGameLogic() {

        if (player1Check.equals("TRUE") && player2Check.equals("TRUE")) {
            Log.d("DO I GET HERE?", " HELLO WORLD, PERFORM LOGIC");

            int player1CardValue = player1Card.getCardValue();
            int player2CardValue = player2Card.getCardValue();

            if (player1CardValue > player2CardValue) {
                player1Score++;
            } else if (player2CardValue > player1CardValue) {
                player2Score++;
            } else {
                player1Score++;
                player2Score++;

            }
            Log.d("Player1score: ", player1Score + "");
            Log.d("Player2score: ", player2Score + "");

            deckSize = deckSize - 2;
            if (deckSize == 0) {
                String winner;
                if (player1Score > player2Score) {
                    winner = player1Name + " Won the Game!";
                }
                else if (player2Score > player1Score) {
                    winner = player2Name + " Won the Game!";
                }
                else
                    winner = "Tie Game!";

                new CountDownTimer(3000, 100) {

                    public void onTick(long millisUntilFinished) {
                        txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
                    }

                    public void onFinish() {
                        txtTimer.setText("");
                    }
                }.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        player1ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                        player2ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                        txtPlayer1Score.setText(player1Score + "");
                        txtPlayer2Score.setText(player2Score + "");
                        //in case data remains from previous game
                        setCardsToTransparent();
                    }
                }, 3000);

                Intent intent = new Intent(this, WinnerActivity.class);

                Bundle extras = new Bundle();

                extras.putString(EXTRA_GAME_WINNER, winner);

                intent.putExtra(BUNDLE_EXTRAS, extras);

                startActivity(intent);

                finish();
            }
            //wait!
            new CountDownTimer(3000, 100) {

                public void onTick(long millisUntilFinished) {
                    txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
                }

                public void onFinish() {
                    txtTimer.setText("");
                }
            }.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds
                    player1ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                    player2ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                    txtPlayer1Score.setText(player1Score + "");
                    txtPlayer2Score.setText(player2Score + "");
                    //in case data remains from previous game
                    setCardsToTransparent();
                }
            }, 3000);

        }


    }

    public void setPlayer1Name() {
        Firebase player1NameDatabase = mPlayer1CardsDatabase.child("NAME");
        mPlayer1CardsDatabase.child("NAME").setValue("Waiting...");

        player1NameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player1Name = dataSnapshot.getValue(String.class);

                player1Label.setText(player1Name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void setPlayer2Name() {

        Firebase player2NameDatabase = mPlayer2CardsDatabase.child("NAME");
        mPlayer2CardsDatabase.child("NAME").setValue("Waiting...");

        player2NameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player2Name = dataSnapshot.getValue(String.class);

                player2Label.setText(player2Name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setCardsToTransparent() {
        imgPlayer1Card.setImageResource(android.R.color.transparent);
        imgPlayer2Card.setImageResource(android.R.color.transparent);
    }
}