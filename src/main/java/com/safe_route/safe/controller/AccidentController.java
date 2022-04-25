package com.safe_route.safe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.safe_route.safe.service.AccidentService;
import com.safe_route.safe.model.AccidentModel;
import com.safe_route.safe.dto.AccidentDTO;
import com.safe_route.safe.dto.ResponseDTO;

@RestController
@RequestMapping("accident")
public class AccidentController {

    @Autowired
    private AccidentService service;

        /* 현재 위치 근처 사고다발지역 조회 */
        @GetMapping("/frequentzone")
        public ResponseEntity<?> findNode(
            @RequestParam(required = true) int mode,
            @RequestParam(required = true) String srcLati,
            @RequestParam(required = true) String srcLongi,
            @RequestParam(required = false) String dstLati,
            @RequestParam(required = false) String dstLongi
        ){
            try{
                Double lati = Double.parseDouble(srcLati);
                Double longi = Double.parseDouble(srcLongi);
                Double lati2 = 0.0;
                Double longi2 = 0.0;
                List<AccidentModel> zones = new ArrayList<AccidentModel>();
    
                /* 현재 위치 기준 */   
                if(mode == 1){              
                    zones = service.findAccident(lati - 0.03, longi - 0.03, lati + 0.03, longi + 0.03);
                }
                /* 지정한 범위 기준 */   
                else if(mode == 2){
                    lati2 = Double.parseDouble(dstLati);
                    longi2 = Double.parseDouble(dstLongi);
                    zones = service.findAccident(lati, longi, lati2, longi2);
                }
                
                System.out.println(zones);
                List<AccidentDTO> dtos = zones.stream().map(AccidentDTO::new).collect(Collectors.toList());
                ResponseDTO<AccidentDTO> response = ResponseDTO.<AccidentDTO>builder().
                                                            total(zones.size()).
                                                            data(dtos).
                                                            build();
                return ResponseEntity.ok().body(response);  
            }catch(Exception e){
                return ResponseEntity.ok().body(e.toString());
            }
        }
    
}
