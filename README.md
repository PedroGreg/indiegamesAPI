
# Project Title

A brief description of what this project does and who it's for


## API Reference

#### Get all items

```http
  GET /api/items
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Get item

```http
  GET /api/items/${id}
```



#### add(num1, num2)

Takes two numbers and returns the sum.

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
#### Update a game
```http
  PUT /game/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
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
#### Update a category
```http
  PUT /category/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
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
#### Update a developer
```http
  PUT /developer/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |
#### Delete a developer

```http
  DELETE /developer/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |