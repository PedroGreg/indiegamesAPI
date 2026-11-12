API REST para gerenciar um catálogo de **games indie**: desenvolvedores, categorias, plataformas, games, usuários e perfis.

> 📘 Guia completo também em [/guia.html](/guia.html) · 📜 [Termos de Serviço](/termos.html) · 🏠 [Página inicial](/)

---

## 🚀 Primeiros passos

1. **Crie um usuário com dados fictícios** em `POST /api/user/v1`. Use um nome inventado (ex.: `Jogador Pixel`) e um e-mail de exemplo (ex.: `jogador.pixel@exemplo.com`). **Não use seu nome, e-mail ou senha reais**: esta é uma API de estudo, os dados ficam visíveis no banco e somem ao reiniciar.
2. **Faça login** em `POST /api/auth/login` com o e-mail e a senha que você cadastrou. A resposta traz uma `apiKey`.
3. **Use a chave** no header `X-API-Key` sempre que for **alterar (PUT)** ou **deletar (DELETE)** algo.
4. Em todo **cadastro (POST)**, envie o header `X-Idempotency-Key` com um valor único (ex.: um UUID novo).

> 💡 Já existe um usuário de teste: **admin@indiegames.com** / **admin123**.

---

## 🔑 Headers da API

| Header | Quando enviar | O que acontece se faltar |
|---|---|---|
| `X-Idempotency-Key` | Em todo **POST** de cadastro (v1 e v2) | **400** – "O header X-Idempotency-Key é obrigatório" |
| `X-API-Key` | Em todo **PUT** e **DELETE**, e no logout | **401** – "Esta operação exige login" |
| `Content-Type: application/json` | Em todo POST e PUT com corpo | **415** – "Content-Type não suportado" |

**Como funciona a idempotência:** se você reenviar um POST com a **mesma** `X-Idempotency-Key`, a API **não cria outro registro**. Ela devolve o registro criado na primeira vez, com status **200** em vez de 201. Use **uma chave nova para cada cadastro novo**.

---

## 🧭 Versões (v1 e v2)

A versão faz parte da URL.

| Recurso | v1 | v2 |
|---|---|---|
| Categorias | `/api/category/v1` (CRUD completo) | `/api/category/v2` (só consulta, com `gamesCount`) |
| Games | `/api/game/v1` (CRUD completo) | `/api/game/v2` (consulta, cadastro e alteração, com `totalAchievements`) |
| Desenvolvedores | `/api/developer/v1` | – |
| Plataformas | `/api/platform/v1` | – |
| Usuários | `/api/user/v1` | – |
| Perfis | `/api/userprofile/v1` | – |
| Login | `/api/auth/login` e `/api/auth/logout` (sem versão) | – |

**Diferenças da v2:**
- **Categorias v2:** cada categoria traz `gamesCount` (quantos games ela tem).
- **Games v2:** formato resumido (só os nomes do desenvolvedor, da categoria e das plataformas) e o campo **`totalAchievements`**, a quantidade total de conquistas do jogo. Esse campo **não aparece na v1**, e um game criado pela v1 começa com **0** conquistas.

---

## 📚 Exemplos

**Cadastrar uma categoria** — `POST /api/category/v1`
Header: `X-Idempotency-Key: 7c9e6679-7425-40de-944b-e07fc1f90ae7`
```json
{ "name": "Puzzle" }
```

**Cadastrar um usuário (dados fictícios)** — `POST /api/user/v1`
```json
{ "name": "Jogador Pixel", "email": "jogador.pixel@exemplo.com", "password": "senha123", "bio": "Fã de metroidvanias" }
```

**Login** — `POST /api/auth/login`
```json
{ "email": "jogador.pixel@exemplo.com", "password": "senha123" }
```

**Cadastrar um game (v1)** — `POST /api/game/v1`
```json
{ "name": "Celeste", "status": "RELEASED", "developerId": 1, "categoryId": 1, "platformIds": [1, 2] }
```

**Cadastrar um game com conquistas (v2)** — `POST /api/game/v2`
```json
{ "name": "Celeste", "status": "RELEASED", "developerId": 1, "categoryId": 1, "platformIds": [1, 2], "totalAchievements": 32 }
```

**Alterar uma categoria** — `PUT /api/category/v1/1` com o header `X-API-Key: <sua chave>`
```json
{ "name": "RPG de Ação" }
```

**Buscas e filtros**
- `GET /api/category/v1/search?name=rpg` — por parte do nome (sem diferenciar maiúsculas/minúsculas)
- `GET /api/game/v1/status/RELEASED` — por status
- `GET /api/game/v1/developer/1`, `/api/game/v1/category/1`, `/api/game/v1/platform/1` — games de um desenvolvedor, categoria ou plataforma
- `GET /api/userprofile/v1/search?bio=metroidvania` — perfis pela bio
- Paginação em todas as listagens: `?page=0&size=10&sort=name,asc`

