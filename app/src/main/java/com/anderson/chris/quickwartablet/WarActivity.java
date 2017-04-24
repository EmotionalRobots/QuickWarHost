package com.anderson.chris.quickwartablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class WarActivity extends AppCompatActivity {
    TextView txtWarTimer, txtShowWarWinnerMessage;
    ImageView imgPlayer1Card1, imgPlayer1Card2, imgPlayer1Card3, imgPlayer1Card4, imgPlayer2Card1, imgPlayer2Card2, imgPlayer2Card3, imgPlayer2Card4;
    TextView txtGameIDLabel, txtWarAlert, player1Label, player2Label, txtPlayer1Score, txtPlayer2Score, txtTimer, txtGameID, txtIdHelper, txtHandsRemainingLabel;
    Firebase mGameDatabase, mWarWinnerDatabase, mNumOfCardsRemainingDatabase, mRootDatabase, mNumOfPlayers, mPlayer1CardsDatabase, mPlayer2CardsDatabase, player1ScoreRef, player2ScoreRef;
    CardData cardData;
    ArrayList<Card> cardList = new ArrayList<>();
    Card player1Card1, player1Card2, player1Card3, player1Card4, player2Card1, player2Card2, player2Card3, player2Card4, currentCard1, currentCard2;

    List<Card> cardObjectList;
    String gameID, player1Check, player2Check, player1Name, player2Name;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";
    private static final String EXTRA_DECK_POSITION = "EXTRA_GAME_POSITION";
    private static final String EXTRA_PLAYER1_NAME = "EXTRA_PLAYER1_NAME";
    private static final String EXTRA_PLAYER2_NAME = "EXTRA_PLAYER2_NAME";
    private static final String EXTRA_WAR_WINNER = "EXTRA_WAR_WINNER";


    int currentDeckPosition, numOfCardsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war);

        Bundle extrasReceived = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        gameID = extrasReceived.getString(EXTRA_GAME_ID);
        currentDeckPosition = extrasReceived.getInt(EXTRA_DECK_POSITION);
        player1Name = extrasReceived.getString(EXTRA_PLAYER1_NAME);
        player2Name = extrasReceived.getString(EXTRA_PLAYER2_NAME);

        registerListeners();


        //store content locally


        Firebase.setAndroidContext(this);
        mRootDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/");
        mGameDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID);
        mNumOfPlayers = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/NumOfPlayers");
        mGameDatabase.child("SCORE").child("ISWAR").setValue("FALSE");
        mPlayer1CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player1");
        mPlayer2CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player2");
//        mNumOfCardsRemainingDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID);


        assignCardValues(0);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                loadScreen();
            }
        }, 3000);

        //subtract 4 cards from players deck
//        Firebase newFirebase = mGameDatabase.child("NumOfCardsRemaining");
//        newFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                numOfCardsRemaining = dataSnapshot.getValue(Integer.class);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

//        mNumOfCardsRemainingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                numOfCardsRemaining = dataSnapshot.getValue(Integer.class) - 4;
//                Log.d("CardsRem: ", numOfCardsRemaining+"");
//            }
//
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

