package edu.ramapo.jallen6.oplcasino;


import java.util.Observable;
import java.util.Stack;
import java.util.Vector;

public class Hand extends Observable {

    private Vector<CardType> hand;
    private Vector<Integer> selectedIndices;
    private boolean selectionLimitedToOne;
    private int removedIndex;

    Hand(){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = true;
        selectedIndices = new Vector<Integer>(1,1);

    }

    Hand(boolean limitSelection){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
        selectedIndices = new Vector<Integer>(4,4);
    }

    Hand(boolean limitSelection, String data){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
        selectedIndices = new Vector<Integer>(4,4);

        String[] tokens = data.split(" ");
        for(String token:tokens){
            if(token.length() != 2){
                continue;
            }
            hand.add(new Card(token));
        }

    }

    Hand(String data){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = false;
        selectedIndices = new Vector<Integer>(4,4);

        String[] tokens = data.split(" ");

        String spacesRemoved = "";
        for(String token:tokens){
            if(token.length() == 0){
                continue;
            }
            spacesRemoved += token;
        }

        //Stack<Build> pendingBuilds = new Stack<Build>();
        int buildsCount = 0;

        int i =0;
        Vector<Card> cardBuffer = new Vector<Card>(4,1);
        Vector<Build> buildBuffer = new Vector<Build>(4,1);
        while(i< spacesRemoved.length()){
            char current = spacesRemoved.charAt(i);
            if(current == '['){
                buildsCount++;
                i++;
                continue;
            }else if(current ==']'){
                if(cardBuffer.size() > 0){
                    buildBuffer.add(new Build(new Vector<Card>(cardBuffer), "ADD OWNER"));
                    cardBuffer.clear();
                }

                buildsCount--;
                i++;
                if(buildsCount == 0){
                    if(buildBuffer.size() >1){
                        MultiBuild multiBuild = new MultiBuild(new Vector<Build>(buildBuffer), "ADD OWNER");
                        buildBuffer.clear();
                        hand.add(multiBuild);
                    } else{
                        hand.add(buildBuffer.get(0));
                        buildBuffer.clear();
                    }

                }
                continue;

            }
            String cardStr = "" + current + spacesRemoved.charAt(i+1);
            Card newCard = new Card(cardStr);
            if(buildsCount > 0){
                cardBuffer.add(newCard);
            }else{
                hand.add(newCard);
            }
            i+=2;
        }


    }


    Hand(Hand copy){
        hand = new Vector<CardType>(copy.hand);
        selectedIndices = new Vector<Integer>(copy.selectedIndices);
        selectionLimitedToOne = copy.selectionLimitedToOne;
        removedIndex = copy.removedIndex;
    }

    public int size(){
        return hand.size();
    }

    public boolean isEmpty(){
        return hand.size() == 0;
    }

    public boolean hasSelectedCards(){
        return selectedIndices.size() > 0;
    }

    public Vector<Integer> getSelectedIndices(){
        if(selectedIndices == null){
            return null;
        }
        if(selectedIndices.size() == 0){
            return new Vector<Integer>(1,1);
        }
       return new Vector<Integer>(selectedIndices);
    }

    public int selectCard(int index){
        if(selectionLimitedToOne && selectedIndices.size() >0){
            int replacedVal = selectedIndices.remove(0);
            selectedIndices.clear();
            if(replacedVal != index){
                selectedIndices.add(index);
                return replacedVal;
            } else{
                return -1;
            }

        } else{
            if(!selectedIndices.contains(index)){
                selectedIndices.add(index);
            }

            return -1;
        }
    }

    public void unselectCard(int index){
        selectedIndices.remove(index);
    }

    public int getAmountSelect(){
        return selectedIndices.size();
    }

    public void unSelectAllCards(){

        selectedIndices.clear();
    }
    public boolean addCard(CardType add){
        hand.add(add);
        this.setChanged();
        this.notifyObservers();
        return true;
    }

    public CardType peekCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }

        return hand.get(index);
    }
public  int fetchRemovedIndex(){
        int returnVal = removedIndex;
        removedIndex = -1;
        return returnVal;
       // return removedIndex;
}

    public CardType removeCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }
        CardType removed = hand.get(index);
        hand.remove(index);
        removedIndex = index;
        this.setChanged();
        this.notifyObservers();
        return removed;
    }

    public String toString(){
        String formatted = "";
        for(int i =0; i < hand.size(); i++){
            formatted += hand.get(i).toString() + " ";
        }
        return formatted;
    }

    public int countSuit (CardSuit target){
        int amount = 0;
        for(int i=0; i < hand.size(); i++){
            if(hand.get(i).getSuit() == target){
                amount++;
            }
        }
        return amount;
    }

    public boolean containsCard (Card check){
        return hand.contains(check);
    }

    public int countValue (int target){
        int amount = 0;
        for(int i=0; i < hand.size(); i++){
            if(hand.get(i).getValue() == target){
                amount++;
            }
        }
        return amount;
    }

}
