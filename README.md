# Авто звук

Этот проект представляет собой систему управления товарами.
Он включает в себя функции для создания,
обновления, удаления и получения информации о товарах.

## Содержание

- [Описание](#описание)
- [Функции](#функции)
- [Технологии](#технологии)
- [Установка](#установка)
- [Использование](#использование)
- [Тестирование](#тестирование)

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

## Технологии

- **Backend**: Spring Boot
- **Database**: mysql
- **Testing**: JUnit, Mockito
- **Build Tool**: Gradle

## Установка

1. Клонируйте репозиторий:

```bash
git https://github.com/Accord-auto/Backend.git

cd Backend
```
2. Установите зависимости:
```./gradlew build```
3. Запустите приложение:
```./gradlew bootRun```

## Использование
После запуска приложения, вы можете взаимодействовать с API через следующие эндпоинты:

### Products Endpoints
*   GET /products: Получить список всех товаров с пагинацией
*   GET /products/{id}: Получить информацию о товаре по идентификатору
```json
[
  {
    "id": 0,
    "name": "string",
    "brand": "string",
    "count": 0,
    "price": {
      "id": 0,
      "product": "string",
      "value": 0,
      "discount": 0
    },
    "countType": "string",
    "description": "string",
    "article": "string",
    "category": {
      "name": "string",
      "products": [
        "string"
      ],
      "id": 0
    },
    "mainPhotoUrl": "string",
    "additionalPhotos": [
      "string"
    ],
    "properties": [
      {
        "id": 0,
        "product": "string",
        "property": {
          "name": "string",
          "productProperties": [
            "string"
          ],
          "id": 0
        },
        "value": "string"
      }
    ]
  }
]
 
```
*   POST /products: Создать новый товар
*   PUT /products/{id}/price: Обновить цену товара
*   PUT /products/{id}/discount: Обновить скидку на товар
*   PUT /products/{id}/count: Обновить количество товара
*   DELETE /products/{id}: Удалить товар

### Properties Endpoints
*   POST /properties: Создать новую характеристику
*   DELETE /properties/{id}: Удалить характеристику по идентификатору

### Categories Endpoints
*   GET /categories: Получить список всех категорий
*   POST /categories: Создать новую категорию
*   DELETE /categories/{id}: Удалить категорию по идентификатору

### Articles Endpoints

```plaintext
 GET /articles: Получить список всех статей
```
```plaintext
GET /articles/{id}: Получить информацию о статье по идентификатору
```
```plaintext
POST /articles: Создать новую статью
```
```plaintext
PUT /articles/{id}: Обновить статью по идентификатору
```
```plaintext
DELETE /articles/{id}: Удалить статью по идентификатору
```
### Photos Endpoints
*   GET /photos/{photoPath}: Получить фотографию по пути
*   GET /photos/batch: Получить несколько фотографий по списку путей

## Тестирование
Для запуска тестов используйте:
```./gradlew test```