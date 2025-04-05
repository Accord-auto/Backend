#!/bin/bash

# Определяем текущую директорию (где находится этот скрипт)
CURRENT_DIR=$(pwd)

# Определяем корневую директорию проекта (на уровень выше Backend)
ROOT_DIR="$(dirname "$CURRENT_DIR")"

# Проверяем, существует ли файл docker-compose.yml в текущей директории (Backend)
if [[ ! -f "$CURRENT_DIR/docker-compose.yml" ]]; then
  echo "Ошибка: Файл docker-compose.yml не найден в директории Backend."
  exit 1
fi

# Перемещаем docker-compose.yml из Backend в корневую директорию
echo "Перемещение docker-compose.yml из Backend в корневую директорию..."
mv "$CURRENT_DIR/docker-compose.yml" "$ROOT_DIR/"

# Создаем директорию uploads внутри Backend
echo "Создание директории uploads в Backend..."
mkdir -p "$CURRENT_DIR/uploads"

# Устанавливаем права доступа для директории uploads
chmod -R 777 "$CURRENT_DIR/uploads"

echo "Операция завершена успешно!"