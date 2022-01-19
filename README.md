<div align="center">    
 
# 안전 길 찾기 어플리케이션 서버  
         
</div>
       
## USED API
<table>
  <tr>
    <td align="center">API</a></td>
    <td align="center">용도</td>
    <td align="center">형태</td>
    <td align="center">URL</td>
    <td align="center">비고</td>
  </tr>
    <tr>
    <td align="center">T-Map API</a></td>
    <td align="center"><a>최종 도보 경로 반환</a></td>
    <td align="center"><a>-</a></td>
   <td align="center"><a>https://tmapapi.sktelecom.com/</a></td>
  </tr>
    <tr>
    <td align="center">OSRM</a></td>
    <td align="center"><a>도보 최단 경로 찾기 및 통과 도로 조회</a></td>
    <td align="center"><a>REST API</a></td>
   <td align="center"><a>https://github.com/Project-OSRM/osrm-backend</a></td>
  </tr>
    <tr>
    <td align="center">국토교통부<br>CCTV위치도</a></td>
    <td align="center"><a>경유지 추가를 위한 CCTV 위치 조회</a></td>
    <td align="center"><a>REST API</a></td>
   <td align="center"><a>https://www.data.go.kr/data/3068943/openapi.do</a></td>
  </tr>
    <tr>
    <td align="center">cctv 정보</a></td>
    <td align="center"><a>CCTV가 위치한 도로명 주소 확인</a></td>
    <td align="center"><a>REST API</a></td>
   <td align="center"><a>https://www.data.go.kr/data/15058647/openapi.do</a></td>
   </tr>
     </tr>
    <tr>
    <td align="center">서울시 교통정보</a></td>
    <td align="center"><a>서울시 도로별 실시간 교통 정보 확인</a></td>
    <td align="center"><a>-</a></td>
   <td align="center"><a>https://topis.seoul.go.kr/refRoom/openRefRoom_4.do</a></td>
   <td align="center"><a>현재 최종 사용 후보군이 아님</a></td>
   </tr>
 </table>

## OSRM
### Changed
```
# osrm-backend/profile/foot.lua

    speeds = Sequence {
      highway = {
        primary         = walking_speed,
        primary_link    = walking_speed,
        secondary       = walking_speed,
        secondary_link  = walking_speed,
        tertiary        = walking_speed,
        tertiary_link   = walking_speed,
        unclassified    = walking_speed,
        residential     = walking_speed,
        road            = walking_speed,
        living_street   = walking_speed,
        service         = walking_speed,
        track           = walking_speed,
        path            = walking_speed,
        steps           = walking_speed,
        pedestrian      = walking_speed,
        footway         = walking_speed,
        pier            = walking_speed,

        -- add this
        trunk           = walking_speed,
        trunk_link      = walking_speed,
      },

```
### How to Run
```
wget http://download.geofabrik.de/asia/south-korea-latest.osm.pbf
```

```
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/foot.lua /data/south-korea-latest.osm.pbf

```


```
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/south-korea-latest.osrm
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/south-korea-latest.osrm
docker run -t -i -d -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/south-korea-latest.osrm
```
### How to Use


## 버그 제보
버그 제보는 이슈를 통해 부탁드립니다.
