package com.example.TP3_CAR.Actors;

import java.util.List;

import com.example.TP3_CAR.Dto.MapperDTO;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Mapper extends UntypedActor {

    /** Reçoit une ligne d'un fichier, 
     * la sépare en mots et les envoie aux différents reducers en fonction de leur première lettre, dans l'ordre alphabétique. */
    @Override
    public void onReceive(Object message) {
        if (message instanceof MapperDTO dto) {
            String[] words =  dto.getLine().split(" ");
            for (String word : words) {
                if (!word.isEmpty()) {
                    this.distributeWordToReducers(word, dto.getReducers());
                }
            }
        }
    }

    public  void distributeWordToReducers(String word, List<ActorRef> reducers) {
        char firstLetter = Character.toLowerCase(word.charAt(0));
        int reducerIndex = Math.abs((firstLetter - 'a')) % reducers.size();
        reducers.get(reducerIndex).tell(word, ActorRef.noSender());
    }
}
