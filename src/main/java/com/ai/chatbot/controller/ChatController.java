package com.ai.chatbot.controller;

import com.ai.chatbot.dto.ConversationDTO;
import com.ai.chatbot.dto.MessageDTO;
import com.ai.chatbot.dto.MessageRequestDTO;
import com.ai.chatbot.dto.MessageResponseDTO;
import com.ai.chatbot.model.Conversation;
import com.ai.chatbot.model.Message;
import com.ai.chatbot.model.User;
import com.ai.chatbot.service.AIService;
import com.ai.chatbot.service.ChatService;
import com.ai.chatbot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Chat operation")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    private final AIService aiService;
    private final UserService userService;

    @PostMapping("/message")
    @Operation(summary = "Send message")
    public ResponseEntity<MessageResponseDTO> sendMessage(
        @Valid @RequestBody MessageRequestDTO requestDTO) {

    User user = userService.getCurrentUser();

    Conversation conversation = chatService.getOrCreateConversation(
            user,
            requestDTO.getConversationId(),
            requestDTO.getLanguage()
    );

    chatService.saveMessage(
            conversation,
            requestDTO.getMessage(),
            "USER"
    );

    if (conversation.getTitle().equals("Nova Conversa")) {
        String title = aiService.generateTitle(
                requestDTO.getMessage(),
                requestDTO.getLanguage()
        );

        chatService.updateConversationTitle(conversation, title);
    }

    String aiResponse = aiService.generateResponse(
            requestDTO.getMessage(),
            requestDTO.getLanguage() !=null ? requestDTO.getLanguage() : "pt-BR"
    );

    Message botMessage = chatService.saveMessage(
            conversation,
            aiResponse,
            "BOT"
    );

    MessageResponseDTO responseDTO = new MessageResponseDTO(
            botMessage.getContent(),
            conversation.getId(),
            botMessage.getTimestamp()
    );

    return ResponseEntity.ok(responseDTO);
}

    @GetMapping("/conversations")
    @Operation(summary = "Get user conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(){

        User user = userService.getCurrentUser();

        List<ConversationDTO> conversations =
                chatService.getUserConversations(user);

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/{id}/messages")
        @Operation(summary = "Get conversation messages")
        public ResponseEntity<List<MessageDTO>> getConversationMessages(
                @PathVariable Long id) {

        User user = userService.getCurrentUser();

        List<MessageDTO> messages =
                chatService.getConversationMessages(id, user);

        return ResponseEntity.ok(messages);
        }

}
