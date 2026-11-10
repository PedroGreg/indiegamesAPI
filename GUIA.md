# Guia rápido — Indie Games API

## Onde está cada requisito

| Requisito | Onde olhar |
|---|---|
| H2 | `application.properties` (console: `/h2-console`, JDBC `jdbc:h2:mem:indiegames`, usuário `sa`) |
| One-to-One | `User.userProfile` ↔ `UserProfile.user` |
| One-to-Many | `Developer.games` e `Category.games` (o outro lado é `Game.developer` / `Game.category`) |
| Many-to-Many | `Game.platforms` |
| Enum | `GameStatus` |
| Bean Validation | nas entidades (`model/`) e nos `dto/request/` |
| Consultas personalizadas | `GameRepository` e `UserProfileRepository` (`@Query`) |
| Paginação + HATEOAS | controllers (`PagedResourcesAssembler`) e `assembler/` (links) |
| Idempotência | `IdempotencyService` + método `create` de cada controller |
| Login / chave de API | `AuthController`, `AuthService` + método `delete` de cada controller |
| Rate limiting | `config/RateLimitFilter` (20 requisições/min por IP) |
| CORS | `config/CorsConfig` + `app.cors.allowed-origins` |
| Versionamento (URL) | `CategoryV2Controller` (`/api/category/v2`) e `GameV2Controller` (`/api/game/v2`) |
| Erros | `exception/ExceptionHandlerModel` |

## Como funciona

**Idempotência:** todo POST exige o header `X-Idempotency-Key` (ex.: um UUID). O `IdempotencyService` guarda
"chave → id criado". Se a mesma chave chegar de novo, o controller devolve o registro já criado (200) e não cria outro.

**Login:** `POST /api/auth/login` (email e senha) gera uma chave (UUID) e salva no usuário. Todo DELETE exige
essa chave no header `X-API-Key`; o controller chama `authService.validate(apiKey)` e, se ela for inválida, responde 401.
`POST /api/auth/logout` apaga a chave.

**Versionamento:** `/api/category` e `/api/category/v1` são a v1. `/api/category/v2` traz também `gamesCount`;
`/api/game/v2` traz o game resumido (só os nomes).

## Testando (Swagger: http://localhost:8080/swagger-ui/index.html)

Usuário de teste criado ao subir: **admin@indiegames.com / admin123**

1. `POST /api/category` com `X-Idempotency-Key: teste-123` e `{"name":"Puzzle"}` → 201. Repita igual → 200, mesmo id.
2. `POST /api/category` sem o header → 400.
3. `DELETE /api/game/1` sem `X-API-Key` → 401.
4. `POST /api/auth/login` → copie o `apiKey` → `DELETE /api/game/1` com `X-API-Key` → 204.
5. `DELETE /api/category/1` (RPG ainda tem games) → 409.
6. `GET /api/category/1` × `GET /api/category/v2/1` (v2 tem `gamesCount`).
7. `GET /api/game/status/RELEASED`, `/api/game/developer/1`, `/api/userprofile/search?bio=testes`.
8. Mais de 20 requisições em 1 minuto → 429 com `Retry-After`.
