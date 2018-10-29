package edu.ramapo.jallen6.oplcasino;

public abstract class CardType {

    public boolean setValue(int val){
        if(val < 1 || val > 14){
            return false;
        }
        numericValue = val;
        return true;
    }

    public boolean setSuit(CardSuit su){
        if(su == CardSuit.invalid){
            return false;
        }
        suit = su;
        return true;

    }

    public int getValue(){
        return numericValue;
    }
    public char getSymbol(){
        switch(numericValue){
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'X';
            case 11:
                return 'J';
            case 12:
                return 'Q';
            case 13:
                return 'K';
            default:
                return 'A';
        }
    }

    public char suitToChar(){

        switch (suit){
            case heart:
                return 'H';
            case diamond:
                return 'D';
            case spade:
                return 'S';
            case club:
                return 'C';
            case build:
                return 'B';
            default:
                return 'I';
        }

    }

    protected int numericValue;
    protected CardSuit suit;
}
