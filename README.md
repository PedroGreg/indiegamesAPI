# Indie Games API — API de gerenciamento de games indie

API REST desenvolvida com Spring Boot para administrar um catálogo de games indie: desenvolvedores, categorias, plataformas, games, usuários e perfis. O projeto oferece cadastro, consulta, atualização e remoção, com paginação, HATEOAS, versionamento, login por chave de API, idempotência, limite de requisições e documentação interativa com Swagger.

**Autor:** _seu nome_  
**Website:** _link do seu portfólio_  
**Contato:** _seu e-mail_

## Sumário

- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Como executar](#como-executar)
- [Documentação Swagger](#documentação-swagger)
- [Autenticação e headers](#autenticação-e-headers)
- [Versionamento](#versionamento)
- [Endpoints](#endpoints)
- [Dados e validações](#dados-e-validações)
- [Códigos HTTP](#códigos-http)
- [Licença](#licença)

## Funcionalidades

- Cadastro, consulta, atualização e remoção de desenvolvedores, categorias, plataformas, games, usuários e perfis de usuário.
- Busca por nome (desenvolvedores, categorias, plataformas, games e usuários) e pela bio (perfis).
- Consultas personalizadas de games por status, desenvolvedor, categoria e plataforma.
- Paginação e ordenação em todas as listagens (`page`, `size` e `sort`).
- HATEOAS: cada resposta traz `_links` para navegar entre os recursos, e as listagens trazem links de paginação.
- Versionamento pela URL (`/v1` e `/v2`). A v2 traz a quantidade de games por categoria e o total de conquistas de cada game.
- Login com e-mail e senha que gera uma chave de API (`X-API-Key`), exigida em todos os PUT e DELETE.
- Senhas armazenadas como hash BCrypt.
- Idempotência nos cadastros com o header `X-Idempotency-Key`.
- Limite de 20 requisições por minuto por IP (rate limiting), com o header `Retry-After`.
- CORS liberado apenas para origens configuradas.
- Validação de campos com Bean Validation, nos DTOs e nas entidades.
- Tratamento global de erros, com respostas em um formato único e mensagens em português.
- Dados de exemplo criados automaticamente ao iniciar, incluindo um usuário de teste.
- Documentação com OpenAPI e Swagger UI, guia de uso e termos de serviço.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Validation
- Spring HATEOAS
- Spring Security Crypto (BCrypt)
- Springdoc OpenAPI 3.2.0 / Swagger UI
- Bucket4j 8.10.1 (rate limiting)
- H2 (banco em memória)
- Maven

## Requisitos

- JDK 21 ou superior
- Opcional: Docker, para executar em contêiner

## Como executar

### Desenvolvimento com H2

Na raiz do projeto, execute:

**Windows (PowerShell ou Prompt de Comando):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux ou macOS:**

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080` com um banco H2 em memória. Os dados são temporários e são apagados quando a aplicação é encerrada. A cada inicialização, a API cria dados de exemplo e o usuário de teste:

- **E-mail:** `admin@indiegames.com`
- **Senha:** `admin123`

Para iniciar com o banco vazio, altere `app.seed.enabled=false` em `src/main/resources/application.properties`.

O console H2 fica disponível em [http://localhost:8080/h2-console](http://localhost:8080/h2-console), com a JDBC URL `jdbc:h2:mem:indiegames`, o usuário `sa` e a senha em branco.

### Empacotar e executar

```bash
./mvnw clean package
java -jar target/IndieGamesApi-0.0.1-SNAPSHOT.jar
```

No Windows, use `mvnw.cmd` no lugar de `./mvnw`.

### Docker

```bash
docker build -t indie-games-api .
docker run -p 8080:8080 indie-games-api
```

A porta pode ser trocada pela variável de ambiente `PORT`.

### Origens permitidas (CORS)

Os front-ends que podem chamar a API pelo navegador ficam em `app.cors.allowed-origins`, no `application.properties`, separados por vírgula. Por padrão: `http://localhost:3000`, `http://localhost:5173` e `http://localhost:8080`.

## Documentação Swagger

Com a aplicação em execução, acesse:

- Swagger com o tema do projeto: [http://localhost:8080/swagger.html](http://localhost:8080/swagger.html)
- Swagger UI padrão: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Especificação OpenAPI em JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- Guia de uso (exemplos e lista de erros): [http://localhost:8080/guia.html](http://localhost:8080/guia.html)
- Termos de Serviço: [http://localhost:8080/termos.html](http://localhost:8080/termos.html)
- Página inicial (painel): [http://localhost:8080/](http://localhost:8080/)

No Swagger, expanda um grupo, clique em **Try it out**, preencha os parâmetros, os headers e o corpo JSON quando necessário e clique em **Execute**. Os IDs dos registros são números inteiros.

> Use sempre **dados fictícios** nos testes (ex.: nome `Jogador Pixel` e e-mail `jogador.pixel@exemplo.com`). Não cadastre nome, e-mail ou senha reais.

## Autenticação e headers

| Header | Quando enviar | Se faltar |
|---|---|---|
| `X-Idempotency-Key` | Em todo **POST** de cadastro | `400 Bad Request` |
| `X-API-Key` | Em todo **PUT** e **DELETE**, e no logout | `401 Unauthorized` |
| `Content-Type: application/json` | Em todo POST e PUT com corpo | `415 Unsupported Media Type` |

**Login:** envie e-mail e senha para `POST /api/auth/login`. A resposta traz uma `apiKey`, que deve ir no header `X-API-Key`. Cada usuário tem uma chave por vez: um novo login substitui a anterior, e `POST /api/auth/logout` invalida a chave atual.

**Idempotência:** envie um valor único (por exemplo, um UUID) em `X-Idempotency-Key` a cada cadastro. Se o mesmo POST for reenviado com a mesma chave, a API não cria outro registro: devolve o já criado, com `200 OK`. Use uma chave nova para cada cadastro novo.

### Exemplo: login

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@indiegames.com",
  "password": "admin123"
}
```

## Versionamento

A versão faz parte da URL:

- **v1** (`/api/{recurso}/v1`): CRUD completo de todos os recursos.
- **v2** (`/api/category/v2` e `/api/game/v2`):
  - Categorias v2 trazem o campo `gamesCount` (quantidade de games da categoria). Só consulta.
  - Games v2 trazem um formato resumido (só os nomes do desenvolvedor, da categoria e das plataformas) e o campo `totalAchievements` (total de conquistas do jogo). Permitem consulta, cadastro e atualização.

O campo `totalAchievements` não aparece na v1, e um game criado pela v1 começa com 0 conquistas.

## Endpoints

A base local é `http://localhost:8080`. Nas listagens, use `?page=0&size=10&sort=name,asc` para paginar e ordenar.

| Recurso | Método | Rota | Descrição | Header |
|---|---|---|---|---|
| Autenticação | POST | `/api/auth/login` | Faz login e devolve a chave de API | — |
| Autenticação | POST | `/api/auth/logout` | Invalida a chave de API atual | `X-API-Key` |
| Categorias | GET | `/api/category/v1` | Lista as categorias | — |
| Categorias | GET | `/api/category/v1/{id}` | Consulta uma categoria pelo ID | — |
| Categorias | GET | `/api/category/v1/search?name=` | Busca categorias pelo nome | — |
| Categorias | POST | `/api/category/v1` | Cadastra uma categoria | `X-Idempotency-Key` |
| Categorias | PUT | `/api/category/v1/{id}` | Atualiza uma categoria | `X-API-Key` |
| Categorias | DELETE | `/api/category/v1/{id}` | Remove uma categoria | `X-API-Key` |
| Desenvolvedores | GET | `/api/developer/v1` | Lista os desenvolvedores | — |
| Desenvolvedores | GET | `/api/developer/v1/{id}` | Consulta um desenvolvedor pelo ID | — |
| Desenvolvedores | GET | `/api/developer/v1/search?name=` | Busca desenvolvedores pelo nome | — |
| Desenvolvedores | POST | `/api/developer/v1` | Cadastra um desenvolvedor | `X-Idempotency-Key` |
| Desenvolvedores | PUT | `/api/developer/v1/{id}` | Atualiza um desenvolvedor | `X-API-Key` |
| Desenvolvedores | DELETE | `/api/developer/v1/{id}` | Remove um desenvolvedor | `X-API-Key` |
| Plataformas | GET | `/api/platform/v1` | Lista as plataformas | — |
| Plataformas | GET | `/api/platform/v1/{id}` | Consulta uma plataforma pelo ID | — |
| Plataformas | GET | `/api/platform/v1/search?name=` | Busca plataformas pelo nome | — |
| Plataformas | POST | `/api/platform/v1` | Cadastra uma plataforma | `X-Idempotency-Key` |
| Plataformas | PUT | `/api/platform/v1/{id}` | Atualiza uma plataforma | `X-API-Key` |
| Plataformas | DELETE | `/api/platform/v1/{id}` | Remove uma plataforma | `X-API-Key` |
| Games | GET | `/api/game/v1` | Lista os games | — |
| Games | GET | `/api/game/v1/{id}` | Consulta um game pelo ID | — |
| Games | GET | `/api/game/v1/search?name=` | Busca games pelo nome | — |
| Games | GET | `/api/game/v1/status/{status}` | Lista games por status | — |
| Games | GET | `/api/game/v1/developer/{developerId}` | Lista os games de um desenvolvedor | — |
| Games | GET | `/api/game/v1/category/{categoryId}` | Lista os games de uma categoria | — |
| Games | GET | `/api/game/v1/platform/{platformId}` | Lista os games de uma plataforma | — |
| Games | POST | `/api/game/v1` | Cadastra um game | `X-Idempotency-Key` |
| Games | PUT | `/api/game/v1/{id}` | Atualiza um game | `X-API-Key` |
| Games | DELETE | `/api/game/v1/{id}` | Remove um game | `X-API-Key` |
| Usuários | GET | `/api/user/v1` | Lista os usuários | — |
| Usuários | GET | `/api/user/v1/{id}` | Consulta um usuário pelo ID | — |
| Usuários | GET | `/api/user/v1/search?name=` | Busca usuários pelo nome | — |
| Usuários | POST | `/api/user/v1` | Cadastra um usuário (e cria o perfil) | `X-Idempotency-Key` |
| Usuários | PUT | `/api/user/v1/{id}` | Atualiza um usuário | `X-API-Key` |
| Usuários | DELETE | `/api/user/v1/{id}` | Remove um usuário (e o perfil) | `X-API-Key` |
| Perfis | GET | `/api/userprofile/v1` | Lista os perfis | — |
| Perfis | GET | `/api/userprofile/v1/{id}` | Consulta um perfil pelo ID | — |
| Perfis | GET | `/api/userprofile/v1/search?bio=` | Busca perfis pela bio | — |
| Perfis | POST | `/api/userprofile/v1` | Cadastra um perfil avulso | `X-Idempotency-Key` |
| Perfis | PUT | `/api/userprofile/v1/{id}` | Atualiza um perfil | `X-API-Key` |
| Perfis | DELETE | `/api/userprofile/v1/{id}` | Remove um perfil | `X-API-Key` |
| Categorias v2 | GET | `/api/category/v2` | Lista as categorias com `gamesCount` | — |
| Categorias v2 | GET | `/api/category/v2/{id}` | Consulta uma categoria com `gamesCount` | — |
| Games v2 | GET | `/api/game/v2` | Lista os games no formato v2 | — |
| Games v2 | GET | `/api/game/v2/{id}` | Consulta um game no formato v2 | — |
| Games v2 | POST | `/api/game/v2` | Cadastra um game com `totalAchievements` | `X-Idempotency-Key` |
| Games v2 | PUT | `/api/game/v2/{id}` | Atualiza um game com `totalAchievements` | `X-API-Key` |

### Exemplo: cadastrar uma categoria

```http
POST /api/category/v1
Content-Type: application/json
X-Idempotency-Key: 7c9e6679-7425-40de-944b-e07fc1f90ae7
```

```json
{
  "name": "Puzzle"
}
```

### Exemplo: atualizar uma categoria

```http
PUT /api/category/v1/1
Content-Type: application/json
X-API-Key: SUA_CHAVE
```

```json
{
  "name": "RPG de Ação"
}
```

### Exemplo: cadastrar um game com conquistas (v2)

```http
POST /api/game/v2
Content-Type: application/json
X-Idempotency-Key: 1b4e28ba-2fa1-41d2-883f-0016d3cca427
```

```json
{
  "name": "Celeste",
  "status": "RELEASED",
  "developerId": 1,
  "categoryId": 1,
  "platformIds": [1, 2],
  "totalAchievements": 32
}
```

## Dados e validações

Os corpos das requisições devem ser enviados em JSON. O identificador é um número gerado pela aplicação; em consultas, atualizações e remoções, informe o ID na rota.

- **Categoria:** `name` obrigatório, de 3 a 255 caracteres.
- **Desenvolvedor:** `name` obrigatório, de 2 a 255 caracteres.
- **Plataforma:** `name` obrigatório, de 2 a 50 caracteres.
- **Game (v1):** `name` obrigatório (até 255 caracteres), `status` (`DEVELOPMENT`, `EARLY_ACCESS` ou `RELEASED`), `developerId`, `categoryId` e `platformIds` (lista de IDs, pode ser vazia). Os IDs informados precisam existir.
- **Game (v2):** os mesmos campos da v1, mais `totalAchievements` obrigatório, de 0 a 10000.
- **Usuário:** `name` obrigatório (3 a 255 caracteres), `email` obrigatório, válido e único, `password` de 6 a 255 caracteres (obrigatória no cadastro e opcional no PUT, quando a senha atual é mantida) e `bio` opcional (até 255 caracteres). O perfil do usuário é criado junto no cadastro, e a senha nunca é devolvida nas respostas.
- **Perfil:** `bio` obrigatória (pode ser vazia), até 255 caracteres.

Campos obrigatórios e regras de formato são validados pela API. Todos os erros seguem o mesmo formato JSON:

```json
{
  "timestamp": "2026-11-12T17:45:00.000+00:00",
  "message": "name: O name da categoria deve ser preenchido",
  "details": "uri=/api/category/v1"
}
```

## Códigos HTTP

- `200 OK`: consulta e atualização bem-sucedidas, login e reenvio de um POST com a mesma `X-Idempotency-Key`.
- `201 Created`: cadastro criado com sucesso.
- `204 No Content`: remoção ou logout executado com sucesso.
- `400 Bad Request`: dados inválidos, JSON mal formatado, e-mail já cadastrado, ID não numérico, parâmetro de busca ausente ou header `X-Idempotency-Key` ausente.
- `401 Unauthorized`: PUT ou DELETE sem `X-API-Key` válida, ou login com e-mail ou senha incorretos.
- `404 Not Found`: registro ou rota inexistente, inclusive `developerId`, `categoryId` ou `platformIds` de um game.
- `405 Method Not Allowed`: método não disponível na rota (ex.: `DELETE /api/category/v2/{id}`).
- `409 Conflict`: remoção de um registro em uso (ex.: categoria, desenvolvedor ou plataforma que ainda têm games, ou perfil vinculado a um usuário).
- `415 Unsupported Media Type`: corpo enviado sem `Content-Type: application/json`.
- `429 Too Many Requests`: mais de 20 requisições por minuto do mesmo IP; o header `Retry-After` informa quantos segundos esperar.
- `500 Internal Server Error`: erro inesperado no servidor.

**Observações:**
- Não há remoção em cascata: para apagar uma categoria, um desenvolvedor ou uma plataforma, remova ou altere antes os games ligados a eles. Apagar um usuário remove o perfil dele junto.
- No PUT, o corpo é validado antes da chave: um PUT com corpo inválido recebe `400`, mesmo sem `X-API-Key`.
- As chaves de login e de idempotência ficam em memória e são apagadas quando a aplicação reinicia.

## Licença

Distribuído sob a licença MIT. Consulte os termos em [mit-license.org](https://mit-license.org/).
