package com.example.TP3_CAR.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TP3_CAR.Actors.Mapper;
import com.example.TP3_CAR.Actors.Reducer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class AkkaService {

    private ActorSystem system;

    private List<ActorRef> mappers = new ArrayList<ActorRef>();
    private List<ActorRef> reducers = new ArrayList<ActorRef>();;

    /* create 3 acteurs mapper et 2 acteur reducer */
    public void init() {
        this.system = ActorSystem.create("AkkaSystem");
        ActorRef Mapper1,Mapper2,Mapper3,Reducer1,Reducer2;
        Mapper1 = system.actorOf( Props.create(Mapper.class), "Mapper1" );
        Mapper2 = system.actorOf( Props.create(Mapper.class), "Mapper2" );
        Mapper3 = system.actorOf( Props.create(Mapper.class), "Mapper3" );
        Reducer1 = system.actorOf( Props.create(Reducer.class), "Reducer1" );
        Reducer2 = system.actorOf( Props.create(Reducer.class), "Reducer2" );

        mappers.add(Mapper1);
        mappers.add(Mapper2);
        mappers.add(Mapper3);   
        reducers.add(Reducer1);
        reducers.add(Reducer2); 
    }
   
    /** Distribuer les lignes du fichier alternativement à chaque acteur Mapper */
    public void analyze(File file) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < lines.size(); j++) {
            mappers.get(j % mappers.size()).tell(lines.get(j), ActorRef.noSender());
        }
    }

    
    /** Interroger les acteurs Reducer pour obtenir le nombre d’occurrences d’un mot*/
    public void search(String word) {
       
       //todo 
    }
}
