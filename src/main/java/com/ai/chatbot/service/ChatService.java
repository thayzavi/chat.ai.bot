package com.ai.chatbot.service;

import com.ai.chatbot.dto.ConversationDTO;
import com.ai.chatbot.dto.MessageDTO;
import com.ai.chatbot.model.Conversation;
import com.ai.chatbot.model.Message;
import com.ai.chatbot.model.User;
import com.ai.chatbot.repository.ConversationRepository;
import com.ai.chatbot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public Conversation getOrCreateConversation(User user, Long conversationId, String language) {

        if (conversationId != null){
            return  conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
        }
        Conversation conversation = new Conversation();
        conversation.setUser(user);
        conversation.setTitle("Nova Conversa");
        conversation.setLanguage(language);

        return conversationRepository.save(conversation);
    }

    public Message saveMessage(Conversation conversation, String content, String sender){

        Message message = new Message();
        message.setConversation(conversation);
        message.setContent(content);
        message.setSender(sender);

        return messageRepository.save(message);
    }

    public List<ConversationDTO> getUserConversations(User user) {

        return conversationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(conversation -> new ConversationDTO(
                        conversation.getId(),
                        conversation.getTitle(),
                        conversation.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public  List<MessageDTO> getConversationMessages(Long conversationId) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        return messageRepository.findByConversationOrderByTimestampAsc(conversation)
                .stream()
                .map(message -> new MessageDTO(
                        message.getContent(),
                        message.getSender(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());
    }
}
