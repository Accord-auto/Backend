#!/bin/bash

# Определяем корневую директорию проекта (где находится этот скрипт)
ROOT_DIR=$(pwd)

# Проверяем, существует ли файл docker-compose.yml в директории Backend
if [[ ! -f "$ROOT_DIR/Backend/docker-compose.yml" ]]; then
  echo "Ошибка: Файл docker-compose.yml не найден в директории Backend."
  exit 1
fi

# Перемещаем docker-compose.yml из Backend в корневую директорию
echo "Перемещение docker-compose.yml из Backend в корневую директорию..."
mv "$ROOT_DIR/Backend/docker-compose.yml" "$ROOT_DIR/"

# Создаем директорию uploads внутри Backend
echo "Создание директории uploads в Backend..."
mkdir -p "$ROOT_DIR/Backend/uploads"

# Устанавливаем права доступа для директории uploads
chmod -R 777 "$ROOT_DIR/Backend/uploads"

echo "Операция завершена успешно!"