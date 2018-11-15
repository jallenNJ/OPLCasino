package edu.ramapo.jallen6.oplcasino;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;

public class Serializer {

    public static final String fileExtension = ".csav";
    private static boolean fileLoaded;

    private static int roundNum;
    private static PlayerSaveData[] players;
    private static String table;
    private static String deck;
    private static Vector<String> buildOwners;
    private static String lastCapturer;
    private static String nextPlayer;

    private static String fileName = "";


    public static void init(){
        fileLoaded = false;

        roundNum = 0;
        players = new PlayerSaveData[2];
        table = "";
        deck = "";
        buildOwners = new Vector<String>(4,1);
        lastCapturer = "";

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

    public static void setLastCapturer(String l){
        lastCapturer = l;
    }

    public static void setNextPlayer(String s){
        nextPlayer = s;
    }

    public static void setFileName(String name){
        fileName = name;
    }


    public static void readInSaveFile(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        List<String> fileData;
        try{
            fileData = Files.readAllLines(Paths.get(directory+File.separator+fileName), Charset.defaultCharset());
        } catch (Exception e){
            return;
        }

        for(int i =0; i < fileData.size();i++){
            String current = fileData.get(i);

        }
    }

    public static void writeToSaveFile(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File save = new File(directory, "Save.csav");
        try{
            FileWriter outputStream = new FileWriter(new File(directory, "Save.csav"));
            outputStream.write("Round: " +Integer.toString(roundNum)+"\n");

            for(int i =1; i >=0; i--){
                PlayerSaveData current = players[i];
                outputStream.write(current.getName() +":\n");
                outputStream.write("\t Hand: " + current.getHand());
                outputStream.write("\n\t Pile: " + current.getPile());
                outputStream.write("\n\n");
            }

            outputStream.write("Table: " + table);
            outputStream.write("\n\nLast Capturer: "+ lastCapturer);
            outputStream.write("\n\nDeck: " + deck);
            outputStream.write("\n\nNext Player: " + nextPlayer);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e){
            return;
        }


    }


}
