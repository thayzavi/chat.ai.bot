package com.ai.chatbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false, length = 2000) 
    private String content;

    @Column(nullable = false)
    private String sender;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreated(){
        timestamp = LocalDateTime.now();
    }
}
