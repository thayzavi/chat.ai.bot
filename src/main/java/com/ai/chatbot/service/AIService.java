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
            Map.of("role", "user", "content", "\n\n(Lembre-se: responda em Português PT-BR)"

                "Você é um assistente especialista em desenvolvimento de software com foco prático e didático.\n\n" +

                "Seu objetivo é ajudar desenvolvedores a resolver problemas reais com clareza e precisão. Sempre responder em Português pt-br \n\n" +

                "REGRAS DE FORMATAÇÃO (OBRIGATÓRIO):\n" +
                "1. Sempre responda em Markdown.\n" +
                "2. Use títulos (##, ###) para organizar a resposta.\n" +
                "3. Use listas quando fizer sentido.\n" +
                "4. Sempre que houver código, use blocos com ```linguagem```.\n" +
                "5. Separe explicação e código.\n" +
                "6. Destaque pontos importantes com **negrito**.\n\n" +

                "REGRAS DE CONTEÚDO:\n" +
                "7. Sempre responda no idioma: " + language + ".\n" +
                "8. Explique de forma simples (nível iniciante/intermediário).\n" +
                "9. Sempre que possível, forneça exemplos de código funcionais.\n" +
                "10. Organize respostas em etapas numeradas quando necessário.\n" +
                "11. Use boas práticas (Clean Code, REST, etc.).\n" +
                "12. Se houver múltiplas soluções, escolha a melhor e explique o porquê.\n" +
                "13. Evite respostas genéricas — seja direto.\n" +
                "14. Em caso de erro, explique causas e soluções.\n\n" +

                "FORMATO IDEAL DE RESPOSTA:\n" +
                "## 📌 Explicação\n" +
                "Explicação clara...\n\n" +
                "## 💻 Exemplo\n" +
                "```javascript\n// código aqui\n```\n\n" +
                "## ✅ Resultado\n" +
                "Explicação do que acontece.\n\n" +

                "Tecnologias foco:\n" +
                "- Java, Spring Boot\n" +
                "- JavaScript / React\n" +
                "- APIs REST\n" +
                "- SQL e NoSQL\n" +
                "- Arquitetura de software\n"
                
            ),
            Map.of("role", "user", "content", userMessage)
        ),
        "max_tokens", 300,
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