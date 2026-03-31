# MetroAcesso

Sistema de identificação automática e notificação de usuários PcD no metrô, desenvolvido como Projeto Integrador do curso Técnico em Desenvolvimento de Sistemas.

---

## Sobre o projeto

Atualmente, o suporte ao usuário PcD no metrô depende de abordagem presencial e comunicação manual entre agentes de diferentes estações — um modelo reativo, sujeito a falhas e com pouca previsibilidade para o usuário.

O **MetroAcesso** propõe automatizar esse processo: quando um usuário PcD entra na estação, o sistema identifica sua tag automaticamente e notifica os agentes de atendimento disponíveis, permitindo um atendimento mais ágil, coordenado e proativo — sem depender da iniciativa do usuário.

---

## Funcionalidades (MVP)

- Cadastro de usuários PcD com informações de perfil e tipo de deficiência
- Identificação automática do PcD na entrada da estação via tag
- Notificação automática de agentes disponíveis (apenas para usuários que desejam suporte)
- Confirmação de atendimento pelos agentes
- Gestão de administradores, agentes, estações, sensores e tags
- Fluxo de solicitação e aprovação de cadastro via formulário com comprovação de deficiência

---

## Tecnologias

- **Java 21** com **Spring Boot**
- **Spring Security** com autenticação via **JWT**
- **Spring Data JPA** com **Hibernate**
- **Bean Validation** (`jakarta.validation`)
- **Lombok**
- **Springdoc OpenAPI** (Swagger UI)
- **PostgreSQL**

---

## Equipe

| Nome | Função |
|---|---|
| Brayan Laines | Scrum Master / Backend |
| Gabriel Mazzieri | Backend |
| Kauanne Oliveira | Mobile |
| Ruan Macionil | Frontend |
| João Vitor | IoT |
| Prof. Rafael Costa | Product Owner |

---

## Estrutura do projeto

```
src/
├── application/
│   ├── dto/          # Objetos de transferência de dados
│   └── service/      # Regras de negócio
├── domain/
│   ├── entity/       # Entidades JPA
│   ├── enums/        # Enumerações de domínio
│   ├── exception/    # Exceções de domínio
│   └── repository/   # Interfaces de repositório
├── infrastructure/
│   ├── config/       # Configurações gerais
│   ├── mqtt/         # Comunicação MQTT
│   └── security/     # Autenticação e autorização JWT
└── interface_ui/
    ├── controller/   # Controllers REST
    └── exception/    # Tratamento global de exceções
```

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven
- PostgreSQL

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-org/metro-acesso-backend.git
cd metro-acesso-backend
```

2. Configure as variáveis de ambiente ou o arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/metroacesso
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=sua_chave_secreta
```

3. Execute a aplicação:
```bash
./mvnw spring:boot run
```

4. Acesse a documentação dos endpoints (não concluído): 
```
http://localhost:8080/swagger-ui/index.html
```
---

## Autenticação

A API utiliza autenticação via JWT. Para acessar endpoints protegidos:

1. Faça login em `POST /auth/login` com e-mail e senha
2. Copie o token retornado
3. Inclua no header das requisições: `Authorization: Bearer <token>`

### Roles disponíveis

| Role | Descrição |
|---|---|
| `ADMINISTRADOR` | Acesso total ao sistema |
| `AGENTE_ATENDIMENTO` | Acesso operacional (leitura e atualização) |
| `USUARIO_PCD` | Acesso restrito aos próprios dados |

---

## Endpoints principais

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/auth/login` | Autenticação | Público |
| `POST` | `/api/formulario` | Envio de formulário de cadastro | Público |
| `POST` | `/api/formulario/validar/{email}` | Aprovar ou reprovar formulário | Administrador |
| `GET` | `/api/formulario/pendentes` | Listar formulários pendentes | Administrador |
| `GET` | `/api/pcd/{email}` | Buscar usuário PcD | Administrador, Agente, próprio PcD |
| `GET` | `/api/agente/{email}` | Buscar agente de atendimento | Administrador, próprio agente |
| `GET` | `/api/estacao` | Listar estações | Administrador |

Documentação completa disponível via Swagger UI após iniciar a aplicação.

---

## Observações

- Criptografia de senhas prevista para versão futura
- Armazenamento de arquivos de comprovação em storage externo previsto para versão futura
- A comunicação automática entre estações (embarque → desembarque) está fora do escopo do MVP

---

## Período

Planejamento: **2025/2** — Desenvolvimento: **2026/1**
