package com.example.TP3_CAR.Actors;

import java.util.HashMap;
import java.util.Map;

import com.example.TP3_CAR.Dto.ReducerDTO;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {

    private Map<String, Integer> map = new HashMap<>();
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof String m) {
            map.put(m, map.getOrDefault(m, 0) + 1);
        } 
        else if (message instanceof ReducerDTO dto) {
            int count = map.getOrDefault(dto.getWord(), 0);
            dto.setCount(count);
            getSender().tell(dto, getSelf());
        }
    }

    public int getWordCount(String word) {
        return map.get(word);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
    
}
