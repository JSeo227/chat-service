package com.example.chat.chat_service.domain.room;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("T")
@Data
@EqualsAndHashCode(callSuper=true)
public class TextRoom extends Room {

    public static Room createTextRoom(String name, String password, Integer max) {
        return createRoom(name, password, max, RoomType.TXT);
    }

}
