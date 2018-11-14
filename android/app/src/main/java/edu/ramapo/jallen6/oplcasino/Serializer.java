package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Serializer {

    private static boolean fileLoaded;

    private static int roundNum;
    private static PlayerSaveData[] players;
    private static String table;
    private static String deck;
    private static Vector<String> buildOwners;


    public static void init(){
        fileLoaded = false;

        roundNum = 0;
        players = new PlayerSaveData[2];
        table = "";
        deck = "";
        buildOwners = new Vector<String>(4,1);

    }


    public static void setHumanPlayer(String name, int score, String Hand, String Pile){
        players[0] = new PlayerSaveData(name, score, Hand, Pile);
    }
    public static void setComputerPlayer(String name, int score, String Hand, String Pile){
        players[1] = new PlayerSaveData(name, score, Hand, Pile);
    }

    public static void setTable(String t){
        table = t;
    }

    public static void setDeck (String d){
        deck = d;
    }




}
