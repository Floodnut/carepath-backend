package com.safe_route.safe.function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AStar {
    private int id;
    private AStar parent;
    private Double fScore; //   total Score = g + h
    private Double gScore; //   시작노드 to 현재 노드 거리 (누적)
    private Double hScore; //   휴리스틱 추정 값 : 현재노드 to 최종 목적지
    private Double sScore; //   안전도

    // public Double safeDegreeEquation(){
        
    // }
}