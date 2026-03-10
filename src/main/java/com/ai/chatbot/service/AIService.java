package com.ai.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final WebClient webClient;

    @Value("${huggingface.api-key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    @Value("${huggingface.url}")
    private String url;

    public AIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateResponse(String userMessage, String language) {

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "Você é um assistente virtual de atendimento ao cliente, educado, " +
                                        "claro e objetivo. Responda sempre no idioma." + language),
                        Map.of("role", "user", "content", userMessage)
                ),
                "max_tokens", 100
        );

        try {
            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

            return "Não consegui gerar uma resposta no momento.";

        } catch (Exception e) {
            System.err.println("Erro na API HuggingFace: " + e.getMessage());
            return "Erro ao conectar com a API da HuggingFace.";
        }
    }
}