package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Human extends Player {



    Human(){
        super();
    }
    @Override
    public PlayerMove doMove() {

        return  new PlayerMove(PlayerActions.Trail, hand.getSelectedIndices().get(0), new Vector<Integer>(1,1));


    }
}
