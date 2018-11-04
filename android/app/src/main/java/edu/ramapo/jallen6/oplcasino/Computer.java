package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Computer extends Player {

    public PlayerMove doMove() {

        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }
}