---

## ✅ O que você pode fazer

- Listar, buscar e ver detalhes de tudo **sem login** (GET é público).
- Cadastrar (POST) **sem login**, desde que envie a `X-Idempotency-Key`.
- Alterar (PUT) e deletar (DELETE) **com login** (`X-API-Key`).
- Navegar pela API seguindo os `_links` de cada resposta (HATEOAS): de um game para o desenvolvedor dele, de uma categoria para os games dela, da v1 para a v2...
- Reenviar um POST com a mesma `X-Idempotency-Key` sem medo de duplicar o registro.

---

## ⛔ O que NÃO pode (e o erro que você recebe)

Todos os erros vêm no mesmo formato:
```json
{ "timestamp": "2026-11-12T17:45:00.000+00:00", "message": "explicação do erro", "details": "uri=/api/..." }
```

| Status | Situação | Como resolver |
|---|---|---|
| **400** | Campo obrigatório vazio ou ausente (ex.: `name` em branco) | Preencha o campo indicado na `message` |
| **400** | Nome de campo errado (ex.: `"naem"` no lugar de `"name"`) | O campo certo fica vazio e a validação reclama; corrija o nome |
| **400** | Tamanho inválido: categoria com menos de 3 caracteres, plataforma com mais de 50, usuário com menos de 3, senha com menos de 6, bio com mais de 255 | Ajuste o tamanho do texto |
| **400** | E-mail em formato inválido ou **já cadastrado** | Use outro e-mail (fictício) |
| **400** | Cadastro de usuário **sem senha** | Envie a `password` |
| **400** | `status` diferente de `DEVELOPMENT`, `EARLY_ACCESS` ou `RELEASED` | Use um dos três valores, em maiúsculas |
| **400** | `totalAchievements` negativo, acima de 10000 ou ausente (v2) | Envie um número de 0 a 10000 |
| **400** | JSON mal formatado (vírgula sobrando, aspas faltando, texto no lugar de número) | Confira a sintaxe do JSON |
| **400** | POST sem o header `X-Idempotency-Key` | Envie o header com um valor único |
| **400** | ID não numérico na URL (ex.: `/api/game/v1/abc`) | Use um número |
| **400** | Busca sem o parâmetro (ex.: `/search` sem `?name=`) | Envie o parâmetro |
| **401** | PUT ou DELETE **sem** `X-API-Key` | Faça login e envie a chave |
| **401** | Chave antiga: depois de um **novo login** ou de um **logout**, a chave anterior deixa de valer | Use a chave do último login |
| **401** | Login com e-mail ou senha errados | Confira os dados |
| **404** | ID que não existe (inclusive `developerId`, `categoryId` ou algum `platformIds` de um game) | Consulte a listagem e use um ID existente |
| **404** | Rota que não existe ou sem a versão (ex.: `/api/category` sem `/v1`) | Use `/v1` ou `/v2` |
| **405** | Método que a rota não tem (ex.: `DELETE /api/category/v2/1`) | Para alterar e deletar categorias, use a v1 |
| **409** | Deletar categoria, desenvolvedor ou plataforma que ainda tem games | Delete ou altere os games antes |
| **409** | Deletar um perfil que pertence a um usuário | Delete o usuário (o perfil vai junto) |
| **415** | Corpo enviado sem `Content-Type: application/json` | Envie o header |
| **429** | Mais de **20 requisições por minuto** do mesmo IP | Espere os segundos do header `Retry-After` |
| **500** | Erro inesperado no servidor | Tente de novo; se continuar, verifique o log |

**Cuidados importantes:**
- ⚠️ **Não reutilize** uma `X-Idempotency-Key` para um cadastro diferente: você vai receber o registro **antigo** (200), e o novo não será criado.
- ⚠️ A mesma chave de idempotência usada na v1 e na v2 de games conta como chaves separadas.
- ⚠️ O banco é **em memória (H2)**: ao reiniciar a API, **tudo volta aos dados de exemplo** e as chaves de login e de idempotência são apagadas.
- ⚠️ Cada usuário tem **uma** chave de API por vez: um novo login invalida a anterior.
- ⚠️ Num PUT, o corpo é validado **antes** da chave: um PUT com corpo inválido recebe **400** mesmo sem `X-API-Key`. Com o corpo válido e sem chave, a resposta é **401**.
- ⚠️ Não há DELETE em cascata: apagar um registro em uso dá **409**.
- ⚠️ Use sempre **dados fictícios**. Nunca cadastre seu nome, e-mail ou senha reais.
