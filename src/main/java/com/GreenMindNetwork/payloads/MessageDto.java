package com.GreenMindNetwork.payloads;

import com.GreenMindNetwork.entities.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private int id;
    private String content;
    private int userId;
    private String receiverName;
    private UserDto user;
}
