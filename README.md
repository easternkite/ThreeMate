# ThreeMate(쓰리 메이트)
음식 메뉴 선정에 어려움을 겪는 1인 가구를 위해 만든 음식추천 모바일 앱.  
스무고개 형식과 룰렛 형식의 음식추천, BMR, BMI, 일일 권장 칼로리 계산, 내 주변 음식점 찾기, 음식 사전 기능이 있다.



## Technical Lead
* Android Native(JAVA)  
* Firebase (Authentication, Realtime Database)  
* SQLite DB와 Firebase DB 상호 연동  
* KakaoMap API를 이용한 맵서비스  
* Kakao 로컬 검색 REST API(JSON)룰 이용한 내 주변 음식점 찾기  
* 식품의약품안전처 영양정보 Open API를 통한 음식 영양정보 파싱(Xml)  
* Recycler View 기반의 Custom ListView 구현  
* JSON data를 이용한 질문 리스트 및 음식 정보 구현  


## Project Lead  
* Zoom 화상회의와 메신저를 활용한 주기적 진행사항 보고  
* 간트 차트를 이용한 프로젝트 일정관리 및 업무분담  
* GitHub를 통한 개발 프로세스 기록 및 버전관리  
* 간트차트를 참고하여 업무 진행이 부진한 팀원을 도와주는 방식으로 진행  

## Problem Solving
* **P1** : 추천 음식 영양정보 출력 시 음식 사진이 나왔으면 좋겠음
* **Sol1** : 음식 List가 담긴JSON파일을 따로 만들어 사진 URL 기재함
* **P2** : BMI, BMR을 구하려면 유저 정보(키,몸무게, 운동강도 등)이 필요함  
* **Sol2** :  첫 로그인 시 정보 기입 Dialog를 띄워 유저 정보 DB에 저장함
* **P3** : 내 주변 음식점의 맛집 랭킹을 구현하고 싶음  
* **Sol3** : [**해결 실패**] 순위를 구현하기 위해 웹 크롤링을 통해 내 주변 음식점 평점을 크롤링하는 방법을 생각 하였음.  
하지만 이는 내 음식점을 하나하나 검색하여 평점을 크롤링을 해야 했었음
당연히 데이터 뜨는 속도가 현저히 줄어들어 결국 포기해야만 했다
* **P4** : KAKAO MAP API를 적용시켰지만 KAKAO 로고만 뜨고 지도는 표시 안되는 문제
* **Sol4** : API구동을 위한 디버깅용 키 해시 등록을 하며 해결
