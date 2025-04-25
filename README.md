💬 채팅 서비스
실시간 채팅 서비스 구현 프로젝트

🧩 주요 기능
WebSocket(STOMP) 기반 실시간 채팅

Kafka를 통한 메시지 비동기 전송 및 처리

MongoDB에 채팅 메시지 저장

Spring + JPA + MySQL로 회원 및 채팅방 관리

Thymeleaf 기반 웹 프론트엔드 구성

⚙️ 기술 스택

영역	사용 기술
백엔드	Spring Boot, JPA, MySQL
메시징	Kafka, Spring Kafka Consumer, MongoDB
프론트엔드	Thymeleaf
통신	WebSocket, STOMP
개발 도구	IntelliJ, Postman

🏗️ 구조 요약
회원 및 방 관리: RDB(MySQL) + JPA

메시지 처리: Kafka Producer → Kafka Consumer → MongoDB 저장

UI/UX: 서버 사이드 렌더링 방식의 Thymeleaf 템플릿