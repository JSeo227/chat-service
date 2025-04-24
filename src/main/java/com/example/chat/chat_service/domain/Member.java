package com.example.chat.chat_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

//@Entity
@Data
public class Member {

//    @Id @GeneratedValue
//    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

}
