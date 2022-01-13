package com.safe_route.safe;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "a";
    }
}