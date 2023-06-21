package com.futurefactorytech.reviewer.api_rest.controllers.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String getRootUrl() {
        return "Backend API is live.";
    }

}
