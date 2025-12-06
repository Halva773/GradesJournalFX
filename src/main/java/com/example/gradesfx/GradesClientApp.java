package com.example.gradesfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Главный класс JavaFX приложения "Журнал оценок студентов".
 * Клиент для взаимодействия с REST API сервера.
 * 
 * @author Student
 * @version 1.0
 */
public class GradesClientApp extends Application {
    
    private static final String APP_TITLE = "Журнал оценок студентов";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Загрузка FXML разметки
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/main.fxml"));
            Parent root = loader.load();
            
            // Создание сцены
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Настройка окна
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            
            // Центрирование окна
            primaryStage.centerOnScreen();
            
            // Показ окна
            primaryStage.show();
            
            System.out.println("Приложение запущено успешно");
            System.out.println("REST API: http://localhost:8080/api/grades");
            
        } catch (IOException e) {
            System.err.println("Ошибка загрузки FXML: " + e.getMessage());
            e.printStackTrace();
            showErrorAndExit("Не удалось загрузить интерфейс приложения");
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
            showErrorAndExit("Произошла непредвиденная ошибка при запуске");
        }
    }
    
    /**
     * Показать сообщение об ошибке и завершить приложение.
     */
    private void showErrorAndExit(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Ошибка запуска");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(1);
    }
    
    @Override
    public void stop() {
        System.out.println("Приложение завершено");
    }
    
    /**
     * Точка входа в приложение.
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Установка кодировки UTF-8 для корректного отображения кириллицы
        System.setProperty("file.encoding", "UTF-8");
        
        System.out.println("=========================================");
        System.out.println("   Журнал оценок студентов - JavaFX      ");
        System.out.println("   REST API Client                       ");
        System.out.println("=========================================");
        
        launch(args);
    }
}


