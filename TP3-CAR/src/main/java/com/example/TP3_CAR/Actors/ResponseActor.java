// filepath: /home/m1ipint/thomas.lienard.etu/CAR/tp3_CAR/TP3-CAR/src/main/java/com/example/TP3_CAR/Actors/ResponseActor.java
package com.example.TP3_CAR.Actors;

import java.util.concurrent.CompletableFuture;

import com.example.TP3_CAR.Dto.ReducerDTO;

import akka.actor.UntypedActor;

public class ResponseActor extends UntypedActor {
    private final CompletableFuture<Integer> future;

    public ResponseActor(CompletableFuture<Integer> future) {
        this.future = future;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof ReducerDTO dto) {
            future.complete(dto.getCount());
            getContext().stop(getSelf()); // Arrêter l'acteur après réception de la réponse
        } else {
            unhandled(message);
        }
    }
}