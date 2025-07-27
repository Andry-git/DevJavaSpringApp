# User API Application

## Описание

Простое веб-приложение на Spring Boot для управления пользователями с REST API и in-memory базой данных H2.

## Функционал

- Получение списка всех пользователей
- Добавление нового пользователя
- Получение пользователей, не принадлежащих к указанной стране, с сортировкой по возрасту

## Технологии

- Java 17
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- H2 Database (in-memory)
- Lombok

## Запуск приложения

1. Клонируйте репозиторий
2. Соберите проект:
   ```bash
   mvn clean package
   ```
3. Запустите приложение:

## Доступные эндпоинты

### 1. Получить всех пользователей
```
GET /user-api/v1/users
```

Пример ответа:
```json
[
    {
        "id": 1,
        "firstName": "Andrey",
        "age": 33,
        "country": "RUSSIA"
    },
    ...
]
```

### 2. Добавить нового пользователя
```
POST /user-api/v1/users
```
Тело запроса (JSON):
```json
{
    "firstName": "New User",
    "age": 25,
    "country": "GERMANY"
}
```

### 3. Получить пользователей не из указанной страны (сортировка по возрасту)
```
GET /user-api/v1/additional-info?country=COUNTRY_NAME
```
Пример:
```
GET /user-api/v1/additional-info?country=RUSSIA
```

## Доступ к H2 Console

После запуска приложения доступна консоль H2 по адресу:
```
http://localhost:8083/h2-console
```

Параметры подключения:
- JDBC URL: `jdbc:h2:mem:userdb`
- User Name: `sa`
- Password: (оставить пустым)

## Структура проекта

```
src/
├── main/
│   ├── java/
│   │   └── org/ad/userapi/
│   │       ├── config/       - Конфигурационные классы
│   │       ├── controller/   - REST контроллеры
│   │       ├── model/        - Сущности и enum'ы
│   │       ├── repository/   - JPA репозитории
│   │       ├── service/      - Сервисный слой
│   │       └── UserApiApplication.java - Главный класс
│   └── resources/
│       └── application.properties - Настройки приложения
```

## Инициализация данных

При первом запуске автоматически создаются тестовые данные:
- 5 пользователей из разных стран
- Данные создаются только если таблица пуста

## Примеры запросов

### Добавление пользователя:
```bash
curl -X POST -H "Content-Type: application/json" \
-d '{"firstName":"Maria","age":29,"country":"BRAZIL"}' \
http://localhost:8083/user-api/v1/users
```

### Получение пользователей не из Японии:
```bash
curl http://localhost:8083/user-api/v1/additional-info?country=JAPAN
```