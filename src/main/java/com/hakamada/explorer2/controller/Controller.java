package com.hakamada.explorer2.controller;


import com.hakamada.explorer2.reader.IReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {

    @FXML
    private TextField tfRootPath;
    @FXML
    private TextField tfTargetPath;
    @FXML
    private ListView<String> lvLogger;
    private ObservableList<String> listViewData = FXCollections.observableArrayList();
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnReset;

    private IReader iReader;

    @FXML
    private void initialize() {
        iReader = new IReader();
        // search panel
        tfRootPath.setText(iReader.getRootPath());
        tfTargetPath.setText(iReader.getTargetPath());
        btnStart.setOnAction(this::pressStart);
    }

    @FXML
    private void pressStart(ActionEvent event) {
        String rootPath = tfRootPath.getText();
        String targetPath = tfTargetPath.getText();
        if (rootPath != null) {
            iReader.setRootPath(rootPath);
            iReader.setTargetPath(targetPath);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    iReader.scanPath(rootPath, lvLogger);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }



}
