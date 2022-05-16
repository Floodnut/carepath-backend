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
   <tr>
   <td align="center">경찰청_경찰관서 위치, 주소 현황</a></td>
   <td align="center"><a>경유지 추가를 위한 경찰관서 위치 조회 </a></td>
   <td align="center"><a>File Data</a></td>
   <td align="center"><a>https://www.data.go.kr/data/15054711/fileData.do</a></td>
      <tr>
   <td align="center">유흥주점영업</a></td>
   <td align="center"><a>경유지 추가를 위한 유흥주점 밀도 조회</a></td>
   <td align="center"><a>File Data</a></td>
   <td align="center"><a>https://www.localdata.go.kr/data/dataView.do?opnSvcId=07_23_02_P</a></td>
      <tr>
   <td align="center">경상남도 창원시_공동주택현황</a></td>
   <td align="center"><a>경유지 추가를 위한 창원지역 공동주택 위치 조회</a></td>
   <td align="center"><a>File Data</a></td>
   <td align="center"><a>https://www.data.go.kr/data/3066430/fileData.do</a></td>
  </tr>
 </table>

## 2. 개발 환경 및 사용 기술
<table>
  <tr>
   <td align="center">담당</a></td>
   <td align="center">역할</td>
   <td align="center">개발환경</td>
   <td align="center">사용 기술</td>
  </tr>
  <tr>
   <td align="center"> <a href="https://github.com/nsih"> 남시현 </a></td>
   <td align="center"><a> 사용자 애플리케이션 개발 </a></td>
   <td align="center"><a>Windows 10, Android Studio</a></td>
   <td align="center"><a>JAVA, Android</a></td>
  </tr>
    <tr>
   <td align="center"> <a href="https://github.com/Floodnut">정금종 </a></td>
   <td align="center"><a> API 서버 개발 </a></td>
   <td align="center"><a>MacOS BigSur, Ubuntu, VSCode</a></td>
   <td align="center"><a>JAVA, Spring Boot, NodeJS, K3S, MYSQL, Docker, Redis</a></td>
  </tr>
 </table>

## 3. 구동 환경
### 사용자 애플리케이션
- JAVA, [Android](https://github.com/nsih/Find_Path_Application)
### API 서버
- JAVA, Spring Boot
### 보행자경로/교통정보 요청 서버
- Docker, [NodeJS-Express](https://github.com/Floodnut/nodejs-server-1)
### DataBase
- K3S, MySQL
- Docker, Redis

## 4. 참고 자료
- [참고자료]()

## 5. 버그 제보
버그 제보는 이슈를 통해 부탁드립니다.
