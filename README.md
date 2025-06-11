# User API - Spring Boot Приложение

Это простое Java web-приложение, построенное на Spring Boot, которое предоставляет базовую функциональность управления пользователями.

## Функциональность

*   **Получить всех пользователей:** Получает JSON-ответ со списком всех доступных пользователей с эндпойнта `/user-api/v1/users` используя `GET` запрос.
*   **Создать пользователя:** Добавляет нового пользователя с заданными параметрами в базу данных через `POST` запрос к эндпойнту `/user-api/v1/users`. Информация о пользователе передается в теле запроса в формате JSON.

## Используемые технологии

*   **Java**
*   **Spring Boot**
   *   Spring Web
   *   Spring Data JPA
*   **H2 Database (В памяти)**

## Архитектура

Приложение следует многослойной архитектуре:

*   **Слой репозитория:**  Обрабатывает взаимодействие с базой данных, используя Spring Data JPA. Включает в себя интерфейс `UserRepository`.
*   **Слой сервиса:** Содержит бизнес-логику приложения.  Включает класс `UserService`.
*   **Слой контроллера:** Обрабатывает входящие HTTP-запросы и возвращает ответы.  Включает класс `UserController`.

## Сущность пользователя (`User`)

Сущность `User` имеет следующие поля:

*   `id` (Long): Уникальный идентификатор (первичный ключ, генерируется автоматически).
*   `firstName` (String): Имя.
*   `lastName` (String): Фамилия.
*   `role` (Role): Роль пользователя (см. ниже).

## Enum Роли (`Role`)

Enum `Role` определяет возможные роли для пользователя:

*   `ADMIN`
*   `DEVELOPER`
*   `TESTER`
*   `SYSTEM_ANALYST`
*   `TEAM_LEAD`

## База данных

Приложение использует базу данных H2 в памяти для хранения данных.  Имя таблицы - `app_user` (может быть настроено с помощью аннотации `@Table` в сущности `User`).

### Начальные данные

При запуске приложения таблица базы данных автоматически заполняется 5 записями пользователей, по одной для каждой роли, определенной в enum `Role`.

## Настройка и запуск приложения

1.  **Предварительные требования:**
   *   Java Development Kit (JDK) 17 или выше
   *   Maven или Gradle (инструмент сборки)

2.  **Клонируйте репозиторий и переключитесь на ветку lab4:**

    ```bash
    git checkout lab4
    cd /DevJavaSpringApp/user-api
    ```

3.  **Сборка приложения:**

   *   **Maven:**

       ```bash
       ./mvnw clean install
       ```

4.  **Запуск приложения:**

   *   **Maven:**

       ```bash
       ./mvnw spring-boot:run
       ```

    Приложение запустится и будет прослушивать порт `http://localhost:8080` по умолчанию.

## Конфигурация

Приложение настраивается с использованием файла `application.properties`.

**Пример `application.properties`:**

```properties
# Server port
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:userdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
spring.jpa.defer-datasource-initialization=true

# SQL Initialization
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql