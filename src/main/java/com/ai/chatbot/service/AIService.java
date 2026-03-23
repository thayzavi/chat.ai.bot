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
                "Você é um assistente especialista em desenvolvimento de software com foco prático e didático. " +

                "Seu objetivo é ajudar desenvolvedores a resolver problemas reais com clareza e precisão. " +

                "Siga rigorosamente estas regras:\n" +
                "1. Sempre responda no idioma: " + language + ".\n" +
                "2. Explique conceitos de forma simples, como para um desenvolvedor iniciante/intermediário.\n" +
                "3. Sempre que possível, forneça exemplos de código funcionais.\n" +
                "4. Organize respostas complexas em etapas numeradas.\n" +
                "5. Use boas práticas e padrões de mercado (Clean Code, REST, etc.).\n" +
                "6. Se houver múltiplas soluções, apresente a melhor e explique o porquê.\n" +
                "7. Evite respostas genéricas — seja direto e específico.\n" +
                "8. Quando o problema envolver erro, sugira possíveis causas e soluções.\n" +

                "Tecnologias de foco:\n" +
                "- Java, Spring Boot\n" +
                "- JavaScript / React\n" +
                "- APIs REST\n" +
                "- Bancos de dados (SQL e NoSQL)\n" +
                "- Arquitetura de software\n"
            ),
            Map.of("role", "user", "content", userMessage)
        ),
        "max_tokens", 200,
        "temperature", 0.3
    );

        return callAI(body);
    }

    public String generateTitle(String userMessage, String language) {

        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content",
                    "Gere um título curto (máximo 3 palavras) para uma conversa de programação.\n" +
                    "Regras:\n" +
                    "1. Seja específico e descritivo\n" +
                    "2. Use termos técnicos quando possível\n" +
                    "3. Evite palavras genéricas como 'ajuda' ou 'dúvida'\n" +
                    "4. Responda em PT-BR\n" +
                    "5. Retorne apenas o título, sem explicações"
                ),
                Map.of("role", "user", "content", userMessage)
            ),
            "max_tokens", 10,
            "temperature", 0.2
        );

        String title = callAI(body);


        if (title == null || title.isBlank()) {
            return "Nova Conversa";
        }

        return title.replace("\"", "").trim();
    }

    private String callAI(Map<String, Object> body) {

        try {
            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) response.get("choices");

                if (!choices.isEmpty()) {
                    Map<String, Object> message =
                            (Map<String, Object>) choices.get(0).get("message");

                    return (String) message.get("content");
                }
            }

            return "Não consegui gerar uma resposta.";

        } catch (Exception e) {
            System.err.println("Erro na API: " + e.getMessage());
            return "Erro ao conectar com a IA.";
        }

    }
}