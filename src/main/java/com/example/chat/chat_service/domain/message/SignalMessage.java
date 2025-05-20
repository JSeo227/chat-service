package com.example.chat.chat_service.domain.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {

    private Long roomId;

    private Long senderId; // member id

    private String senderName; //member name

    private Object candidate; // 상태

    private Object sdp; // sdp 정보

    private String type; // OFFER, ANSWER, ICE, ENTER, LEAVE

}
