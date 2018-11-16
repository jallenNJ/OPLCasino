package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public abstract class BuildType extends CardType {

    BuildType(){
        suit = CardSuit.build;
    }

    public abstract Vector<Card> getCards();

    public abstract Card[] getCardsAsArray();

}
