package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Computer extends Player {

    Computer(){
        super();
        name = "Computer";
    }

    public PlayerMove doMove(final Hand table) {

        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }
}
