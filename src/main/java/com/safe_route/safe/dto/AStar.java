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


    /* 노드 고유 ID */
    public void setId(int _id){
        this.id = _id;
    }

    /* 부모 노드 */
    public void setParent(AStar node){
        this.parent = node;
    }  

    /* 노드 도로 유형 */
    public void setType(int _type){
        this.type = _type;
    }

    /* 노드 명 */
    public void setName(String _name){
        this.name = _name;
    }

    /* 노드 레이어 */
    public void setLayer(int _layer){
        this.layer = _layer;
    }

    /* 출발지-노드 간 거리 가중치 */
    public void setGScore(Double distance){
        this.gScore = this.parent.getGScore() + distance;
    }

    /* 출발지-노드 간 거리 가중치 */
    public void setGScore(){
        this.gScore = 0.0;
    }

    /* 노드-목적지 간 휴리스틱 거리 가중치 */
    public void setHScore(Double _hScore){
        this.hScore = _hScore;
    }

    /* 노드-목적지 간 휴리스틱 거리 가중치 */
    public void setHScore(){
        this.hScore = 0.0;
    }

    /* 노드 안전도 가중치 */
    public void setSScore(){
        this.sScore = 0.0; 
    }

    /* 노드 총 가중치 */
    public void setSScore(SafePosModel node){
        this.sScore = (Double)0.0; 
    }

    /* 노드 총 가중치 */
    public void setSScore(Double roadSafety){
        this.sScore = roadSafety; 
    }

    /* 노드 총 가중치 */
    public void setFScore(){
        this.fScore = ((this.gScore + this.hScore) * this.sScore) / 10000;
    }

    /* 노드 좌표(경도) */
    public void setLongi(Double longi){
        this.longi = longi;
    }

    /* 노드 좌표(위도) */
    public void setLati(Double lati){
        this.lati = lati;
    }

    /* 노드 집합 중복 판정 */
    @Override
    public boolean equals(Object t){
        if(t instanceof AStar){
            return id == ((AStar)t).id;
        } else {
            return false;
        }
    }
    
    /* 노드 ID 확인 */
    @Override
    public int hashCode(){
        return id;
    }
}