package com.safe_route.safe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.PropertySource;

@RestController
@RequestMapping
@PropertySource("classpath:api.properties")
public class DefaultController {

    @GetMapping
    public String defaultPage(){
        try{
            return "Default Page";
        }
        catch(Exception e){
            return "Default Page";
        }
    }

    /* API Test */
    @GetMapping("/api")
    public String apiPage() {
        try{
            return "This is api page";
        }
        catch(Exception e){
            return "Invalid data";
        }
    }
}
