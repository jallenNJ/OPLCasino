package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.PlayerActions.Trail;

public class Human extends Player {

    Human(){
        super();
        //Init to trail as that is the default option
        moveToUse = Trail;
        name = "Human";
        //If save data, load the save file
        if(Serializer.isFileLoaded()){
            PlayerSaveData saveData = Serializer.getHumanSaveData();
            score = saveData.getScore();
            hand = new Hand(true, saveData.getHand());
            pile = new Hand(false, saveData.getPile());
        }
    }

    @Override
    public PlayerMove doMove(final Hand table) {
        //Get selected, and return a move with the current move selected through controller.
        final Vector<Integer> selected = new Vector<Integer> (table.getSelectedIndices());
        return new PlayerMove(moveToUse, hand.getSelectedIndices().get(0), selected);
    }
}
