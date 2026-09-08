# AutoEscola 3ESR — API ReST (Checkpoint 4)

API ReST desenvolvida com **Spring Boot** para agendamento e gestão de instruções de uma auto-escola.
Disciplina: **SOA e WebServices** — FIAP.

## Integrantes do grupo

| Nome | RM |
|------|----|
| Alexia Ramalho | 558385 |
| Enzo Real | 557943 |
| Gustavo Pasquini | 555454 |
| Hellen Silva | 559008 |
| Lorenzo Acquesta | 557397 |

## Stack

- Java 25 / Spring Boot 4
- Spring Web, Spring Data JPA, Spring Security (JWT via `com.auth0:java-jwt`)
- Bean Validation, Flyway, MySQL, Lombok
- `spring-dotenv` (segredos fora do código)
- Testes: JUnit 5 + Mockito

## Configuração (variáveis de ambiente)

Nenhum segredo fica no código. A aplicação lê estas variáveis do ambiente ou de
um arquivo `.env` na raiz (carregado automaticamente por `spring-dotenv`):

| Variável | Descrição | Default (dev) |
|----------|-----------|---------------|
| `DB_URL` | URL JDBC do MySQL | `jdbc:mysql://localhost:3306/autoescola3esr` |
| `DB_USERNAME` | usuário do banco | `root` |
| `DB_PASSWORD` | senha do banco | `fiap` |
| `JWT_SECRET` | segredo HMAC-256 do token JWT (use ≥ 32 chars aleatórios) | valor de dev |
| `SERVER_PORT` | porta HTTP | `8081` |

```bash
cp .env.example .env      # e ajuste os valores
```

O `.env` está no `.gitignore` — **não** é versionado. Em produção, defina as
variáveis direto no ambiente.

## Como executar

1. Suba um MySQL local e crie a base:
   ```sql
   create database autoescola3esr;
   ```
2. Copie `.env.example` para `.env` e ajuste (ou exporte as variáveis).
3. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   A API sobe em `http://localhost:8081`. O Flyway cria/atualiza o schema
   automaticamente (migrations `V1` … `V11`).

## Testes

```bash
./mvnw test
```

Cobrem as regras de negócio do agendamento e do cancelamento (validadores
isolados, com JUnit 5 + Mockito): horário de funcionamento, hora inteira,
antecedência de 30 min, limite de 2 instruções/dia, conflito de horário do
instrutor, aluno inativo e antecedência de 24h para cancelamento.

### Usuário administrador inicial

A migration `V9` cadastra um administrador para bootstrap:

| login | senha | perfil |
|-------|-------|--------|
| `admin@autoescola3esr.com.br` | `123456` | `ADMIN` |

## Autenticação

Todas as rotas (exceto `POST /login`) exigem o header:

```
Authorization: Bearer <token-jwt>
```

**Obter token:**

```http
POST /login
Content-Type: application/json

{ "login": "admin@autoescola3esr.com.br", "senha": "123456" }
```

## Endpoints

### Autenticação
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/login` | público | Retorna o token JWT |

### Usuários — `/usuarios`
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/usuarios` | **ADMIN** | Cadastra usuário (senha gravada com hash BCrypt) |
| GET | `/usuarios` | **ADMIN** | Lista usuários (paginado, 10/pág, ordem por login) |
| PUT | `/usuarios` | **ADMIN** | Atualiza o perfil (`USER` / `ADMIN`) de um usuário |
| DELETE | `/usuarios/{id}` | **ADMIN** | Exclui um usuário |
| PUT | `/usuarios/senha` | autenticado | Usuário altera a **própria** senha (confirma a senha atual) |

```jsonc
// POST /usuarios
{ "login": "joao@fiap.com.br", "senha": "1234", "perfil": "USER" }

// PUT /usuarios/senha
{ "senha_atual": "123456", "nova_senha": "novaSenha123" }
```

### Instrutores — `/instrutores`
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/instrutores` | **ADMIN** | Cadastra instrutor (telefone é opcional — informado depois via PUT) |
| GET | `/instrutores` | ADMIN/USER | Lista ativos: nome, e-mail, CNH, especialidade (paginado, 10/pág, ordem por nome) |
| GET | `/instrutores/{id}` | **ADMIN** | Detalha instrutor (o `telefone` só aparece depois de informado via PUT) |
| PUT | `/instrutores` | **ADMIN** | Atualiza **nome, telefone e endereço** (e-mail, CNH e especialidade são imutáveis) |
| DELETE | `/instrutores/{id}` | **ADMIN** | Exclusão lógica (marca como inativo) |

### Alunos — `/alunos`
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/alunos` | **ADMIN** | Cadastra aluno |
| GET | `/alunos` | ADMIN/USER | Lista ativos (paginado, 10/pág, ordem por nome) |
| GET | `/alunos/{id}` | **ADMIN** | Detalha aluno |
| PUT | `/alunos` | **ADMIN** | Atualiza **nome, telefone e endereço** (e-mail e CPF são imutáveis) |
| DELETE | `/alunos/{id}` | **ADMIN** | Exclusão lógica (marca como inativo) |

```jsonc
// POST /alunos
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "telefone": "11999990000",
  "cpf": "12345678901",
  "endereco": {
    "logradouro": "Rua A", "bairro": "Centro", "cidade": "São Paulo",
    "uf": "SP", "cep": "01001-000"
  }
}
```

### Instruções — `/instrucoes`
| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/instrucoes` | autenticado | Agenda uma instrução |
| DELETE | `/instrucoes` | autenticado | Cancela uma instrução |

```jsonc
// POST /instrucoes
// instrutor é opcional; se omitido, o sistema sorteia um instrutor disponível.
// especialidade também é opcional — quando informada, restringe o sorteio.
{ "id_aluno": 1, "id_instrutor": 2, "data_hora": "20/09/2026 - 09:00" }

// POST /instrucoes  (sem instrutor: sorteio; especialidade opcional)
{ "id_aluno": 1, "especialidade": "CARROS", "data_hora": "20/09/2026 - 09:00" }

// DELETE /instrucoes
{ "id_instrucao": 5, "motivo": "ALUNO_DESISTIU" }
```

## Regras de negócio

### Agendamento de instrução
- Funcionamento de segunda a sábado, das 06:00 às 21:00;
- Duração fixa de 1 hora (agendamento em hora cheia);
- Antecedência mínima de 30 minutos;
- Aluno e instrutor precisam estar ativos;
- Máximo de **2 instruções por dia** para o mesmo aluno;
- Um instrutor não pode ter duas instruções na mesma data/hora;
- Instrutor opcional — se omitido, o sistema sorteia aleatoriamente um instrutor
  disponível na data/hora; se a especialidade for informada, o sorteio fica
  restrito a instrutores dela.

### Cancelamento de instrução
- É obrigatório informar o motivo: `ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU` ou `OUTROS`;
- Só é permitido cancelar com antecedência mínima de **24 horas**;
- O cancelamento não apaga o registro — grava o motivo e a data do cancelamento,
  liberando o horário do instrutor.

### Usuários
- A senha nunca é armazenada em texto puro — usa hash **BCrypt**;
- Apenas administradores gerenciam usuários;
- Qualquer usuário autenticado pode trocar a própria senha.

## Utilitário

`br.com.fiap3esr.autoescola3esr.util.GerarSenhaHash` (em `src/test`, fora do
artefato de produção) gera um hash BCrypt no console — útil para criar seeds de
usuários em migrations. Rode a classe pela IDE (tem `main`), passando a senha
como argumento (sem argumento, usa `123456`).
