package com.example.gradesfx.controller;

import com.example.gradesfx.model.Grade;
import com.example.gradesfx.service.GradeApiService;
import com.example.gradesfx.service.GradeApiService.ApiException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Контроллер главного окна приложения.
 * Управляет таблицей оценок и формой редактирования.
 */
public class MainController implements Initializable {
    
    // Таблица оценок
    @FXML private TableView<Grade> gradesTable;
    @FXML private TableColumn<Grade, Long> idColumn;
    @FXML private TableColumn<Grade, String> studentNameColumn;
    @FXML private TableColumn<Grade, String> subjectColumn;
    @FXML private TableColumn<Grade, Integer> gradeColumn;
    
    // Форма ввода
    @FXML private TextField studentNameField;
    @FXML private TextField subjectField;
    @FXML private Spinner<Integer> gradeSpinner;
    
    // Кнопки
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Button clearButton;
    
    // Индикатор загрузки
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;
    
    private final GradeApiService apiService = new GradeApiService();
    private final ObservableList<Grade> gradesList = FXCollections.observableArrayList();
    
    // Текущая выбранная запись для редактирования
    private Grade selectedGrade = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupSpinner();
        setupSelectionListener();
        updateButtonStates();
        loadGrades();
    }
    
    /**
     * Настройка таблицы.
     */
    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // Центрирование для колонок ID и Оценка
        idColumn.setStyle("-fx-alignment: CENTER;");
        gradeColumn.setStyle("-fx-alignment: CENTER;");
        
        gradesTable.setItems(gradesList);
        gradesTable.setPlaceholder(new Label("Нет данных для отображения"));
    }
    
    /**
     * Настройка спиннера для оценок (1-5).
     */
    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        gradeSpinner.setValueFactory(valueFactory);
        gradeSpinner.setEditable(false);
    }
    
    /**
     * Настройка слушателя выбора строки в таблице.
     */
    private void setupSelectionListener() {
        gradesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedGrade = newValue;
                    if (newValue != null) {
                        fillFormWithGrade(newValue);
                    }
                    updateButtonStates();
                });
    }
    
    /**
     * Заполнение формы данными выбранной записи.
     */
    private void fillFormWithGrade(Grade grade) {
        studentNameField.setText(grade.getStudentName());
        subjectField.setText(grade.getSubject());
        gradeSpinner.getValueFactory().setValue(grade.getGrade());
    }
    
    /**
     * Обновление состояния кнопок.
     */
    private void updateButtonStates() {
        boolean hasSelection = selectedGrade != null;
        editButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
    }
    
    /**
     * Загрузка всех оценок с сервера.
     */
    @FXML
    private void loadGrades() {
        setLoading(true);
        setStatus("Загрузка данных...");
        
        Task<List<Grade>> task = new Task<>() {
            @Override
            protected List<Grade> call() throws Exception {
                return apiService.getAllGrades();
            }
        };
        
        task.setOnSucceeded(event -> {
            gradesList.clear();
            gradesList.addAll(task.getValue());
            setLoading(false);
            setStatus("Загружено записей: " + gradesList.size());
        });
        
        task.setOnFailed(event -> {
            setLoading(false);
            Throwable error = task.getException();
            setStatus("Ошибка загрузки");
            showError("Ошибка загрузки данных", error.getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Добавление новой оценки.
     */
    @FXML
    private void addGrade() {
        if (!validateForm()) {
            return;
        }
        
        Grade newGrade = createGradeFromForm();
        setLoading(true);
        setStatus("Добавление записи...");
        
        Task<Grade> task = new Task<>() {
            @Override
            protected Grade call() throws Exception {
                return apiService.createGrade(newGrade);
            }
        };
        
        task.setOnSucceeded(event -> {
            setLoading(false);
            clearForm();
            loadGrades();
            showInfo("Успешно", "Запись успешно добавлена");
        });
        
        task.setOnFailed(event -> {
            setLoading(false);
            Throwable error = task.getException();
            setStatus("Ошибка добавления");
            showError("Ошибка добавления записи", error.getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Редактирование выбранной оценки.
     */
    @FXML
    private void editGrade() {
        if (selectedGrade == null) {
            showWarning("Предупреждение", "Выберите запись для редактирования");
            return;
        }
        
        if (!validateForm()) {
            return;
        }
        
        Grade updatedGrade = createGradeFromForm();
        Long id = selectedGrade.getId();
        
        setLoading(true);
        setStatus("Обновление записи...");
        
        Task<Grade> task = new Task<>() {
            @Override
            protected Grade call() throws Exception {
                return apiService.updateGrade(id, updatedGrade);
            }
        };
        
        task.setOnSucceeded(event -> {
            setLoading(false);
            clearForm();
            loadGrades();
            showInfo("Успешно", "Запись успешно обновлена");
        });
        
        task.setOnFailed(event -> {
            setLoading(false);
            Throwable error = task.getException();
            setStatus("Ошибка обновления");
            showError("Ошибка обновления записи", error.getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Удаление выбранной оценки.
     */
    @FXML
    private void deleteGrade() {
        if (selectedGrade == null) {
            showWarning("Предупреждение", "Выберите запись для удаления");
            return;
        }
        
        // Подтверждение удаления
        Optional<ButtonType> result = showConfirmation(
                "Подтверждение удаления",
                "Вы уверены, что хотите удалить запись?",
                String.format("Студент: %s\nПредмет: %s\nОценка: %d",
                        selectedGrade.getStudentName(),
                        selectedGrade.getSubject(),
                        selectedGrade.getGrade())
        );
        
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        
        Long id = selectedGrade.getId();
        setLoading(true);
        setStatus("Удаление записи...");
        
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                apiService.deleteGrade(id);
                return null;
            }
        };
        
        task.setOnSucceeded(event -> {
            setLoading(false);
            clearForm();
            loadGrades();
            showInfo("Успешно", "Запись успешно удалена");
        });
        
        task.setOnFailed(event -> {
            setLoading(false);
            Throwable error = task.getException();
            setStatus("Ошибка удаления");
            showError("Ошибка удаления записи", error.getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Очистка формы.
     */
    @FXML
    private void clearForm() {
        studentNameField.clear();
        subjectField.clear();
        gradeSpinner.getValueFactory().setValue(5);
        gradesTable.getSelectionModel().clearSelection();
        selectedGrade = null;
        updateButtonStates();
        setStatus("Готово");
    }
    
    /**
     * Валидация формы ввода.
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        String studentName = studentNameField.getText();
        if (studentName == null || studentName.trim().isEmpty()) {
            errors.append("• Имя студента обязательно для заполнения\n");
        } else if (studentName.trim().length() < 2 || studentName.trim().length() > 100) {
            errors.append("• Имя студента должно содержать от 2 до 100 символов\n");
        }
        
        String subject = subjectField.getText();
        if (subject == null || subject.trim().isEmpty()) {
            errors.append("• Предмет обязателен для заполнения\n");
        } else if (subject.trim().length() < 2 || subject.trim().length() > 100) {
            errors.append("• Название предмета должно содержать от 2 до 100 символов\n");
        }
        
        Integer grade = gradeSpinner.getValue();
        if (grade == null || grade < 1 || grade > 5) {
            errors.append("• Оценка должна быть от 1 до 5\n");
        }
        
        if (errors.length() > 0) {
            showWarning("Ошибка валидации", errors.toString());
            return false;
        }
        
        return true;
    }
    
    /**
     * Создание объекта Grade из данных формы.
     */
    private Grade createGradeFromForm() {
        return new Grade(
                studentNameField.getText().trim(),
                subjectField.getText().trim(),
                gradeSpinner.getValue()
        );
    }
    
    /**
     * Установка состояния загрузки.
     */
    private void setLoading(boolean loading) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(loading);
            addButton.setDisable(loading);
            editButton.setDisable(loading || selectedGrade == null);
            deleteButton.setDisable(loading || selectedGrade == null);
            refreshButton.setDisable(loading);
            clearButton.setDisable(loading);
        });
    }
    
    /**
     * Установка текста статуса.
     */
    private void setStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }
    
    // ===== Диалоговые окна =====
    
    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            styleAlert(alert);
            alert.showAndWait();
        });
    }
    
    private void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            styleAlert(alert);
            alert.showAndWait();
        });
    }
    
    private void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            styleAlert(alert);
            alert.showAndWait();
        });
    }
    
    private Optional<ButtonType> showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert);
        return alert.showAndWait();
    }
    
    private void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif;");
    }
}


