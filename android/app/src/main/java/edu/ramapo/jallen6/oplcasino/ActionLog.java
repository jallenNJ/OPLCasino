package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class ActionLog {

    static private Vector<String> log;


    public static void init(){
        log = new Vector<String>(40, 10);
    }

    public static String getLast(){
        return log.lastElement();
    }


   public static int getLogSize(){
        return log.size();
   }

    public static void addLog(String s){
        log.add(s);
    }

    /**
     Get log entry at specified index
     @param  index -- The index of log to retrieve
     @return String at the ith location. Returns empty string if invalid index
     */

   public static String getLogEntry(int index){
        if(index < 0 || index >= log.size()){
            return "";
        }
        return log.get(index);
   }
}
