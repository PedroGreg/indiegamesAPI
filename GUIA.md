# Indie Games API — mapa do projeto

Com a API rodando:
- **Página inicial:** http://localhost:8080/
- **Swagger com o tema do projeto:** http://localhost:8080/swagger.html (o padrão continua em `/swagger-ui/index.html`)
- **Guia de uso** (exemplos, erros e regras): http://localhost:8080/guia.html (o mesmo texto aparece no topo do Swagger)
- **Termos de Serviço:** http://localhost:8080/termos.html
- **Console do H2:** http://localhost:8080/h2-console (JDBC `jdbc:h2:mem:indiegames`, usuário `sa`, sem senha)

Usuário de teste: **admin@indiegames.com / admin123**

| Requisito | Onde olhar |
|---|---|
| One-to-One | `User.userProfile` ↔ `UserProfile.user` |
| One-to-Many | `Developer.games`, `Category.games` |
| Many-to-Many | `Game.platforms` |
| Enum | `GameStatus` |
| Bean Validation | entidades (`model/`) e `dto/request/` |
| Consultas personalizadas | `GameRepository`, `UserProfileRepository` (`@Query`) |
| Paginação + HATEOAS | controllers (`PagedResourcesAssembler`) e `assembler/` |
| Idempotência (`X-Idempotency-Key`) | `IdempotencyService` + método `create` dos controllers |
| Login (`X-API-Key` em PUT e DELETE) | `AuthController`, `AuthService` + métodos `update` e `delete` |
| Rate limiting | `config/RateLimitFilter` (20 req/min por IP, só em `/api/**`) |
| CORS | `config/CorsConfig` + `app.cors.allowed-origins` |
| Versionamento (URL) | `/api/{recurso}/v1` e `/api/category/v2`, `/api/game/v2` |
| Tratamento de erros | pasta `exception/` (comentada em detalhes) |
| Documentação | `config/OpenAPI`, `static/guia.md`, `static/swagger.html`, `static/swagger-custom.css` |
