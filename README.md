# Авто звук

Этот проект представляет собой систему управления товарами.
Он включает в себя функции для создания,
обновления, удаления и получения информации о товарах.

## Содержание

- [Описание](#описание)
- [Функции](#функции)
- [Технологии](#технологии)
- [Установка](#установка)

## Описание

Проект "Авто звук" предназначен для управления информацией о запчастях для товарах.
Он предоставляет API для выполнения CRUD операций над товарами,
а также позволяет управлять ценами, скидками и количеством товара.

## Функции

- Создание нового товара
- Обновление информации о товаре
- Удаление товара
- Получение информации о товаре по идентификатору
- Получение списка всех товаров с пагинацией
- Обновление цены товара
- Обновление скидки на товар
- Обновление количества товара
- Переключение специальных предложений
- Получение списка товаров по предварительной фильтрации
- создания характеристик и удаление, также создание значений характеристик
- создания статей и удаления их

## Технологии

- **Backend**: Spring Boot
- **Database**: mysql
- **Testing**: JUnit, Mockito
- **Build Tool**: Gradle
- **Containerization** Docker

## Установка(перед этим сделайте корневую директорию для проектов, и склонируйте frontend с админокой, либо уберите из docker-compose.yml сервисы front и front-admin)

1. Клонируйте репозиторий:

```bash
git https://github.com/Accord-auto/Backend.git
```
```bash
cd Backend
```
2. введите переменные в docker-compose.yml
```dockerfile
  backend:
    build:
      context: ./Backend/
      args:
        DOCKER_BUILDKIT: 1
    ports:
      - "8080:8080"
    volumes:
      - ./Backend/uploads:/uploads
    environment:
    #Тут настроить все
      #app
      SERVER_PORT: "8080"
      SPRING_DATASOURCE_URL: "jdbc:mysql://mysql.ru:3306/"
      SPRING_DATASOURCE_USERNAME: "example_login"
      SPRING_DATASOURCE_PASSWORD: "oiefowenjf"
        
      # Geonames
      # зарегистрируйте на geonames и получите доступ к их api
      GEONAMES_LOGIN: "Login"
      GEONAMES_API_URL: "http://api.geonames.org/searchJSON"

      # Admin 
      # данные входа в admin(хэшируеться)
      ADMIN_USERNAME: "admin"
      ADMIN_PASSWORD: "securePassword12345"
    networks:
      - app-network
```
3. Как использовать скрипт sh?
    
   * Сделайте скрипт исполняемым:
   ```bash
   chmod +x move_docker_compose.sh
   ```
   * Запустите скрипт:
   ```bash
   ./prepare_backend.sh
   ```
   * скрипт переместит docker-compose.yml в корневую папку проекта, и создаст директорию для хранения фотографий.

4. запустите docker-compose.yml
```bash
docker compose up -d --build
```