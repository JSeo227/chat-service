package com.example.chat.chat_service.domain.room;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("V")
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoRoom extends Room {
}
