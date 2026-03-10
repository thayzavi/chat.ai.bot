package com.ai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO {

    private String content;
    private String sender;
    private LocalDateTime timestamp;
}
