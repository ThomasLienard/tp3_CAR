package com.example.TP3_CAR.Actors;

import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {

    private Map<String, Integer> map = new HashMap<String, Integer>();
    
    @Override
    public void onReceive( Object message ) {
        if( message instanceof String m ) {
            if (map.containsKey(m)) {
                map.put(m, map.get(m) + 1);
            } else {
                map.put(m, 1);
            }
        } 
    }

    public int getWordCount(String word) {
        return map.get(word);
    }
    
}
