package com.anderson.chris.quickwartablet;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class GamePlay extends AppCompatActivity {
    int deckSize;
    int player1Score = 0;
    int player2Score = 0;
    int currentNumberOfPlayers = 0;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_GAME_ID = "EXTRA_GAME_ID";
    private static final String EXTRA_GAME_WINNER = "EXTRA_GAME_WINNER";
    private static final String EXTRA_WAR_WINNER = "EXTRA_WAR_WINNER";
    private static final String EXTRA_PLAYER1_NAME = "EXTRA_PLAYER1_NAME";
    private static final String EXTRA_PLAYER2_NAME = "EXTRA_PLAYER2_NAME";
    private static final String EXTRA_DECK_POSITION = "EXTRA_GAME_POSITION";


    TextView txtGameIDLabel, txtWarAlert, player1Label, player2Label, txtPlayer1Score, txtPlayer2Score, txtTimer, txtGameID, txtIdHelper, txtHandsRemainingLabel;
    ImageView imgPlayer1Card, imgPlayer2Card;
    Firebase mGameDatabase, mWarWinnerDatabase, mNumOfCardsRemainingDatabase, mRootDatabase, mNumOfPlayers, mPlayer1CardsDatabase, mPlayer2CardsDatabase, player1ScoreRef, player2ScoreRef;
    CardData cardData;
    Card player1Card, player2Card;
    List<String> cardList;
    List<Card> cardObjectList;
    String gameID, player1Check, player2Check, player1Name, player2Name;
    //counting backwards
    int currentDeckPosition = 25;
    String warWinnerName = "null";


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
        mGameDatabase.child("SCORE").child("ISWAR").setValue("FALSE");
        mPlayer1CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player1");
        mPlayer2CardsDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/Player2");
        player1ScoreRef = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/SCORE/PLAYER1");
        player2ScoreRef = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/SCORE/PLAYER2");
        mNumOfCardsRemainingDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/NumOfCardsRemaining");
        mWarWinnerDatabase = new Firebase("https://quickwar-a9fde.firebaseio.com/" + gameID + "/SCORE");
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
        imgPlayer1Card = (ImageView) findViewById(R.id.imgPlayer1Card1);
        imgPlayer2Card = (ImageView) findViewById(R.id.imgPlayer2Card);
        player1Label = (TextView) findViewById(R.id.txtPlayer1Label);
        player2Label = (TextView) findViewById(R.id.txtPlayer2Label);
        txtTimer = (TextView) findViewById(R.id.txtxCountdown);
        txtGameIDLabel = (TextView) findViewById(R.id.txtGameIDLabel);
        txtHandsRemainingLabel = (TextView) findViewById(R.id.txtHandsRemainingLabel);
        txtPlayer1Score = (TextView) findViewById(R.id.txtPlayer1Score);
        txtPlayer2Score = (TextView) findViewById(R.id.txtPlayer2Score);
        txtGameID = (TextView) findViewById(R.id.txtShowGameID);
        txtWarAlert = (TextView) findViewById(R.id.txtWarAlert);
        txtIdHelper = (TextView) findViewById(R.id.txtEnterIDLabel);
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

                    } else {
                        //try and perform logic
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
                    } else {
                        //try and perform logic
                        performGameLogic();
                    }
                } catch (Exception e) {
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
            //this is heard by mobile devices to update personal screens
            mNumOfCardsRemainingDatabase.setValue(currentDeckPosition);
            currentDeckPosition -= 1;
            int player1CardValue = player1Card.getCardValue();
            int player2CardValue = player2Card.getCardValue();

            if (player1CardValue > player2CardValue) {
                player1Score++;
            } else if (player2CardValue > player1CardValue) {
                player2Score++;
            } else {

                if (currentDeckPosition >= 4) {
                    Log.d("WAR", "WAR!!!");
                    //start WAR
                    //calls flashing on screen for mobile devices
                    mGameDatabase.child("SCORE").child("ISWAR").setValue("TRUE");
                    performWar();
                    Log.d("p1scorse: ", player1Score + "");
                    Log.d("p2scorse: ", player2Score + "");
                    txtPlayer1Score.setText(player1Score + "");
                    txtPlayer2Score.setText(player2Score + "");
                } else {
                    player1Score++;
                    player2Score++;
                    Toast.makeText(getBaseContext(), "Not enough cards for a WAR. Both players receive a point.",
                            Toast.LENGTH_LONG).show();
                }

            }

            Log.d("Player1score: ", player1Score + "");
            Log.d("Player2score: ", player2Score + "");

            deckSize -= 2;
            //txtGameID.setText((deckSize / 2) + "");

            //wait!
            new CountDownTimer(3000, 100) {

                public void onTick(long millisUntilFinished) {
                    txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
                }

                public void onFinish() {
                    player1ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                    player2ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                    txtPlayer1Score.setText(player1Score + "");
                    txtPlayer2Score.setText(player2Score + "");
                    txtTimer.setText("");
                    setCardsToTransparent();

                }
            }.start();

            //checkIfWinner
            if (deckSize == 0) {
                String winner;
                if (player1Score > player2Score) {
                    winner = player1Name + " Won the Game!";
                } else if (player2Score > player1Score) {
                    winner = player2Name + " Won the Game!";
                } else
                    winner = "Tie Game!";


                //Displays onScreen timer until next hand is playable
                new CountDownTimer(3000, 100) {

                    public void onTick(long millisUntilFinished) {
                        txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
                    }

                    public void onFinish() {
                        txtTimer.setText("");
                        player1ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                        player2ScoreRef.child("PLAY_CHECK").setValue("FALSE");
                        txtPlayer1Score.setText(player1Score + "");
                        txtPlayer2Score.setText(player2Score + "");
                        setCardsToTransparent();
                    }
                }.start();

                Intent intent = new Intent(this, WinnerActivity.class);

                Bundle extras = new Bundle();

                extras.putString(EXTRA_GAME_WINNER, winner);

                intent.putExtra(BUNDLE_EXTRAS, extras);

                startActivity(intent);

                finish();
            }


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

                try {
                    if (!player2Name.equals("Waiting...")) {
                        txtIdHelper.setText("");
                        txtGameIDLabel.setText("");

                        //txtHandsRemainingLabel.setText("Hands Remaining:");
                        txtGameID.setText("");
                    }
                } catch (Exception e) {

                }


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

    public void performWar() {
        Log.d("WAR", "WAR!!!");
        txtWarAlert.setText("WAR!!!");
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txtWarAlert.startAnimation(anim);

        new CountDownTimer(3000, 100) {

            public void onTick(long millisUntilFinished) {
                txtTimer.setText("" + ((millisUntilFinished / 1000) + 1));
            }

            public void onFinish() {
                txtTimer.setText("");
                //calls flashing on screen for mobile devices
                mGameDatabase.child("SCORE").child("ISWAR").setValue("FALSE");
                txtWarAlert.setText("");
                startWarActivity();
            }
        }.start();

    }

    public void startWarActivity() {
        Intent intent = new Intent(this, WarActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_PLAYER1_NAME, player1Name);
        extras.putString(EXTRA_PLAYER2_NAME, player2Name);
        extras.putString(EXTRA_GAME_ID, gameID);
        extras.putInt(EXTRA_DECK_POSITION, (26 - currentDeckPosition));
        Log.d("DECKSIZE", deckSize / 2 + "");
        intent.putExtra(BUNDLE_EXTRAS, extras);
        deckSize -= 4;
        //
        currentDeckPosition -= 4;
        startActivityForResult(intent, 1);

        //add 4 points to whoever won, no points if tie


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Bundle extrasReceived = data.getBundleExtra(BUNDLE_EXTRAS);
        warWinnerName = extrasReceived.getString(EXTRA_WAR_WINNER);

        new CountDownTimer(1000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                result();

            }
        }.start();

    }

    public void result(){
        //currentDeckPosition -=4;
        //txtGameID.setText(currentDeckPosition + "");
        mGameDatabase.child("NumOfCardsRemaining").setValue(currentDeckPosition);

        //store content locally

//        Firebase newFirebase = mWarWinnerDatabase.child("WarWinner");
//        newFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                warWinnerName = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });



//        mWarWinnerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                warWinner = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

        Log.d("WARWINNER: ", warWinnerName );

        txtPlayer1Score = (TextView) findViewById(R.id.txtPlayer1Score);
        txtPlayer2Score = (TextView) findViewById(R.id.txtPlayer2Score);


        if (warWinnerName.equals("PLAYER1")) {
            new CountDownTimer(3000, 100) {

                public void onTick(long millisUntilFinished) {
                    if ((millisUntilFinished / 1000 + 1) == 3) {
                        txtPlayer1Score.setText((player1Score + 1+""));
                    } else if ((millisUntilFinished / 1000 + 1) == 2) {
                        txtPlayer1Score.setText((player1Score + 2+""));
                    } else if ((millisUntilFinished / 1000 + 1) == 1) {
                        txtPlayer1Score.setText((player1Score + 3)+"");
                    }

                }

                public void onFinish() {
                    txtPlayer1Score.setText((player1Score + 4)+"");
                    player1Score += 4;
                }
            }.start();
        } else if (warWinnerName.equals("PLAYER2")) {
            new CountDownTimer(3000, 100) {

                public void onTick(long millisUntilFinished) {
                    if ((millisUntilFinished / 1000 + 1) == 3) {
                        txtPlayer2Score.setText((player2Score + 1) + "");
                    } else if ((millisUntilFinished / 1000 + 1) == 2) {
                        txtPlayer2Score.setText((player2Score + 2)+ "");
                    } else if ((millisUntilFinished / 1000 + 1) == 1) {
                        txtPlayer2Score.setText((player2Score + 3)+"");
                    }
                }

                public void onFinish() {
                    txtPlayer2Score.setText((player2Score + 4)+ "");
                    player2Score += 4;
                }
            }.start();
        } else {
            //no points added
        }

        Log.d("p1scorse: ", player1Score + "");
        Log.d("p2scorse: ", player2Score + "");
    }
}