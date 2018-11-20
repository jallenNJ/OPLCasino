package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public abstract class Player {

    protected Hand hand;
    protected int score;
    protected Hand pile;
    protected Vector<Integer> reservedValues;
    protected String name;
    protected PlayerActions moveToUse;
    protected String actionReason;
    protected String rejectionReason;


    /**
     Constructs a player object for a new game/tour
     */
    Player(){
        hand = new Hand();
        pile = new Hand();
        score = 0;
        reservedValues = new Vector<Integer>(2,1);
        moveToUse = PlayerActions.Invalid;
        name = "Player";
        actionReason = "";
        rejectionReason = "";

    }

    public Hand getHand() {
        return hand;
    }

    public Hand getPile(){
        return pile;
    }

    public int getPileSize(){
        return pile.size();
    }

    public String getName(){return name;}
    public int getScore(){
        return score;
    }

    public boolean addCardToHand(Card add){
        hand.addCard(add);
        return true;
    }
    public boolean addCardsToHand(Card[] add){
        for(int i =0; i < add.length; i++){
            addCardToHand(add[i]);
        }
        return true;
    }
    public boolean addCardToPile(Card add){
        pile.addCard(add);
        return true;
    }

    /**
     Checks if card contains the specified value
     @param val the value to check for
     @return True if in the hand, false if not
     */
    public boolean doesHandContain(int val){
        if(val < 0 || val > 14){
            return false;
        }
        if(val == 14 ){
            val = 1;
        }
        for(int i =0; i < hand.size(); i++){
            if(hand.peekCard(i).getValue() == val){
                return true;
            }
        }
        return  false;
    }

    public boolean addCardsToPile(Card[] add){
        for(int i =0; i < add.length; i++){
            addCardToPile(add[i]);
        }
        return true;
    }

    public Card removeCardFromHand(int i){
        return (Card)hand.removeCard(i);
    }
    public int getHandSize(){
        return hand.size();
    }

    public int getSelectedIndex() {
        if(hand.getSelectedIndices().size() == 0){
            return -1;
        }
        return hand.getSelectedIndices().get(0);
    }



    public void setScore(int s){
        score = s;
    }
    public void setRejectionReason(String rejection) {
        rejectionReason = "Reason: " + rejection;
    }

    public boolean reserveBuildValue(Build res){
        reservedValues.add(res.getValue());
        return true;
    }
    public boolean releaseBuildValue(CardType res){
        if(res == null){
            return false;
        }
        reservedValues.removeElement(res.getValue());
        return true;
    }

    public boolean releaseBuildValue(int val){
        reservedValues.removeElement(val);
        return true;
    }

    public boolean hasReservedValue(){
        return reservedValues.size() > 0;
    }
    public boolean isReservedValue(int val){
        for(int i =0; i < reservedValues.size(); i++){
            if(reservedValues.get(i) == val){
                return true;
            }
        }
        return false;
    }

    /**
      Adds the move the player just made into the ActionLog's Log
     @param move The playermove which was made
     @param table The table cards at the time the move was made
     */
   public void addMoveToLog(PlayerMove move, Hand table){
        String entry = "";

        //If advisor, print out as recommendation instead of as an action report
        if(name.equals("Advisor")){
            entry = "Move recommendation: ";
        }else{
            entry = name + " ";
        }

        //Localize the string context based on the action which was preformed
        switch (move.getAction()){
            case Capture:
                entry += "is capturing with " + hand.peekCard(move.getHandCardIndex()).toString() +
                        " to capture " ;
                break;
            case Build:
                entry += "is building with " + hand.peekCard(move.getHandCardIndex()).toString() +
                        " using ";
                break;
            case Trail:
                entry += "is trailing with " + hand.peekCard(move.getHandCardIndex()).toString();
                break;
            case Invalid:
                default:
                entry += " tried an invalid move." + rejectionReason;
                ActionLog.addLog(entry);
                return;
        }


        Vector<Integer> tableIndices = move.getTableCardIndices();

        //Sum the cards which were used
       int sum = hand.peekCard(move.getHandCardIndex()).getValue();
        for(int i =0; i < move.getTableCardIndiciesSize(); i++){
            entry += table.peekCard(tableIndices.get(i)).toString() + " ";
            sum += table.peekCard(tableIndices.get(i)).getValue();
        }

        //If not a trail, replace the last space with a period.
       //Otherwise add a period. (Due to trailing whitespace in toString in capture / build
        if(move.getAction() != PlayerActions.Trail){
            entry = entry.substring(0, entry.length() -1) + ".";

        }else{
            entry += ".";
        }

        //If build, print out what it sums to
        if(move.getAction() == PlayerActions.Build){
            entry += "(Sums to " + sum +")";
        }

        //If an action reason is given, print it out
        if(!actionReason.equals("")){
            entry += "\n" + actionReason;
        }

        //Add it to the log
        ActionLog.addLog(entry);

    }

    public void setMoveToUse(PlayerActions move) {
        moveToUse = move;
    }

    public PlayerSaveData toSaveData(){
        return new PlayerSaveData(name, 0, hand.toString(), pile.toString());
    }

    public int countSpadesInPile(){
        return pile.countSuit(CardSuit.spade);
    }



    /**
     Check if a card is in the pile
     @param check The card to chexk
     @return True if it is in the pile, false if not
     */
    public boolean containsCardInPile(Card check){
        for(int i =0; i < pile.size(); i++){
            Card current = (Card)pile.peekCard(i);
            //Check suit and value
            if(current.getSuit() == check.getSuit() &&
                    current.getValue() == check.getValue()){
                return true;
            }
        }
        return false;
    }

    public int countAcesInPile(){
        return pile.countValue(1);
    }

    //Abstract entry point for all children to do a move
    public abstract PlayerMove doMove(final Hand table);
}
