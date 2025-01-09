package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Product;
import com.example.model.Role;
import com.example.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PersonnelTableController {

    @FXML
    private TableView<User> personnelTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> username;

    @FXML
    private TableColumn<User, Role> roleColumn;

    private final ObservableList<User> personnelList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        DatabaseConnection db = new DatabaseConnection();
        List<User> personnel = db.getAllUsers();
        personnelList.setAll(personnel);
        personnelTable.setItems(personnelList);
    }

    public void handleAddPersonnel(ActionEvent actionEvent) {
    }

    public void handleEditPersonnel(ActionEvent actionEvent) {
    }

    public void handleRemovePersonnel(ActionEvent actionEvent) {
    }

    public void handleClose(ActionEvent actionEvent) {
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
