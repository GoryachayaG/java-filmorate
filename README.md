# java-filmorate

![ER-диаграмма]([https://github.com/GoryachayaG/java-filmorate/blob/add-friends-likes/ERдиаграмма.png)


### - Film

Содержит данные о фильмах

### - Genre

Содержит данные о существующих жанрах

### - Rating

Содержит данные о существующих рейтингах МРА

### - Likes

Содержит данные о том, какой пользователь какой фильм лайкнул

### - User

Содержит данные о пользователях

### - Friends

Содержит данные о том, кто чьим другом является и id таблицы с подробностями о статусе их «дружбы»

### - Friendship

Содержит данные о «дружбе» между двумя пользователями
Поле FriendshipID – идентификационный номер «дружбы» между двумя пользователями
Поле FriendshipStatus – true / false дружат пользователи или нет
Поле RequestStatusCode – код статуса запроса в друзья

### - RequestStatus

Содержит данные о статусе запроса в друзья – находится ли он в стадии запроса или уже подтвержден
Поле StatusCode (R,A) – уникальный код по первым буквам названия статуса
Поле Name (Requested, Accepted) – название статуса




### 1. Получение всех фильмов
```
SELECT *
FROM films
```

### 2. Получение всех пользователей
```
SELECT *
FROM users
```

### 3. Получение топ 5 наиболее популярных фильмов
```
SELECT f.name
	COUNT(l.user_id) AS rate
FROM films AS f
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.name
ORDER BY rate DESC
LIMIT 5;
```
 
### 4. Получение списка общих друзей пользователя с id55 с другим пользователем с id 99
```
SELECT u.user_id
FROM users AS u
INNER JOIN friends AS f ON u.user_id = f.friend_id
WHERE u.user_id = 55

INTERSECT

SELECT u.user_id
FROM users AS u
INNER JOIN friends AS f ON u.user_id = f.friend_id
WHERE u.user_id = 99;
```
