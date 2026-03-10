package com.ai.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequestDTO {
    @NotBlank(message = "A mensagem é obrigatória")
    private String message;

    private Long conversationId;

    private String language = "pt";
}
