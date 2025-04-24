package com.example.chat.chat_service.domain;

public class WebRtcMessage {
    private String from; // memberId
    private String type; // MSG or RTC
    private String data; // roomId
    private Object candidate; // 상태
    private Object sdp; // sdp 정보
}
