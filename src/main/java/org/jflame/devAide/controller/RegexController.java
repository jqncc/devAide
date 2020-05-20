package org.jflame.devAide.controller;

import org.controlsfx.control.CheckComboBox;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RegexController {

    @FXML
    private TextArea srcText;
    @FXML
    private TextArea resultText;
    @FXML
    private TextArea regexText;
    @FXML
    private Button btnHandle;
    @FXML
    private CheckComboBox<String> cbxRegexMode;

    public RegexController() {

    }

    @FXML
    private void initialize() {
        cbxRegexMode.getItems()
                .add("CASE_INSENSITIVE");
    }

}
