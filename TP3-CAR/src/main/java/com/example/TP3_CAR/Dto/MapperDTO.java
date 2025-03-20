package com.example.TP3_CAR.Dto;

import java.util.List;

import akka.actor.ActorRef;

public class MapperDTO {
    
    private String line;
    private List<ActorRef> reducers;
    
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public List<ActorRef> getReducers() {
        return reducers;
    }
    public void setReducers(List<ActorRef> reducers) {
        this.reducers = reducers;
    }
}
