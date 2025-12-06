package com.example.gradesfx.service;

import com.example.gradesfx.model.Grade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для взаимодействия с REST API журнала оценок.
 * Использует java.net.http.HttpClient для HTTP запросов.
 */
public class GradeApiService {
    
    private static final String BASE_URL = "http://localhost:8080/api/grades";
    private static final Duration TIMEOUT = Duration.ofSeconds(30);
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public GradeApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Получить все оценки.
     * 
     * @return список всех оценок
     * @throws ApiException при ошибке запроса
     */
    public List<Grade> getAllGrades() throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(TIMEOUT)
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), 
                        new TypeReference<List<Grade>>() {});
            } else {
                throw new ApiException("Ошибка получения данных: " + response.statusCode());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Ошибка соединения с сервером: " + e.getMessage(), e);
        }
    }
    
    /**
     * Получить оценку по ID.
     * 
     * @param id идентификатор записи
     * @return Optional с оценкой или пустой, если не найдена
     * @throws ApiException при ошибке запроса
     */
    public Optional<Grade> getGradeById(Long id) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(TIMEOUT)
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 200) {
                Grade grade = objectMapper.readValue(response.body(), Grade.class);
                return Optional.of(grade);
            } else if (response.statusCode() == 404) {
                return Optional.empty();
            } else {
                throw new ApiException("Ошибка получения данных: " + response.statusCode());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Ошибка соединения с сервером: " + e.getMessage(), e);
        }
    }
    
    /**
     * Создать новую оценку.
     * 
     * @param grade данные новой оценки (без ID)
     * @return созданная оценка с присвоенным ID
     * @throws ApiException при ошибке запроса
     */
    public Grade createGrade(Grade grade) throws ApiException {
        try {
            String jsonBody = objectMapper.writeValueAsString(grade);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .timeout(TIMEOUT)
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Grade.class);
            } else if (response.statusCode() == 400) {
                throw new ApiException("Ошибка валидации: проверьте введённые данные");
            } else {
                throw new ApiException("Ошибка создания записи: " + response.statusCode());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Ошибка соединения с сервером: " + e.getMessage(), e);
        }
    }
    
    /**
     * Обновить существующую оценку.
     * 
     * @param id идентификатор записи для обновления
     * @param grade новые данные оценки
     * @return обновлённая оценка
     * @throws ApiException при ошибке запроса
     */
    public Grade updateGrade(Long id, Grade grade) throws ApiException {
        try {
            String jsonBody = objectMapper.writeValueAsString(grade);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .timeout(TIMEOUT)
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Grade.class);
            } else if (response.statusCode() == 404) {
                throw new ApiException("Запись не найдена");
            } else if (response.statusCode() == 400) {
                throw new ApiException("Ошибка валидации: проверьте введённые данные");
            } else {
                throw new ApiException("Ошибка обновления записи: " + response.statusCode());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Ошибка соединения с сервером: " + e.getMessage(), e);
        }
    }
    
    /**
     * Удалить оценку по ID.
     * 
     * @param id идентификатор записи для удаления
     * @throws ApiException при ошибке запроса
     */
    public void deleteGrade(Long id) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .DELETE()
                    .timeout(TIMEOUT)
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 204 || response.statusCode() == 200) {
                return; // Успешное удаление
            } else if (response.statusCode() == 404) {
                throw new ApiException("Запись не найдена");
            } else {
                throw new ApiException("Ошибка удаления записи: " + response.statusCode());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Ошибка соединения с сервером: " + e.getMessage(), e);
        }
    }
    
    /**
     * Исключение для ошибок API.
     */
    public static class ApiException extends Exception {
        public ApiException(String message) {
            super(message);
        }
        
        public ApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}


