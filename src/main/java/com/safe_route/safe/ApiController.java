package com.safe_route.safe;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    
    @RequestMapping("/api/")
    public String api() {
        return "This is api page";
    }
}
