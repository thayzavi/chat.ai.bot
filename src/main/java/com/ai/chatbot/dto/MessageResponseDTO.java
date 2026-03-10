package com.ai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponseDTO {
    private String response;
    private Long conversationId;
    private LocalDateTime timestamp;
}
