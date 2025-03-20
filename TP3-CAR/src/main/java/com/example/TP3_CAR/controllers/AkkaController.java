package com.example.TP3_CAR.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.TP3_CAR.service.AkkaService;



@Controller
@RequestMapping("/akka")
public class AkkaController {

    @Autowired
    private AkkaService service;

    @GetMapping("")
    public String homeBis() {
        return "/akka/home";
    }
    

    @GetMapping("/home")
    public String home() {
        return "/akka/home";
    }
    
    @GetMapping("/init")
    public RedirectView init() {
        service.init();
        return new RedirectView("/akka/home");
    }

    @PostMapping("/analyze")
    public RedirectView analyze(@RequestParam("file") MultipartFile file) {
        service.analyze(file);
        return new RedirectView("/akka/home");
    }
    
    @PostMapping("/search")
    public ModelAndView search(@RequestParam String word) {
        var count = service.search(word);
        var model = Map.of(
            "word", word,
            "count", count
        );
        return new ModelAndView("/akka/home", model);
    }
    
}
