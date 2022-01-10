package com.safe_route.safe;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "This is Demo index page";
    }
}
