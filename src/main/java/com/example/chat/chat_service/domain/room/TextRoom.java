package com.example.chat.chat_service.domain.room;

import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("T")
public class TextRoom extends ChatRoom {
}
