package com.example.chat.chat_service.domain.room;

import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("V")
public class VideoRoom extends ChatRoom {
}
