package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Human extends Player {



    Human(){
        super();
        name = "Human";
    }
    @Override
    public PlayerMove doMove(final Hand table) {

        final Vector<Integer> selected = new Vector<Integer> (table.getSelectedIndices());
        if(selected.size() == 0){
            return  new PlayerMove(PlayerActions.Trail, hand.getSelectedIndices().get(0), new Vector<Integer>(1,1));
        } else{
            return  new PlayerMove(PlayerActions.Capture, hand.getSelectedIndices().get(0), selected);
        }



    }
}
