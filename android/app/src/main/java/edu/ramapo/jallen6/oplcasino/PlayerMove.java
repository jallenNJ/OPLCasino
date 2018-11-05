package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class PlayerMove {

    private PlayerActions action;
    private int handCardIndex;
    private Vector<Integer> tableCardIndices;

    PlayerMove(){
        action = PlayerActions.Invalid;
        handCardIndex = 0;
        tableCardIndices = new Vector<Integer>(1,1);
    }
    PlayerMove(PlayerActions ac, int hand, Vector<Integer> tableCI){
        action = ac;
        handCardIndex = hand;
        tableCardIndices = (Vector<Integer> )tableCI.clone();

    }

    PlayerMove(PlayerMove copy){
        action = copy.getAction();
        handCardIndex = copy.getHandCardIndex();
        tableCardIndices = copy.getTableCardIndices();
    }

    public PlayerActions getAction(){
        return action;
    }
    public int getHandCardIndex(){
        return handCardIndex;
    }
    public Vector<Integer>getTableCardIndices(){
        return tableCardIndices;
    }
    public int getTableCardIndiciesSize(){
        return tableCardIndices.size();
    }
}
