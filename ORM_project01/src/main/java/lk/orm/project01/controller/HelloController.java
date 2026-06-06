package lk.orm.project01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Serenity Mental Health Therapy Center!");
    }
}
