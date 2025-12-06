# 📊 Журнал оценок — JavaFX Client

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/JavaFX-21.0.1-blue?style=for-the-badge" alt="JavaFX 21.0.1">
  <img src="https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven" alt="Maven">
  <img src="https://img.shields.io/badge/REST-Client-green?style=for-the-badge" alt="REST Client">
</p>

> **Десктопный клиент** для управления журналом оценок студентов через REST API

## 📋 Описание

JavaFX приложение — графический клиент для взаимодействия с [Spring Boot сервером](../SpringDataManager/). Обеспечивает полный CRUD-функционал (создание, чтение, редактирование, удаление) для таблицы оценок студентов через REST API.

### Возможности

| Функция | Описание |
|---------|----------|
| 📋 **Просмотр** | Отображение всех оценок в таблице (TableView) |
| ➕ **Добавление** | Создание новых записей через форму |
| ✏️ **Редактирование** | Изменение существующих записей |
| 🗑️ **Удаление** | Удаление записей с подтверждением |
| 🔄 **Обновление** | Синхронизация данных с сервером |

---

## 🛠 Технологии

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 21 | Язык программирования |
| JavaFX | 21.0.1 | GUI фреймворк |
| Jackson | 2.16.1 | Сериализация/десериализация JSON |
| java.net.http | — | HTTP клиент (встроенный в JDK) |
| Maven | 3.8+ | Сборка проекта |

---

## 🚀 Быстрый старт

### Требования

- **JDK 21** или выше
- **Maven 3.8+**
- Работающий REST API сервер на `http://localhost:8080`

### Запуск сервера (SpringDataManager)

Перед запуском клиента необходимо запустить серверное приложение:

```bash
cd ../SpringDataManager
mvn spring-boot:run
```

Сервер будет доступен по адресу: `http://localhost:8080`

### Запуск клиента

1. **Скомпилируйте проект:**
   ```bash
   mvn clean compile
   ```

2. **Запустите приложение:**
   ```bash
   mvn javafx:run
   ```

### Создание JAR

```bash
mvn clean package
```

---

## 📁 Структура проекта

```
src/main/java/
├── module-info.java                    # Модульная конфигурация Java
└── com/example/gradesfx/
    ├── GradesClientApp.java            # Точка входа приложения
    ├── controller/
    │   └── MainController.java         # Контроллер интерфейса
    ├── model/
    │   └── Grade.java                  # Модель данных (DTO)
    └── service/
        └── GradeApiService.java        # HTTP клиент для REST API

src/main/resources/
├── view/
│   └── main.fxml                       # FXML разметка интерфейса
└── styles/
    └── main.css                        # Стили оформления
```

---

## 🔌 REST API

Приложение взаимодействует с REST API по адресу `http://localhost:8080/api/grades`

### Endpoints

| Метод | URL | Описание |
|-------|-----|----------|
| `GET` | `/api/grades` | Получить все оценки |
| `GET` | `/api/grades/{id}` | Получить оценку по ID |
| `POST` | `/api/grades` | Создать новую оценку |
| `PUT` | `/api/grades/{id}` | Обновить оценку |
| `DELETE` | `/api/grades/{id}` | Удалить оценку |

### Формат данных

```json
{
  "id": 1,
  "studentName": "Иванов Иван",
  "subject": "Математика",
  "grade": 5
}
```

### Поля модели Grade

| Поле | Тип | Описание | Валидация |
|------|-----|----------|-----------|
| `id` | Long | Уникальный идентификатор | Генерируется сервером |
| `studentName` | String | ФИО студента | 2-100 символов |
| `subject` | String | Название предмета | 2-100 символов |
| `grade` | Integer | Оценка | 1-5 |

---

## 🎨 Интерфейс

Приложение имеет современный дизайн:

- 🎯 **Градиентный заголовок** — визуальное выделение названия
- 📊 **Адаптивная таблица** — отображение данных с подсветкой строк
- 📝 **Форма ввода** — удобные поля с валидацией
- 🎨 **Цветовые индикаторы** — кнопки разного цвета для разных действий
- 📌 **Статус-бар** — информация о количестве записей и состоянии

---

## ⚠️ Обработка ошибок

- 🔌 **Нет соединения** — Alert при недоступности сервера
- ✅ **Валидация** — проверка данных перед отправкой
- 💬 **Информирование** — понятные сообщения об ошибках
- ❓ **Подтверждение** — диалог перед удалением записи

---

## 📦 Зависимости (pom.xml)

```xml
<!-- JavaFX -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.1</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.1</version>
</dependency>

<!-- Jackson JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.1</version>
</dependency>
```

---

## 🔗 Связанные проекты

| Проект | Описание |
|--------|----------|
| [SpringDataManager](https://github.com/Halva773/SpringDataManager) | REST API сервер (Spring Boot) |

---

## 👤 Автор

**Студент Финансового университета**  
Вариант: **18 (Оценки / Grades)**

---

## 📄 Лицензия

Учебный проект

---

<p align="center">
  <sub>Сделано с ❤️ используя JavaFX</sub>
</p>
