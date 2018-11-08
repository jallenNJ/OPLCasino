package edu.ramapo.jallen6.oplcasino;

import java.util.Collections;
import java.util.Comparator;
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
        tableCardIndices = new Vector<Integer>(tableCI);
        Collections.sort(tableCardIndices, Collections.<Integer>reverseOrder());

    }

    PlayerMove(PlayerMove copy){
        action = copy.getAction();
        handCardIndex = copy.getHandCardIndex();
        tableCardIndices = copy.getTableCardIndices();
        Collections.sort(tableCardIndices, Collections.<Integer>reverseOrder());
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
