package com.safe_route.safe.dto;

import com.safe_route.safe.model.SafePosModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AStar {
    private int id;
    private AStar parent;
    private int type;
    private String name;
    private Double fScore;  //   total Score = g + h + s
    private Double gScore;  //   누적 거리
    private Double hScore;  //   휴리스틱 추정 값 : 현재노드 to 최종 목적지
    private Double sScore;  //   안전도

    private int layer;
    private Double lati;    //   위도
    private Double longi;   //   경도


    public void setId(int _id){
        this.id = _id;
    }

    public void setParent(AStar node){
        this.parent = node;
    }   

    public void setType(int _type){
        this.type = _type;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public void setLayer(int _layer){
        this.layer = _layer;
    }  

    public void setGScore(Double distance){
        this.gScore = this.parent.getGScore() + distance;
    }

    public void setGScore(){
        this.gScore = 0.0;
    }

    public void setHScore(Double _hScore){
        this.hScore = _hScore;
    }

    public void setSScore(){
        this.sScore = 0.0; 
    }

    public void setSScore(SafePosModel node){
        this.sScore = (Double)0.0; 
    }

    public void setFScore(){
        this.fScore = (this.gScore + this.hScore + this.sScore)/1000;
    }

    public void setLongi(Double longi){
        this.longi = longi;
    }

    public void setLati(Double lati){
        this.lati = lati;
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