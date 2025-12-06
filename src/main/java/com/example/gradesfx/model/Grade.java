package com.example.gradesfx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Модель данных для оценки студента.
 * Соответствует JSON структуре REST API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Grade {
    
    private Long id;
    private String studentName;
    private String subject;
    private Integer grade;
    
    /**
     * Конструктор по умолчанию для Jackson.
     */
    public Grade() {
    }
    
    /**
     * Конструктор с параметрами.
     * 
     * @param studentName имя студента
     * @param subject предмет
     * @param grade оценка (1-5)
     */
    public Grade(String studentName, String subject, Integer grade) {
        this.studentName = studentName;
        this.subject = subject;
        this.grade = grade;
    }
    
    /**
     * Полный конструктор с ID.
     * 
     * @param id идентификатор записи
     * @param studentName имя студента
     * @param subject предмет
     * @param grade оценка (1-5)
     */
    public Grade(Long id, String studentName, String subject, Integer grade) {
        this.id = id;
        this.studentName = studentName;
        this.subject = subject;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", subject='" + subject + '\'' +
                ", grade=" + grade +
                '}';
    }
}


