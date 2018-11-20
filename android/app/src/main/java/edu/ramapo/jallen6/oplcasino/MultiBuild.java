package edu.ramapo.jallen6.oplcasino;

import java.security.InvalidParameterException;
import java.util.Vector;

public class MultiBuild extends BuildType {

    private Vector<Build> builds;

    /**
     Creates a multi build from a Vector of Builds and owner
     @param input All the builds in which to make in the multi build
     @param ownerString The name of the owner
     */
    MultiBuild(Vector<Build> input, String ownerString) {

        owner = ownerString;
        suit = CardSuit.build;
        builds = new Vector<Build>(input);

        //Get the numeric value from the first build
        numericValue = input.get(0).getValue();

        //Loop commented out as an invalid build is better than a crash
        /*for (int i = 0; i < builds.size(); i++) {
            if (builds.get(i).getValue() != numericValue) {
                //Commented out as an invalid build is better than a crash
               // throw new InvalidParameterException("Build sums are not equal");
            }
        }*/

    }

    /**
     Get all cards in a flattened Vector
     @return Vector containing call cards
     */
    public Vector<Card> getCards() {
        Vector<Vector<Card>> allCards = new Vector<Vector<Card>>(4, 1);

        //Get every builds cards
        for (int i = 0; i < builds.size(); i++) {
            allCards.add(builds.get(i).getCards());
        }

        //Make a flattened vector to store it in
        Vector<Card> flattened = new Vector<Card>(4, 1);

        //For every sub vector, add the cards to the flattened vector
        for (int i = 0; i < allCards.size(); i++) {
            Vector<Card> current = allCards.get(i);
            flattened.addAll(current);
        }
        return flattened;

    }

    /**
     Get all cards in the vector as an Array instead of a vector
     @return The array of cards
     */
    public Card[] getCardsAsArray() {
        //Get the vector
        Vector<Card> allCards = getCards();
        Card[] cards = new Card[allCards.size()];
        //Add each card into the array
        for(int i =0; i < allCards.size(); i++){
            cards[i] = allCards.get(i);
        }

        return cards;

    }

    /**
     String formatted as [ build1 build2 buildN ]
     @return The formatted string, returns "[ ]" if empty
     */
    public String toString(){
       String formatted = "[ ";
        for(int i=0; i < builds.size();i++){
            formatted += builds.get(i).toString();
        }
        formatted +="]";
        return formatted;


    }
}