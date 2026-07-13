module com.sep.client {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires org.json;
    requires spring.messaging;
    requires spring.websocket;
    requires org.apache.logging.log4j;
    requires java.desktop;
    requires spring.web;
    requires spring.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.sep.client to javafx.fxml;
    exports com.sep.client;
    exports com.sep.client.extras;
    opens com.sep.client.extras to javafx.fxml;
    exports com.sep.client.controller;
    opens com.sep.client.controller to javafx.fxml;

    opens com.sep.client.model to javafx.fxml;
    exports com.sep.client.model;
}