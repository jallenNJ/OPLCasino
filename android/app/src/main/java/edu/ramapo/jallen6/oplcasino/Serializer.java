package edu.ramapo.jallen6.oplcasino;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
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

    public static void setRoundNum(int round){
        roundNum = round;
    }

    public static void setPlayers(Player[] rawPlayers){
        players[0] = rawPlayers[0].toSaveData();
        players[1] = rawPlayers[1].toSaveData();
    }



    public static void setTable(String t){
        table = t;
    }

    public static void setDeck (String d){
        deck = d;
    }



    public static void writeToSaveFile(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File save = new File(directory, "Save.csav");
        try{
            FileWriter outputStream = new FileWriter(new File(directory, "Save.csav"));
            outputStream.write("Hello World!");
            outputStream.write("More text");
            outputStream.close();
        } catch (Exception e){
            return;
        }

    }


}
