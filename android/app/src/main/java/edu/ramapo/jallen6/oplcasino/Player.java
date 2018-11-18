package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public abstract class Player {

    protected Hand hand;
    protected int score;
    protected int selectedIndex;
    protected Hand pile;
    protected Vector<Integer> reservedValues;
    protected String name;
    protected PlayerActions moveToUse;
    protected String actionReason;
    protected String rejectionReason;

    Player(){


        hand = new Hand();
        pile = new Hand();
        score = 0;
        selectedIndex = -1;
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
        return hand.getSelectedIndices().get(0);
    }

    public boolean selectCard(int index){
        if(index < 0 || index >= hand.size()){
            return false;
        }
        selectedIndex = index;
        return true;
    }

    public boolean unselectCard(){
        selectedIndex = -1;
        return true;
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

   public void addMoveToLog(PlayerMove move, Hand table){
        String entry = "";
        if(name.equals("Advisor")){
            entry = "Move recommendation: ";
        }else{
            entry = name + " ";
        }

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
        for(int i =0; i < move.getTableCardIndiciesSize(); i++){
            entry += table.peekCard(tableIndices.get(i)).toString() + " ";
        }
        if(move.getAction() != PlayerActions.Trail){
            entry = entry.substring(0, entry.length() -1) + ".";
        }else{
            entry += ".";
        }

        if(!actionReason.equals("")){
            entry += "\n" + actionReason;
        }
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

    public boolean containsCardInPile(Card check){
        return pile.containsCard(check);
    }

    public int countAcesInPile(){
        return pile.countValue(1);
    }

    public abstract PlayerMove doMove(final Hand table);
}
