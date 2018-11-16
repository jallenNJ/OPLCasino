package edu.ramapo.jallen6.oplcasino;

import java.security.InvalidParameterException;
import java.util.Vector;

public class MultiBuild extends BuildType {

    private Vector<Build> builds;

    MultiBuild(Vector<Build> input, String ownerString) {

        owner = ownerString;
        suit = CardSuit.build;
        builds = new Vector<Build>(input);

        numericValue = input.get(0).getValue();

        for (int i = 0; i < builds.size(); i++) {
            if (builds.get(i).getValue() != numericValue) {
                throw new InvalidParameterException("Build sums are not equal");
            }
        }

    }


    public Vector<Card> getCards() {
        Vector<Vector<Card>> allCards = new Vector<Vector<Card>>(4, 1);

        for (int i = 0; i < builds.size(); i++) {
            allCards.add(builds.get(i).getCards());
        }

        Vector<Card> flattened = new Vector<Card>(4, 1);

        for (int i = 0; i < allCards.size(); i++) {
            Vector<Card> current = allCards.get(i);
            flattened.addAll(current);
        }
        return flattened;

    }

    public Card[] getCardsAsArray() {
        Vector<Card> allCards = getCards();
        Card[] cards = new Card[allCards.size()];

        for(int i =0; i < allCards.size(); i++){
            cards[i] = allCards.get(i);
        }

        return cards;

    }
}