package com.example.TP3_CAR.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.TP3_CAR.Actors.Mapper;
import com.example.TP3_CAR.Actors.Reducer;
import com.example.TP3_CAR.Actors.ResponseActor;
import com.example.TP3_CAR.Dto.MapperDTO;
import com.example.TP3_CAR.Dto.ReducerDTO;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class AkkaService {

    private ActorSystem Mappersystem;
    private ActorSystem Reducersystem;

    private final List<ActorRef> mappers = new ArrayList<>();
    private final List<ActorRef> reducers;

    public AkkaService() {
        this.reducers = new ArrayList<>();
    }

    /* create 3 acteurs mapper et 2 acteur reducer */
    public void init() {
        this.Mappersystem = ActorSystem.create("MapperSystem");
        this.Reducersystem = ActorSystem.create("Reducersystem");
        ActorRef Mapper1,Mapper2,Mapper3,Reducer1,Reducer2;
        Mapper1 = Mappersystem.actorOf( Props.create(Mapper.class), "Mapper1" );
        Mapper2 = Mappersystem.actorOf( Props.create(Mapper.class), "Mapper2" );
        Mapper3 = Mappersystem.actorOf( Props.create(Mapper.class), "Mapper3" );
        Reducer1 = Reducersystem.actorOf( Props.create(Reducer.class), "Reducer1" );
        Reducer2 = Reducersystem.actorOf( Props.create(Reducer.class), "Reducer2" );

        mappers.add(Mapper1);
        mappers.add(Mapper2);
        mappers.add(Mapper3);   
        reducers.add(Reducer1);
        reducers.add(Reducer2); 
    }
   
    /** Distribuer les lignes du fichier alternativement à chaque acteur Mapper */
    public void analyze(MultipartFile multipartFile) {
        Path tempFilePath = Paths.get("src/main/resources/targetFile.tmp");

        try {
            Files.write(tempFilePath, multipartFile.getBytes());

            try (BufferedReader br = Files.newBufferedReader(tempFilePath)) {
                String line;
                int mapperIndex = 0;

                while ((line = br.readLine()) != null) {
                    MapperDTO dto = new MapperDTO();
                    dto.setLine(line);
                    dto.setReducers(reducers);
                    mappers.get(mapperIndex % mappers.size()).tell(dto, ActorRef.noSender());
                    mapperIndex++;  
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(tempFilePath);
            } catch (IOException e) {
                System.err.println("Failed to delete temporary file: " + e.getMessage());
            }
        }
    }

    /** Interroger les acteurs Reducer pour obtenir le nombre d’occurrences d’un mot*/
    public int search(String word) {
        if (word.isEmpty() || word.equals(" ")) {
            return 0;
        }
        char firstLetter = Character.toLowerCase(word.charAt(0));
        int reducerIndex = Math.abs((firstLetter - 'a')) % reducers.size();
        ReducerDTO dto = new ReducerDTO(word);

        CompletableFuture<Integer> future = new CompletableFuture<>();

        reducers.get(reducerIndex).tell(dto, ActorRef.noSender());

        ActorRef responseActor = Mappersystem.actorOf(Props.create(ResponseActor.class, future));

        reducers.get(reducerIndex).tell(dto, responseActor);

        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return 0; 
        }
    }
}
