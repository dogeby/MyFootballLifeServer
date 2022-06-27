# MyFootballLifeServer
[MyFootballLife](https://github.com/dogeby/MyFootballLife) 구현을 위해 만든 서버
## 기술 스택
Spring Boot, Kotlin, Retrofit, Firebase, Youtube Data Api, Twitter Api
## 주요 기능
축구 팀의 공식 트위터나, 유튜브 채널 <br>
또는 축구 관련 트위터 User, 유튜브 Channel에대해서 
1. 정해진 시간 마다 User 정보를 Firebase Realtime DB에 업데이트한다.
2. 정해진 시간 마다 최신 트윗들을 Firebase Realtime DB에 업데이트한다.
3. 정해진 시간 마다 Channel 정보를 Firebase Realtime DB에 업데이트한다.
4. 정해진 시간 마다 Uploads Playlist의 최신 Video들을 Firebase Realtime DB에 업데이트한다.
