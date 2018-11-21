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


    /**
     * Functions which inits all the variables. Acts as constructor for static object
     */
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

    /**
     * Sets the players to read data from
     * @param rawPlayers the players to read data from
     */
    public static void setPlayers(Player[] rawPlayers){
        players[0] = rawPlayers[0].toSaveData();
        players[1] = rawPlayers[1].toSaveData();
    }



    public static void setTable(String t){
        table = t;
    }
    public static void setBuildOwners (Vector<String> b){
        buildOwners = new Vector<String>(b);
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

    /**
     * Sets the file name to read or write to
     * @param name The name to set
     * @param hasExtension If it has a file extension
     */
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

    /**
     * Gets the owner of a build based on its string
     * @param build The build to check against
     * @return The name of the owner. "Owner not found" if not found
     */

    public static String getOwnerOfBuild (String build){
        build = removeWhiteSpace(build);
        String buildForComp = build + "Computer";
        String buildForHuman = build + "Human";


        for(int i =0; i < buildOwners.size(); i++){
            String current = removeWhiteSpace(buildOwners.get(i));
            if(current.equals(buildForComp)){
                return "Computer";
            } else if(current.equals(buildForHuman)){
                return "Human";
            }
        }

        return "Owner not found";
        //throw new RuntimeException("No build owner found");

    }

    public static boolean isLastCapturerHuman(){
        return lastCapturer.trim().equals("Human");
    }


    /**
     * Resets the Serializer after the file has had all the data read from it
     */
    public static void clearLoadedFile(){
        fileLoaded = false;
        init();
    }

    /**
     * Read in the file from the fileName that was set from a controller
     */
    public static void readInSaveFile(){

        //Use the downloads for easy placement of custom files
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        List<String> fileData;

        //Get all the lines, if failure to read, return
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


        //If not enough lines made it through the parser, file is invalid
        if(parsedData.size() < 12){
            return;
        }


        //Set all the data
        roundNum = Integer.parseInt(parsedData.get(0).trim());

        //Computer data
        players[1].setName(parsedData.get(1));
        players[1].setScore(Integer.parseInt(parsedData.get(2).trim()));
        players[1].setHand(parsedData.get(3));
        players[1].setPile(parsedData.get(4));

        //Human data
        players[0].setName(parsedData.get(5));
        players[0].setScore(Integer.parseInt(parsedData.get(6).trim()));
        players[0].setHand(parsedData.get(7));
        players[0].setPile(parsedData.get(8));

        table = parsedData.get(9);

        for(int i = 10; i < parsedData.size()-3; i++){
            buildOwners.add(parsedData.get(i));
        }

        lastCapturer = parsedData.get(parsedData.size()-3).trim();
        deck = parsedData.get(parsedData.size()-2);
        nextPlayer = parsedData.lastElement().trim();
        fileLoaded = true;
    }

    /**
     * Write the current game state to a file with specified name
     */
    public static void writeToSaveFile(){
        //Get the directory
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        try{
            //Open the file
            FileWriter outputStream = new FileWriter(new File(directory, fileName));
            outputStream.write("Round: " +Integer.toString(roundNum)+"\n");

            //Write both players
            for(int i =1; i >=0; i--){
                PlayerSaveData current = players[i];
                outputStream.write(current.getName() +":\n");
                outputStream.write("\t Score: "+ Integer.toString(current.getScore()));
                outputStream.write("\n\t Hand: " + current.getHand());
                outputStream.write("\n\t Pile: " + current.getPile());
                outputStream.write("\n\n");
            }

            outputStream.write("Table: " + table);
            //Write all the owners
            for(int i =0; i < buildOwners.size(); i++){
                outputStream.write("\n\nBuild Owner: "+ buildOwners.get(i));
            }
            outputStream.write("\n\nLast Capturer: "+ lastCapturer);
            outputStream.write("\n\nDeck: " + deck);
            outputStream.write("\n\nNext Player: " + nextPlayer);

            //Ensure all data is written and close
            outputStream.flush();
            outputStream.close();
        } catch (Exception e){
            return;
        }


    }

    /**
     * Remove all white space from a string
     * @param input The string to remove whitespace from
     * @return The string without spaces
     */
    public static String removeWhiteSpace(String input){
        String[] tokens = input.split(" ");
        String spacesRemoved = "";
        for(String token:tokens){
            if(token.length() == 0){
                continue;
            }
            spacesRemoved += token;
        }

        return spacesRemoved;
    }


    /**
     * Remove the header from a save file
     * @param raw The String to remove the header from
     * @return The string without the header (still has the leading white space)
     */
    private static String removeHeader(String raw){
        if(raw == null){
            return "";
        }

        //Find the colon and remove it
        int index = raw.indexOf(":");
        if(index >= 0){
            return raw.substring(index+1);
        }else{
            return raw;
        }
    }

    /**
     * Removes headers from a Vector of strings
     * @param raw Vector of Strings to remove headers from
     * @return A (copy) of the vector of strings with all the headers removed
     */
    private static Vector<String> removeHeaders(Vector<String> raw){
        if(raw == null){
            return new Vector <String>(1,1);
        }
        Vector<String> result = new Vector<String>(raw.size(), 1);

        //For every string
        for(int i = 0; i < raw.size(); i++){
            String parsed = removeHeader(raw.get(i));
            //If not blank after parsing it, add
            if(checkIfNonBlank(parsed)){
                result.add(parsed);
            } else{
                //These are labels themselves, so remove the colon at the end
                result.add(raw.get(i).substring(0, raw.get(i).length()-1));
            }
        }

        return result;
    }

    /**
     * Remove all blank lines from the List of strings
     * @param input The List of strings to remove blank ones
     * @return A **VECTOR** of Strings without blank lines
     */
    private static Vector<String> removeBlankLines(List<String> input){
        if(input == null){
            return new Vector<>(1,1);
        }

        Vector <String> vector = new Vector<String>(input.size(), 1);

        //If not blank, add to result
        for(int i =0; i < input.size(); i++){
            String current = input.get(i);
            if(checkIfNonBlank(current)){
                vector.add(current);
            }
        }

        return vector;

    }

    /**
     * Check if a line contains only white space
     * @param line The String to check
     * @return True if not blank, false if null or blank
     */
    private static boolean checkIfNonBlank (String line){
        if(line == null){
            return false;
        }

        return !line.equals("") && line.trim().length() > 0 ;
    }


}
