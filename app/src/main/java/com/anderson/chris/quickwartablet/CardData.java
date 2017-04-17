package com.anderson.chris.quickwartablet;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by christopheranderson on 3/31/17.
 */

//this class assigns random key values (1 -> 52) to each card identifier...
public class CardData {


    private List<String> cards;
    private List<Card> cardObjectList;

    public CardData() {
        //populate database with keys to indexes, assigned randomly
        cards = new ArrayList<>();
        cardObjectList = new ArrayList();
        addCards();

        //shuffle cards
        Collections.shuffle(cardObjectList);

        System.out.println();
    }

    public void addCards() {
        cards.add("ace_of_clubs");
        cards.add("ace_of_diamonds");
        cards.add("ace_of_spades");
        cards.add("ace_of_hearts");

        cards.add("two_of_clubs");
        cards.add("two_of_diamonds");
        cards.add("two_of_spades");
        cards.add("two_of_hearts");

        cards.add("three_of_clubs");
        cards.add("three_of_diamonds");
        cards.add("three_of_spades");
        cards.add("three_of_hearts");

        cards.add("four_of_clubs");
        cards.add("four_of_diamonds");
        cards.add("four_of_spades");
        cards.add("four_of_hearts");

        cards.add("five_of_clubs");
        cards.add("five_of_diamonds");
        cards.add("five_of_spades");
        cards.add("five_of_hearts");

        cards.add("six_of_clubs");
        cards.add("six_of_diamonds");
        cards.add("six_of_spades");
        cards.add("six_of_hearts");

        cards.add("seven_of_clubs");
        cards.add("seven_of_diamonds");
        cards.add("seven_of_spades");
        cards.add("seven_of_hearts");

        cards.add("eight_of_clubs");
        cards.add("eight_of_diamonds");
        cards.add("eight_of_spades");
        cards.add("eight_of_hearts");

        cards.add("nine_of_clubs");
        cards.add("nine_of_diamonds");
        cards.add("nine_of_spades");
        cards.add("nine_of_hearts");

        cards.add("ten_of_clubs");
        cards.add("ten_of_diamonds");
        cards.add("ten_of_spades");
        cards.add("ten_of_hearts");

        cards.add("jack_of_clubs");
        cards.add("jack_of_diamonds");
        cards.add("jack_of_spades");
        cards.add("jack_of_hearts");

        cards.add("queen_of_clubs");
        cards.add("queen_of_diamonds");
        cards.add("queen_of_spades");
        cards.add("queen_of_hearts");

        cards.add("king_of_clubs");
        cards.add("king_of_diamonds");
        cards.add("king_of_spades");
        cards.add("king_of_hearts");

        for (int i = 0; i < cards.size(); i++) {
            Card card = new Card();
            String tempName = cards.get(i);
            card.setXmlCardName(tempName);

            switch (tempName.substring(0, 3)) {
                case "ace": {
                    card.setCardValue(14);
                    break;
                }
                case "two": {
                    card.setCardValue(2);
                    break;
                }
                case "thr": {
                    card.setCardValue(3);
                    break;
                }
                case "fou": {
                    card.setCardValue(4);
                    break;
                }
                case "fiv": {
                    card.setCardValue(5);
                    break;
                }
                case "six": {
                    card.setCardValue(6);
                    break;
                }
                case "sev": {
                    card.setCardValue(7);
                    break;
                }
                case "eig": {
                    card.setCardValue(8);
                    break;
                }
                case "nin": {
                    card.setCardValue(9);
                    break;
                }
                case "ten": {
                    card.setCardValue(10);
                    break;
                }
                case "jac": {
                    card.setCardValue(11);
                    break;
                }
                case "que": {
                    card.setCardValue(12);
                    break;
                }
                case "kin": {
                    card.setCardValue(13);
                    break;
                }
            }
            cardObjectList.add(card);
        }
    }

    public List<Card> getCardObjectList() {

        Log.d("SIZE", cardObjectList.size()+ "");
        return cardObjectList;
    }

    public List<String> getList() {
        return cards;
    }


}
