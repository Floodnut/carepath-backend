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
    private Double fScore;  //   total Score = g + h + s
    private Double gScore;  //   누적 거리
    private Double hScore;  //   휴리스틱 추정 값 : 현재노드 to 최종 목적지
    private Double sScore;  //   안전도

    private int layer;
    private Double lati;    //   위도
    private Double longi;   //   경도

    @Override
    public void setGScore(){
        this.gScore = this.parent.getGScore() +
    }

    @Override
    public void setFScore(){
        this.fScore = this.gScore + this.hScore + this.sScore;
    }

    @Override
    public void setSScore(){
        this.sScore = 0; 
    }

    @Override
    public boolean equals(Object t){
        if(t instanceof AStar){
            return id == ((AStar)t).id;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return id;
    }
}