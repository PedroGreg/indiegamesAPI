
# Indie Games API

Uma API para gestão de jogos INDIES

## 🛠️ Como Rodar o Projeto

### Pré-requisitos
* **Java 17** ou superior instalado
* **Maven 3.8+** instalado (ou utilize o wrapper `./mvnw` do projeto)
* Git

### Passos para execução

1. **Clonar o repositório:**
2. **Compilar e rodar a aplicação**
   - NO MAC/ LINUX: ./mvnw spring-boot:run
   - NO WINDOWS: mvnw.cmd spring-boot:run
3. Acessar API e Documentação
   - URL Base da API: http://localhost:8080/api
   - Documentação interativa Swagger: http://localhost:8080/swagger-ui/index.html
   
## API Reference

## INDIE GAMES API

### Games
#### Get all games
```http
  GET /game
```
#### Get one game
```http
  GET /game/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create game
```http
  POST /game
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Game` | **Required**. Objeto tipo game|
#### Update a game
```http
  PUT /game/{id}
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Game` | **Required**. Objeto tipo game|
#### Delete a game
```http
  DELETE /game/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
---
### Categories
#### Get all categories
```http
  GET /category
```
#### Get one category
```http
  GET /category/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create category
```http
  POST /category
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Category` | **Required**. Objeto tipo Category|
#### Update a category
```http
  PUT /category/{id}
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Category` | **Required**. Objeto tipo Category|
#### Delete a category
```http
  DELETE /category/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
---
### Platforms
#### Get all platforms
```http
  GET /platform
```
#### Get one platform
```http
  GET /platform/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create platforms
```http
  POST /platform
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Platform` | **Required**. Objeto tipo Platform|
#### Update a platform
```http
  PUT /platform/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Delete a platform
```http
  DELETE /platform/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
---
### Developers
#### Get all developer
```http
  GET /developer
```
#### Get one developer
```http
  GET /developer/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create developer
```http
  POST /developer
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Developer` | **Required**. Objeto tipo Developer|
#### Update a developer
```http
  PUT /developer/{id}
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Developer` | **Required**. Objeto tipo Developer|
#### Delete a developer

```http
  DELETE /developer/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
---
### Users
#### Get all users
```http
  GET /user
```
#### Get one user
```http
  GET /user/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create user
```http
  POST /user
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `User` | **Required**. Objeto tipo User|
#### Update a user
```http
  PUT /user/{id}
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `User` | **Required**. Objeto tipo User|
#### Delete a user

```http
  DELETE /user/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
---
### UserProfile
#### Get all users profiles
```http
  GET /userprofile
```
#### Get one userprofile
```http
  GET /userprofile/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Create userprofile
```http
  POST /userprofile
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Userprofile` | **Required**. Objeto tipo UserProfile|
#### Update a userprofile
```http
  PUT /userprofile/{id}
```
| Request | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Body`      | `Userprofile` | **Required**. Objeto tipo UserProfile|
#### Delete a user

```http
  DELETE /userprofile/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |

### Objects examples:
#### Category
"category": {
    "id": 1, (Gera automaticamente)
    "name": "Metroidvania", (Obrigatório)
    "createdAt": "2026-10-06T10:00:00",(Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00"(Gera automaticamente)
  }
#### Platform
"platform": {
    "id": 1, (Gera automaticamente)
    "name": "PC (Steam)", (Obrigatório)
    "createdAt": "2026-10-06T10:00:00",(Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00"(Gera automaticamente)
  }
#### Developer
"developer": {
    "id": 1,(Gera automaticamente)
    "name": "Team Cherry", (Obrigatório)
    "createdAt": "2026-10-06T10:00:00",(Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00"(Gera automaticamente)
  }
#### User
"user": {
    "id": 1, (Gera automaticamente)
    "name": "Pedro Gregorio", (Obrigatório)
    "email": "pedro@email.com", (Obrigatório)
    "userProfile": { (Obrigatório)
      "id": 1,(Gera automaticamente)
      "bio": "Desenvolvedor Backend e Entusiasta de Jogos Indies", (Obrigatório pode ser vazio)
      "createdAt": "2026-10-06T10:00:00", (Gera automaticamente)
      "updatedAt": "2026-10-06T10:00:00" (Gera automaticamente)
    },
    "createdAt": "2026-10-06T10:00:00", (Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00" (Gera automaticamente)
  },
#### UserProfile
"userProfile": {
    "id": 1,(Gera automaticamente)
    "bio": "Desenvolvedor Backend e Entusiasta de Jogos Indies", (Obrigatório pode ser vazio)
    "user": { (Não deve ser enviado, apenas para requisições GET)
       "name": "Pedro Gregorio", 
       "email": "pedro@email.com",
       "createdAt": "2026-10-06T17:50:19.084396",
       "updatedAt": 2026-10-06T17:50:19.084396,
       "id": 1
   },
    "createdAt": "2026-10-06T10:00:00", (Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00" (Gera automaticamente)
  }
#### Game
"game": {
    "id": 1, (Gera automaticamente)
    "name": "Hollow Knight",
    "status": "RELEASED",
    "developer": {
      "id": 1,
      "name": "Team Cherry",
      "createdAt": "2026-10-06T10:00:00",
      "updatedAt": "2026-10-06T10:00:00"
    }, (Para requisições POST e PUT, enviar apenas o valor do ID, ex: "category" : "1")
    "category": {
      "id": 1,
      "name": "Metroidvania",
      "createdAt": "2026-10-06T10:00:00",
      "updatedAt": "2026-10-06T10:00:00"
    },
    "platforms": [
      {
        "id": 1,
        "name": "PC (Steam)",
        "createdAt": "2026-10-06T10:00:00",
        "updatedAt": "2026-10-06T10:00:00"
      },
    {
        "id": 2,
        "name": "Nintendo Switch",
        "createdAt": "2026-10-06T10:00:00",
        "updatedAt": "2026-10-06T10:00:00"
      }
    ],
    "createdAt": "2026-10-06T10:00:00", (Gera automaticamente)
    "updatedAt": "2026-10-06T10:00:00" (Gera automaticamente)
  }
