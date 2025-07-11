# 채팅 서비스 프로젝트

> 실시간 채팅 서비스 구현을 통해 WebSocket, WebRTC, Kafka 등 최신 웹 기술을 학습하고 적용한 프로젝트 입니다.

---

## 1. 주요 기능

- **Spring Boot + JPA + MySQL** 로 회원 및 채팅방 관리
- **Thymeleaf** 기반 서버사이드 렌더링 웹 프론트엔드
- **WebSocket (STOMP)** 기반 실시간 텍스트 채팅
- **WebRTC** 기반 실시간 화상 채팅
- **Kafka + MongoDB**로 메시지 비동기 처리 및 관리

---

## 2. 기술 스택

| 영역 | 사용 기술 |
|----------|-----------------------------|
| **백엔드** | Spring Boot, JPA |
| **프론트엔드** | Thymeleaf |
| **통신** | WebSocket, WebRTC |
| **메시지 브로커** | Zookeeper, Kafka |
| **데이터베이스** | MySQL(회원/채팅방), MongoDB(메시지) |
| **개발 환경** | IntelliJ, Postman, Docker |

---

## 3. 시스템 구조 요약

- **회원 및 채팅방 관리** : RDB (MySQL) + JPA 사용
- **메시지 처리** : NoSQL (MongoDB) + Kafka 사용
- **UI/UX** : 서버사이드 렌더링 방식 (Thymeleaf 템플릿)

---

## 4. 실행 및 접속 안내

### 1️⃣ 서버 실행

아래 명령어로 Docker 기반 전체 서비스를 실행하세요:

```bash
docker compose up -d --build
```

### 2️⃣ 웹 접속

서버가 실행되면 웹 브라우저에서 아래 주소로 접속합니다:

🔗 **[https://localhost:8443](https://localhost:8443)**

> ⚠️ *HTTPS 사용으로 인해 인증서 경고가 발생할 수 있습니다.*  
> 브라우저에서 "고급 설정 → 계속 진행"을 선택하여 접속하세요.