///        mGameDatabase.child("NumOfCardsRemaining").setValue(numOfCardsRemaining);
    }

    public void registerListeners() {
        imgPlayer1Card1 = (ImageView) findViewById(R.id.imgPlayer1Card1);
        imgPlayer1Card2 = (ImageView) findViewById(R.id.imgPlayer1Card2);
        imgPlayer1Card3 = (ImageView) findViewById(R.id.imgPlayer1Card3);
        imgPlayer1Card4 = (ImageView) findViewById(R.id.imgPlayer1Card4);
        imgPlayer2Card1 = (ImageView) findViewById(R.id.imgPlayer2Card1);
        imgPlayer2Card2 = (ImageView) findViewById(R.id.imgPlayer2Card2);
        imgPlayer2Card3 = (ImageView) findViewById(R.id.imgPlayer2Card3);
        imgPlayer2Card4 = (ImageView) findViewById(R.id.imgPlayer2Card4);
        txtWarTimer = (TextView) findViewById(R.id.txtWarCountdownTimer);
        txtShowWarWinnerMessage = (TextView) findViewById(R.id.txtWarCountdownTimer);
        player1Label = (TextView) findViewById(R.id.txtWarPlayer1Label);
        player2Label = (TextView) findViewById(R.id.txtWarPlayer2Label);
        player1Label.setText(player1Name);
        player2Label.setText(player2Name);

    }

    public void loadScreen() {
        imgPlayer1Card1.setImageResource(R.drawable.card);
        imgPlayer1Card2.setImageResource(R.drawable.card);
        imgPlayer1Card3.setImageResource(R.drawable.card);
        imgPlayer1Card4.setImageResource(R.drawable.card);
        imgPlayer2Card1.setImageResource(R.drawable.card);
        imgPlayer2Card2.setImageResource(R.drawable.card);
        imgPlayer2Card3.setImageResource(R.drawable.card);
        imgPlayer2Card4.setImageResource(R.drawable.card);
//        int id = getResources().getIdentifier(player1Card1.getXmlCardName(), "drawable", getPackageName());
        new CountDownTimer(4000, 100) {

            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000 + 1) == 4) {
                    int id1 = getResources().getIdentifier(player1Card1.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer1Card1.setImageResource(id1);
                    int id2 = getResources().getIdentifier(player2Card1.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer2Card1.setImageResource(id2);
                } else if ((millisUntilFinished / 1000 + 1) == 3) {
                    int id1 = getResources().getIdentifier(player1Card2.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer1Card2.setImageResource(id1);
                    int id2 = getResources().getIdentifier(player2Card2.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer2Card2.setImageResource(id2);
                } else if ((millisUntilFinished / 1000 + 1) == 2) {
                    int id1 = getResources().getIdentifier(player1Card3.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer1Card3.setImageResource(id1);
                    int id2 = getResources().getIdentifier(player2Card3.getXmlCardName(), "drawable", getPackageName());
                    imgPlayer2Card3.setImageResource(id2);
                }
                else if ((millisUntilFinished / 1000 + 1) == 1) {

                }
            }

            public void onFinish() {

                warCountDown();
            }
        }.start();

//        for(int i = 0; i < cardList.size(); i++) {
//            int id = getResources().getIdentifier(cardl.getXmlCardName(), "drawable", getPackageName());
//            imgPlayer2Card.setImageResource(id);
//        }
    }

    public void warCountDown() {

        new CountDownTimer(3000, 50) {

            public void onTick(long millisUntilFinished) {
                txtWarTimer.setText("" + ((millisUntilFinished / 1000) + 1));
            }

            public void onFinish() {
                txtWarTimer.setText("");
                int id1 = getResources().getIdentifier(player1Card4.getXmlCardName(), "drawable", getPackageName());
                imgPlayer1Card4.setImageResource(id1);
                int id2 = getResources().getIdentifier(player2Card4.getXmlCardName(), "drawable", getPackageName());
                imgPlayer2Card4.setImageResource(id2);
                performGameLogic();
            }
        }.start();

    }

    public void performGameLogic() {
        new CountDownTimer(3000, 100) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                int player1WarCardValue = player1Card4.getCardValue();
                int player2WarCardvalue = player2Card4.getCardValue();

                Bundle extras = new Bundle();



                if (player1WarCardValue > player2WarCardvalue) {
                    txtShowWarWinnerMessage.setText(player1Name + " won the war!");
                    extras.putString(EXTRA_WAR_WINNER, "PLAYER1");
                } else if (player2WarCardvalue > player1WarCardValue) {
                    txtShowWarWinnerMessage.setText(player2Name + " won the war!");
                    extras.putString(EXTRA_WAR_WINNER, "PLAYER2");

                } else {
                    txtShowWarWinnerMessage.setText("Tie! No points awarded.");
                    extras.putString(EXTRA_WAR_WINNER, "TIE");

                }
                Intent resultIntent = new Intent();

                resultIntent.putExtra(BUNDLE_EXTRAS, extras);


                setResult(Activity.RESULT_OK, resultIntent);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        }.start();

    }

    public void assignCardValues(int counter) {


        //retrieves next card from database

//                Firebase temp = mPlayer1CardsDatabase.child(currentDeckPosition + "");
//        Log.d("REF: ", temp.toString());
//                temp.addListenerForSingleValueEvent(new ValueEventListener() {
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Card card = dataSnapshot.getValue(Card.class);
//                        Log.d("PEZ CARD NAME: ", card.getXmlCardName() + "");
//                        setCurrentCard1Helper(dataSnapshot.getValue(Card.class));
//                    }
//
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//                Firebase temp2 = mPlayer2CardsDatabase.child(currentDeckPosition + "");
//
//                temp2.addListenerForSingleValueEvent(new ValueEventListener() {
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Card card = dataSnapshot.getValue(Card.class);
//                        Log.d("PEZ CARD NAME: ", card.getXmlCardName() + "");
//                        setCurrentCard2Helper(dataSnapshot.getValue(Card.class));
//                    }
//
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });

//            currentDeckPositio4
// +;


        Firebase player1Card1Data = mPlayer1CardsDatabase.child(currentDeckPosition + "");
        player1Card1Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player1Card1 = dataSnapshot.getValue(Card.class);
                Log.d("P1C1", player1Card1.getXmlCardName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase player2Card1Data = mPlayer2CardsDatabase.child(currentDeckPosition + "");
        player2Card1Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player2Card1 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentDeckPosition++;

        Firebase player1Card2Data = mPlayer1CardsDatabase.child(currentDeckPosition + "");
        player1Card2Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player1Card2 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase player2Card2Data = mPlayer2CardsDatabase.child(currentDeckPosition + "");
        player2Card2Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player2Card2 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentDeckPosition++;

        Firebase player1Card3Data = mPlayer1CardsDatabase.child(currentDeckPosition + "");
        player1Card3Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player1Card3 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase player2Card3Data = mPlayer2CardsDatabase.child(currentDeckPosition + "");
        player2Card3Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player2Card3 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentDeckPosition++;
        Firebase player1Card4Data = mPlayer1CardsDatabase.child(currentDeckPosition + "");
        player1Card4Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player1Card4 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase player2Card4Data = mPlayer2CardsDatabase.child(currentDeckPosition + "");
        player2Card4Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player2Card4 = dataSnapshot.getValue(Card.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
