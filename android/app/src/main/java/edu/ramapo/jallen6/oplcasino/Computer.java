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
                return new PlayerMove(PlayerActions.Capture, i, options);
            }


        }
        return new PlayerMove(PlayerActions.Invalid, 0, new Vector<Integer>(1,1));
    }

    public PlayerMove doMove(final Hand table) {

        PlayerMove potentialMove;
        potentialMove = checkCaptureOptions(table);
        if(potentialMove.getAction() == PlayerActions.Capture){
            actionReason = "As it was an opportunity to capture.";
            return potentialMove;
        }

        actionReason = "As there are no other moves.";
        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }
}
