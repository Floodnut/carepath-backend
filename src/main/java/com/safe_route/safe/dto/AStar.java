package com.safe_route.safe.dto;

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
    private float fScore; //total Score -> f = (g+h) * s
    private float gScore; //거리
    private float hScore; //남은 거리
    private float sScore; //안전도
}