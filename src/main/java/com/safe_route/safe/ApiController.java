package com.safe_route.safe;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;  
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;



@RestController
public class ApiController {
    
    @RequestMapping("api/")
    public String api() {
        return "This is api page";
    }

    
    @GetMapping("safeRoute/findSafenode")
    public String safeNode(
        @RequestParam(value = "srcLong")String srcLong,
        @RequestParam(value = "srcLati")String srcLati,
        @RequestParam(value = "dstLong")String dstLong,
        @RequestParam(value = "dstLati")String dstLati) {
        
        JSONObject data = new JSONObject();
        JSONObject res = new JSONObject();

        data.put("srcLong", srcLong);
        data.put("srcLati", srcLati);
        data.put("dstLong", dstLong);
        data.put("dstLati", dstLati);

        res.put("response",data);
        res.put("status","200");

        String jsonResponse = res.toJSONString();

        
        return jsonResponse;
    }
    public class WayPoint{

        private String wayLong;
        private String wayLati;
    
        public WayPoint(String Long, String Lati){
            this.wayLong = Long;
            this.wayLati = Lati;
        }
    }
}