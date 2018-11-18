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
        players[0] = new PlayerSaveData();
        players[1] = new PlayerSaveData();
        table = "";
        deck = "";
        buildOwners = new Vector<String>(4,1);
        lastCapturer = "";

        setFileName("Save", false);

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

    public static void setFileName(String name, boolean hasExtension){

        if(hasExtension){
            fileName = name;
        } else{
            fileName = name + fileExtension;
        }
    }

    public static boolean isHumanFirst(){
        return nextPlayer.trim().equals("Human");
    }


    public static boolean isFileLoaded(){
        return fileLoaded;
    }

    public static int getRoundNum(){
        return roundNum;
    }

    public static String getDeck(){
        return deck;
    }

    public static PlayerSaveData getHumanSaveData(){
        return players[0];
    }

    public static PlayerSaveData getComputerSaveData(){
        return players[1];
    }

    public static String getTable(){
        return table;
    }

    public static boolean isLastCapturerHuman(){
        return lastCapturer.trim().equals("Human");
    }
    public static boolean isNextPlayerHuman(){
        return nextPlayer.trim().equals("Human");
    }


    public static void clearLoadedFile(){
        fileLoaded = false;
        init();
    }

    public static void readInSaveFile(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        List<String> fileData;
        try{
            fileData = Files.readAllLines(Paths.get(directory+File.separator+fileName), Charset.defaultCharset());
        } catch (Exception e){
            return;
        }

        //Convert to vector and remove blank lines
        Vector<String> data = removeBlankLines(fileData);

        //11 is the minimum amount for a valid file
        if(data == null || data.size() <13){
            //TODO: Add better handling
            return;
        }

        Vector<String> parsedData = removeHeaders(data);


        if(parsedData.size() < 12){
            return;
        }
        roundNum = Integer.parseInt(parsedData.get(0).trim());

        players[1].setName(parsedData.get(1));
        players[1].setScore(Integer.parseInt(parsedData.get(2).trim()));
        players[1].setHand(parsedData.get(3));
        players[1].setPile(parsedData.get(4));

        players[0].setName(parsedData.get(5));
        players[0].setScore(Integer.parseInt(parsedData.get(6).trim()));
        players[0].setHand(parsedData.get(7));
        players[0].setPile(parsedData.get(8));

        table = parsedData.get(9);

        //Build owners loop is here

        lastCapturer = parsedData.get(parsedData.size()-3).trim();
        deck = parsedData.get(parsedData.size()-2);
        nextPlayer = parsedData.lastElement().trim();
        fileLoaded = true;
    }

    public static void writeToSaveFile(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File save = new File(directory, "Save.csav");
        try{
            FileWriter outputStream = new FileWriter(new File(directory, fileName));
            outputStream.write("Round: " +Integer.toString(roundNum)+"\n");

            for(int i =1; i >=0; i--){
                PlayerSaveData current = players[i];
                outputStream.write(current.getName() +":\n");
                outputStream.write("\t Score: "+ Integer.toString(current.getScore()));
                outputStream.write("\n\t Hand: " + current.getHand());
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



    private static String removeHeader(String raw){
        if(raw == null){
            return "";
        }

        int index = raw.indexOf(":");
        if(index >= 0){
            return raw.substring(index+1);
        }else{
            return raw;
        }
    }

    private static Vector<String> removeHeaders(Vector<String> raw){
        if(raw == null){
            return new Vector <String>(1,1);
        }
        Vector<String> result = new Vector<String>(raw.size(), 1);

        for(int i = 0; i < raw.size(); i++){
            String parsed = removeHeader(raw.get(i));
            if(checkIfNonBlank(parsed)){
                result.add(parsed);
            } else{
                result.add(raw.get(i).substring(0, raw.get(i).length()-1));
            }
        }

        return result;
    }

    private static Vector<String> removeBlankLines(List<String> input){
        if(input == null){
            return new Vector<>(1,1);
        }

        Vector <String> vector = new Vector<String>(input.size(), 1);
        for(int i =0; i < input.size(); i++){
            String current = input.get(i);
            if(checkIfNonBlank(current)){
                vector.add(current);
            }
        }

        return vector;

    }

    private static boolean checkIfNonBlank (String line){
        if(line == null){
            return false;
        }

        return !line.equals("") && line.trim().length() > 0 ;
    }


}
