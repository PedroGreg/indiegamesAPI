
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
-- id integerint64
-- name string[3, 255] characters
-- createdAt stringdate-time
-- updatedAt stringdate-time
-
#### Platform
-- id integerint64
-- name string[3, 255] characters
-- createdAt stringdate-time
-- updatedAt stringdate-time
#### Developer
-- id integerint64
-- name string[3, 255] characters
-- createdAt stringdate-time
-- updatedAt stringdate-time
-
#### User
-- id integerint64
-- name string[3, 255] characters
-- email string[3, 255] characters
-- userProfile UserProfile.class
-- createdAt stringdate-time
-- updatedAt stringdate-time
-
#### UserProfile
-- id integerint64
-- bio string
-- user User
-- createdAt stringdate-time
-- updatedAt stringdate-time
-
#### Game
-- id integerint64
-- name string[3, 255] characters
-- status Enum ('DEVELOPMENT','EARLY_ACCESS','RELEASED')
-- developer Developer
-- category Category
-- platforms Array<Platform>
-- createdAt stringdate-time
-- updatedAt stringdate-time
-
