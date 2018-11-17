package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Computer extends Player {

    Computer(){
        super();
        name = "Computer";

        if(Serializer.isFileLoaded()){
            PlayerSaveData saveData = Serializer.getComputerSaveData();
            score = saveData.getScore();
            hand = new Hand(true, saveData.getHand());
            pile = new Hand(false, saveData.getPile());
        }
    }

    Computer(Player advisee){
        //super();
        name = "Advisor";
        hand = new Hand(advisee.getHand());
        pile = new Hand(advisee.getPile());

    }


    private PlayerMove checkCaptureOptions(final Hand table){

        for(int i =0; i < hand.size(); i++){
            Vector<Integer> result = findSetThatSum(table, hand.peekCard(i).getValue());
            if(result.size() != 0){
                actionReason = "As it was an opportunity to capture a set.";
                for(int j =0; j < table.size(); j++){
                    if(table.peekCard(j).getValue() == hand.peekCard(i).getValue()){
                        if(!result.contains(new Integer(j))){
                            result.add(j);
                        }
                    }
                }
                return new PlayerMove(PlayerActions.Capture, i, result);
            }
        }


        //Identical
        Vector<Integer> options = new Vector<>(4,1);
        for(int i =0; i < hand.size(); i++){
            int target = hand.peekCard(i).getValue();
            options.clear();
            for(int j=0; j < table.size(); j++){
                if(table.peekCard(j).getValue() == target){
                    options.add(j);
                }
            }
            if(options.size() > 0){
                actionReason = "As it was an opportunity to capture.";
                return new PlayerMove(PlayerActions.Capture, i, options);
            }


        }
        return new PlayerMove(PlayerActions.Invalid, 0, new Vector<Integer>(1,1));
    }

    public PlayerMove doMove(final Hand table) {


        PlayerMove potentialMove;
        potentialMove = checkCaptureOptions(table);
        if(potentialMove.getAction() == PlayerActions.Capture){
            return potentialMove;
        }

        actionReason = "As there are no other moves.";
        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }


    private Vector<Integer> findSetThatSum(final Hand table, int target){

        Vector<Integer> indices = new Vector<Integer>(4,1);
        for(int i =0; i < table.size(); i++){
            indices.clear();
            Card start = (Card)table.peekCard(i);
            int remaining = target - start.getValue();
            if(remaining <= 0){
                continue;
            }
            indices.add(i);
            int remainingCache = remaining;
            for(int j = i+1; j < table.size(); j++){
                remaining = remainingCache;
                Card firstCard = (Card)table.peekCard(j);
                if(remaining - firstCard.getValue() == 0){
                    indices.add(j);
                    return indices;
                }else if(remaining - firstCard.getValue() <0){
                    continue;
                }
                remaining -= firstCard.getValue();
                for(int k = j+1; k < table.size(); k++){
                    Card current = (Card)table.peekCard(k);
                    if(remaining - current.getValue() == 0){
                        indices.add(k);
                        return indices;
                    }
                }
            }
        }
        if(indices.size() < 2){
            indices.clear();
        }
        return indices;
    }
}
