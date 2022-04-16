<div align="center">    
 
# 안전 길 찾기 어플리케이션 서버  
         
</div>
       
## 1. USED API
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
   <td align="center"><a>보행자 경로 조회, 교통정보 조회 </a></td>
   <td align="center"><a>REST API</a></td>
   <td align="center"><a>https://tmapapi.sktelecom.com/</a></td>
  </tr>
  <tr>
   <td align="center">국가교통정보센터</a></td>
   <td align="center"><a>표준노드링크 기반의 교통정보 조회</a></td>
   <td align="center"><a>File Data, REST API</a></td>
   <td align="center"><a>https://www.its.go.kr/opendata/</a></td>
  </tr>
  <tr>
   <td align="center">전국CCTV표준데이터</a></td>
   <td align="center"><a>경유지 추가를 위한 CCTV 위치 조회</a></td>
   <td align="center"><a>File Data</a></td>
   <td align="center"><a>https://www.data.go.kr/data/15013094/standard.do</a></td>
  </tr>
  <tr>
   <td align="center">전국보안등정보표준데이터</a></td>
   <td align="center"><a>경유지 추가를 위한 보안등 위치 조회</a></td>
   <td align="center"><a>File Data</a></td>
   <td align="center"><a>https://www.data.go.kr/data/15017320/standard.do</a></td>
  </tr>
 </table>

## 2. 구동 환경
### API 서버
- Java-Spring Boot
### 보행자경로/교통정보 요청 서버
- [NodeJS-Express](https://github.com/Floodnut/nodejs-server-1)
- Docker
### DataBase
- MySQL
- K3S


## 3. How to Run

## 4. How to Use

## 5. 버그 제보
버그 제보는 이슈를 통해 부탁드립니다.
