package edu.ramapo.jallen6.oplcasino;

public abstract class CardType {


    protected int numericValue;
    protected CardSuit suit;
    protected String owner;


    /**
     Set the value of the Card
     @param val the value to set
     @return True if valid, false if not
     */
    public boolean setValue(int val){
        if(val < 1 || val > 14){
            return false;
        }
        numericValue = val;
        return true;
    }

    /**
     Set owner of card
     @param o The string of the owner
     */
    public void setOwner(String o){
        owner = o;
    }


    public String getOwner(){
        return owner;
    }

    public int getValue(){
        return numericValue;
    }

    /**
     Gets the symbol on the card
     @return The symbol 2-9 or A J Q K are valid
     */
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
    public CardSuit getSuit(){
        return suit;
    }

    /**
     Get a char representation of the suit
     @return A char to represent the suit. Valid suits: H D S C B I
     */
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

    /**
     Convert a char to its numeric value
     @param val is the symbol to convert
     @return 1-13 (Ace Low) Invalid is zero
     */
    public int symbolToValue (char val){
        val = Character.toUpperCase(val);
        switch (val){
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'X':
                return 10;
            case 'J':
                return 11;
            case 'Q':
                return 12;
            case 'K':
                return 13;
            case '1':
            case 'A':
                return 1;
            default:
                return 0;
        }
    }

    /**
     Convert a char of a suit to its enum
     @param val The char representation of a suit
     @return Invalid suit if invalid char
     */
    public CardSuit charToSuit(char val){
        val = Character.toUpperCase(val);
        switch (val){
            case 'H':
                return CardSuit.heart;
            case 'D':
                return CardSuit.diamond;
            case 'S':
                return CardSuit.spade;
            case 'C':
                return CardSuit.club;
            case 'B':
                return CardSuit.build;
            default:
                return CardSuit.invalid;
        }
    }

}
