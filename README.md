# java-filmorate

![ER-диаграмма](https://github.com/GoryachayaG/java-filmorate/blob/add-friends-likes/ERдиаграмма.png)


### - film

Содержит данные о фильмах

### - genre

Содержит данные о существующих жанрах

### - film_genres

Содержит данные о том, каким жанрам соответствует фильм

### - rating

Содержит данные о существующих рейтингах МРА

### - likes

Содержит данные о том, какой пользователь какой фильм лайкнул

### - user

Содержит данные о пользователях

### - friends

Содержит данные о том, кто чьим другом является и статус их «дружбы»


### 1. Получение всех фильмов
```
SELECT *
FROM films;
```

### 2. Получение всех пользователей
```
SELECT *
FROM users;
```

### 3. Получение топ 5 наиболее популярных фильмов
```
SELECT name
FROM films
WHERE film_id IN (SELECT film_id
		FROM likes
		GROUP BY film_id
		ORDER BY COUNT(user_id) DESC
		LIMIT 5);
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
