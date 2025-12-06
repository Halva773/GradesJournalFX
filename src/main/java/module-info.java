module com.example.gradesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    
    opens com.example.gradesfx to javafx.fxml;
    opens com.example.gradesfx.controller to javafx.fxml;
    opens com.example.gradesfx.model to com.fasterxml.jackson.databind;
    
    exports com.example.gradesfx;
    exports com.example.gradesfx.controller;
    exports com.example.gradesfx.model;
    exports com.example.gradesfx.service;
}


