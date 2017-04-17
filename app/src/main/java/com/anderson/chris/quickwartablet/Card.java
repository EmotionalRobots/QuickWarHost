package com.anderson.chris.quickwartablet;

/**
 * Created by christopheranderson on 4/4/17.
 */

public class Card {
    String xmlCardName;
    int cardValue;

    public Card() {
    }

    public Card(String name, int cardValue) {
        xmlCardName = name;
        this.cardValue = cardValue;
    }

    public String getXmlCardName() {
        return xmlCardName;
    }

    public void setXmlCardName(String xmlCardName) {
        this.xmlCardName = xmlCardName;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
