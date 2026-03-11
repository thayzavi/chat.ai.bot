# 🤖 AI Chatbot API (Spring Boot + JWT + HuggingFace)

API REST de um **chatbot com inteligência artificial**, desenvolvida com **Spring Boot**, que permite:

- Registro e autenticação de usuários
- Autenticação segura com **JWT**
- Criação de conversas
- Envio de mensagens
- Resposta automática gerada por **IA (HuggingFace)**
- Histórico de conversas e mensagens

O sistema foi desenvolvido seguindo boas práticas de **arquitetura em camadas**, utilizando **DTOs, Services, Repositories e Controllers**.

---

# 📌 Funcionalidades

✔ Cadastro de usuário  
✔ Login com geração de **JWT Token**  
✔ Autenticação segura via **Spring Security**  
✔ Criação automática de conversas  
✔ Envio de mensagens para a IA  
✔ Respostas geradas pela **HuggingFace API**  
✔ Histórico completo de conversas  
✔ Histórico de mensagens por conversa  
✔ Suporte a múltiplos idiomas  

---

# 🧱 Arquitetura do Projeto

O projeto segue uma estrutura organizada em camadas:

```
src/main/java/com/ai/chatbot

config
 ├── Config.java
 ├── JwtAuthenticationFilter.java
 ├── PasswordEncoderConfig.java
 └── WebClientConfig.java

controller
 ├── AuthController.java
 └── ChatController.java

dto
 ├── AuthResponseDTO.java
 ├── LoginRequestDTO.java
 ├── RegisterRequestDTO.java
 ├── MessageRequestDTO.java
 ├── MessageResponseDTO.java
 ├── MessageDTO.java
 └── ConversationDTO.java

model
 ├── User.java
 ├── Conversation.java
 └── Message.java

repository
 ├── UserRepository.java
 ├── ConversationRepository.java
 └── MessageRepository.java

service
 ├── AIService.java
 ├── ChatService.java
 ├── JwtService.java
 └── UserService.java
```


---

# 🧰 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security
- JWT (Json Web Token)
- Spring Data JPA
- Hibernate
- PostgreSQL / MySQL
- WebClient
- HuggingFace API
- Swagger OpenAPI
- Lombok

---

# 🔐 Autenticação

O sistema utiliza **JWT (JSON Web Token)** para autenticação.

Fluxo:

1️⃣ Usuário faz login  
2️⃣ API gera um **Token JWT**  
3️⃣ O token deve ser enviado nas requisições protegidas  

Header necessário:
Authorization: Bearer SEU_TOKEN


---


---

# ⚙️ Configuração do Projeto

## 1️⃣ Clonar o repositório

```bash
git clone https://github.com/seuusuario/chat.ai.bot.git

# 🤖 Integração com Inteligência Artificial

O chatbot utiliza a **API da HuggingFace** para gerar respostas automaticamente.

Fluxo de funcionamento:
Usuário → API → HuggingFace → Resposta gerada → Banco de dados → Usuário
```
2️⃣ Configurar application.properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/chatbot
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.security.jwt.secret=SEU_SECRET_BASE64

huggingface.api-key=SEU_TOKEN_HUGGINGFACE
huggingface.model=google/flan-t5-large
huggingface.url=https://api-inference.huggingface.co/models/
```
▶️ Executar o Projeto

Execute o projeto com:

```
mvn spring-boot:run

```

ou

```
./mvnw spring-boot:run

```

A aplicação iniciará em:

```
http://localhost:8080

```
📚 Documentação da API (Swagger)

Após iniciar o projeto, acesse:

http://localhost:8080/swagger-ui/index.html

🔒 Segurança

A segurança do sistema inclui:

- JWT Authentication
- Password Hashing com BCrypt
- Filtro personalizado JwtAuthenticationFilter
- Rotas protegidas com Spring Security

Rotas públicas:

```
/api/auth/**
/swagger-ui/**
/v3/api-docs/**

```
👩‍💻 Autor

Desenvolvido por Thay

Estudante de Análise e Desenvolvimento de Sistemas
Focada em Desenvolvimento Full Stack e Inteligência Artificial aplicada.

