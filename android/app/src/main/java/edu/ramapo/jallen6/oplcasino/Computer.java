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

    private PlayerMove checkBuildOptions(final Hand table){
        for(int i = 0; i < hand.size(); i ++){
            Card sumTo = (Card)hand.peekCard(i);
            for(int j =0; j < hand.size(); j++){
                if(i==j){
                    continue;
                }
                Card played = (Card)hand.peekCard(j);
                if(played.getValue() < sumTo.getValue()){
                    continue;
                }
                Vector<Integer> indices = findSetThatSum(table, sumTo.getValue() - played.getValue(), true);
                if(indices.size() > 0){
                    actionReason = "Saw an opportunity to make a build";
                    return new PlayerMove(PlayerActions.Build, j, indices);
                }
            }
        }

        return new PlayerMove(PlayerActions.Invalid, 0, new Vector<Integer>(1,1));
    }

    private PlayerMove checkCaptureOptions(final Hand table){

        for(int i =0; i < hand.size(); i++){
            Vector<Integer> result;
            if(hand.peekCard(i).getValue() != 1){
               result = findSetThatSum(table, hand.peekCard(i).getValue(), false);
            } else{
                result = findSetThatSum(table, 14, false);
            }

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
        potentialMove = checkBuildOptions(table);
        if(potentialMove.getAction() == PlayerActions.Build){
            return potentialMove;
        }
        potentialMove = checkCaptureOptions(table);
        if(potentialMove.getAction() == PlayerActions.Capture){
            return potentialMove;
        }

        actionReason = "As there are no other moves.";
        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }


    private Vector<Integer> findSetThatSum(final Hand table, int target, boolean forBuild){

        Vector<Integer> indices = new Vector<Integer>(4,1);
        if(target < 1 || target > 14) {
            return indices;
        }
        for(int i =0; i < table.size(); i++){
            indices.clear();
            CardType start = table.peekCard(i);
            if(!forBuild){
                if(start.getSuit() == CardSuit.build){
                    continue;
                }
            }
            int remaining = target - start.getValue();
            if(remaining < 0){
                continue;
            }
            indices.add(i);
            if(remaining == 0){
                if(forBuild){
                    return indices;
                } else{
                    continue;
                }
            }
            int remainingCache = remaining;
            for(int j = i+1; j < table.size(); j++){
                remaining = remainingCache;
                CardType firstCard = table.peekCard(j);
                if(!forBuild){
                    if(firstCard.getSuit() == CardSuit.build){
                        continue;
                    }
                }
                if(remaining - firstCard.getValue() == 0){
                    indices.add(j);
                    return indices;
                }else if(remaining - firstCard.getValue() <0){
                    continue;
                }
                remaining -= firstCard.getValue();
                for(int k = j+1; k < table.size(); k++){
                    CardType current = table.peekCard(k);
                    if(!forBuild){
                        if(current.getSuit() == CardSuit.build){
                            continue;
                        }
                    }
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
