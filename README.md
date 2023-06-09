# News management

RESTful web-service, реализующий функционал для работы с
системой управления новостями
## запуск
- docker network create app-network && docker compose up
## Application
Адреса доступны только аутентифицированым пользователям

 - Аутентификация
  ```sh
   POST /api/auth/login
   ```
 - Регистрация возможна:
   - подписчик
    ```sh
       POST /api/auth/registration/journalist
    ```
   - журналист
   ```sh
    POST /api/auth/registration/subscriber
    ```
   - админ
   ```sh
   POST /api/auth/registration/admin
    ```
 - Авторизация с помощью jwt-токена:
 
Доступные адреса:

  - работа с новостями
 > News:
     id,
     time,
      title,
      text,
      comments,
     username
 - Создавние новости возможно только журналисту
```sh
POST /api/v1/news
```
- Поиск по id
```sh
GET /api/v1/news/{id}
```
- Список с учетом пагинации
```sh
GET /api/v1/news?limit={pageSize}&offset={page}
```
 - Обновляются только те поля, которые передаются в запросе (доступно журналисту, только свои новости)
```sh
PUT /api/v1/news/{id}
```
 - Удаление по id (доступно журналисту, только свои новости)
```sh
DELETE /api/v1/news/{id}
```
 - Полнотекстовый поиск по различным параметрам
```sh
GET /api/v1/news/search?size=3&page=0&nameFeild=param
```
  - Работа с коментариями
  >Comment:
      id,
      time, text, username, news_id
 - Создание коментария возможно только подписчику
 ```sh
POST /api/v1/comments
```
- Поиск по id 
```sh
GET /api/v1/comments/{id}
```
- Список с учетом пагинации
```sh
GET /api/v1/comments?limit={pageSize}&offset={page}
```
 - Обновляются только те поля, которые передаются в запросе (доступно подписчику, только свои коментарии)
```sh
PUT /api/v1/comments/{id}
```
 - Удаление по id (доступно подписчику, только свои коментарии)
```sh
DELETE /api/v1/comments/{id}
```  
 - Полнотекстовый поиск по различным параметрам
```sh
GET /api/v1/comments/search?size=3&page=0&nameFeild=param
```

