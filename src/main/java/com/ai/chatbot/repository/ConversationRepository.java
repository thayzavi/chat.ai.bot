package com.ai.chatbot.repository;

import com.ai.chatbot.model.Conversation;
import com.ai.chatbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserOrderByCreatedAtDesc(User user);
}
