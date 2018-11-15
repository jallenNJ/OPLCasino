package edu.ramapo.jallen6.oplcasino;


import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Observable;
import java.util.Vector;

public class Deck extends Observable {
    private Vector<Card> deck;


    Deck(){
        deck = new Vector<Card>(52, 5);

        if(Serializer.isFileLoaded()){
            loadSavedDeck();
        } else{
            makeNewDeck();
        }



    }

    private void makeNewDeck(){
        deck.clear();
        for(CardSuit i : CardSuit.values() ){
            if(i == CardSuit.invalid || i == CardSuit.build){
                continue;
            }
            for(int j = 1; j < 14; j++){
                deck.add(new Card(i, j));
            }
        }
        Collections.shuffle(deck);

    }

    private void loadSavedDeck(){
        String deckStr = Serializer.getDeck();
        String[] tokens = deckStr.split(" ");

        for(String token:tokens){
            if (token.length() != 2){
                continue;
            }
            deck.add(new Card(token));
        }

    }


    public boolean isEmpty(){
        return deck.size() == 0;
    }
    private Card drawCard(){
        if(isEmpty()){
            return null;
        }
        Card returnCard = deck.firstElement();
        deck.remove(0);
        return returnCard;
    }

    public void dealFourCardsToHand(Hand target){
        for(int i = 0; i < 4; i++){
            //TODO: Here is probably where the seg fault at end of came is from
            target.addCard(drawCard());
        }
        this.setChanged();
        this.notifyObservers();
        return;
    }

    public Card[] getFourCards(){
        Card[] returnVal = new Card[4];
        for(int i = 0;i < 4; i++ ){
            //TODO: OR this may be the seg fault
            returnVal[i] = drawCard();
        }
        this.setChanged();
        this.notifyObservers();
        return returnVal;
    }

    public int size(){
        return deck.size();
    }
    public Card peekCard(int index){
        if(index < 0 || index >= deck.size()){
            return null;
        }
        return deck.get(index);
    }
    public String toString(){
        String formatted = "";
        for(int i =0; i < deck.size(); i++){
            formatted += deck.get(i).toString() + " ";
        }
        return formatted;
    }

}
