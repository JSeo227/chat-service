# 채팅 서비스 프로젝트

> 실시간 채팅 서비스 구현을 통해 WebSocket, WebRTC, Kafka 등 최신 웹 기술을 학습하고 적용한 프로젝트 입니다.

---

## 프로젝트 개요
- **개발기간**: 2025.03~2025.06(4개월)
- **팀구성**: 개인프로젝트
- **목표**: 실시간 통신 시스템 구현

---

## 1. 주요 기능

**실시간 텍스트 채팅**
- N:M 채팅 지원
- 채팅방 인원 실시간 표시
- 이전 채팅 기록 확인
- 접속/퇴장 알림 실시간 표시

**실시간 화상 채팅**
- WebRTC 기반 P2P 연결을 통한 실시간 영상 통화
- 음성/영상 on/off 제어
- STUN 서버를 활용한 NAT 환경 대응

**비동기 메시지 처리**
- Kafka를 통한 메시지 큐잉 및 비동기 처리
- MongoDB 기반 메시지 영속성 보장
- 이벤트 기반 아키텍처로 확장성 확보
  
**회원 및 채팅방 관리**
- Session 기반 로그인/로그아웃 
- 회원 가입, 채팅방 생성/삭제
- 실시간 사용자 상태 관리

---

## 2. 기술 스택

| 영역 | 사용 기술 |
|----------|-----------------------------|
| **백엔드** | Spring Boot 3., JPA |
| **프론트엔드** | JavaScript, Thymeleaf |
| **통신** | WebSocket(STOMP), WebRTC |
| **이벤트 브로커** | Zookeeper, Apache Kafka |
| **데이터베이스** | MySQL(회원/채팅방), MongoDB(메시지) |
| **개발 환경** | Git, IntelliJ, Postman, Docker |

---

## 3. 시스템 구조 요약
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Client    │◄──►│ Spring Boot │◄──►│   Kafka     │
│ (Browser)   │    │   Server    │    │  Cluster    │
└─────────────┘    └─────────────┘    └─────────────┘
                          │                    │
                          ▼                    ▼
                   ┌─────────────┐    ┌─────────────┐
                   │   MySQL     │    │  MongoDB    │
                   │ (채팅방/회원)│    │  (메시지)    │
                   └─────────────┘    └─────────────┘
```

**데이터 처리**
- 메시지 송신: Client → WebSocket → Spring Boot → Kafka Producer
- 메시지 저장: Kafka Consumer → MongoDB
- 메시지 수신: MongoDB → Spring Boot → WebSocket → Client

---

4. 개발 과정 및 문제상황

**Kafka 도입**
- 원인: 메시지 로그를 확인하기 위해서 RDB 는 비효율적임
- 해결: Kafka와 MongoDB를 사용
- 결과: 로그 확인과 함께 처리 속도 향상

**WebRTC 구현**
- 원인: WebRTC를 위해서는 STUN/TURN 서버 필요
- 해결: Google에서 제공하는 서버 사용
- 결과: P2P 연결 성공률 향상으로 문제 해결

**HTTPS 적용**
- 원인: WebRTC를 하기 위해서는 HTTPS를 사용해야함
- 해결: 자체 서명 인증서를 사용하여 HTTPS 환경 구축(로컬서버를 사용해 Nginx 사용포기)
- 결과: 보안 연결을 통한 WebRTC 정상 동작 확인

---

5. 화면구성

**메인화면**
- 채팅방 목록 표시
- 새 채팅방 생성 기능

**채팅화면**
- 이전 메시지 스크롤 조회
- 실시간 메시지 송/수신
- 참여자 목록 확

**화상채팅화면**
- 비디오 화면 표시
- 음성/비디오 제어 패널

---

## 6. 실행 및 접속 안내

### 6-1. 시스템 요구사항
- Spring Boot 3.x 이상
- Java 17 이상

### 6-2. 로컬 개발 환경 구성

```bash
# 저장소 클론
git clone https://github.com/username/chat-service.git
cd chat-service

# Docker 기반 전체 서비스 실행
docker-compose up -d --build
```

### 6-3. 웹 접속

서버가 실행되면 웹 브라우저에서 아래 주소로 접속합니다:

🔗 **[https://localhost:8443](https://localhost:8443)**

> ⚠️ *HTTPS 사용으로 인해 인증서 경고가 발생할 수 있습니다.*  
> 브라우저에서 "고급 설정 → 계속 진행"을 선택하여 접속하세요.

---

개발자 정보
- Name : 서준석
- Email : glad100@naver.com
- GitHub : https://github.com/JSeo227

---

감사의 말

이 프로젝트는 다음 오픈소스 프로젝트들의 도움을 받았습니다:
- https://github.com/orgs/spring-projects/repositories?type=all
- https://github.com/Benkoff/WebRTC-SS
- https://github.com/FiloSottile/mkcert

참고 문서
- Spring WebSocket Documentation
- Apache Kafka Documentation
- WebRTC API Documentation
