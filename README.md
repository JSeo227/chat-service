# 채팅 서비스 프로젝트

> 실시간 텍스트 및 화상 채팅 서비스 구현 및 채팅 메시지 운영 및 관리

---

## 1. 주요 기능

- **Spring Boot + JPA + MySQL** 로 회원 및 채팅방 관리
- **Thymeleaf** 기반 서버사이드 렌더링 웹 프론트엔드
- **WebSocket (STOMP)** 기반 실시간 텍스트 채팅
- **WebRTC** 기반 실시간 화상 채팅
- **Kafka + MongoDB**로 메시지 비동기 처리 및 관리

---

## 2. 기술 스택

| 영역       | 사용 기술                       |
|----------|-----------------------------|
| 백엔드      | Spring Boot, JPA            |
| 프론트엔드    | Thymeleaf                   |
| 데이터베이스   | MySQL(회원/채팅방), MongoDB(메시지) |
| 통신       | WebSocket, WebRTC           |
| 메시지 브로커  | Zookeeper, Kafka |
| 개발 도구    | IntelliJ, Postman           |
| 인프라 및 배포 | Docker                      |   

---

## 3. 시스템 구조 요약

- **회원 및 채팅방 관리** : RDB (MySQL) + JPA 사용
- **메시지 처리** : NoSQL (MongoDB) + Kafka 사용
- **UI/UX** : 서버사이드 렌더링 방식 (Thymeleaf 템플릿)

---

## 4. 접속 안내

프로젝트를 다운로드한 후, 웹 브라우저에서 아래 주소로 접속하세요.

➡️ **[https://localhost:8443](https://localhost:8443)**  
(*HTTPS 프로토콜을 사용하므로, 접속 시 SSL 인증서 경고가 발생할 수 있습니다.*)
