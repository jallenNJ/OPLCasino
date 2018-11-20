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

    /**
     Create a Player move with all fields specified
     @param ac The PlayerAction which was used
     @param hand the index of the hand card
     @param tableCI The index of the table cards which were selected

     tableCard Indices gets stored in descending order
     */
    PlayerMove(PlayerActions ac, int hand, Vector<Integer> tableCI){
        action = ac;
        handCardIndex = hand;
        tableCardIndices = new Vector<Integer>(tableCI);
        Collections.sort(tableCardIndices, Collections.<Integer>reverseOrder());

    }

    /**
     Copy constructor
     @param copy The object to copy from
     */
    PlayerMove(PlayerMove copy){
        action = copy.getAction();
        handCardIndex = copy.getHandCardIndex();
        tableCardIndices = copy.getTableCardIndices();
        Collections.sort(tableCardIndices, Collections.<Integer>reverseOrder());
    }

    public PlayerActions getAction(){
        return action;
    }

    public void markInvalid(){
        action = PlayerActions.Invalid;
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
