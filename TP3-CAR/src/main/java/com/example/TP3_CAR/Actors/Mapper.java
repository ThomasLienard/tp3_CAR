package com.example.TP3_CAR.Actors;

import akka.actor.UntypedActor;

public class Mapper extends UntypedActor {
    
    @Override
    public void onReceive( Object message ) {
        if( message instanceof String m ) {
            String[] words = m.split(" ");
            for (String word : words) {
                getSender().tell(word, getSelf());
            }   
        } 
    }
    
}
