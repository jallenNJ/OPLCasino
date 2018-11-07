package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class ActionLog {

    static private Vector<String> log;


    static void init(){
        log = new Vector<String>(40, 10);
    }

    static String getLast(){
        return log.lastElement();
    }

    static void addLog(String s){
        log.add(s);
    }
}
