package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.PlayerActions.Trail;

public class Human extends Player {



    Human(){
        super();
        moveToUse = Trail;
        name = "Human";
    }
    @Override
    public PlayerMove doMove(final Hand table) {

        final Vector<Integer> selected = new Vector<Integer> (table.getSelectedIndices());
        return new PlayerMove(moveToUse, hand.getSelectedIndices().get(0), selected);
     /*   if(selected.size() == 0){
            return  new PlayerMove(Trail, hand.getSelectedIndices().get(0), new Vector<Integer>(1,1));
        } else{
            return  new PlayerMove(PlayerActions.Capture, hand.getSelectedIndices().get(0), selected);
        }*/



    }
}
