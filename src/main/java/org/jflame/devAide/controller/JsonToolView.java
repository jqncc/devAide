package org.jflame.devAide.controller;

import org.jflame.commons.util.StringHelper;
import org.jflame.devAide.util.UIComponentCreater;

import com.alibaba.fastjson.JSON;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class JsonToolView {

    @FXML
    private Button btnJsonFmt;
    @FXML
    private TextArea srcText;
    @FXML
    private TextArea targetText;

    @FXML
    private void formattHandle(ActionEvent event) {
        String srcJson = srcText.getText();
        String toJson = "";
        if (StringHelper.isNotEmpty(srcJson)) {
            if (JSON.isValid(srcJson)) {
                toJson = JSON.toJSONString(JSON.parse(srcJson), true);
            } else {
                UIComponentCreater.createAlert("非法的JSON字符串")
                        .show();
            }
        }
        targetText.setText(toJson);
    }
}
