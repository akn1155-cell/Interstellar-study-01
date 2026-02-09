package org.example.brickscoreapitest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrickScoreController {

    @GetMapping("/api/hello")
    public String hello() {
        return "안녕 도커테스트야!";
    }
}
