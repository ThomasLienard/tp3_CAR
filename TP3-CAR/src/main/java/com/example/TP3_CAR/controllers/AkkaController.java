package com.example.TP3_CAR.controllers;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.TP3_CAR.service.AkkaService;



@Controller
@RequestMapping("/akka")
public class AkkaController {

    @Autowired
    private AkkaService service;

    @GetMapping("../")
    public String index() {
        return "/akka/home";
    }

    @GetMapping("/home")
    public String home() {
        return "/akka/home";
    }
    
    @GetMapping("/init")
    public String init() {
        service.init();
        return "/akka/home";
    }

    @PostMapping("/analyze")
    public RedirectView analyze(@RequestBody File entity) {
        service.analyze(entity);
        return new RedirectView("/akka/home");
    }
    
    @PostMapping("/search")
    public ModelAndView search(@RequestBody String word) {
        service.search(word);
        var model = Map.of(
            "word", word,
            "count", 0
        );
        return new ModelAndView("/akka/home", model);
    }
    
}
