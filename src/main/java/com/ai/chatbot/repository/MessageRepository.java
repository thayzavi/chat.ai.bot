package com.ai.chatbot.repository;

import com.ai.chatbot.model.Conversation;
import com.ai.chatbot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByTimestampAsc(Conversation conversation);
}
